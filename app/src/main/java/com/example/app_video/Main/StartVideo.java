package com.example.app_video.Main;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Adapter.Categories.Categories;
import com.example.app_video.Adapter.HotVideo.Video;
import com.example.app_video.Adapter.ItemCategory.ItemCategory;
import com.example.app_video.Adapter.ItemCategory.ItemCategoryAdapter;
import com.example.app_video.DefineURL;
import com.example.app_video.Frag.FragItemCategory;
import com.example.app_video.InterOnClick;
import com.example.app_video.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartVideo extends AppCompatActivity {
    private static final String TAG = "StartVideo";
    VideoView Videoview;
    ImageView backVideo;
    TextView tvnameVideo;
    ImageView btPauseVideo, btPreVideo_10s, btStartVideo, btNextVideo_10s;
    SeekBar seekBar;

    ImageView btfullScreen, btExitFullScreen;
    ArrayList<ItemCategory> arrayList;
    String url = DefineURL.ITEM_CATEGORY_URL;

    RelativeLayout toolbar;

    TextView tvTimeEnd, tvTimeStart;
    RelativeLayout controler;
    int getTimeCurrent;

    LinearLayout lnViewChangeVol, lnViewChangePosition;

    TextView tvCurrentVol;
    TextView tvgetCurrentPotision, tvDuration;

    int x1, y1;
    AudioManager audioManager;
    Handler handler = new Handler();
    RelativeLayout relaVideoView;
    boolean reChangeVol = true;
    boolean reChangePosition = true;
    int maxVol, stepVol, currentVol;

    RelativeLayout relaDetailVideoPlaying;
    TextView tvNameVideoPlaying;
    TextView tvTimeCreatVideoPlaying;
    LinearLayout lnSuggestVideo;
    RecyclerView rvSuggestVideo;
    ItemCategoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startvideo);

        relaVideoView = findViewById(R.id.RelayVideoView);
        lnSuggestVideo = findViewById(R.id.lnSuggestVideo);
        // setAdapter suggestVideo
        rvSuggestVideo=findViewById(R.id.rvListSuggest);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        rvSuggestVideo.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter= new ItemCategoryAdapter(arrayList);
        new dogetItem(url).execute();
        adapter.setItemCategoryAdapter(new InterOnClick() {
            @Override
            public void onClickVideo(Video video) {


            }

            @Override
            public void oncLickCategory(Categories categories) {

            }

            @Override
            public void onClickItem(ItemCategory itemCategory) {
                tvTimeCreatVideoPlaying.setText(itemCategory.getDate());
                tvNameVideoPlaying.setText(itemCategory.getName());
                tvnameVideo.setText(itemCategory.getName());
                Uri uri = Uri.parse(itemCategory.getUrl());
                Videoview.setVideoURI(uri);
                Videoview.requestFocus();
                Videoview.start();
            }
        });
        rvSuggestVideo.setAdapter(adapter);



        relaDetailVideoPlaying = findViewById(R.id.relaDetailVideoPlaying);
        tvNameVideoPlaying = findViewById(R.id.tvNameVideoPlaying);
        tvTimeCreatVideoPlaying = findViewById(R.id.tvTimeCreatVideoPlaying);


        lnViewChangePosition = findViewById(R.id.lnViewChangePosition);
        tvgetCurrentPotision = findViewById(R.id.tvCurrentPosition);
        tvDuration = findViewById(R.id.tvDuration);

        lnViewChangeVol = findViewById(R.id.lnViewChangeVol);
        tvCurrentVol = findViewById(R.id.tvCurrentVol);

        Videoview = findViewById(R.id.videoview);
        backVideo = findViewById(R.id.btBack);
        tvnameVideo = findViewById(R.id.tvNameStart);
        toolbar = findViewById(R.id.toolbar);
        controler = findViewById(R.id.Controler);


        btPauseVideo = findViewById(R.id.btPause);
        btPreVideo_10s = findViewById(R.id.btPre);
        btStartVideo = findViewById(R.id.btStart);
        btNextVideo_10s = findViewById(R.id.btNext);
        seekBar = findViewById(R.id.seekBar);
        btfullScreen = findViewById(R.id.btChangeScreen);
        btExitFullScreen = findViewById(R.id.btExitFullScreen);
        tvTimeStart = findViewById(R.id.tvTimeStart);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);


        btStartVideo.setVisibility(View.INVISIBLE);
        btfullScreen.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.GONE);


        String url = getIntent().getStringExtra("url");
        String name = getIntent().getStringExtra("name");
        String time = getIntent().getStringExtra("time");


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvnameVideo.setText(name);
        tvTimeCreatVideoPlaying.setText(time);
        tvNameVideoPlaying.setText(name);

        backVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Uri uri = Uri.parse(url);
        Videoview.setVideoURI(uri);
        Videoview.requestFocus();
        Videoview.start();
        updateSeekBar();


        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        stepVol = 100 / maxVol;
        currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVol = currentVol * stepVol;
        tvCurrentVol.setText(String.valueOf(currentVol));


        ShowContrler_delay_10s showContrler_delay_10s = new ShowContrler_delay_10s();
        handler.postDelayed(showContrler_delay_10s, 5000);

        // control  Volume and position
        Videoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) motionEvent.getX();
                        y1 = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        tvDuration.setText(millisecondsToString(Videoview.getDuration()));

                        if (Math.abs(motionEvent.getX() - x1) > 50) {
                            reChangeVol = false;
                        }
                        if (Math.abs(motionEvent.getY() - y1) > 50) {
                            reChangePosition = false;
                        }
                        if (Math.abs(motionEvent.getX() - x1) > 50 && reChangePosition) {
                            lnViewChangePosition.setVisibility(View.VISIBLE);
                            int timeCurent = (Videoview.getCurrentPosition() + (int) motionEvent.getX() - x1);
                            Videoview.seekTo(timeCurent);
                            tvgetCurrentPotision.setText(millisecondsToString(timeCurent));

                        }

                        if (Math.abs(motionEvent.getY() - y1) > 50 && reChangeVol) {
                            lnViewChangeVol.setVisibility(View.VISIBLE);
                            if (motionEvent.getY() - y1 < 0 && currentVol < 100) {
                                currentVol++;
                                tvCurrentVol.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                                // audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            } else if (motionEvent.getY() - y1 > 0 && currentVol > 0) {
                                currentVol--;
                                tvCurrentVol.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                                // audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        lnViewChangePosition.setVisibility(View.INVISIBLE);
                        lnViewChangeVol.setVisibility(View.INVISIBLE);
                        reChangeVol = true;
                        reChangePosition = true;
                        break;
                }
                controler.setVisibility(View.VISIBLE);
                return true;
            }
        });

        // Custom Control Play
        tvTimeStart.setText(millisecondsToString(0));
        tvTimeEnd.setText(millisecondsToString(Videoview.getDuration()));

        btPauseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Videoview.pause();
                btPauseVideo.setVisibility(View.INVISIBLE);
                btStartVideo.setVisibility(View.VISIBLE);
            }
        });

        btStartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Videoview.getCurrentPosition() == Videoview.getDuration()) {
                    Videoview.seekTo(Videoview.getCurrentPosition());
                }
                Videoview.start();
                btStartVideo.setVisibility(View.INVISIBLE);
                btPauseVideo.setVisibility(View.VISIBLE);

            }
        });
        btNextVideo_10s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = Videoview.getCurrentPosition();
                int duration = Videoview.getDuration();
                int NextTime = 10000;
                if (currentPosition + NextTime < duration) {
                    Videoview.seekTo(currentPosition + NextTime);
                } else {
                    Videoview.seekTo(0);
                }

            }
        });
        btPreVideo_10s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = Videoview.getCurrentPosition();
                int previousTime = 10000;
                if (currentPosition - previousTime > 0) {
                    Videoview.seekTo(currentPosition - previousTime);
                } else {
                    Videoview.seekTo(0);
                }
            }
        });

        btExitFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btfullScreen.setVisibility(View.VISIBLE);
                btExitFullScreen.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                relaDetailVideoPlaying.setVisibility(View.VISIBLE);
                lnSuggestVideo.setVisibility(View.VISIBLE);

                getTimeCurrent = Videoview.getCurrentPosition();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) relaVideoView.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = 600;
                relaVideoView.setLayoutParams(params1);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Videoview.seekTo(getTimeCurrent);
            }
        });
        btfullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btExitFullScreen.setVisibility(View.VISIBLE);
                btfullScreen.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                relaDetailVideoPlaying.setVisibility(View.GONE);
                lnSuggestVideo.setVisibility(View.GONE);

                getTimeCurrent = Videoview.getCurrentPosition();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
               RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) relaVideoView.getLayoutParams();
               params1.width = params.MATCH_PARENT;
                params1.height = params.MATCH_PARENT;
                relaVideoView.setLayoutParams(params1);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                Videoview.seekTo(getTimeCurrent);


            }
        });
    }

    private String millisecondsToString(int milliseconds) {
        long hours = TimeUnit.MICROSECONDS.toHours((long) milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);

        if (hours == 0) {
            return minutes + ":" + seconds;
        } else
            return hours + ":" + minutes + ":" + seconds;
    }

    class ShowContrler_delay_10s implements Runnable {
        public void run() {
            handler.postDelayed(this, 5000);
            controler.setVisibility(View.GONE);
        }
    }

    //updating SeekBar
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Videoview != null) {
                int mCurrentPosition = Videoview.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

                int timeCurent = Videoview.getCurrentPosition();
                tvTimeStart.setText(millisecondsToString(timeCurent));

                int timeEnd = Videoview.getDuration();
                tvTimeEnd.setText(millisecondsToString(timeEnd));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
        seekBar.setMax(Videoview.getDuration());
    }


    public void restartVideo() {
        int timeCurrent = Videoview.getCurrentPosition();
        int timeEnd = Videoview.getDuration();
        if (timeCurrent == timeEnd) {
            Videoview.seekTo(0);
        }
    }

    public  class  dogetItem extends AsyncTask<Void,Void,Void> {
        String newURL;
        String json="";

        public dogetItem(String newURL) {
            this.newURL = newURL;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url =new URL(newURL);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();

                int character;
                while ((character = inputStream.read()) != -1) {

                    json += (char) character;
                }
                Log.d(TAG, "doInBackground: " + json);


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String avt = object.getString("avatar");
                    String date_created = object.getString("date_created");
                    String name = object.getString("title");
                    String url = object.getString("file_mp4");
                    arrayList.add(new ItemCategory(avt,name,date_created,url));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
