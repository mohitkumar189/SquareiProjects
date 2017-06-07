package com.app.lunavista.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.lunavista.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Logiguyz on 7/18/2016.
 */
public class ActivityRecordVideo extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Button capture;
    private ImageView switchCamera, button_volume;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = true;
    private String videoPath = "", videoUrl = "", songId = "", downloadVideoUrl = "";
    VideoView video_view;
    private MediaController mediaControls;
    BroadcastReceiver recieveBroadcast;
    ImageView img_loading1, img_loading2, img_loading3;
    // ProgressDialog dialog;
    RelativeLayout rl_download_video, rl_record_video, rl_playvideo;
    MediaPlayer mp1;
    int count = 0;
    Boolean isVolumeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video1);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        initialize();
        // dialog = ProgressDialog.show(myContext, "", "Preparing...");
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/

        Log.e("requestFocus", "cameraPreview");
        cameraPreview.requestFocus();

       /*     }
        }, 10000);*/
        Intent in = getIntent();
        videoUrl = in.getExtras().getString("videoUrl");
        songId = in.getExtras().getString("songId");
        String fileName = "LunaVideo" + songId + ".mp4";
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
       /* if (!file.exists()) {*/
        Intent intent = new Intent(myContext, DownLoadFile.class);
        intent.putExtra(DownLoadFile.FILENAME, fileName);
        intent.putExtra(DownLoadFile.URL, videoUrl);
        myContext.startService(intent);
        rl_download_video.setVisibility(View.VISIBLE);
        rl_record_video.setVisibility(View.GONE);
        showProgress();
        setListener();
        registerBroadcastReciever();
       /* } else {
            downloadVideoUrl = file.getAbsolutePath();
            rl_download_video.setVisibility(View.GONE);
            rl_record_video.setVisibility(View.VISIBLE);
            capture.setEnabled(true);
            capture.setBackgroundResource(R.drawable.recording_btn);*/
          /* *//* if (dialog != null)
                dialog.cancel();*//*
        }*/

        Log.e("downloadVideoUrl", "****" + downloadVideoUrl);

        // DownloadFile(videoUrl, "LunaVideo.mp4");
        if (mediaControls == null) {
            mediaControls = new MediaController(ActivityRecordVideo.this);

        }

    }

    private void registerBroadcastReciever() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("service receiver");

        recieveBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                downloadVideoUrl = intent.getExtras().getString("filepath");

                rl_download_video.setVisibility(View.GONE);
                rl_record_video.setVisibility(View.VISIBLE);
                button_volume.setVisibility(View.VISIBLE);
                capture.setVisibility(View.VISIBLE);
                capture.setBackgroundResource(R.drawable.recording_btn);
                capture.setEnabled(true);
              /*  if (dialog != null)
                    dialog.cancel();*/
            }
        };

        registerReceiver(recieveBroadcast, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recieveBroadcast != null) {
            unregisterReceiver(recieveBroadcast);
        }
    }

    private void setListener() {


        button_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVolumeOn) {
                    Log.e("mute sound", "only music");

                    if (mp1 != null) {
                        button_volume.setImageResource(R.drawable.vocal_btn);
                        mp1.setVolume(1f, 0f);
                        button_volume.setAlpha(1.0f);
                        isVolumeOn = false;
                    }
                } else {
                    Log.e("on sound", "no sound");

                    if (mp1 != null) {
                        button_volume.setImageResource(R.drawable.vocal_btn);
                        button_volume.setAlpha(0.5f);
                        mp1.setVolume(1f, 1f);
                        isVolumeOn = true;
                    }
                }

            }
        });
    }


    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
                mCamera = Camera.open(findBackFacingCamera());
            } else {
                mCamera = Camera.open(findFrontFacingCamera());
            }
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        video_view = (VideoView) findViewById(R.id.video_view);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        capture = (Button) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);
        rl_download_video = (RelativeLayout) findViewById(R.id.rl_download_video);
        rl_playvideo = (RelativeLayout) findViewById(R.id.rl_playvideo);
        rl_record_video = (RelativeLayout) findViewById(R.id.rl_record_video);
        capture.setEnabled(false);

        switchCamera = (ImageView) findViewById(R.id.image_ChangeCamera);
        img_loading1 = (ImageView) findViewById(R.id.img_loading1);
        img_loading2 = (ImageView) findViewById(R.id.img_loading2);
        img_loading3 = (ImageView) findViewById(R.id.img_loading3);
        button_volume = (ImageView) findViewById(R.id.button_volume);
        switchCamera.setOnClickListener(switchCameraListener);


    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {
                    Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };

    public void chooseCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            //   switchCamera.setImageResource(R.drawable.front_camera);
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            //switchCamera.setImageResource(R.drawable.back_camera);
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    boolean recording = false;
    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rl_download_video.setVisibility(View.GONE);
            rl_record_video.setVisibility(View.GONE);
            //  button_volume.setVisibility(View.GONE);
            rl_playvideo.setVisibility(View.VISIBLE);
            video_view.setVisibility(View.VISIBLE);
            //  video_view.setMediaController(mediaControls);
            Log.e("downloadVideoUrlVideo", "****" + downloadVideoUrl);
            video_view.setVideoPath(downloadVideoUrl);

            MediaController mc = new MediaController(myContext);
            mc.setAnchorView(video_view);
            mc.setMediaPlayer(video_view);
            video_view.setMediaController(mc);

            video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp1 = mp;
                    mp.setVolume(1f, 0f);
                    video_view.start();
                }
            });

            //mediaControls.setAnchorView(video_view);
            if (recording) {
                capture.setBackgroundResource(R.drawable.recording_btn);

                //   capture.setText("REC");
                // stop recording and release camera

                if (mediaRecorder != null)
                    mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                // Toast.makeText(ActivityRecordVideo.this, "Video captured!", Toast.LENGTH_LONG).show();
                recording = false;

                finish();
                Intent i = new Intent(ActivityRecordVideo.this, PlayMyVideo.class);
                i.putExtra("videoPath", videoPath);
                i.putExtra("songId", songId);
                startActivity(i);

            } else {
                capture.setBackgroundResource(R.drawable.stop);


                //  capture.setText("STOP");
                if (!prepareMediaRecorder()) {
                    Toast.makeText(ActivityRecordVideo.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table
                        try {

                            mediaRecorder.start();
                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });
                recording = true;
            }
        }
    };

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    private boolean prepareMediaRecorder() {

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "LunaVista");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate(250000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoFrameRate(24);
        //mediaRecorder.setVideoSize(320, 240);
        mediaRecorder.setVideoSize(640, 480);

      /*
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);*/

        //  mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        // mediaRecorder.setVideoSize(640, 480);
/*

        mediaRecorder.setVideoFrameRate(16); //might be auto-determined due to lighting
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// MPEG_4_SP
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
*/

        videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LunaVista" + File.separator + "lunavista_rec" + System.currentTimeMillis() + ".mp4";
        mediaRecorder.setOutputFile(videoPath);

        // mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
       /* mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
*/
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    private void showProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (count % 3 == 0) {
                    img_loading1.setAlpha(0.4f);
                    img_loading2.setAlpha(1f);
                    img_loading3.setAlpha(1f);
                } else if (count % 3 == 1) {
                    img_loading1.setAlpha(1f);
                    img_loading2.setAlpha(0.4f);
                    img_loading3.setAlpha(1f);
                } else if (count % 3 == 2) {
                    img_loading1.setAlpha(1f);
                    img_loading2.setAlpha(1f);
                    img_loading3.setAlpha(0.4f);
                }
                count++;
                showProgress();
            }
        }, 500);

    }


}
