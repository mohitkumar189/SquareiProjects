package com.app.lunavista.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.app.lunavista.Adapters.AdapterCommentList;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.AppUtils.CircleTransform;
import com.app.lunavista.Fragments.FragmentCommentList;
import com.app.lunavista.Fragments.FragmentPlayList;
import com.app.lunavista.Fragments.FragmentSongList;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponseNoProgress;
import com.app.lunavista.customview.ExpandableHeightListView;
import com.app.lunavista.interfaces.ConnectionDetector;
import com.app.lunavista.interfaces.ListenerPostData;
import com.app.lunavista.interfaces.OnCustomItemClicListener;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaySongVideo extends FragmentActivity implements ListenerPostData, OnCustomItemClicListener, View.OnClickListener {


    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private BroadcastReceiver broadcastReceiver;
    int seekPoistion = 0;
    // Toolbar toolbar;
    RelativeLayout rl_comment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    FragmentCommentList fragmentCommentList;
    EditText edit_comment;
    TextView text_done, text_song_title, text_song_desc, text_lets_sing;
   // VideoView video_view = null;
   // ProgressBar progressBar1;
   // private MediaController mediaControls;
    ImageView image_comment, image_like, image_share, image_song, image_user;
    ArrayList<ModelVideo> arrayListSongs;
    String songDetail = "", is_like = "", videoUrl = "", comment_like = "";
    ConnectionDetector cd;
    ExpandableHeightListView listComments;
    ArrayList<ModelVideo> arrayListComments;
    ModelVideo modelVideo;
    Context context;
    boolean isLiked = false;
   // ImageView image_fullscreen;
    AdapterCommentList adapterCommentList;
    boolean isPortrait = true;
    String isRecording = "";
    int commentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song_video_updated);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        context = this;
        init();
        initVideoPlayer();
      /*  if (mediaControls == null) {
            mediaControls = new MediaController(PlaySongVideo.this);
        }*/
        fragmentManager = getSupportFragmentManager();
       /* setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        listComments = (ExpandableHeightListView) findViewById(R.id.list_comment);
        listComments.setExpanded(true);
        arrayListComments = new ArrayList<>();
        Intent in = getIntent();
        songDetail = in.getExtras().getString("songDetail");
        videoUrl = in.getExtras().getString("videoUrl");
        isRecording = in.getExtras().getString("isRecording");
        if (!AppUtils.getUserImage(context).equalsIgnoreCase("")) {

            Picasso.with(context).load(AppUtils.getUserImage(context))
                    .placeholder(R.drawable.placeholder).transform(new CircleTransform()).into(image_user);

        }
        setListener();
        setSongDetail();
       /* video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                progressBar1.setVisibility(View.GONE);

                video_view.seekTo(seekPoistion);
                *//*if (seekPoistion == 0) {
                    video_view.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    video_view.pause();
                }*//*

            }
        });*/

        if (isRecording.equalsIgnoreCase("1")) {
            text_lets_sing.setVisibility(View.GONE);
        } else {
            text_lets_sing.setVisibility(View.VISIBLE);
        }

    }





    private void setSongDetail() {

        try {

            arrayListSongs = new ArrayList<>();
            JSONObject jo = new JSONObject(songDetail);

            modelVideo = new ModelVideo();
            modelVideo.setSongId(jo.getString("songId"));
            modelVideo.setSongThumb(jo.getString("songThumb"));
            modelVideo.setSongDetail(jo.toString());
            modelVideo.setSongTitle(jo.getString("songTitle"));
            modelVideo.setSongVideoURL(jo.getString("songVideoURL"));
            modelVideo.setSongDescription(jo.getString("songDescription"));
            modelVideo.setViewCount(jo.getString("viewCount"));
            modelVideo.setSongAspectRatio(jo.getString("songAspectRatio"));
            modelVideo.setIsFavorite(jo.getString("isFavorite"));

            arrayListSongs.add(modelVideo);

            if (arrayListSongs.get(0).getIsFavorite().equalsIgnoreCase("1")) {
                image_like.setImageResource(R.drawable.favorite_red);
            } else {
                image_like.setImageResource(R.drawable.favorite);
            }

            text_song_title.setText(arrayListSongs.get(0).getSongTitle());
            text_song_desc.setText(arrayListSongs.get(0).getSongDescription());

            Picasso.with(context)
                    .load(arrayListSongs.get(0).getSongThumb())
                    .transform(new CircleTransform())
                    //.placeholder(R.drawable.placeholder)
                    .into(image_song);

            Log.e("songidfirst", "**" + arrayListSongs.get(0).getSongId());

            getComments();
          /*  Bundle b = new Bundle();
            b.putString("song_id", arrayListSongs.get(0).getSongId());

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentCommentList = new FragmentCommentList();
            fragmentCommentList.setArguments(b);
            fragmentTransaction.replace(R.id.rl_container, fragmentCommentList);
            fragmentTransaction.commit();*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLiked) {
                setResult(23);
            }
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getComments() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                Toast.makeText(context, "Internet Connection Error, Please connect to working Internet connection", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "song_id", arrayListSongs.get(0).getSongId()));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_recording", isRecording));

                new AsyncPostDataResponseNoProgress(PlaySongVideo.this, 3, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_songDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void likeUnlikeSong() {

        try {
            isLiked = true;

            if (arrayListSongs.get(0).getIsFavorite().equalsIgnoreCase("1")) {
                is_like = "0";
            } else {
                is_like = "1";
            }

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                Toast.makeText(context, "Internet Connection Error, Please connect to working Internet connection", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "song_id", arrayListSongs.get(0).getSongId()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_like", is_like));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_recording", isRecording));


                new AsyncPostDataResponseNoProgress(PlaySongVideo.this, 4, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_likeUnlikeSong));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

           /* video_view.setVisibility(View.VISIBLE);
            video_view.setMediaController(mediaControls);
           */ //video_view.setVideoPath("http://dev.logiguys.com/lunavista/uploads/videos/1.mp4");
            Log.e("videourl", "*" + videoUrl);
         //   Uri uri = Uri.parse(videoUrl);
           /* video_view.setVideoURI(uri);
            video_view.start();
            mediaControls.setAnchorView(video_view);*/

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
      //  video_view = (VideoView) findViewById(R.id.video_view);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);

        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        text_done = (TextView) findViewById(R.id.text_done);
        text_song_title = (TextView) findViewById(R.id.text_song_title);
        text_lets_sing = (TextView) findViewById(R.id.text_lets_sing);
        text_song_desc = (TextView) findViewById(R.id.text_song_desc);
        image_comment = (ImageView) findViewById(R.id.image_comment);
        image_user = (ImageView) findViewById(R.id.image_user);
        image_like = (ImageView) findViewById(R.id.image_like);
        image_share = (ImageView) findViewById(R.id.image_share);
        image_song = (ImageView) findViewById(R.id.image_song);
       // image_fullscreen = (ImageView) findViewById(R.id.image_fullscreen);
      //  progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
    }

    private void submitComment() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                Toast.makeText(context, "Internet Connection Error, Please connect to working Internet connection", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "song_id", arrayListSongs.get(0).getSongId()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_recording", isRecording));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "comment", edit_comment.getText().toString()));

                new AsyncPostDataResponseNoProgress(PlaySongVideo.this, 1, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_setSongComment));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void setListener() {

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    setResult(23);

                }
                finish();
            }
        });*/

       /* image_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPortrait) {
                    isPortrait = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    isPortrait = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });
*/
        text_lets_sing.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(context, ActivityRecordVideo.class);
                        in.putExtra("videoUrl", videoUrl);
                        //   in.putExtra("videoPath", videoUrl);
                        //in.putExtra("videoUrl", "http://dev.logiguys.com/lunavista/uploads/videos/1.mp4");
                        in.putExtra("songId", arrayListSongs.get(0).getSongId());
                        startActivity(in);

                    }
                });
        image_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_comment.requestFocus();
            }
        });
        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Listen " + arrayListSongs.get(0).getSongTitle() + " in Luna Vista, Also find many new video features, so what are u waiting for. " + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likeUnlikeSong();
            }
        });


        text_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edit_comment.getText().toString().equalsIgnoreCase("")) {

                    submitComment();

                } else {

                    Toast.makeText(context, "Please enter comment", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    @Override
    public void onPostRequestSucess(int position, String response) {
        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    getComments();
                    edit_comment.setText("");

                } else {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    // Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    arrayListSongs.get(0).setIsFavorite(is_like);

                    if (arrayListSongs.get(0).getIsFavorite().equalsIgnoreCase("1")) {
                        image_like.setImageResource(R.drawable.favorite_red);
                    } else {
                        image_like.setImageResource(R.drawable.favorite);
                    }


                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data1 = commandResult.getJSONObject("data");
                    arrayListSongs.removeAll(arrayListSongs);
                    modelVideo = new ModelVideo();
                    modelVideo.setSongId(data1.getString("songId"));
                    modelVideo.setSongThumb(data1.getString("songThumb"));
                    modelVideo.setSongDetail(data1.toString());
                    modelVideo.setSongTitle(data1.getString("songTitle"));
                    modelVideo.setSongDescription(data1.getString("songDescription"));
                    modelVideo.setViewCount(data1.getString("viewCount"));
                    modelVideo.setSongAspectRatio(data1.getString("songAspectRatio"));
                    modelVideo.setIsFavorite(data1.getString("isFavorite"));

                    arrayListSongs.add(modelVideo);

                    Log.e("songid", "**" + arrayListSongs.get(0).getSongId());


                    JSONArray comments = data1.getJSONArray("comments");

                    arrayListComments.removeAll(arrayListComments);
                    for (int i = 0; i < comments.length(); i++) {

                        JSONObject jo = comments.getJSONObject(i);

                        modelVideo = new ModelVideo();

                        modelVideo.setName(jo.getString("commentBy"));
                        modelVideo.setCommentId(jo.getString("commentId"));
                        modelVideo.setComment(jo.getString("comment"));
                        modelVideo.setDate(jo.getString("commentOn"));
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));
                        modelVideo.setSongThumb(jo.getString("profileImage"));

                        arrayListComments.add(modelVideo);

                    }

                    adapterCommentList = new AdapterCommentList(context, this, arrayListComments);
                    listComments.setAdapter(adapterCommentList);


                } else {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();


                }
            } else if (position == 5) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    // Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    arrayListComments.get(commentPosition).setIsFavorite(comment_like);
                    adapterCommentList.notifyDataSetChanged();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }


   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", video_view.getCurrentPosition());
        video_view.pause();
    }*/

    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        seekPoistion = savedInstanceState.getInt("Position");
        // video_view.seekTo(seekPoistion);
    }
*/

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Log.e("likeclicked", "likecomment" + position);
            commentPosition = position;
            likeUnlikeComment();
            //  Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    private void likeUnlikeComment() {

        try {

            if (arrayListComments.get(commentPosition).getIsFavorite().equalsIgnoreCase("1")) {
                comment_like = "0";
            } else {
                comment_like = "1";
            }

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                Toast.makeText(context, "Internet Connection Error, Please connect to working Internet connection", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "comment_id", arrayListComments.get(commentPosition).getCommentId()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_like", comment_like));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_recording", isRecording));


                new AsyncPostDataResponseNoProgress(PlaySongVideo.this, 5, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_likeUnlikeComment));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    // Video Player

    private void initVideoPlayer(){
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);

        startDLNAService();
    }


    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(com.android.tedcoder.wkvideoplayer.view.MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(com.android.tedcoder.wkvideoplayer.view.MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };

    @Override
    public void onClick(View view) {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);

        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatName("720P");
       videoUrl1.setFormatUrl(videoUrl);
       // videoUrl1.setFormatUrl("http://dev.logiguys.com/lunavista/uploads/videos/7_CLIPCHAMP_240p.mp4");
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        video.setVideoName("Name");
        video.setVideoUrl(arrayList1);
        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);

        mSuperVideoPlayer.loadMultipleVideo(videoArrayList,0,0,0);
    }

    /***
     *
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNAService();
        unregisterReceiver(broadcastReceiver);
    }
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(com.android.tedcoder.wkvideoplayer.view.MediaController.PageType.SHRINK);
        }
    }

    private void startDLNAService() {
        // Clear the device container.
        DLNAContainer.getInstance().clear();
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        startService(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        stopService(intent);
    }

}
