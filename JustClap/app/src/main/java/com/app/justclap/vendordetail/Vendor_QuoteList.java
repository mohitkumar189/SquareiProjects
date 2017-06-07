package com.app.justclap.vendordetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.ViewDetailAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterVendorQuoteList;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

/**
 * Created by admin on 12-02-2016.
 */
public class Vendor_QuoteList extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    RecyclerView mRecyclerView;
    AdapterVendorQuoteList adapterVendorQuoteList;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    LinearLayoutManager layoutManager;
    ConnectionDetector cd;
    Context context;
    RelativeLayout rl_main_layout, rl_network;
    String service_name = "", service_id = "";
    private BroadcastReceiver broadcastReceiver;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_quote_list);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        service_list = new ArrayList<>();
        setListener();

        if (!AppUtils.getvendorId(context).equalsIgnoreCase("")) {
            hitService();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
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
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

            Intent in = new Intent(context, Vendor_Lead_dashboardBidirectional.class);
            setResult(2, in);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);

        } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
            Intent in = new Intent(context, Vendor_Lead_dashboard.class);
            setResult(2, in);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
        }


    }

    private void hitService() {

        try {

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


                new AsyncPostDataResponse(Vendor_QuoteList.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getAllCustomerRequestRelatedToVendor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                    Intent in = new Intent(context, Vendor_Lead_dashboardBidirectional.class);
                    setResult(2, in);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);

                } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
                    Intent in = new Intent(context, Vendor_Lead_dashboard.class);
                    setResult(2, in);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
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

            Intent in = new Intent(Vendor_QuoteList.this, ViewDetailAnswers.class);
            in.putExtra("service_id", service_id);
            in.putExtra("service_name", service_name);
            in.putExtra("user_id", service_list.get(position).getUserId());
            in.putExtra("searchId", service_list.get(position).getSearchId());
            startActivity(in);


        } else if (flag == 4) {

            //chats
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

                    JSONObject main = commandResult.getJSONObject("data");
                    getSupportActionBar().setTitle(main.getString("serviceName"));
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
                        serviceDetail.setSearchId(jo.getString("searchID"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setQuote("Rs. " + jo.getString("price"));
                        serviceDetail.setMobileNumber(jo.getString("phoneNo"));
                        serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("profileImage"));
                        service_list.add(serviceDetail);

                    }

                    adapterVendorQuoteList = new AdapterVendorQuoteList(getApplicationContext(), this, service_list);
                    mRecyclerView.setAdapter(adapterVendorQuoteList);

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(Vendor_QuoteList.this, getResources().getString(R.string.problem_server));

    }
}
