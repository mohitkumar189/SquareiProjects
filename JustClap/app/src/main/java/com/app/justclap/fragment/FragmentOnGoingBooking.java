package com.app.justclap.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.justclap.activities.ActivityChat;
import com.app.justclap.activities.ActivityRating;
import com.app.justclap.activities.ActivityShowRoute;
import com.app.justclap.activities.Activity_RequestCompleted;
import com.app.justclap.activities.Activity_RequestStatus;
import com.app.justclap.R;
import com.app.justclap.activities.Activity_VendorHired;
import com.app.justclap.activities.Vendor_Detail;
import com.app.justclap.activities.ViewDetailAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterOnGoing;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;


public class FragmentOnGoingBooking extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    RecyclerView list_request;
    AdapterOnGoing adapterResponses;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    Context context;
    LinearLayout linear_empty_list;
    ConnectionDetector cd;
    int deleteposition, menu_position;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RelativeLayout rl_main_layout, rl_network;
    String reason = "", maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_ongoing_booking, container, false);
        context = getActivity();
        service_list = new ArrayList<>();
        linear_empty_list = (LinearLayout) view_service.findViewById(R.id.linear_empty_list);
        rl_main_layout = (RelativeLayout) view_service.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view_service.findViewById(R.id.rl_network);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        list_request = (RecyclerView) view_service.findViewById(R.id.list_request);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);

        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
               if (context != null && isAdded()) {
                   getServicelist();
               }
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

                                        new AsyncPostDataFragmentNoProgress(getActivity(), FragmentOnGoingBooking.this, 4, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getCustomerOngoingServices));

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
                                + getString(R.string.getCustomerOngoingServices));

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
                //  AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");
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
                                + getString(R.string.getCustomerOngoingServices));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }


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
            Intent in = new Intent(getActivity(), ActivityRating.class);
            in.putExtra("service_id", service_list.get(position).getServiceID());
            in.putExtra("service_name", service_list.get(position).getServiceName());
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("searchId", service_list.get(position).getSearchId());
            startActivity(in);
        } else if (flag == 1) {

            Intent in = new Intent(context, Vendor_Detail.class);
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("service_id", service_list.get(position).getServiceID());
            in.putExtra("service_name", service_list.get(position).getServiceName());
            in.putExtra("searchId", service_list.get(position).getSearchId());
            in.putExtra("flag", "4");
            startActivity(in);

        } else if (flag == 4) {

            Intent in = new Intent(context, ActivityChat.class);
            in.putExtra("reciever_id", service_list.get(position).getVendorID());
            in.putExtra("name", service_list.get(position).getVendorName());
            in.putExtra("image", service_list.get(position).getVendorimage());
            in.putExtra("searchID", service_list.get(position).getSearchId());
            startActivity(in);

        }else if (flag == 11) {

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

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data1 = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");

                    JSONArray data = data1.getJSONArray("details");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setVendorID(jo.getString("vendorID"));
                        serviceDetail.setVendorName(jo.getString("vendorName"));
                        if (jo.getString("totHire").equalsIgnoreCase("0")) {
                            serviceDetail.setHiredcount("Not Hired till now");
                        } else {
                            if (Integer.parseInt(jo.getString("totHire").trim()) > 1) {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " times");
                            } else {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " time");
                            }
                        }
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setRating(jo.getString("avgRating"));
                        serviceDetail.setTotalreview(jo.getString("review"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendorImage"));
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        service_list.add(serviceDetail);

                    }
                    adapterResponses = new AdapterOnGoing(getActivity(), this, service_list);
                    list_request.setAdapter(adapterResponses);
                    //   mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    Log.e("OngoinglistSize", "*" + service_list.size());

                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {

                        Log.e("OngoinglistSizeEmpty", "*" + service_list.size());
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }


                } else {
                    linear_empty_list.setVisibility(View.VISIBLE);

                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data1 = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");
                    JSONArray data = data1.getJSONArray("details");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setVendorID(jo.getString("vendorID"));
                        serviceDetail.setVendorName(jo.getString("vendorName"));
                        serviceDetail.setRowType(1);
                        if (jo.getString("totHire").equalsIgnoreCase("0")) {
                            serviceDetail.setHiredcount("Not Hired till now");
                        } else {
                            if (Integer.parseInt(jo.getString("totHire").trim()) > 1) {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " times");
                            } else {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " time");
                            }
                        }
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setRating(jo.getString("avgRating"));
                        serviceDetail.setTotalreview(jo.getString("review"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendorImage"));
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));

                        service_list.add(serviceDetail);

                    }

                    adapterResponses = new AdapterOnGoing(getActivity(), this, service_list);
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
                    // AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    linear_empty_list.setVisibility(View.VISIBLE);
                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");


                } else {
                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }


            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data1 = commandResult.getJSONObject("data");
                    maxlistLength = commandResult.getString("total");

                    JSONArray data = data1.getJSONArray("details");

                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();

                        serviceDetail.setVendorID(jo.getString("vendorID"));
                        serviceDetail.setVendorName(jo.getString("vendorName"));
                        if (jo.getString("totHire").equalsIgnoreCase("0")) {
                            serviceDetail.setHiredcount("Not Hired till now");
                        } else {
                            if (Integer.parseInt(jo.getString("totHire").trim()) > 1) {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " times");
                            } else {
                                serviceDetail.setHiredcount("Hired " + jo.getString("totHire") + " time");
                            }
                        }
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setRating(jo.getString("avgRating"));
                        serviceDetail.setTotalreview(jo.getString("review"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));

                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendorImage"));
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        service_list.add(serviceDetail);

                    }

                    //   mSwipeRefreshLayout.setRefreshing(false);
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
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }
            }
        } catch (JSONException e) {
            if (position == 4) {
                loading = true;
                skipCount = skipCount - 1;
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        if (context != null && isAdded()) {
            AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
        }
    }
}
