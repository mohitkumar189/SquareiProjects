package com.app.lunavista.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.lunavista.Adapters.AdapterVideoCollection;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataFileResponse;
import com.app.lunavista.asyntask.AsyncPostDataFileResponseFragment;
import com.app.lunavista.interfaces.ListenerPostData;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class UploadVideo extends AppCompatActivity implements ListenerPostData {


    TextView text_name, text_private, text_title, text_public, text_description;
    Context context;
    Toolbar toolbar;
    Bitmap thumb, thumb1, thumb2, thumb3, thumb4;
    String videoPath = "", songId = "", thumbPath = "";
    ImageView image_thumbnail, image_thumbnail1, image_thumbnail2, image_thumbnail3, image_thumbnail4;
    ImageView thumb_private, thumb_public, imag_switch;
    String is_public = "0";
    CheckBox checkbox_public;
    MediaMetadataRetriever retriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        context = this;
        initialize();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Save your recording");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListener();

        Intent in = getIntent();
        videoPath = in.getExtras().getString("videoUrl");
        songId = in.getExtras().getString("songId");
        getVideoThumbnail();

    }

    public void initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        text_name = (TextView) findViewById(R.id.text_name);
        text_title = (TextView) findViewById(R.id.text_title);
        text_private = (TextView) findViewById(R.id.text_private);
        text_public = (TextView) findViewById(R.id.text_public);
        text_description = (TextView) findViewById(R.id.text_description);
        thumb_private = (ImageView) findViewById(R.id.thumb_private);
        thumb_public = (ImageView) findViewById(R.id.thumb_public);
        imag_switch = (ImageView) findViewById(R.id.imag_switch);
        image_thumbnail = (ImageView) findViewById(R.id.image_thumbnail);
        image_thumbnail1 = (ImageView) findViewById(R.id.image_thumbnail1);
        image_thumbnail2 = (ImageView) findViewById(R.id.image_thumbnail2);
        image_thumbnail3 = (ImageView) findViewById(R.id.image_thumbnail3);
        image_thumbnail4 = (ImageView) findViewById(R.id.image_thumbnail4);
        checkbox_public = (CheckBox) findViewById(R.id.checkbox_public);

    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        checkbox_public.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    is_public = "1";
                } else {
                    is_public = "0";
                }

            }
        });

        imag_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_public.equalsIgnoreCase("0")) {

                    thumb_public.setVisibility(View.VISIBLE);
                    thumb_private.setVisibility(View.GONE);
                    is_public = "1";
                    text_public.setTextColor(getResources().getColor(R.color.colorPrimary));
                    text_private.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

                } else {

                    thumb_private.setVisibility(View.VISIBLE);
                    thumb_public.setVisibility(View.GONE);
                    is_public = "0";
                    text_private.setTextColor(getResources().getColor(R.color.colorPrimary));
                    text_public.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

                }

            }
        });


        text_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thumb_private.setVisibility(View.VISIBLE);
                thumb_public.setVisibility(View.GONE);
                is_public = "0";
                text_private.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_public.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

            }
        });
        text_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thumb_public.setVisibility(View.VISIBLE);
                thumb_private.setVisibility(View.GONE);
                is_public = "1";
                text_public.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_private.setTextColor(getResources().getColor(R.color.txtbox_text_color_darek));

            }
        });

        ((Button) findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text_name.getText().toString().equalsIgnoreCase("")) {
                    uploadFile();
                } else {
                    Toast.makeText(context, "Please input description.", Toast.LENGTH_LONG).show();
                }
            }
        });

        image_thumbnail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_thumbnail.setImageBitmap(thumb);
                thumbPath = savebitmap(thumb);
            }
        });
        image_thumbnail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_thumbnail.setImageBitmap(thumb2);
                thumbPath = savebitmap(thumb2);
            }
        });
        image_thumbnail3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_thumbnail.setImageBitmap(thumb3);
                thumbPath = savebitmap(thumb3);
            }
        });
        image_thumbnail4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_thumbnail.setImageBitmap(thumb4);
                thumbPath = savebitmap(thumb4);
            }
        });


    }

    public void getVideoThumbnail() {
        thumb = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);

        image_thumbnail.setImageBitmap(thumb);
        thumbPath = savebitmap(thumb);
        retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);

           /* thumb = retriever.getFrameAtTime(30 * 10000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC);*/
            image_thumbnail1.setImageBitmap(thumb);
            thumb2 = retriever.getFrameAtTime(2 * 100000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            image_thumbnail2.setImageBitmap(thumb2);
            thumb3 = retriever.getFrameAtTime(3 * 100000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            image_thumbnail3.setImageBitmap(thumb3);
            thumb4 = retriever.getFrameAtTime(5 * 100000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            image_thumbnail4.setImageBitmap(thumb4);


        } catch (Exception ex) {
            Log.i("MyDebugCode", "MediaMetadataRetriever got exception:" + ex);
        }


    }


    private void uploadFile() {
        Charset encoding = Charset.forName("UTF-8");
        MultipartEntity reqEntity = new MultipartEntity();
        try {


            Log.e("videoPath", "***" + videoPath);
            Log.e("thumbPath", "***" + thumbPath);

            StringBody sb1 = new StringBody(AppUtils.getUserId(context), encoding);
            StringBody sb11 = new StringBody(songId, encoding);
            StringBody sb111 = new StringBody(text_name.getText().toString(), encoding);
            StringBody ispublic = new StringBody(is_public, encoding);
            FileBody videoFile = new FileBody(new File(videoPath));
            FileBody imageFile = new FileBody(new File(thumbPath));

            reqEntity.addPart("recording", videoFile);
            reqEntity.addPart("thumbnail", imageFile);
            reqEntity.addPart("user_id", sb1);
            reqEntity.addPart("song_id", sb11);
            reqEntity.addPart("remarks", sb111);
            reqEntity.addPart("is_public", ispublic);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

				/*if (!selectedPath1.equalsIgnoreCase("")) {*/

        try {

            if (AppUtils.isNetworkAvailable(context)) {
                String url = getString(R.string.url_base)
                        + getString(R.string.url_submitRecording);
                new AsyncPostDataFileResponse(context, 1, reqEntity, url);
            } else {
                Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }

    }

    private String savebitmap(Bitmap bmp) {
        File file = null;
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/LunaVista";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            file = new File(dir, "tuhmb" + System.currentTimeMillis() + ".png");
            FileOutputStream fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return "";
        }
    }

    @Override
    public void onPostRequestSucess(int position, String response) {
        try {
            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);


                    Intent in = new Intent(context, Home.class);
                    startActivity(in);
                    finish();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}
