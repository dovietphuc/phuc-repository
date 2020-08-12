package com.example.musicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

public class MediaPlaybackFragment extends Fragment {

    View mView;
    AllSongsFragment mAllSongsFragment;
    MiniDescriptionFragment mMminiDescriptionFragment;
    ImageView mImageAlbumView;
    ImageButton mBtnListSong;
    ImageButton mBtnMenu;
    TextView mTitle;
    TextView mAlbum;
    String title;
    String album;

    public MediaPlaybackFragment(String title, String album){
        this.title = title;
        this.album = album;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_media_playback, container, false);
        mImageAlbumView = mView.findViewById(R.id.img_header_album);
        mBtnListSong = mView.findViewById(R.id.btn_list_allsongs);
        mBtnMenu = mView.findViewById(R.id.btn_menu);
        mTitle = mView.findViewById(R.id.tvd_header_songname);
        mTitle.setText(title);
        mAlbum = mView.findViewById(R.id.tvd_header_album);
        mAlbum.setText(album);
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ActivityMusic activity = (ActivityMusic) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.show();
        mAllSongsFragment = (AllSongsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fm_list_song);
        mMminiDescriptionFragment = (MiniDescriptionFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_description);
        mMminiDescriptionFragment.mView.setY(mMminiDescriptionFragment.mView.getY() - mMminiDescriptionFragment.mView.getHeight());
    }
}