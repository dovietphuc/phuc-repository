package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MiniDescriptionFragment extends Fragment {

    View mView;
    ImageView mImageAlbumView;
    ImageButton mImageButton;
    TextView mTitle;
    TextView mAlbum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mini_description, container, false);
        mImageAlbumView = mView.findViewById(R.id.img_album);
        mImageButton = mView.findViewById(R.id.imageButton_play_pause);
        mTitle = mView.findViewById(R.id.tvd_songname);
        mAlbum = mView.findViewById(R.id.tvd_album);
        return mView;
    }

}