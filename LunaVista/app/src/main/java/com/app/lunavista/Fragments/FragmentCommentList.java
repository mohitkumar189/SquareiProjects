package com.app.lunavista.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.lunavista.Adapters.AdapterCommentList;
import com.app.lunavista.Adapters.AdapterPlayList;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponseNoProgress;
import com.app.lunavista.customview.ExpandableHeightListView;
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

public class FragmentCommentList extends Fragment implements ListenerPostData {

    ExpandableHeightListView listComments;
    ArrayList<ModelVideo> arrayListComments;
    ModelVideo modelVideo;
    Context context;
    AdapterCommentList adapterCommentList;
    ConnectionDetector cd;
    Bundle b;
    String song_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        listComments = (ExpandableHeightListView) view.findViewById(R.id.list_comment);
        arrayListComments = new ArrayList<>();
        b = getArguments();
        song_id = b.getString("song_id");

        getComments();

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
                                "song_id", song_id));

                new AsyncPostDataResponseNoProgress(FragmentCommentList.this, 1, nameValuePairs,
                        getString(R.string.url_base)
                                + getString(R.string.url_songDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();

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

                    JSONObject data1 = commandResult.getJSONObject("data");

                    JSONArray comments = data1.getJSONArray("comments");

                    arrayListComments.removeAll(arrayListComments);
                    for (int i = 0; i < comments.length(); i++) {

                        JSONObject jo = comments.getJSONObject(i);

                        modelVideo = new ModelVideo();

                        modelVideo.setName(jo.getString("commentBy"));
                        modelVideo.setCommentId(jo.getString("commentId"));
                        modelVideo.setComment(jo.getString("comment"));
                        modelVideo.setDate(jo.getString("commentOn"));
                        modelVideo.setSongThumb(jo.getString("profileImage"));

                        arrayListComments.add(modelVideo);

                    }

                  /*  adapterCommentList = new AdapterCommentList(context, arrayListComments);
                    listComments.setAdapter(adapterCommentList);*/


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

}
