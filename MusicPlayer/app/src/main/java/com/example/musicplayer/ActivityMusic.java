package com.example.musicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityMusic extends AppCompatActivity implements MediaController.MediaPlayerControl {

    AllSongsFragment mAllSongsFragment;
    MiniDescriptionFragment mMminiDescriptionFragment;
    private ArrayList<Song> songList;
    private ListView songView;
    private MediaPlaybackService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean paused = false;
    private boolean playbackPaused = false;
    SongAdapter songAdapter;
    int currentSongIndex = -1;

    // Connect with the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlaybackService.class);
            bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mAllSongsFragment = (AllSongsFragment) getSupportFragmentManager().findFragmentById(R.id.fm_list_song);
        mMminiDescriptionFragment = (MiniDescriptionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_description);
        songView = mAllSongsFragment.mListView;
        songList = new ArrayList<Song>();
        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
        songAdapter = new SongAdapter(mAllSongsFragment.getContext(), songList);
        songView.setAdapter(songAdapter);
    }

    public void songPicked(View view) {
        TextView index = (TextView) view.findViewById(R.id.tv_index);
        currentSongIndex = Integer.valueOf(index.getText().toString()) - 1;
        musicService.setSong(currentSongIndex);
        musicService.playSong();
        mMminiDescriptionFragment.mImageButton.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        mMminiDescriptionFragment.mTitle.setText(musicService.songs.get(currentSongIndex).name);
        mMminiDescriptionFragment.mAlbum.setText(musicService.songs.get(currentSongIndex).album);
        playbackPaused = !playbackPaused;
    }

    // Method to retrieve song infos from device
    public void getSongList() {
        // Query external audio resources
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        // Iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            // Get columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int timesColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                Long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                Long thisTimes = musicCursor.getLong(timesColumn);
                Log.e("id: ", thisId.toString());
                songList.add(new Song(thisId, thisTitle, thisTimes, thisArtist, thisAlbum));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    public void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    private void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    private void playPrev() {
        musicService.playPrev();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicService != null && musicBound && musicService.isPlaying()) {
            return musicService.getDur();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int i) {
        musicService.seek(i);
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && musicBound) {
            return musicService.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void clickMore(View view) {
        ImageButton button = view.findViewById(R.id.imageButton);
        PopupMenu popup = new PopupMenu(this, button);
        popup.inflate(R.menu.song_item_menu);
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_view, menu);
        return true;
    }


    public void clickPlay(View view) {
        if (currentSongIndex == -1) return;
        ImageButton imageButton = mMminiDescriptionFragment.mImageButton;
        if (isPlaying()) {
            imageButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            musicService.pausePlayer();
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            musicService.playSong();
        }
    }

    public void goToPlayBack(View view) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mMminiDescriptionFragment.mView.setY(mMminiDescriptionFragment.mView.getY() + mMminiDescriptionFragment.mView.getHeight());
        MediaPlaybackFragment mediaPlaybackFragment = new MediaPlaybackFragment(musicService.songs.get(currentSongIndex).name, musicService.songs.get(currentSongIndex).album);
        addFragment(mediaPlaybackFragment);
    }

    protected void addFragment(Fragment fragment) {

        FragmentManager fmgr = getSupportFragmentManager();

        FragmentTransaction ft = fmgr.beginTransaction();

        ft.add(R.id.allsongs_layout, fragment);

        ft.addToBackStack(fragment.getClass().getSimpleName());

        ft.commit();

    }

    public ActionBar getSuportActionBar(){
        return getSupportActionBar();
    }

}