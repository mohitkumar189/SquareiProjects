package com.app.justclap.fragment;

import android.content.Context;
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

import com.app.justclap.adapters.AdapterCancelledBooking;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;


public class FragmentCancelledBooking extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    RecyclerView list_request;
    AdapterCancelledBooking adapterResponses;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    Context context;
    LinearLayout linear_empty_list;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_canceled_booking, container, false);
        context = getActivity();
        service_list = new ArrayList<>();
        linear_empty_list = (LinearLayout) view_service.findViewById(R.id.linear_empty_list);
        rl_main_layout = (RelativeLayout) view_service.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view_service.findViewById(R.id.rl_network);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        list_request = (RecyclerView) view_service.findViewById(R.id.list_request);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);

        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
            getServicelist();
        }

        setListener();

        return view_service;
    }

    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getServicelistrefresh();

            }
        });

        list_request.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(service_list.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    serviceDetail = new ModelService();
                                    serviceDetail.setRowType(2);
                                    service_list.add(serviceDetail);
                                    adapterResponses.notifyDataSetChanged();

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

                                        new AsyncPostDataFragmentNoProgress(getActivity(), FragmentCancelledBooking.this, 4, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getCustomerCancelServices));

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + service_list.size());
                    }
                }
            }
        });


    }


    private void getServicelist() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

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
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));
                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getCustomerCancelServices));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getServicelistrefresh() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

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
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));


                new AsyncPostDataFragmentNoProgress(getActivity(), (ListenerPostData) this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getCustomerCancelServices));

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

                    JSONObject data = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    JSONArray services = data.getJSONArray("details");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setCancel_reason(jo.getString("cancel_reason"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setServiceID(jo.getString("serviceID"));


                        service_list.add(serviceDetail);
                    }

                    adapterResponses = new AdapterCancelledBooking(getActivity(), this, service_list);
                    list_request.setAdapter(adapterResponses);
                    //   mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }


                } else {
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    linear_empty_list.setVisibility(View.VISIBLE);
                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    JSONObject data = commandResult.getJSONObject("data");

                    JSONArray services = data.getJSONArray("details");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setCancel_reason(jo.getString("cancel_reason"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));

                        service_list.add(serviceDetail);
                    }

                    adapterResponses = new AdapterCancelledBooking(getActivity(), this, service_list);
                    list_request.setAdapter(adapterResponses);
                    mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }


                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    linear_empty_list.setVisibility(View.VISIBLE);
                }


            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    JSONArray services = data.getJSONArray("details");
                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setCancel_reason(jo.getString("cancel_reason"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));

                        service_list.add(serviceDetail);
                    }

                    loading = true;

                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                    adapterResponses.notifyDataSetChanged();

                } else {
                    adapterResponses.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                }
            }
        } catch (JSONException e) {
            if (position == 4) {
                loading = true;
                skipCount = skipCount - 1;
            }
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server)
        );
    }
}
