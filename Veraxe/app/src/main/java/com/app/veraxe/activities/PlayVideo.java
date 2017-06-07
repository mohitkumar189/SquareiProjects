package com.app.veraxe.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.app.veraxe.R;

public class PlayVideo extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    Toolbar toolbar;
    Context context;
    VideoView video_view = null;
    ProgressBar progressBar1;
    private MediaController mediaControls;
    String videoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        context = this;
        init();
        if (mediaControls == null) {
            mediaControls = new MediaController(PlayVideo.this);

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent in = getIntent();
        videoPath = in.getExtras().getString("videoPath");


        setListener();
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                progressBar1.setVisibility(View.GONE);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            video_view.setVisibility(View.VISIBLE);
            video_view.setMediaController(mediaControls);
            video_view.setVideoURI(Uri.parse(videoPath));
            video_view.start();
            mediaControls.setAnchorView(video_view);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        video_view = (VideoView) findViewById(R.id.video_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}