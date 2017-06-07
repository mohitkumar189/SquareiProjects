package com.app.lunavista.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lunavista.Adapters.HomePagerAdapter;
import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.AppUtils.CircleTransform;
import com.app.lunavista.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class Home extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout frameLayout;
    RelativeLayout rl_container, rl_update;
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Context context;
    HomePagerAdapter homePagerAdapter;
    ViewPager pager;
    RelativeLayout rl_home, rl_lets_sing, rl_fav_song, rl_notification, rl_private_message,
            rl_membership, rl_help, rl_aboutus, rl_rateus, rl_close, rl_video_collection;
    float lastSliderPosition = 0;
    TabLayout tabLayout;
    ImageView image_edit;
    TextView txtUserName, txtUserEmail;
    ImageView imgProfile;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuline);
        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), 4);
        pager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        setlistener();


    }

    @Override
    protected void onResume() {
        super.onResume();

        txtUserName.setText(AppUtils.getUserName(context));
        txtUserEmail.setText(AppUtils.getUserEmail(context));
        if (!AppUtils.getUserImage(context).equalsIgnoreCase("")) {

            Picasso.with(context).load(AppUtils.getUserImage(context))
                    .placeholder(R.drawable.placeholder).transform(new CircleTransform()).into(imgProfile);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void init() {

        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;

        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Quick permission check
                int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
                permissionCheck += this.checkSelfPermission("Manifest.permission.CAMERA");
                permissionCheck += this.checkSelfPermission("Manifest.permission.RECORD_AUDIO");

                if (permissionCheck != 0) {

                    this.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO}, 1001); //Any number
                }

            }
        }


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
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.header_layout);
        rl_container = (RelativeLayout) findViewById(R.id.rl_container);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_update = (RelativeLayout) findViewById(R.id.rl_update);
        rl_lets_sing = (RelativeLayout) findViewById(R.id.rl_lets_sing);
        rl_fav_song = (RelativeLayout) findViewById(R.id.rl_fav_song);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        rl_private_message = (RelativeLayout) findViewById(R.id.rl_private_message);
        rl_membership = (RelativeLayout) findViewById(R.id.rl_membership);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        rl_rateus = (RelativeLayout) findViewById(R.id.rl_rateus);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        rl_video_collection = (RelativeLayout) findViewById(R.id.rl_video_collection);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        image_edit = (ImageView) findViewById(R.id.image_edit);

    }

    private void setupTabIcons() {

        tabLayout.getTabAt(1).setText("SONGLIST");
        tabLayout.getTabAt(2).setText("PLAYLIST");
        tabLayout.getTabAt(0).setText("ALL SONGS");
        tabLayout.getTabAt(3).setText("POPULAR SONGS");
        tabLayout.setTabTextColors(getResources().getColor(R.color.txtbox_text_color_darek),
                getResources().getColor(R.color.text_red_dark));
    }


    public void setlistener() {

        rl_lets_sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);

                pager.setCurrentItem(1);


            }
        });
        rl_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);



            }
        });


        rl_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Home.this, UpgradeToVip.class);
                startActivity(in);
            }
        });
        rl_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Home.this, Webactivity.class);
                in.putExtra("title", "About us");
                in.putExtra("url", "https://www.google.co.in/intl/en/about/");
                startActivity(in);
            }
        });
        rl_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Home.this, Webactivity.class);
                in.putExtra("title", "Help");
                in.putExtra("url", "https://www.google.co.in/intl/en/about/");
                startActivity(in);
            }
        });

        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, UpdateProfile.class);
                startActivity(in);
            }
        });
        rl_private_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(context, ActivityPrivateMessage.class);
                startActivity(in);
            }
        });

        rl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, UpdateProfile.class);
                startActivity(in);
            }
        });
        rl_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);

                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
            }
        });

        rl_fav_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(context, ActivityPlayList.class);
                startActivity(in);

            }
        });
        rl_video_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(context, ActivityVideoCollection.class);
                startActivity(in);

            }
        });

        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);
                showLogoutBox();
            }
        });
        rl_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(frameLayout);
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar, //<-- This is the icon provided by Google itself
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {


            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?

                if (drawerLayout.isDrawerOpen(frameLayout)) {
                    drawerLayout.closeDrawer(frameLayout);

                } else {
                    drawerLayout.openDrawer(frameLayout);
                }
            }
        });
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //   Log.e("slide menu", "**" + slideOffset);

                ValueAnimator anim = ValueAnimator.ofFloat(lastSliderPosition, slideOffset);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //  Log.e("open", "draweropen");
                        float slideOffset = (Float) valueAnimator.getAnimatedValue();
                        mDrawerToggle.onDrawerSlide(drawerLayout, slideOffset);
                    }
                });
                anim.setInterpolator(new DecelerateInterpolator());
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                anim.setDuration(500);
                anim.start();
                mDrawerToggle.syncState();
                lastSliderPosition = slideOffset;

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Home.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        AppUtils.setUserName(context, "");
                        AppUtils.setUserImage(context, "");
                        AppUtils.setUserEmail(context, "");
                        AppUtils.setSongsList(context, "");
                        AppUtils.setPlayList(context, "");
                        AppUtils.setPublicrecordings(context, "");

                        SharedPreferences.Editor ed;
                        SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                        ed = pref.edit();
                        ed.putBoolean("Deactivate", false);
                        ed.commit();

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);
                        finish();

                        Intent in = new Intent(context, Login.class);
                        setResult(512, in);
                        startActivity(in);
                        finish();


                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }
}
