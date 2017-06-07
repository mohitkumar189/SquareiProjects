package com.app.justclap.activities;

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

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterVendor_list;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VendorList extends AppCompatActivity implements OnCustomItemClicListener {


    Context context;
    AdapterVendor_list adapterVendor_list;
    ModelService serviceDetail;
    ConnectionDetector cd;
    private RecyclerView mRecyclerView;
    ArrayList<ModelService> service_list;
    String serviceArray = "", catName = "", serviceId = "", flag = "",vendorname="";
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    String call = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_vendor_list);

        context = this;
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

        init();
        service_list = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        serviceArray = in.getExtras().getString("servicearray");
        catName = in.getExtras().getString("service_name");
        serviceId = in.getExtras().getString("serviceId");
        flag = in.getExtras().getString("flag");
        Log.e("flag", "*" + flag);
        getSupportActionBar().setTitle(catName);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        setListener();
        parseCategoryData();

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);


    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }

        });
    }

    private void parseCategoryData() {

        try {

            JSONArray services = new JSONArray(serviceArray);

            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                serviceDetail = new ModelService();
                serviceDetail.setServiceName(catName);
                serviceDetail.setVendorID(jo.getString("vendorID"));
                serviceDetail.setVendorName(jo.getString("vendorName"));
                serviceDetail.setVendorEmail(jo.getString("vendorEmail"));
                serviceDetail.setVendorMobile(jo.getString("vendorMobile"));
                call = jo.getString("vendorMobile").trim();
                serviceDetail.setQuote(jo.getString("price"));
                serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("imageID"));
                service_list.add(serviceDetail);

            }

            adapterVendor_list = new AdapterVendor_list(getApplicationContext(), this, service_list);
            mRecyclerView.setAdapter(adapterVendor_list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag1) {


        if (flag1 == 4) {

            Intent in = new Intent(context, ActivityChatOld.class);
            in.putExtra("reciever_id",service_list.get(position).getVendorID());
            in.putExtra("name", "");
            startActivity(in);

        }
        if (flag1 == 5) {
            try {

                String no = "01139595767";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + call));
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        } else if (flag1 == 6) {

            Intent in = new Intent(context, Vendor_Detail.class);
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("service_id", serviceId);
            in.putExtra("service_name", catName);
            in.putExtra("flag", flag);
            startActivity(in);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }


}
