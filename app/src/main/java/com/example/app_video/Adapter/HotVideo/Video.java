package com.example.app_video.Adapter.HotVideo;

import android.widget.Button;

public class Video {
    String avatar, timePlay, timeCreat, nameVideo;
    String url;
    Button btStart;

    public Button getBtStart() {
        return btStart;
    }

    public void setBtStart(Button btStart) {
        this.btStart = btStart;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Video(String avatar, String timePlay, String timeCreat, String nameVideo, String url) {
        this.avatar = avatar;
        this.timePlay = timePlay;
        this.timeCreat = timeCreat;
        this.nameVideo = nameVideo;
        this.url = url;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimePlay() {
        return timePlay;
    }

    public void setTimePlay(String timePlay) {
        this.timePlay = timePlay;
    }

    public String getTimeCreat() {
        return timeCreat;
    }

    public void setTimeCreat(String timeCreat) {
        this.timeCreat = timeCreat;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }
}
