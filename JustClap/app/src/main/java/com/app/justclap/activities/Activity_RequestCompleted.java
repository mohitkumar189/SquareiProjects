package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterRequestCompleted;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterRequeststatusHireProf;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class Activity_RequestCompleted extends AppCompatActivity implements ListenerPostData, OnCustomItemClicListener {

    RecyclerView mRecyclerView;
    AdapterRequestCompleted adapterRequestCompleted;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    ConnectionDetector cd;
    TextView text_service_name;
    ImageView img_back, image_background;
    Context context;
    RelativeLayout rl_main_layout, rl_network;
    String service_name = "", service_id = "", pageFlag = "0", searchId = "", vendor_id = "";
    private BroadcastReceiver broadcastReceiver;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int rowPosition;
    String reason = "", maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 1;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__request_completed);

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
        service_list = new ArrayList<>();
        Intent in = getIntent();
        service_id = in.getExtras().getString("service_id");

        service_name = in.getExtras().getString("service_name");

        if (in.hasExtra("searchId")) {

            searchId = in.getStringExtra("searchId");
        }
        if (in.hasExtra("pageFlag")) {
            pageFlag = in.getExtras().getString("pageFlag");
        }
        text_service_name.setText(service_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
            hitService();
        }
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void vendorListRefresh() {

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

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                new AsyncPostDataResponseNoProgress(Activity_RequestCompleted.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorsQuotes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));
                new AsyncPostDataResponse(Activity_RequestCompleted.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorsQuotes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        image_background = (ImageView) findViewById(R.id.image_background);
        text_service_name = (TextView) findViewById(R.id.text_service_name);

        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        img_back = (ImageView) findViewById(R.id.img_back);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
    }

    private void setListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }

        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                vendorListRefresh();

            }
        });

/*
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
                                    adapterRequestCompleted.notifyDataSetChanged();

                                    skipCount = skipCount + 10;
                                    Log.v("...", "Last Item Wow !");

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "userID", AppUtils.getUserId(context)));

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "serviceID", service_id));

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "searchId", searchId));

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));
                                        new AsyncPostDataResponse(Activity_RequestCompleted.this, 3, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getVendorsQuotes));

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
*/


    }


    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 2) {

            String number = service_list.get(position).getMobileNumber();

            try {
                String uri = "tel:" + number.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            } catch (SecurityException e) {
                e.printStackTrace();
            }


        } else if (flag == 3) {

            Intent in = new Intent(Activity_RequestCompleted.this, Vendor_Detail.class);
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("service_id", service_id);
            in.putExtra("searchId", searchId);
            startActivity(in);

        } else if (flag == 4) {

            Intent in = new Intent(Activity_RequestCompleted.this, ActivityChat.class);
            in.putExtra("reciever_id", service_list.get(position).getVendorID());
            in.putExtra("name", service_list.get(position).getVendorName());
            in.putExtra("image", service_list.get(position).getVendorimage());
            in.putExtra("searchID", searchId);
            startActivity(in);

            //chats
        } else if (flag == 5) {
            rowPosition = position;
            showHireAlert();
        } else if (flag == 11) {
            rowPosition = position;

            Intent in = new Intent(context, ActivityShowRoute.class);
            in.putExtra("srcLat", service_list.get(position).getSourceLatitude());
            in.putExtra("srcLng", service_list.get(position).getSourceLongitude());
            in.putExtra("venLat", service_list.get(position).getVendorLatitude());
            in.putExtra("venLng", service_list.get(position).getVendorLongitude());
            startActivity(in);


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     /*   Intent in = new Intent(Activity_RequestStatus.this, DashBoardActivity.class);
        startActivity(in);*/
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject main = commandResult.getJSONObject("data");
                    //     maxlistLength = main.getString("total");
                    if (main.has("serviceImage")) {
                        Picasso.with(context).load(getResources().getString(R.string.img_url) + main.getString("serviceImage"))
                                .into(image_background);
                    }
                    JSONArray data = main.getJSONArray("vendors");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setRowType(1);
                        serviceDetail.setVendorID(jo.getString("vendorID"));
                        serviceDetail.setVendorName(jo.getString("vendorName"));

                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));

                        if (jo.getString("noOfTimeHire").equalsIgnoreCase("0")) {
                            serviceDetail.setHiredcount("Not Hired till now");
                        } else {
                            serviceDetail.setHiredcount("Hired " + jo.getString("noOfTimeHire") + " times");
                        }
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setRating(jo.getString("averageRating"));
                        serviceDetail.setTotalreview(jo.getString("noOfReview"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendorImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterRequestCompleted = new AdapterRequestCompleted(getApplicationContext(), this, service_list);
                    mRecyclerView.setAdapter(adapterRequestCompleted);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject main = commandResult.getJSONObject("data");
                    // maxlistLength = main.getString("total");
                    if (main.has("serviceImage")) {
                        Picasso.with(context).load(getResources().getString(R.string.img_url) + main.getString("serviceImage"))
                                .into(image_background);
                    }
                    JSONArray data = main.getJSONArray("vendors");

                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setRowType(1);
                        serviceDetail.setVendorID(jo.getString("vendorID"));
                        serviceDetail.setVendorName(jo.getString("vendorName"));
                        serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                        serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                        serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                        serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                        serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));


                        if (jo.getString("noOfTimeHire").equalsIgnoreCase("0")) {
                            serviceDetail.setHiredcount("Not Hired till now");
                        } else {
                            serviceDetail.setHiredcount("Hired " + jo.getString("noOfTimeHire") + " times");
                        }
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setRating(jo.getString("averageRating"));
                        serviceDetail.setTotalreview(jo.getString("noOfReview"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendorImage"));
                        service_list.add(serviceDetail);

                    }


                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                    loading = true;
                    adapterRequestCompleted.notifyDataSetChanged();

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);


                } else {
                    adapterRequestCompleted.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }


            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.showCustomAlert(Activity_RequestCompleted.this, commandResult.getString("message"));
                    Intent in = new Intent(context, Activity_VendorHired.class);
                    in.putExtra("service_id", data.getString("serviceID"));
                    in.putExtra("vendor_id", data.getString("vendorID"));
                    in.putExtra("service_name", data.getString("serviceName"));
                    in.putExtra("pageFlag", "1");
                    in.putExtra("searchId", searchId);
                    startActivity(in);
                    finish();

                } else {
                    AppUtils.showCustomAlert(Activity_RequestCompleted.this, commandResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            if (position == 3) {
                loading = true;
                skipCount = skipCount - 1;
            }
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {


        AppUtils.showCustomAlert(Activity_RequestCompleted.this, getResources().getString(R.string.problem_server));
    }

    private void showHireAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Hire Now");

        alertDialog.setMessage("Are you sure you want to hire this professional?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        hireNow();

                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void hireNow() {

        try {

            // for gcm condition check
            // =================================================

            cd = new com.app.justclap.interfaces.ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                // AppUtils.showCustomAlert(Vendor_Detail.this, getResources().getString(R.string.message_network_problem));

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
                                "vendorID", service_list.get(rowPosition).getVendorID()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));


                new AsyncPostDataResponse(Activity_RequestCompleted.this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.hireVendor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
