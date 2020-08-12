package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {
    // Song list và layout
    private ArrayList<Song> songs;
    private ArrayList<Song> arraylist;
    private LayoutInflater songInf;

    // Constructor
    public SongAdapter(Context c, ArrayList<Song> theSongs) {
        songs = theSongs;
        arraylist = new ArrayList<>();
        arraylist.addAll(songs);
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Ánh xạ đến layout mỗi bài
        ConstraintLayout songLayout = (ConstraintLayout) songInf.inflate(R.layout.song_item, parent, false);

        TextView index = (TextView) songLayout.findViewById(R.id.tv_index);
        TextView songName = (TextView) songLayout.findViewById(R.id.tvd_songname);
        TextView times = (TextView) songLayout.findViewById(R.id.tv_times);

        // Lấy bài hát hiện
        Song currentSong = songs.get(position);

        // Lấy tên tiêu đề và tác
        index.setText(String.valueOf(position + 1));
        songName.setText(currentSong.name);
        times.setText(currentSong.times);

        // Cài đặt tag cho mỗi bài là vị trí của mỗi
        //songLayout.setTag(position);
        return songLayout;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        songs.clear();
        if (charText.length() == 0) {
            songs.addAll(arraylist);
        } else {
            for (Song wp : arraylist) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    songs.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
