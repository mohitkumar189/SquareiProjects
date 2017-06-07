package com.app.justclap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterNotification;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Notification extends Fragment implements OnCustomItemClicListener, ListenerPostData {


    Bundle b;
    Context context;
    RecyclerView mRecyclerView;
    ModelService itemList;
    AdapterNotification adapterNotification;
    ArrayList<ModelService> arrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;
    LinearLayout linear_empty_list;
    String maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_about = inflater.inflate(R.layout.fragment_notification, container, false);
        context = getActivity();
        cd = new ConnectionDetector(context);
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        if (!AppUtils.getUserIdChat(context).equalsIgnoreCase("")) {
            if (context != null && isAdded()) {
                NotifaicationList();
            }
        } else {
            linear_empty_list.setVisibility(View.VISIBLE);
        }

        setListener();

    }

    public void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                refreshnotification();
                // refreshStudymaterial();
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

                                    itemList = new ModelService();
                                    itemList.setRowType(2);
                                    arrayList.add(itemList);
                                    adapterNotification.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "userID", AppUtils.getUserId(context)));

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));

                                        new AsyncPostDataFragmentNoProgress(getActivity(), Fragment_Notification.this, 3, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.notificationsList));

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


    public void NotifaicationList() {
        try {
            skipCount=0;
            // for gcm condition check
            // =================================================


            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                new AsyncPostDataResponse(Fragment_Notification.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.notificationsList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void refreshnotification() {
        try {
            skipCount=0;
            // for gcm condition check
            // =================================================

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                mSwipeRefreshLayout.setRefreshing(true);
                new AsyncPostDataResponseNoProgress(Fragment_Notification.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.notificationsList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostRequestSucess(int position, String response) {
        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    arrayList.clear();
                    JSONArray data = commandResult.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        itemList = new ModelService();
                        itemList.setServiceName(jo.getString("notification_text"));
                        itemList.setRowType(1);
                        itemList.setCreatedDate(jo.getString("date"));
                        arrayList.add(itemList);

                    }
                    adapterNotification = new AdapterNotification(context, this, arrayList);
                    mRecyclerView.setAdapter(adapterNotification);
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (arrayList.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {
                    linear_empty_list.setVisibility(View.VISIBLE);
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    arrayList.remove(arrayList.size() - 1);
                    JSONArray data = commandResult.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        itemList = new ModelService();
                        itemList.setServiceName(jo.getString("notification_text"));
                        itemList.setRowType(1);
                        itemList.setCreatedDate(jo.getString("date"));
                        arrayList.add(itemList);

                    }
                    adapterNotification.notifyDataSetChanged();
                    loading = true;

                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                } else {
                    loading = true;
                    adapterNotification.notifyDataSetChanged();


                    skipCount = skipCount - 10;


                }
            }
        } catch (Exception e) {
            if (position == 3) {
                loading = true;
                skipCount = skipCount - 10;
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}
