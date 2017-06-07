package com.app.justclap.vendor_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.ActivityChat;
import com.app.justclap.activities.ActivityShowRoute;
import com.app.justclap.activities.ViewDetailAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterVendorOnGoingLeads;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

/**
 * Created by admin on 12-02-2016.
 */
public class FragmentOngoingLeads extends Fragment implements OnCustomItemClicListener, ListenerPostData {


    RecyclerView mRecyclerView;
    AdapterVendorOnGoingLeads adapterVendorOnGoingLeads;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    ConnectionDetector cd;
    Context context;
    RelativeLayout rl_main_layout, rl_network;
    String service_name = "", service_id = "", maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 1;
    private boolean loading = true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout linear_empty_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewProfile = inflater.inflate(R.layout.fragment_vendor_quote_list, container, false);
        context = getActivity();

        return viewProfile;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rl_main_layout = (RelativeLayout) view.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view.findViewById(R.id.rl_network);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        linear_empty_list = (LinearLayout) view.findViewById(R.id.linear_empty_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        service_list = new ArrayList<>();


        if (!AppUtils.getvendorId(context).equalsIgnoreCase("")) {
            if (context != null && isAdded()) {
                hitService();
            }
        }
        setListener();
    }


    private void hitService() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                // AppUtils.showCustomAlert(Vendor_QuoteList.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));

                new AsyncPostDataFragment(getActivity(), FragmentOngoingLeads.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.ongoingLeadsOfVendor));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItems();

            }
        });
    }


    private void refreshItems() {

        try {
            skipCount = 0;

            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //  AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", "0"));


                new AsyncPostDataResponseNoProgress(FragmentOngoingLeads.this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.ongoingLeadsOfVendor));


            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    adapterVendorOnGoingLeads.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "vendorID", AppUtils.getvendorId(context)));
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));

                                        new AsyncPostDataResponseNoProgress(FragmentOngoingLeads.this, 3, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.ongoingLeadsOfVendor));

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

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 2) {

            String number = service_list.get(position).getMobileNumber();

            String uri = "tel:" + number.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);

        } else if (flag == 3) {

            Intent in = new Intent(context, ViewDetailAnswers.class);
            in.putExtra("service_id", service_id);
            in.putExtra("service_name", service_name);
            in.putExtra("user_id", service_list.get(position).getUserId());
            in.putExtra("searchId", service_list.get(position).getSearchId());
            startActivity(in);


        } else if (flag == 4) {
            Intent in = new Intent(context, ActivityChat.class);
            in.putExtra("reciever_id", service_list.get(position).getUserId());
            in.putExtra("name", service_list.get(position).getUsername());
            in.putExtra("image", service_list.get(position).getVendorimage());
            in.putExtra("searchID", service_list.get(position).getSearchId());
            startActivity(in);

        } else if (flag == 11) {

            Intent in = new Intent(context, ActivityShowRoute.class);
            in.putExtra("srcLat", service_list.get(position).getSourceLatitude());
            in.putExtra("srcLng", service_list.get(position).getSourceLongitude());
            in.putExtra("venLat", service_list.get(position).getVendorLatitude());
            in.putExtra("venLng", service_list.get(position).getVendorLongitude());
            startActivity(in);


        }
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {
            Log.e("position", position + "");
            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject main = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    service_id = main.getString("serviceID");
                    service_name = main.getString("serviceName");
                    JSONArray data = main.getJSONArray("userDetail");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setUserId(jo.getString("userID"));
                        serviceDetail.setUsername(jo.getString("userName"));
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("latitude"));
                        serviceDetail.setSourceLongitude(jo.getString("longitude"));
                        serviceDetail.setCreatedDate(jo.getString("date"));
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterVendorOnGoingLeads = new AdapterVendorOnGoingLeads(context, this, service_list);
                    mRecyclerView.setAdapter(adapterVendorOnGoingLeads);
                    mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {
                    service_list.removeAll(service_list);
                    mRecyclerView.setAdapter(null);
                    linear_empty_list.setVisibility(View.VISIBLE);
                    //   AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject main = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    service_id = main.getString("serviceID");
                    service_name = main.getString("serviceName");
                    JSONArray data = main.getJSONArray("userDetail");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setUserId(jo.getString("userID"));
                        serviceDetail.setUsername(jo.getString("userName"));
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setCreatedDate(jo.getString("date"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("latitude"));
                        serviceDetail.setSourceLongitude(jo.getString("longitude"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterVendorOnGoingLeads = new AdapterVendorOnGoingLeads(context, this, service_list);
                    mRecyclerView.setAdapter(adapterVendorOnGoingLeads);
                    mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {
                    service_list.removeAll(service_list);
                    adapterVendorOnGoingLeads.notifyDataSetChanged();
                    linear_empty_list.setVisibility(View.VISIBLE);
                    //    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject main = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    service_id = main.getString("serviceID");
                    service_name = main.getString("serviceName");
                    JSONArray data = main.getJSONArray("userDetail");
                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setUserId(jo.getString("userID"));
                        serviceDetail.setUsername(jo.getString("userName"));
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setCreatedDate(jo.getString("date"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("latitude"));
                        serviceDetail.setSourceLongitude(jo.getString("longitude"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                    loading = true;
                    adapterVendorOnGoingLeads.notifyDataSetChanged();


                } else {
                    adapterVendorOnGoingLeads.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        } catch (JSONException e) {

            if (position == 3) {
                loading = true;
                skipCount = skipCount - 10;
            }
            mSwipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        if (context != null && isAdded()) {

            AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
            mSwipeRefreshLayout.setRefreshing(false);
        }

        }
    }
