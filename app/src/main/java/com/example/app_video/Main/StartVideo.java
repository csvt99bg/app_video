package com.example.app_video.Main;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Adapter.SuggestVideoAdapter;
import com.example.app_video.Contact.ItemCategory;
import com.example.app_video.SQLHelperSave;
import com.example.app_video.define.Define_Methods;
import com.example.app_video.Interface.Click_Item_CateGory;
import com.example.app_video.define.DefineURL;
import com.example.app_video.R;
import com.example.app_video.SQLHelper;
import com.example.app_video.define.Define_kw;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StartVideo extends AppCompatActivity {
    private static final String TAG = "StartVideo";
    RelativeLayout toolbar;

    RelativeLayout controler;
    int getTimeCurrent;
    VideoView Videoview;
    ImageView btBackVideo;
    TextView tvnameVideo;
    ImageView btPauseVideo, btPreVideo, btStartVideo, btNextVideo, bt_X ,btSave;
    SeekBar seekBar;
    TextView tvTimeEnd, tvTimeStart;
    ImageView btfullScreen, btExitFullScreen;

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
    SuggestVideoAdapter adapter;
    ArrayList<ItemCategory> arrayList;
    String url_suggest = DefineURL.ITEM_CATEGORY_URL;

    ItemCategory itemPlaying;

    SQLHelperSave sqlHelperSave;
    ArrayList<ItemCategory> arrayListItemSave;
    SQLHelper sqlHelper;
    Define_Methods define_methods = new Define_Methods();
    ArrayList<ItemCategory> arrayListSQL;
    SpinKitView loading;
    int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startvideo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        loading= findViewById(R.id.loading);
        relaVideoView = findViewById(R.id.RelayVideoView);
        lnSuggestVideo = findViewById(R.id.lnSuggestVideo);

        relaDetailVideoPlaying = findViewById(R.id.relaDetailVideoPlaying);
        tvNameVideoPlaying = findViewById(R.id.tvNameVideoPlaying);
        tvTimeCreatVideoPlaying = findViewById(R.id.tvTimeCreatVideoPlaying);

        lnViewChangePosition = findViewById(R.id.lnViewChangePosition);
        tvgetCurrentPotision = findViewById(R.id.tvCurrentPosition);
        tvDuration = findViewById(R.id.tvDuration);

        lnViewChangeVol = findViewById(R.id.lnViewChangeVol);
        tvCurrentVol = findViewById(R.id.tvCurrentVol);

        Videoview = findViewById(R.id.videoview);
        btBackVideo = findViewById(R.id.btBack);
        btSave = findViewById(R.id.btSaveVideo);
        tvnameVideo = findViewById(R.id.tvNameStart);
        toolbar = findViewById(R.id.toolbar);
        controler = findViewById(R.id.Controler);

        // Custom Control
        btPauseVideo = findViewById(R.id.btPause);
        btPreVideo = findViewById(R.id.btPre);
        btStartVideo = findViewById(R.id.btStart);
        btNextVideo = findViewById(R.id.btNext);
        bt_X = findViewById(R.id.btBackVideo);
        seekBar = findViewById(R.id.seekBar);
        btfullScreen = findViewById(R.id.btChangeScreen);
        btExitFullScreen = findViewById(R.id.btExitFullScreen);
        tvTimeStart = findViewById(R.id.tvTimeStart);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);

        btStartVideo.setVisibility(View.INVISIBLE);
        btfullScreen.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.GONE);

        String url = getIntent().getStringExtra(Define_kw.KW_URL);
        String name = getIntent().getStringExtra(Define_kw.KW_NAME);
        String time = getIntent().getStringExtra(Define_kw.KW_TIME);
        String img = getIntent().getStringExtra(Define_kw.KW_IMG);
        itemPlaying = new ItemCategory(img,name,time,url);

        tvnameVideo.setText(name);
        tvTimeCreatVideoPlaying.setText(time);
        tvNameVideoPlaying.setText(name);
        btBackVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

 // Playing Video
        Uri uri = Uri.parse(url);
        Videoview.setVideoURI(uri);
        Videoview.requestFocus();
        Videoview.start();
        updateSeekBar();


 // setup SeekBar
        ShowContrler_delay_5s showContrler_delay_10s = new ShowContrler_delay_5s();
        handler.postDelayed(showContrler_delay_10s, 5000);

// setAdapter suggestVideo
        rvSuggestVideo = findViewById(R.id.rvListSuggest);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        rvSuggestVideo.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        new dogetItem(url_suggest).execute();
        setUpSuggestVideo();
        rvSuggestVideo.setAdapter(adapter);


// set auto next video
        Videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                adapter = new SuggestVideoAdapter(arrayList);
                if (arrayList.isEmpty()==false){
                    ItemCategory itemNext = arrayList.get(0);
                    arrayList.remove(0);
                    arrayList.add(itemNext);
                    Uri nextUri = Uri.parse(itemNext.getUrl());
                    Videoview.setVideoURI(nextUri);
                    Videoview.requestFocus();
                    Videoview.start();
                    addToHistory(itemNext);

                    tvTimeCreatVideoPlaying.setText(itemNext.getDate());
                    tvNameVideoPlaying.setText(itemNext.getName());
                    tvnameVideo.setText(itemNext.getName());

                    position++;
                }  rvSuggestVideo.setAdapter(adapter);

            }
        });

 // setUp Volume and Position
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVol = 100 / maxVol;
        currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVol = currentVol * stepVol;
        tvCurrentVol.setText(String.valueOf(currentVol));

// Control Volume and Position
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

 // Custom Control Play Video
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
        btNextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemCategory item = arrayList.get(0);
                if (position >= 0) {
                    addToHistory(item);
                    tvTimeCreatVideoPlaying.setText(item.getDate());
                    tvNameVideoPlaying.setText(item.getName());
                    tvnameVideo.setText(item.getName());
                    Uri uri = Uri.parse(item.getUrl());
                    Videoview.setVideoURI(uri);
                    Videoview.requestFocus();
                    Videoview.start();

                }
                arrayList.remove(0);
                arrayList.add(item);
                setUpSuggestVideo();
                position++;
                itemPlaying = item;

            }
        });
        btPreVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    ItemCategory item = arrayList.get(arrayList.size() - 2);
                    addToHistory(item);
                    tvTimeCreatVideoPlaying.setText(item.getDate());
                    tvNameVideoPlaying.setText(item.getName());
                    tvnameVideo.setText(item.getName());
                    Uri uri = Uri.parse(item.getUrl());
                    Videoview.setVideoURI(uri);
                    Videoview.requestFocus();
                    Videoview.start();
                    position--;
                    itemPlaying = item;
                } else position = 0;

            }
        });
        bt_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSave(itemPlaying);
                Toast.makeText(getBaseContext(),"Save",Toast.LENGTH_LONG).show();
            }
        });

// set exit full screen
        btExitFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btfullScreen.setVisibility(View.VISIBLE);
                btExitFullScreen.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
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

// set full screen
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
                params1.height = params1.MATCH_PARENT;
                relaVideoView.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Videoview.seekTo(getTimeCurrent);
            }
        });
    }
    // format time of video
    private String millisecondsToString(int milliseconds) {
        return new SimpleDateFormat("mm:ss").format(milliseconds);
    }

    // display seekBar in 5s
    class ShowContrler_delay_5s implements Runnable {
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

// Get List Video Suggest
    public class dogetItem extends AsyncTask<Void, Void, Void> {
        String newURL;
        String json = "";

        public dogetItem(String newURL) {
            this.newURL = newURL;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(newURL);
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
                    arrayList.add(new ItemCategory(avt, name, date_created, url));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void setUpSuggestVideo() {
        adapter = new SuggestVideoAdapter(arrayList);
        adapter.setSuggestVideoAdapter(new Click_Item_CateGory() {
            @Override
            public void onClickItem(ItemCategory itemCategory) {
                addToHistory(itemCategory);

                tvTimeCreatVideoPlaying.setText(itemCategory.getDate());
                tvNameVideoPlaying.setText(itemCategory.getName());
                tvnameVideo.setText(itemCategory.getName());
                Uri uri = Uri.parse(itemCategory.getUrl());
                Videoview.setVideoURI(uri);
                Videoview.requestFocus();
                Videoview.start();
                ItemCategory item = itemCategory;
                arrayList.remove(itemCategory);
                arrayList.add(item);
                itemPlaying = item;
                position++;
               rvSuggestVideo.setAdapter(adapter);
            }
        });

        rvSuggestVideo.setAdapter(adapter);
    }

    public void addToHistory(ItemCategory itemCategory){
        sqlHelper = new SQLHelper(getBaseContext());
        arrayListSQL = sqlHelper.getAllItem();
        if (arrayListSQL.isEmpty() == false && define_methods.CHECK(itemCategory.getName(), arrayListSQL)) {
            sqlHelper.deleteItem(itemCategory.getName());
        }
        sqlHelper.insertItem(itemCategory);
    }

    public void addSave (ItemCategory itemCategory){
        sqlHelperSave = new SQLHelperSave(getBaseContext());
        arrayListItemSave = sqlHelperSave.getALLItem();
        if (arrayListItemSave.isEmpty() == false && define_methods.CHECK(itemCategory.getName(), arrayListItemSave)) {
            sqlHelperSave.deleteItem(itemCategory.getName());
        }
        sqlHelperSave.insertItem(itemCategory);
    }
}
