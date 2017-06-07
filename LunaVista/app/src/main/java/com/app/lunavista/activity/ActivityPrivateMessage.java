package com.app.lunavista.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.lunavista.Adapters.AdapterPrivateMessage;
import com.app.lunavista.Adapters.AdapterVideoCollection;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponse;
import com.app.lunavista.asyntask.AsyncPostDataResponseNoProgress;
import com.app.lunavista.interfaces.ConnectionDetector;
import com.app.lunavista.interfaces.ListenerPostData;
import com.app.lunavista.interfaces.OnCustomItemClicListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityPrivateMessage extends AppCompatActivity implements ListenerPostData, OnCustomItemClicListener {


    Context context;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ModelVideo> arrayListSongs;
    ModelVideo modelVideo;
    ConnectionDetector cd;
    AdapterPrivateMessage adapterPrivateMessage;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    String is_like = "", maxlistLength = "";
    private boolean loading = true;
    int favSongposition;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);

        context = this;
        init();
        arrayListSongs = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setListener();
        setSongsList();
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            favSongposition = position;
            if (arrayListSongs.get(position).getIsFavorite().equalsIgnoreCase("1")) {
                is_like = "0";
            } else {
                is_like = "1";
            }

            likeUnlikeSong();


        } else if (flag == 2) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Listen " + arrayListSongs.get(position).getSongTitle() + " in Luna Vista, Also find many new video features, so what are u waiting for. " + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        } else if (flag == 3) {

            Intent in = new Intent(context, PlayMyRecordings.class);
            // in.putExtra("songDetail", arrayListSongs.get(position).getSongDetail());
            in.putExtra("videoUrl", arrayListSongs.get(position).getSongVideoURL());
            //in.putExtra("videoUrl", "http://dev.logiguys.com/lunavista/uploads/videos/1.mp4");
            startActivityForResult(in, 32);

        }

    }

    private void likeUnlikeSong() {

        try {
            skipCount = 0;

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
                                "song_id", arrayListSongs.get(favSongposition).getSongId()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_like", is_like));


                new AsyncPostDataResponseNoProgress(ActivityPrivateMessage.this, 4, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_likeUnlikeSong));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setSongsList() {

        try {
            skipCount = 0;

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
                                "skip_count", skipCount + ""));

                new AsyncPostDataResponse(ActivityPrivateMessage.this, 1, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_songsList));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void getSongsListRefresh() {

        try {
            skipCount = 0;

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
                                "skip_count", skipCount + ""));

                new AsyncPostDataResponseNoProgress(ActivityPrivateMessage.this, 2, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_songsList));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 32 && resultCode == 23) {

            getSongsListRefresh();
        }
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getSongsListRefresh();
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

                    JSONObject data1 = commandResult.getJSONObject("data");

                    JSONObject songs = data1.getJSONObject("Songs");
                    maxlistLength = songs.getString("total");


                    JSONArray data = songs.getJSONArray("songs");

                    arrayListSongs.removeAll(arrayListSongs);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelVideo = new ModelVideo();
                        modelVideo.setSongDetail(jo.toString());
                        modelVideo.setSongId(jo.getString("songId"));
                        modelVideo.setSongTitle(jo.getString("songTitle"));
                        modelVideo.setViewCount(jo.getString("viewCount"));
                        modelVideo.setRowType(1);
                        modelVideo.setSongThumb(jo.getString("songThumb"));
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));

                        arrayListSongs.add(modelVideo);

                    }
                    adapterPrivateMessage = new AdapterPrivateMessage(context, this, arrayListSongs);
                    mRecyclerView.setAdapter(adapterPrivateMessage);
                    //   mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();


                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data1 = commandResult.getJSONObject("data");

                    JSONObject songs = data1.getJSONObject("Songs");
                    maxlistLength = songs.getString("total");

                    JSONArray data = songs.getJSONArray("songs");

                    arrayListSongs.removeAll(arrayListSongs);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelVideo = new ModelVideo();
                        modelVideo.setSongId(jo.getString("songId"));
                        modelVideo.setSongDetail(jo.toString());
                        modelVideo.setSongTitle(jo.getString("songTitle"));
                        modelVideo.setViewCount(jo.getString("viewCount"));
                        modelVideo.setRowType(1);
                        modelVideo.setSongThumb(jo.getString("songThumb"));
                        modelVideo.setSongVideoURL(jo.getString("recordingURL"));
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));

                        arrayListSongs.add(modelVideo);

                    }
                    adapterPrivateMessage = new AdapterPrivateMessage(context, this, arrayListSongs);
                    mRecyclerView.setAdapter(adapterPrivateMessage);
                    mSwipeRefreshLayout.setRefreshing(false);
                    AppUtils.setPlayList(context, data1.getJSONObject("Songs").toString());
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }


            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    arrayListSongs.get(favSongposition).setIsFavorite(is_like);

                    if (arrayListSongs.size() == 1) {

                        getSongsListRefresh();
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                    maxlistLength = Integer.parseInt(maxlistLength) - 1 + "";
                    arrayListSongs.remove(favSongposition);
                    adapterPrivateMessage.notifyDataSetChanged();
                    //   getSongsListRefresh();
                    //    mSwipeRefreshLayout.setRefreshing(true);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (position == 3) {
                loading = true;
                skipCount = skipCount - 10;
            }
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }


}
