package com.app.justclap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.justclap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.activities.ActivityChat;
import com.app.justclap.activities.ActivityChatOld;
import com.app.justclap.adapters.AdapterChats;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelContactList;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Chat extends Fragment implements ListenerPostData, OnCustomItemClicListener {


    RecyclerView list_request;
    Bundle b;
    Context context;
    AdapterChats adapterChats;
    ModelContactList modelChat;
    ArrayList<ModelContactList> arrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RelativeLayout rl_main_layout, rl_network;
    ConnectionDetector cd;
    LinearLayout linear_empty_list;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;
    String maxlistLength = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_about = inflater.inflate(R.layout.fragment_chat, container, false);
        context = getActivity();
        arrayList = new ArrayList<>();
        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linear_empty_list = (LinearLayout) view.findViewById(R.id.linear_empty_list);
        rl_main_layout = (RelativeLayout) view.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view.findViewById(R.id.rl_network);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        list_request = (RecyclerView) view.findViewById(R.id.list_request);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);

        setlistener();

        if (!AppUtils.getUserIdChat(context).equalsIgnoreCase("")) {
            if (context != null && isAdded()) {
                getServicelist();
            }
        } else {
            linear_empty_list.setVisibility(View.VISIBLE);
        }

    }

    private void setlistener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getServicelistRefresh();
            }
        });

        list_request.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(arrayList.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    modelChat = new ModelContactList();
                                    modelChat.setRowType(2);
                                    arrayList.add(modelChat);
                                    adapterChats.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "receiverID", AppUtils.getUserIdChat(context)));

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));

                                        new AsyncPostDataFragmentNoProgress(getActivity(), Fragment_Chat.this, 4, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.chatList));

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + arrayList.size());
                    }
                }
            }
        });

    }

    @Override
    public void onItemClickListener(int position, int flag) {

        Intent in = new Intent(context, ActivityChat.class);
        if (arrayList.get(position).getUserId().equalsIgnoreCase(AppUtils.getUserIdChat(context))) {
            in.putExtra("reciever_id", arrayList.get(position).getSenderID());
        } else {
            in.putExtra("reciever_id", arrayList.get(position).getUserId());
        }
        in.putExtra("name", arrayList.get(position).getSenderName());
        in.putExtra("image", arrayList.get(position).getReceiverImage());
        in.putExtra("searchID", arrayList.get(position).getSearchId());
        startActivity(in);


    }

    private void getServicelist() {

        try {

            skipCount = 0;

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                //  receiverID,skipCount

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "receiverID", AppUtils.getUserIdChat(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));

                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.chatList));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void getServicelistRefresh() {

        try {
            skipCount = 0;
            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getUserIdChat(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "receiverID", AppUtils.getUserIdChat(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                new AsyncPostDataFragmentNoProgress(getActivity(), (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.chatList));

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

                    JSONArray data = commandResult.getJSONArray("data");
                    maxlistLength = commandResult.getString("total");
                    arrayList.removeAll(arrayList);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelChat = new ModelContactList();

                        modelChat.setUserId(jo.getString("receiverID"));
                        modelChat.setSenderID(jo.getString("senderID"));
                        modelChat.setSenderID(jo.getString("senderID"));
                        modelChat.setUsername(jo.getString("receiverName"));
                        modelChat.setMessage(jo.getString("message"));
                        modelChat.setSearchId(jo.getString("searchID"));
                        modelChat.setSenderName(jo.getString("senderName"));
                        modelChat.setReceiverImage(getResources().getString(R.string.img_url) +jo.getString("senderImage"));
                        modelChat.setRequestId(jo.getString("requestID"));
                        modelChat.setUnreadCount(jo.getString("unreadCount"));
                        modelChat.setIs_read(jo.getString("is_read"));
                        modelChat.setRowType(1);


                        modelChat.setDate(jo.getString("message_date"));
                        modelChat.setUserImage(getResources().getString(R.string.img_url) + jo.getString("receiverImage"));
                        arrayList.add(modelChat);
                    }
                    adapterChats = new AdapterChats(getActivity(), this, arrayList);
                    list_request.setAdapter(adapterChats);
                    //   mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (arrayList.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    linear_empty_list.setVisibility(View.VISIBLE);
                    //    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }
            } else if (position == 4) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    JSONArray data = commandResult.getJSONArray("data");

                    arrayList.remove(arrayList.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelChat = new ModelContactList();
                        modelChat.setUserId(jo.getString("receiverID"));
                        modelChat.setSenderID(jo.getString("senderID"));
                        modelChat.setUsername(jo.getString("receiverName"));
                        modelChat.setMessage(jo.getString("message"));
                        modelChat.setSearchId(jo.getString("searchID"));
                        modelChat.setSenderName(jo.getString("senderName"));
                        modelChat.setUnreadCount(jo.getString("unreadCount"));
                        modelChat.setIs_read(jo.getString("is_read"));
                        modelChat.setReceiverImage(jo.getString("receiverImage"));
                        modelChat.setRequestId(jo.getString("requestID"));
                        modelChat.setRowType(1);
                        modelChat.setDate(jo.getString("message_date"));
                        //   modelChat.setUserImage(getResources().getString(R.string.img_url) + jo.getString("userImage"));
                        arrayList.add(modelChat);
                    }
                    adapterChats.notifyDataSetChanged();
                    loading = true;
                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                } else {

                    adapterChats.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        if (context != null && isAdded())
            AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
    }

}
