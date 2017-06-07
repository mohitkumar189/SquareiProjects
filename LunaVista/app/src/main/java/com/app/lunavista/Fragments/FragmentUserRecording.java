package com.app.lunavista.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.lunavista.Adapters.AdapterAllSong;
import com.app.lunavista.Adapters.AdapterUserRecording;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.activity.PlaySongVideo;
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

public class FragmentUserRecording extends Fragment implements ListenerPostData, OnCustomItemClicListener, SearchView.OnQueryTextListener {

    Context context;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ModelVideo> arrayListSongs;
    ModelVideo modelVideo;
    ConnectionDetector cd;
    AdapterUserRecording adapterUserRecording;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    String is_like = "", maxlistLength = "";
    private boolean loading = true;
    int favSongposition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.user_recording_list, container, false);
        context = getActivity();
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        arrayListSongs = new ArrayList<>();

        setListener();
        setSongsList();

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

                new AsyncPostDataResponseNoProgress(FragmentUserRecording.this, 1, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_getPublicRecordingList));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {
            ModelVideo model = adapterUserRecording.getFilter(position);
            favSongposition = position;
            Log.e("like songPosition", "**"+ adapterUserRecording.getFilter(favSongposition).getSongId());
            if (model.getIsFavorite().equalsIgnoreCase("1")) {
                is_like = "0";
            } else {
                is_like = "1";
            }

            likeUnlikeSong();

        } else if (flag == 2) {
            ModelVideo model = adapterUserRecording.getFilter(position);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Listen " + model.getSongTitle() + " in Luna Vista, Also find many new video features, so what are u waiting for. " + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        } else if (flag == 3) {
            ModelVideo model = adapterUserRecording.getFilter(position);
            Intent in = new Intent(context, PlaySongVideo.class);
            in.putExtra("songDetail", model.getSongDetail());
            if (model.getIsRecording().equalsIgnoreCase("1")) {

                in.putExtra("videoUrl", model.getRecordingURL());
            } else {
                in.putExtra("videoUrl", model.getSongVideoURL());
            }
            in.putExtra("isRecording", model.getIsRecording());
            startActivityForResult(in, 32);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 32 && resultCode == 23) {

            getSongsListRefresh();
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
                                "song_id", adapterUserRecording.getFilter(favSongposition).getSongId()));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_recording", adapterUserRecording.getFilter(favSongposition).getIsRecording()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_like", is_like));


                new AsyncPostDataResponseNoProgress(FragmentUserRecording.this, 4, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_likeUnlikeSong));
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
             /*   String data = URLEncoder.encode("user_id", "UTF-8")
                        + "=" + URLEncoder.encode(AppUtils.getUserId(context), "UTF-8");

                data += "&" + URLEncoder.encode("skip_count", "UTF-8") + "="
                        + URLEncoder.encode(skipCount + "", "UTF-8");*/
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skip_count", skipCount + ""));

                new AsyncPostDataResponseNoProgress(FragmentUserRecording.this, 2, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_getPublicRecordingList));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void init() {

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

    }

    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getSongsListRefresh();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(arrayListSongs.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    modelVideo = new ModelVideo();
                                    modelVideo.setRowType(2);
                                    arrayListSongs.add(modelVideo);
                                    adapterUserRecording.notifyDataSetChanged();

                                    skipCount = skipCount + 10;
                                    Log.v("...", "Last Item Wow !");

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "user_id", AppUtils.getUserId(context)));


                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skip_count", skipCount + ""));
                                        ;

                                        new AsyncPostDataResponseNoProgress(FragmentUserRecording.this, 3, nameValuePairs,
                                                getString(R.string.url_base)
                                                        + getString(R.string.url_getPublicRecordingList));

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    }
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

                    JSONObject data1 = commandResult.getJSONObject("data");

                    JSONObject songs = data1.getJSONObject("Songs");
                    maxlistLength = songs.getString("total");


                    JSONArray data = songs.getJSONArray("songs");

                    arrayListSongs.removeAll(arrayListSongs);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelVideo = new ModelVideo();
                        modelVideo.setSongId(jo.getString("songId"));
                        modelVideo.setSongThumb(jo.getString("songThumb"));
                        modelVideo.setSongDetail(jo.toString());
                        modelVideo.setSongTitle(jo.getString("songTitle"));
                        modelVideo.setSongVideoURL(jo.getString("songVideoURL"));
                        modelVideo.setSongDescription(jo.getString("songDescription"));
                        modelVideo.setIsRecording(jo.getString("isRecording"));
                        modelVideo.setViewCount(jo.getString("viewCount"));
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));
                        modelVideo.setRowType(1);

                        modelVideo.setSongAspectRatio(jo.getString("songAspectRatio"));
                        modelVideo.setRecordingRemarks(jo.getString("recordingRemarks"));
                        modelVideo.setRecordingURL(jo.getString("recordingURL"));
                        modelVideo.setRecordingThumbURL(jo.getString("recordingThumbURL"));
                        modelVideo.setCreated_on(jo.getString("created_on"));


                        arrayListSongs.add(modelVideo);

                    }
                    adapterUserRecording = new AdapterUserRecording(context, this, arrayListSongs);
                    mRecyclerView.setAdapter(adapterUserRecording);
                    //   mSwipeRefreshLayout.setRefreshing(false);
                    AppUtils.setPublicrecordings(context, data1.toString());

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
                        modelVideo.setSongThumb(jo.getString("songThumb"));
                        modelVideo.setSongTitle(jo.getString("songTitle"));
                        modelVideo.setSongVideoURL(jo.getString("songVideoURL"));
                        modelVideo.setSongDetail(jo.toString());
                        modelVideo.setIsRecording(jo.getString("isRecording"));
                        modelVideo.setSongDescription(jo.getString("songDescription"));
                        modelVideo.setViewCount(jo.getString("viewCount"));
                        modelVideo.setRowType(1);
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));
                        modelVideo.setSongAspectRatio(jo.getString("songAspectRatio"));
                        modelVideo.setRecordingRemarks(jo.getString("recordingRemarks"));
                        modelVideo.setRecordingURL(jo.getString("recordingURL"));
                        modelVideo.setRecordingThumbURL(jo.getString("recordingThumbURL"));
                        modelVideo.setCreated_on(jo.getString("created_on"));

                        arrayListSongs.add(modelVideo);

                    }
                    adapterUserRecording = new AdapterUserRecording(context, this, arrayListSongs);
                    mRecyclerView.setAdapter(adapterUserRecording);
                    mSwipeRefreshLayout.setRefreshing(false);
                    AppUtils.setPublicrecordings(context, data1.toString());

                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data1 = commandResult.getJSONObject("data");

                    JSONObject songs = data1.getJSONObject("Songs");
                    maxlistLength = songs.getString("total");

                    JSONArray data = songs.getJSONArray("songs");
                    arrayListSongs.remove(arrayListSongs.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelVideo = new ModelVideo();
                        modelVideo.setSongId(jo.getString("songId"));
                        modelVideo.setSongThumb(jo.getString("songThumb"));
                        modelVideo.setSongDetail(jo.toString());
                        modelVideo.setSongTitle(jo.getString("songTitle"));
                        modelVideo.setSongVideoURL(jo.getString("songVideoURL"));
                        modelVideo.setIsRecording(jo.getString("isRecording"));
                        modelVideo.setSongDescription(jo.getString("songDescription"));
                        modelVideo.setViewCount(jo.getString("viewCount"));
                        modelVideo.setRowType(1);
                        modelVideo.setIsFavorite(jo.getString("isFavorite"));
                        modelVideo.setSongAspectRatio(jo.getString("songAspectRatio"));
                        modelVideo.setRecordingRemarks(jo.getString("recordingRemarks"));
                        modelVideo.setRecordingURL(jo.getString("recordingURL"));
                        modelVideo.setRecordingThumbURL(jo.getString("recordingThumbURL"));
                        modelVideo.setCreated_on(jo.getString("created_on"));


                        arrayListSongs.add(modelVideo);
                    }
                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                    loading = true;
                    adapterUserRecording.notifyDataSetChanged();

                } else {

                    adapterUserRecording.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                    //    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    adapterUserRecording.getFilter(favSongposition).setIsFavorite(is_like);
                    adapterUserRecording.notifyDataSetChanged();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        inflater.inflate(R.menu.menu_splash, menu);

        final MenuItem item = menu.findItem(R.id.searchview);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(FragmentUserRecording.this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        if (arrayListSongs.size() > 0) {
                            adapterUserRecording.setFilter(arrayListSongs);
                        }

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.searchview) {


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (arrayListSongs.size() > 0) {
            final ArrayList<ModelVideo> filteredModelList = filter(arrayListSongs, query);
            adapterUserRecording.setFilter(filteredModelList);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (arrayListSongs.size() > 0) {
            final ArrayList<ModelVideo> filteredModelList = filter(arrayListSongs, newText);
            adapterUserRecording.setFilter(filteredModelList);
        }
        return true;
    }

    private ArrayList<ModelVideo> filter(ArrayList<ModelVideo> models, String query) {
        query = query.toLowerCase();

        final ArrayList<ModelVideo> filteredModelList = new ArrayList<>();
        for (ModelVideo model : models) {
            final String text = model.getSongTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


}
