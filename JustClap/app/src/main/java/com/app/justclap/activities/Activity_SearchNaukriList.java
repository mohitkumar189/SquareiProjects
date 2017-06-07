package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterSearchNaukriList;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class Activity_SearchNaukriList extends AppCompatActivity implements ListenerPostData, OnCustomItemClicListener {

    RecyclerView mRecyclerView;
    AdapterSearchNaukriList adapterSearchNaukriList;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    LinearLayoutManager layoutManager;
    ConnectionDetector cd;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    int row_position;
    RelativeLayout rl_main_layout, rl_network;
    String service_name = "", service_id = "", searchId = "", vendor_naukri = "2", pageFlag = "0";
    private BroadcastReceiver broadcastReceiver;
    LinearLayout linear_empty_list;
    String maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int skipCount = 0;
    private boolean loading = true;
    FloatingActionButton floatingIcon;
    JSONArray latArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_naukri_list);

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

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search List");
        service_list = new ArrayList<>();
        Intent in = getIntent();
        service_id = in.getExtras().getString("service_id");
        if (in.hasExtra("vendor_naukri")) {
            vendor_naukri = in.getExtras().getString("vendor_naukri");
        }
        if (in.hasExtra("pageFlag")) {
            pageFlag = in.getExtras().getString("pageFlag");
        }
        searchId = in.getExtras().getString("searchId");
        service_name = in.getExtras().getString("service_name");
        setListener();

        hitService();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

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
                //  AppUtils.showCustomAlert(Activity_RequestCompleted.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                if (vendor_naukri.equalsIgnoreCase("1")) {
                    nameValuePairs
                            .add(new BasicNameValuePair(
                                    "userID", AppUtils.getvendorId(context)));
                } else {

                    nameValuePairs
                            .add(new BasicNameValuePair(
                                    "userID", AppUtils.getUserId(context)));
                }

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_vendor", vendor_naukri));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                new AsyncPostDataResponse(Activity_SearchNaukriList.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorsOfBidirectional));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void refreshItems() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                //  AppUtils.showCustomAlert(Activity_RequestCompleted.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                if (vendor_naukri.equalsIgnoreCase("1")) {
                    nameValuePairs
                            .add(new BasicNameValuePair(
                                    "userID", AppUtils.getvendorId(context)));
                } else {

                    nameValuePairs
                            .add(new BasicNameValuePair(
                                    "userID", AppUtils.getUserId(context)));
                }

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "is_vendor", vendor_naukri));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                new AsyncPostDataResponseNoProgress(Activity_SearchNaukriList.this, 3, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorsOfBidirectional));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void init() {

        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        floatingIcon = (FloatingActionButton) findViewById(R.id.floatingIcon);
        linear_empty_list = (LinearLayout) findViewById(R.id.linear_empty_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItems();
            }
        });

        floatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, VendorsSearchMapView.class);
                in.putExtra("latArray",latArray.toString());
                startActivity(in);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pageFlag.equalsIgnoreCase("1")) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);
                    Intent in = new Intent(Activity_SearchNaukriList.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                } else {

                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                }

            }
        });

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
                                    adapterSearchNaukriList.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        if (vendor_naukri.equalsIgnoreCase("1")) {
                                            nameValuePairs
                                                    .add(new BasicNameValuePair(
                                                            "userID", AppUtils.getvendorId(context)));
                                        } else {

                                            nameValuePairs
                                                    .add(new BasicNameValuePair(
                                                            "userID", AppUtils.getUserId(context)));
                                        }
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "searchId", searchId));
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "is_vendor", vendor_naukri));
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));


                                        new AsyncPostDataResponseNoProgress(Activity_SearchNaukriList.this, 6, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getVendorsOfBidirectional));
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

            try {
                String number = service_list.get(position).getMobileNumber();

                String uri = "tel:" + number.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }


        } else if (flag == 3) {

            if (service_list.get(position).getIsvendor().equalsIgnoreCase("1")) {
                Intent in = new Intent(Activity_SearchNaukriList.this, Vendor_Detail_Bidirectional.class);
                in.putExtra("vendor_id", service_list.get(position).getVendorID());
                in.putExtra("is_vendor", service_list.get(position).getIsvendor());
                in.putExtra("service_id", service_id);
                in.putExtra("matchId", service_list.get(position).getSearchId());
                in.putExtra("searchId", searchId);
                in.putExtra("flag", "3");
                startActivity(in);
            } else {

                Intent in = new Intent(Activity_SearchNaukriList.this, UserProfile.class);
                in.putExtra("user_id", service_list.get(position).getVendorID());
                in.putExtra("name", service_list.get(position).getVendorName());
                in.putExtra("email", service_list.get(position).getVendorID());
                in.putExtra("mobile", service_list.get(position).getMobileNumber());
                in.putExtra("searchId", searchId);
                in.putExtra("image", service_list.get(position).getVendorimage());
                startActivity(in);

            }

        } else if (flag == 4) {

            Intent in = new Intent(Activity_SearchNaukriList.this, ActivityChat.class);
            in.putExtra("reciever_id", service_list.get(position).getVendorID());
            in.putExtra("name", service_list.get(position).getVendorName());
            in.putExtra("image", service_list.get(position).getVendorimage());
            in.putExtra("searchID", searchId);
            startActivity(in);


        } else if (flag == 5) {


            row_position = position;
            if (service_list.get(position).getIsApplied().equalsIgnoreCase("0")) {
                applyJob();
            } else {
                AppUtils.showCustomAlert(Activity_SearchNaukriList.this, "Already Applied");
            }


        } else if (flag == 6) {

            row_position = position;
            if (service_list.get(position).getIssaved().equalsIgnoreCase("0")) {
                saveJob();
            } else {
                AppUtils.showCustomAlert(Activity_SearchNaukriList.this, "Already saved");
            }


        } else if (flag == 7) {

            Intent in = new Intent(context, ActivityRatingBidirectional.class);
            in.putExtra("service_id", service_id);
            in.putExtra("service_name", service_name);
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("searchId", searchId);
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

    private void applyJob() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                //  AppUtils.showCustomAlert(Activity_RequestCompleted.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
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
                                "grouptypeID", service_list.get(row_position).getGrouptypeID()));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", service_list.get(row_position).getVendorID()));

                new AsyncPostDataResponse(Activity_SearchNaukriList.this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.applyFromCustomer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void saveJob() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                //  AppUtils.showCustomAlert(Activity_RequestCompleted.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
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
                                "vendorID", service_list.get(row_position).getVendorID()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                new AsyncPostDataResponse(Activity_SearchNaukriList.this, 4, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.saveApplyCustomerBiDi));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (pageFlag.equalsIgnoreCase("1")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent in = new Intent(Activity_SearchNaukriList.this, DashBoardActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            } else {

                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                    //JSONObject main = commandResult.getJSONObject("data");

                    JSONArray data = commandResult.getJSONArray("data");
                    latArray = commandResult.getJSONArray("data");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        serviceDetail = new ModelService();
                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail.setVendorName(jo.getString("userName"));
                        serviceDetail.setVendorID(jo.getString("userId"));
                        serviceDetail.setIsvendor(jo.getString("is_vendor"));
                        serviceDetail.setIssaved(jo.getString("save"));
                        serviceDetail.setIs_read(jo.getString("is_read"));
                        serviceDetail.setIsApplied(jo.getString("apply"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setVendorLatitude(jo.getString("latitude"));
                        serviceDetail.setVendorLongitude(jo.getString("longitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setRating(jo.getString("average_rating"));
                        serviceDetail.setTotalreview(jo.getString("reviewCount"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setGrouptypeID(jo.getString("grouptypeID"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterSearchNaukriList = new AdapterSearchNaukriList(getApplicationContext(), this, service_list);
                    mRecyclerView.setAdapter(adapterSearchNaukriList);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {

                    linear_empty_list.setVisibility(View.VISIBLE);
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }


            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Intent in = new Intent(Activity_SearchNaukriList.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                }
            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Intent in = new Intent(Activity_SearchNaukriList.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                }
            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONArray data = commandResult.getJSONArray("data");
                    latArray = commandResult.getJSONArray("data");
                    maxlistLength = commandResult.getString("total");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {


                        serviceDetail = new ModelService();
                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail.setVendorName(jo.getString("userName"));
                        serviceDetail.setVendorID(jo.getString("userId"));
                        serviceDetail.setIssaved(jo.getString("save"));
                        serviceDetail.setIsApplied(jo.getString("apply"));
                        serviceDetail.setIsvendor(jo.getString("is_vendor"));
                        serviceDetail.setGrouptypeID(jo.getString("grouptypeID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setVendorLatitude(jo.getString("latitude"));
                        serviceDetail.setVendorLongitude(jo.getString("longitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));
                        serviceDetail.setIs_read(jo.getString("is_read"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setRating(jo.getString("average_rating"));
                        serviceDetail.setTotalreview(jo.getString("reviewCount"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterSearchNaukriList = new AdapterSearchNaukriList(getApplicationContext(), this, service_list);
                    mRecyclerView.setAdapter(adapterSearchNaukriList);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {

                    linear_empty_list.setVisibility(View.VISIBLE);

                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }


            } else if (position == 6) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONArray data = commandResult.getJSONArray("data");
                    latArray = commandResult.getJSONArray("data");
                    maxlistLength = commandResult.getString("total");
                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        serviceDetail = new ModelService();
                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail.setVendorName(jo.getString("userName"));
                        serviceDetail.setVendorID(jo.getString("userId"));
                        serviceDetail.setIsvendor(jo.getString("is_vendor"));
                        serviceDetail.setIssaved(jo.getString("save"));
                        serviceDetail.setIsApplied(jo.getString("apply"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setRowType(1);
                        serviceDetail.setUserEmail(jo.getString("email"));
                        serviceDetail.setRating(jo.getString("average_rating"));
                        serviceDetail.setTotalreview(jo.getString("reviewCount"));
                        serviceDetail.setVendorLatitude(jo.getString("latitude"));
                        serviceDetail.setVendorLongitude(jo.getString("longitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));
                        serviceDetail.setIs_read(jo.getString("is_read"));
                        serviceDetail.setMatchId(jo.getString("searchId"));
                        serviceDetail.setGrouptypeID(jo.getString("grouptypeID"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterSearchNaukriList.notifyDataSetChanged();
                    loading = true;

                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }

                    swipeRefreshLayout.setRefreshing(false);


                } else {
                    skipCount = skipCount - 10;
                    swipeRefreshLayout.setRefreshing(false);
                    loading = true;

                }


            }
        } catch (JSONException e) {
            if (position == 6) {
                skipCount = skipCount - 10;
                swipeRefreshLayout.setRefreshing(false);
                loading = true;
            }
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {


        AppUtils.showCustomAlert(Activity_SearchNaukriList.this, getResources().getString(R.string.problem_server));
    }


}
