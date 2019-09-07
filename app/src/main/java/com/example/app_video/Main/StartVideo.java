package com.example.app_video.Main;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.R;

import java.util.concurrent.TimeUnit;

public class StartVideo extends AppCompatActivity {
    VideoView Videoview;
    ImageView backVideo;
    TextView tvnameVideo;
    ImageView btPauseVideo, btPreVideo, btStartVideo, btNextVideo;
    SeekBar seekBar;
    ImageView btfullScreen, btExitFullScreen;
    RecyclerView rvListVideo;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startvideo);

        relaVideoView = findViewById(R.id.RelayVideoView);
        rvListVideo = findViewById(R.id.rvListVideo);

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
        btPreVideo = findViewById(R.id.btPre);
        btStartVideo = findViewById(R.id.btStart);
        btNextVideo = findViewById(R.id.btNext);
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvnameVideo.setText(name);
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
        currentVol =  currentVol*stepVol;
        tvCurrentVol.setText(String.valueOf(currentVol));


        ShowContrler_delay_10s showContrler_delay_10s = new ShowContrler_delay_10s();
        handler.postDelayed(showContrler_delay_10s, 5000);


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
        btPreVideo.setOnClickListener(new View.OnClickListener() {
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

                getTimeCurrent = Videoview.getCurrentPosition();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relaVideoView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) 600;
                Videoview.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Videoview.seekTo(getTimeCurrent);
            }
        });
        btfullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btExitFullScreen.setVisibility(View.VISIBLE);
                btfullScreen.setVisibility(View.GONE);
                toolbar.setVisibility(View.INVISIBLE);

                getTimeCurrent = Videoview.getCurrentPosition();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relaVideoView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                Videoview.setLayoutParams(params);
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
}
