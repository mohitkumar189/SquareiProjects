package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterSetting;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.vendordetail.ChangePasswordVendor;

import java.util.ArrayList;


public class ActivitySetting extends AppCompatActivity implements OnCustomItemClicListener {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    AdapterSetting adapterSetting;
    Context context;
    ModelService list;
    ArrayList<ModelService> arrayList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    SharedPreferences.Editor ed;
    private BroadcastReceiver broadcastReceiver;
    String is_vendor = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");
        setListener();
        Intent in = getIntent();
        if (in.hasExtra("is_vendor")) {

            is_vendor = in.getExtras().getString("is_vendor");
        }


        list = new ModelService();
        list.setName("Contact us");
        list.setUrl("http://formula2ride.com/Mobile/cancellationrescheduling_mobile.html");
        arrayList.add(list);

        list = new ModelService();
        list.setName("Terms and Conditions");
        list.setUrl("http://formula2ride.com/Mobile/howitworks_mobile.html");
        arrayList.add(list);

        list = new ModelService();
        list.setName("Privacy policy");
        list.setUrl("http://formula2ride.com/Mobile/Eligibility_mobile.html");
        arrayList.add(list);

        list = new ModelService();
        list.setName("About us");
        list.setUrl("http://formula2ride.com/Mobile/faq_mobile.html");

        arrayList.add(list);

        list = new ModelService();
        list.setName("Change Password");
        list.setUrl("http://formula2ride.com/Mobile/faq_mobile.html");

        arrayList.add(list);


        adapterSetting = new AdapterSetting(context, this, arrayList);
        mRecyclerView.setAdapter(adapterSetting);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    public void init() {
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

    }


    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            }
        });

    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {

            if (position == 0) {
                Intent in = new Intent(ActivitySetting.this, ContactUs.class);
                startActivity(in);
            } else if (position == 1) {
                Intent in = new Intent(ActivitySetting.this, TermsAndconditions.class);
                startActivity(in);

            } else if (position == 2) {

                Intent in = new Intent(ActivitySetting.this, PrivacyPolicy.class);
                startActivity(in);
            } else if (position == 3) {
                Intent in = new Intent(ActivitySetting.this, AboutUs.class);
                startActivity(in);

            } else if (position == 4) {
                if (is_vendor.equalsIgnoreCase("1")) {
                    Intent in = new Intent(ActivitySetting.this, ChangePasswordVendor.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(ActivitySetting.this, ChangePassword.class);
                    startActivity(in);
                }
            }


        }
    }


}
