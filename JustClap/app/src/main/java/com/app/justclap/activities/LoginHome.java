package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.justclap.R;

import com.app.justclap.adapters.VendorDashboardPagerAdapterBidirectional;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.VendorLogin;
import com.app.justclap.vendordetail.Vendor_Lead_dashboard;
import com.app.justclap.vendordetail.Vendor_Lead_dashboardBidirectional;

public class LoginHome extends AppCompatActivity {


    RelativeLayout rl_individual, rl_agent;
    Context context;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login_home);

        context = this;
        init();
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

        setListener();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);


    }


    private void init() {

        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        rl_individual = (RelativeLayout) findViewById(R.id.rl_inividual);
        rl_agent = (RelativeLayout) findViewById(R.id.rl_agent);
    }

    private void setListener() {

        rl_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginHome.this, Login.class);
                startActivity(intent);

            }
        });

        rl_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (AppUtils.getvendorId(context).equalsIgnoreCase("")) {
                    Intent intent = new Intent(LoginHome.this, VendorLogin.class);
                    startActivity(intent);

                } else {
                    if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                        Intent intent = new Intent(LoginHome.this, Vendor_Lead_dashboardBidirectional.class);
                        startActivity(intent);
                        finish();

                    } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
                        Intent intent = new Intent(LoginHome.this, Vendor_Lead_dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
}
