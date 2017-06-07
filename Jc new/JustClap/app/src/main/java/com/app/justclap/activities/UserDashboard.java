package com.app.justclap.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapter.DashboardPagerAdapter;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.MyConstant;
import com.app.justclap.vendor.VendorSinup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiResponse {

    private Context context;
    private Button btn_home, btn_requests, btn_chats, btn_notification;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE};
    private ViewPager view_pager;
    private EditText edt_search;
    private DashboardPagerAdapter dashboardPagerAdapter;
    private View main_view;
    private ImageView image_cross;
    private TextView text_login, text_how_it_works, text_rateus, text_share_app, text_logout;
    private Button btn_vendor_login;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        context = this;
        init();
        setListener();
        dashboardPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), 4);
        view_pager.setAdapter(dashboardPagerAdapter);
    }

    private void init() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        main_view = findViewById(R.id.main_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        text_login = (TextView) findViewById(R.id.text_login);
        text_logout = (TextView) findViewById(R.id.text_logout);
        text_how_it_works = (TextView) findViewById(R.id.text_how_it_works);
        text_share_app = (TextView) findViewById(R.id.text_share_app);
        text_rateus = (TextView) findViewById(R.id.text_rateus);
        image_cross = (ImageView) findViewById(R.id.image_cross);
        btn_vendor_login = (Button) findViewById(R.id.btn_vendor_login);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     /*   ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                main_view.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                //below line used to remove shadow of drawer
                drawer.setScrimColor(Color.TRANSPARENT);
            }//this method helps you to aside menu drawer
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        edt_search = (EditText) findViewById(R.id.edt_search);
        btn_chats = (Button) findViewById(R.id.btn_chats);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_requests = (Button) findViewById(R.id.btn_requests);
        btn_notification = (Button) findViewById(R.id.btn_notification);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        AppUtils.fontGotham_Button(btn_chats, context);
        AppUtils.fontGotham_Button(btn_home, context);
        AppUtils.fontGotham_Button(btn_notification, context);
        AppUtils.fontGotham_Button(btn_requests, context);
        AppUtils.fontGotham_edit(edt_search, context);

    }

    private void setWhiteColor() {
        text_how_it_works.setBackgroundColor(getResources().getColor(R.color.white));
        text_rateus.setBackgroundColor(getResources().getColor(R.color.white));
        text_share_app.setBackgroundColor(getResources().getColor(R.color.white));
        text_login.setBackgroundColor(getResources().getColor(R.color.white));
        text_logout.setBackgroundColor(getResources().getColor(R.color.white));

        text_login.setTextColor(getResources().getColor(R.color.textcolordark));
        text_logout.setTextColor(getResources().getColor(R.color.textcolordark));
        text_share_app.setTextColor(getResources().getColor(R.color.textcolordark));
        text_rateus.setTextColor(getResources().getColor(R.color.textcolordark));
        text_how_it_works.setTextColor(getResources().getColor(R.color.textcolordark));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
            text_login.setVisibility(View.VISIBLE);
            text_logout.setVisibility(View.GONE);
        } else {
            text_logout.setVisibility(View.VISIBLE);
            text_login.setVisibility(View.GONE);
        }
    }

    private void setListener() {

        image_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                setWhiteColor();
            }
        });

        btn_vendor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, VendorSinup.class);
                startActivity(in);

            }
        });

        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_login.setTextColor(getResources().getColor(R.color.text_blue));
                text_login.setBackgroundResource(R.drawable.text_bg);
                Intent in = new Intent(context, Login.class);
                startActivity(in);
            }
        });


        text_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_logout.setTextColor(getResources().getColor(R.color.text_blue));
                text_logout.setBackgroundResource(R.drawable.text_bg);
                showLogoutBox();
            }
        });

        text_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_share_app.setTextColor(getResources().getColor(R.color.text_blue));
                text_share_app.setBackgroundResource(R.drawable.text_bg);

            }
        });
        text_how_it_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_how_it_works.setTextColor(getResources().getColor(R.color.text_blue));
                text_how_it_works.setBackgroundResource(R.drawable.text_bg);

            }
        });
        text_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_rateus.setTextColor(getResources().getColor(R.color.text_blue));
                text_rateus.setBackgroundResource(R.drawable.text_bg);
            }
        });


        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(3);
            }
        });

        btn_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(1);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(0);
            }
        });
        btn_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(2);
            }
        });

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_home.setBackgroundResource(R.drawable.round_button);
                    btn_requests.setBackgroundColor(Color.TRANSPARENT);
                    btn_notification.setBackgroundColor(Color.TRANSPARENT);
                    btn_chats.setBackgroundColor(Color.TRANSPARENT);
                } else if (position == 1) {
                    btn_requests.setBackgroundResource(R.drawable.round_button);
                    btn_notification.setBackgroundColor(Color.TRANSPARENT);
                    btn_home.setBackgroundColor(Color.TRANSPARENT);
                    btn_chats.setBackgroundColor(Color.TRANSPARENT);

                } else if (position == 2) {
                    btn_chats.setBackgroundResource(R.drawable.round_button);
                    btn_requests.setBackgroundColor(Color.TRANSPARENT);
                    btn_home.setBackgroundColor(Color.TRANSPARENT);
                    btn_notification.setBackgroundColor(Color.TRANSPARENT);

                } else if (position == 3) {
                    btn_notification.setBackgroundResource(R.drawable.round_button);
                    btn_requests.setBackgroundColor(Color.TRANSPARENT);
                    btn_home.setBackgroundColor(Color.TRANSPARENT);
                    btn_chats.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent in = new Intent(context, Login.class);
            startActivity(in);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                UserDashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        logoutDevice();
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

    private void logoutDevice() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("userId", AppUtils.getUserId(context));
                hm.put("deviceType", MyConstant.DEVICETYPE);
                hm.put("deviceToken", AppUtils.getGcmRegistrationKey(context));

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.logoutDevice);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    AppUtils.setUserId(context, "");
                    AppUtils.setUseremail(context, "");
                    AppUtils.setUserMobile(context, "");
                    AppUtils.setUserName(context, "");
                    AppUtils.setUserImage(context, "");
                    Intent in = new Intent(context, ActivateMobileNumber.class);
                    startActivity(in);
                    finish();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
