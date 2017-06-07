package com.app.justclap.vendordetail;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.activities.AboutUs;
import com.app.justclap.activities.ActivityHome;
import com.app.justclap.activities.ActivityNotification;
import com.app.justclap.activities.ActivitySetting;
import com.app.justclap.activities.ContactUs;
import com.app.justclap.activities.DashBoardActivity;
import com.app.justclap.activities.PrivacyPolicy;
import com.app.justclap.activities.QuestionPage;
import com.app.justclap.R;
import com.app.justclap.activities.TermsAndconditions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.VendorDashboardPagerAdapter;
import com.app.justclap.adapters.VendorDashboardPagerAdapterBidirectional;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 12-02-2016.
 */
public class Vendor_Lead_dashboard extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    Context context;
    FrameLayout frameLayout;
    VendorDashboardPagerAdapter vendorDashboardPagerAdapter;
    TextView text_userName, text_user_email;
    ImageView image_user;
    VendorDashboardPagerAdapterBidirectional vendorDashboardPagerAdapterBidirectional;
    RelativeLayout rl_main_layout, rl_network, rl_sharejob;
    DrawerLayout drawerLayout;
    FloatingActionButton fab_share;
    ViewPager pager;
    RelativeLayout rl_credits, rl_profile, rl_login,
            rl_works, rl_privacy, rl_terms, rl_about, rl_home, rl_chat, rl_contact, rl_rate, rl_share, rl_setting, rl_logout;
    private BroadcastReceiver broadcastReceiver;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.newleads,
            R.drawable.ongoing,
            R.drawable.converted, R.drawable.chats
    };
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    public float lastSliderPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_lead_dashboard);

        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leads Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuline);
        setListener();

        vendorDashboardPagerAdapter = new VendorDashboardPagerAdapter(getSupportFragmentManager(), 4);
        pager.setAdapter(vendorDashboardPagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        rl_sharejob.setVisibility(View.GONE);

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("NewLeads");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.newleads, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Ongoing");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ongoing, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Completed");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.converted, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Chats");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);

        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorPrimary),
                getResources().getColor(R.color.txt_orange));

    }

    private void setupTabIconsBidirectional() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Requests");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.requests, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Chats");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chats, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabfour);

        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorPrimary),
                getResources().getColor(R.color.txt_orange));

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
        rl_works = (RelativeLayout) findViewById(R.id.rl_works);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.header_layout);
        image_user = (ImageView) findViewById(R.id.image_user);
        text_user_email = (TextView) findViewById(R.id.text_useremail);
        text_userName = (TextView) findViewById(R.id.text_username);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
        rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
        rl_rate = (RelativeLayout) findViewById(R.id.rl_rate);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
        rl_credits = (RelativeLayout) findViewById(R.id.rl_credits);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        rl_privacy = (RelativeLayout) findViewById(R.id.rl_privacy);
        rl_terms = (RelativeLayout) findViewById(R.id.rl_terms);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        rl_sharejob = (RelativeLayout) findViewById(R.id.rl_sharejob);
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }*/

    private void setListener() {
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, AboutUs.class);
                startActivity(in);

            }
        });
        rl_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, TermsAndconditions.class);
                startActivity(in);

            }
        });
        rl_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, PrivacyPolicy.class);
                startActivity(in);

            }
        });
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Vendor_Lead_dashboard.this, QuestionPage.class);
                in.putExtra("servicename", "Naukri");
                in.putExtra("vendor_naukri", "1");
                in.putExtra("Is_uniDirectional", AppUtils.getisunidirectional(context));
                in.putExtra("serviceid", AppUtils.getvendorServiceId(context));
                startActivity(in);

            }
        });


        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, Vendor_Lead_dashboard.class);
                startActivity(in);
                finish();

            }
        });
        rl_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, ActivityHome.class);
                in.putExtra("is_Vendor", "2");
                startActivity(in);

            }
        });
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, VendorLogin.class);
                startActivityForResult(in, 511);

            }
        });
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, Vendor_homePage.class);
                startActivity(in);

            }
        });
        rl_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, Vendor_Wallet.class);
                startActivity(in);

            }
        });

        rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
              /*  Intent in = new Intent(DashBoardActivity.this, Login.class);
                startActivity(in);*/

            }
        });
        rl_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, ContactUs.class);
                startActivity(in);

            }
        });
        rl_rate.setOnClickListener(new View.OnClickListener() {
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
        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "JustClap, a fastest growing App ,I find many new things here" +
                        ", You can also find, So why are you waiting Get it now." + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                Intent in = new Intent(Vendor_Lead_dashboard.this, ActivitySetting.class);
                in.putExtra("is_vendor", "1");
                startActivity(in);

            }
        });
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(frameLayout);
                showLogoutBox();

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
                // supportInvalidateOptionsMenu();
                // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            }

            @Override
            public void onDrawerClosed(View drawerView) {


                // supportInvalidateOptionsMenu();


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Vendor_Lead_dashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "userID", AppUtils.getvendorId(context)));

                            new AsyncPostDataResponse(Vendor_Lead_dashboard.this, 3, nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.logout));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    @Override
    protected void onResume() {
        super.onResume();

        if (AppUtils.getvendorId(context).equalsIgnoreCase("")) {

            rl_login.setVisibility(View.VISIBLE);
            rl_setting.setVisibility(View.GONE);
            rl_logout.setVisibility(View.GONE);
            rl_works.setVisibility(View.VISIBLE);
            text_user_email.setText("Welcome to just Clap");
            text_userName.setVisibility(View.GONE);

        } else {

            rl_login.setVisibility(View.GONE);
            rl_setting.setVisibility(View.VISIBLE);
            rl_logout.setVisibility(View.VISIBLE);
            rl_works.setVisibility(View.GONE);
            text_user_email.setText(AppUtils.getvendorEmail(context));
            text_userName.setText(AppUtils.getvendorname(context));
            text_userName.setVisibility(View.VISIBLE);
        }
        if (!AppUtils.getVendorImage(context).equalsIgnoreCase("")) {

            Picasso.with(context)
                    .load(AppUtils.getVendorImage(context))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_user);

        } else {

            image_user.setImageResource(R.drawable.placeholder_logo);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 511 && resultCode == 512) {
            if (AppUtils.getvendorId(context).equalsIgnoreCase("")) {

                rl_login.setVisibility(View.VISIBLE);
                rl_setting.setVisibility(View.GONE);
                rl_logout.setVisibility(View.GONE);
                rl_works.setVisibility(View.VISIBLE);
                text_user_email.setText("Welcome to just Clap");
                text_userName.setVisibility(View.GONE);

            } else {

                rl_login.setVisibility(View.GONE);
                rl_setting.setVisibility(View.VISIBLE);
                rl_works.setVisibility(View.GONE);
                rl_logout.setVisibility(View.VISIBLE);
                text_user_email.setText(AppUtils.getvendorEmail(context));
                text_userName.setText(AppUtils.getvendorname(context));
                text_userName.setVisibility(View.VISIBLE);
            }
            if (!AppUtils.getVendorImage(context).equalsIgnoreCase("")) {

                Picasso.with(context)
                        .load(AppUtils.getVendorImage(context))
                        .placeholder(R.drawable.user)
                        .transform(new CircleTransform())
                        .into(image_user);

            } else {

                image_user.setImageResource(R.drawable.placeholder_logo);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            alertDialogBuilder.setTitle("Are you sure you want to Exit?");

            // set dialog message
            alertDialogBuilder

                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    //   AppUtils.setUserId(context,false);

                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                                    sendBroadcast(broadcastIntent);
                                    finish();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClickListener(int position, int flag) {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.wallet_icon) {

            Intent intent = new Intent(Vendor_Lead_dashboard.this, Vendor_Wallet.class);
            startActivity(intent);
        }
        if (id == R.id.notification_icon) {

            Intent intent = new Intent(Vendor_Lead_dashboard.this, ActivityNotification.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {
            if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    AppUtils.setvendorId(context, "");
                    AppUtils.setvendorEmail(context, "");
                    AppUtils.setvendormobile(context, "");
                    AppUtils.setvendorname(context, "");
                    AppUtils.setvendoruserId(context, "");
                    AppUtils.setVendorImage(context, "");
                    Intent in = new Intent(Vendor_Lead_dashboard.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();


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

    }
}
