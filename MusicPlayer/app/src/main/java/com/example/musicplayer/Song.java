package com.example.musicplayer;

import android.media.Image;

public class Song {
    public long id;
    public String name;
    public String times;
    public String artist;
    public String album;

    public Song(long id, String name, long millis, String artist, String album) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        setTimes(millis);
    }

    public void setTimes(long millis) {
        millis /= 1000;
        long minutes = millis / 60;
        long sec = millis % 60;
        this.times = minutes + ":" + sec;
    }
}
