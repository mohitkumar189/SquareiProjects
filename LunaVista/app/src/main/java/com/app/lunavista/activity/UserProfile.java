package com.app.lunavista.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.lunavista.Adapters.ProfilePagerAdapter;
import com.app.lunavista.R;

public class UserProfile extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    private BroadcastReceiver broadcastReceiver;
    TabLayout tabLayout;
    ViewPager pager;
    TextView text_username;
    ProfilePagerAdapter profilePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupCollapsingToolbar();
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), 2);
        pager.setAdapter(profilePagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        setlistener();
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
       /* NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);*/

    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Recordings");
        tabLayout.getTabAt(1).setText("Duets Songs");

        tabLayout.setTabTextColors(getResources().getColor(R.color.txtbox_text_color_darek),
                getResources().getColor(R.color.text_red_dark));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);

    }


    public void init() {

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
        text_username = (TextView) findViewById(R.id.text_username);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

    }

    public void setlistener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
