package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.justclap.R;
import com.app.justclap.adapters.BookinghistoryPagerAdapter;

public class BookingHistory extends AppCompatActivity {


    Context context;
    private TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager pager;
    BookinghistoryPagerAdapter bookinghistoryPagerAdapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListener();

        bookinghistoryPagerAdapter = new BookinghistoryPagerAdapter(getSupportFragmentManager(), 3);
        pager.setAdapter(bookinghistoryPagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
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


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Ongoing");
        tabLayout.getTabAt(1).setText("Completed");
        tabLayout.getTabAt(2).setText("Cancelled");
        /*tabLayout.addTab(tabLayout.newTab().setText("OnGoing"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));*/
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorPrimary),
                getResources().getColor(R.color.txt_orange));

    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();

            }
        });
    }
}
