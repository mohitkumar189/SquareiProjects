package com.app.lunavista.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.lunavista.AppUtils.CircleTransform;
import com.app.lunavista.Fragments.FragmentCommentList;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class PlayMyVideo extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    Toolbar toolbar;
    Context context;
    VideoView video_view = null;
    ProgressBar progressBar1;
    private MediaController mediaControls;
    String videoPath = "", songId = "";
    TextView text_upload,text_echo,text_chorus,text_der;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_my_video);

        context = this;
        init();
        if (mediaControls == null) {
            mediaControls = new MediaController(PlayMyVideo.this);

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit your recording");

        Intent in = getIntent();
        videoPath = in.getExtras().getString("videoPath");
        songId = in.getExtras().getString("songId");

        setListener();
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                progressBar1.setVisibility(View.GONE);

            }
        });

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
        text_upload = (TextView) findViewById(R.id.text_upload);
        text_der = (TextView) findViewById(R.id.text_der);
        text_echo = (TextView) findViewById(R.id.text_echo);
        text_chorus = (TextView) findViewById(R.id.text_chorus);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PlayMyVideo.this, UploadVideo.class);
                in.putExtra("videoUrl", videoPath);
                in.putExtra("songId", songId);
                startActivity(in);


            }
        });


        text_chorus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_chorus.setTextColor(getResources().getColor(R.color.text_white));
                text_der.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));
                text_echo.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

                text_chorus.setBackgroundResource(R.drawable.fx_btn_hover);
                text_der.setBackgroundResource(R.drawable.fx_btn_normal);
                text_echo.setBackgroundResource(R.drawable.fx_btn_normal);

            }
        });

        text_der.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_chorus.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));
                text_der.setTextColor(getResources().getColor(R.color.text_white));
                text_echo.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

                text_chorus.setBackgroundResource(R.drawable.fx_btn_normal);
                text_der.setBackgroundResource(R.drawable.fx_btn_hover);
                text_echo.setBackgroundResource(R.drawable.fx_btn_normal);

            }
        });

        text_echo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_chorus.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));
                text_der.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));
                text_echo.setTextColor(getResources().getColor(R.color.text_white));

                text_chorus.setBackgroundResource(R.drawable.fx_btn_normal);
                text_der.setBackgroundResource(R.drawable.fx_btn_normal);
                text_echo.setBackgroundResource(R.drawable.fx_btn_hover);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);

    }
}
