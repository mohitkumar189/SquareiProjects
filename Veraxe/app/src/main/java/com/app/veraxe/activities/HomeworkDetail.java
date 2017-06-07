package com.app.veraxe.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.veraxe.R;
import com.app.veraxe.adapter.AdapterEventPhotoDetail;
import com.app.veraxe.adapter.AdapterHomeworkPhotoDetail;
import com.app.veraxe.asyncTask.CommonAsyncTaskHashmap;
import com.app.veraxe.interfaces.ApiResponse;
import com.app.veraxe.interfaces.ConnectionDetector;
import com.app.veraxe.interfaces.OnCustomItemClicListener;
import com.app.veraxe.model.ModelStudent;
import com.app.veraxe.utils.AppUtils;
import com.app.veraxe.utils.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 06-01-2016.
 */
public class HomeworkDetail extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {


    Context context;
    ModelStudent itemList;
    ArrayList<ModelStudent> arrayList;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;
    Toolbar toolbar;
    String homeworkId = "";
    TextView text_detail, text_date, text_event_name, text_stream;
    private BroadcastReceiver broadcastReceiver;
    RecyclerView mRecyclerView;
    AdapterHomeworkPhotoDetail adapterHomeworkPhotoDetail;
    RelativeLayout rl_zoomimage;
    ImageView image_zoom, img_back;
    ImageView background_image;
    boolean isImageVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeworkdetail);

        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cd = new ConnectionDetector(context);
        arrayList = new ArrayList<>();

        Intent in = getIntent();
        homeworkId = in.getStringExtra("homeworkId");

        setListener();
        homeworkDetail();

    }

    private void init() {

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        rl_zoomimage = (RelativeLayout) findViewById(R.id.rl_zoomimage);
        image_zoom = (ImageView) findViewById(R.id.image_zoom);
        img_back = (ImageView) findViewById(R.id.img_back);
        text_detail = (TextView) findViewById(R.id.text_detail);
        background_image = (ImageView) findViewById(R.id.background_image);
        text_date = (TextView) findViewById(R.id.text_date);
        text_stream = (TextView) findViewById(R.id.text_stream);
        text_event_name = (TextView) findViewById(R.id.text_event_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridlayoutManager = new GridLayoutManager(context, 2);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridlayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rl_zoomimage.getVisibility() == View.VISIBLE) {
                    rl_zoomimage.setVisibility(View.GONE);
                    return;
                } else {
                    finish();
                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }


    public void homeworkDetail() {

        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();

            hm.put("homework_id", homeworkId);
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.homework_detail);
            new CommonAsyncTaskHashmap(1, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {

            rl_zoomimage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(arrayList.get(position).getUrl()).into(image_zoom);
            isImageVisible = true;

        } else if (flag == 4) {

            Intent intent = new Intent(context, DownLoadFile.class);
            intent.putExtra(DownLoadFile.FILENAME, arrayList.get(position).getFilename());
            intent.putExtra(DownLoadFile.URL,
                    arrayList.get(position).getUrl());
            context.startService(intent);

            Toast.makeText(context, "Your file download is in progress", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_zoomimage.getVisibility() == View.VISIBLE) {
                rl_zoomimage.setVisibility(View.GONE);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void getResponse(int method, JSONObject response) {
        try {
            if (method == 1) {
                if (response.getString("response").equalsIgnoreCase("1")) {

                    JSONObject result = response.getJSONObject("result");

                    text_stream.setText(result.getString("subject_name"));
                    text_event_name.setText(result.getString("subject_name"));
                    text_detail.setText(result.getString("text"));
                    text_date.setText(result.getString("date_start") + " - " + result.getString("date_end"));

                    JSONArray photos = result.getJSONArray("files");
                    for (int i = 0; i < photos.length(); i++) {

                        JSONObject jo = photos.getJSONObject(i);
                        itemList = new ModelStudent();
                        //  itemList.setId(jo.getString("id"));
                        itemList.setFilename(jo.getString("file"));
                        itemList.setUrl(jo.getString("url"));
                        itemList.setRowType(1);
                        arrayList.add(itemList);
                    }
                    adapterHomeworkPhotoDetail = new AdapterHomeworkPhotoDetail(context, this, arrayList);
                    mRecyclerView.setAdapter(adapterHomeworkPhotoDetail);
                    Picasso.with(context).load(result.getString("banner")).into(background_image);
//                    "result": {
//                        "id": 1,
//                                "title": "Diwali Function",
//                                "description": "Dewali .....",
//                                "start_date": "29/10/2016",
//                                "end_date": "29/10/2016",
//                                "photos": [
//                        {
//                            "id": "10",
//                                "filename": "982fed236637cb9d698ec4e73f6b3b74.jpg",
//                                "url": " http://manage.veraxe.com /uploads/school/event/photo/220/982fed236637cb9d698ec4e73f6b3b74.jpg"
//                        }
//                        ],
//                        "videos": []
//                    }


                } else {

                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
