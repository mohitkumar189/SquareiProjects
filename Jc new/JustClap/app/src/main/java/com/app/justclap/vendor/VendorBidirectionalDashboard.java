package com.app.justclap.vendor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.ActivateMobileNumber;
import com.app.justclap.activities.Login;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.fragments.Fragment_Chat;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.MyConstant;
import com.app.justclap.vendorfragments.FragmentVendorLeads;
import com.app.justclap.vendorfragments.FragmentVendorRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorBidirectionalDashboard extends AppCompatActivity
        implements  ApiResponse {

    private Context context;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView text_login, text_how_it_works, text_rateus, text_share_app, text_logout,
            text_dashboard, text_profile, text_credits, text_settings,text_notification_count;
    private ImageView image_cross,image_wallet,image_notification;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_uni_dashboard);

        context = this;
        init();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setListener();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Dashboard");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        text_login = (TextView) findViewById(R.id.text_login);
        text_logout = (TextView) findViewById(R.id.text_logout);
        text_how_it_works = (TextView) findViewById(R.id.text_how_it_works);
        text_share_app = (TextView) findViewById(R.id.text_share_app);
        text_rateus = (TextView) findViewById(R.id.text_rateus);
        text_dashboard = (TextView) findViewById(R.id.text_dashboard);
        text_profile = (TextView) findViewById(R.id.text_profile);
        text_settings = (TextView) findViewById(R.id.text_settings);
        text_credits = (TextView) findViewById(R.id.text_credits);
        image_cross = (ImageView) findViewById(R.id.image_cross);
        text_notification_count = (TextView) findViewById(R.id.text_notification_count);
        image_notification = (ImageView) findViewById(R.id.image_notification);
        image_wallet = (ImageView) findViewById(R.id.image_wallet);
    }

    private void setWhiteColor() {
        text_how_it_works.setBackgroundColor(getResources().getColor(R.color.white));
        text_rateus.setBackgroundColor(getResources().getColor(R.color.white));
        text_share_app.setBackgroundColor(getResources().getColor(R.color.white));
        text_login.setBackgroundColor(getResources().getColor(R.color.white));
        text_logout.setBackgroundColor(getResources().getColor(R.color.white));
        text_dashboard.setBackgroundColor(getResources().getColor(R.color.white));
        text_profile.setBackgroundColor(getResources().getColor(R.color.white));
        text_credits.setBackgroundColor(getResources().getColor(R.color.white));
        text_settings.setBackgroundColor(getResources().getColor(R.color.white));

        text_login.setTextColor(getResources().getColor(R.color.textcolordark));
        text_logout.setTextColor(getResources().getColor(R.color.textcolordark));
        text_share_app.setTextColor(getResources().getColor(R.color.textcolordark));
        text_rateus.setTextColor(getResources().getColor(R.color.textcolordark));
        text_how_it_works.setTextColor(getResources().getColor(R.color.textcolordark));

        text_dashboard.setTextColor(getResources().getColor(R.color.textcolordark));
        text_profile.setTextColor(getResources().getColor(R.color.textcolordark));
        text_credits.setTextColor(getResources().getColor(R.color.textcolordark));
        text_settings.setTextColor(getResources().getColor(R.color.textcolordark));

        drawer.closeDrawer(GravityCompat.START);
    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                setWhiteColor();
            }
        });

        image_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Vendor_Wallet.class);
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
        text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_profile.setTextColor(getResources().getColor(R.color.text_blue));
                text_profile.setBackgroundResource(R.drawable.text_bg);

                Intent intent = new Intent(context, VendorProfile.class);
                startActivity(intent);

            }
        });
        text_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_settings.setTextColor(getResources().getColor(R.color.text_blue));
                text_settings.setBackgroundResource(R.drawable.text_bg);
            }
        });

        text_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_credits.setTextColor(getResources().getColor(R.color.text_blue));
                text_credits.setBackgroundResource(R.drawable.text_bg);

                Intent intent = new Intent(context, Vendor_Wallet.class);
                startActivity(intent);
            }
        });
        text_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_dashboard.setTextColor(getResources().getColor(R.color.text_blue));
                text_dashboard.setBackgroundResource(R.drawable.text_bg);

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView.setImageResource(R.drawable.request_blue);
                        break;
                    case 1:
                        ImageView imageView1 = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView1.setImageResource(R.drawable.chat_blue);
                        break;
                    case 2:
                        ImageView imageView2 = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView2.setImageResource(R.drawable.greaystar);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView.setImageResource(R.drawable.requestgrey);
                        break;
                    case 1:
                        ImageView imageView1 = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView1.setImageResource(R.drawable.chat_grey);
                        break;
                    case 2:
                        ImageView imageView2 = (ImageView) tab.getCustomView().findViewById(R.id.image_view);
                        imageView2.setImageResource(R.drawable.greaystar);
                        break;

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void setupTabIcons() {

        View tabOne = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textView = (TextView) tabOne.findViewById(R.id.tab);
        textView.setText("Requests");
        AppUtils.fontGotham_Book(textView, context);
        ImageView imageView = (ImageView) tabOne.findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.request_blue);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textView1 = (TextView) tabTwo.findViewById(R.id.tab);
        textView1.setText("Chats");
        AppUtils.fontGotham_Book(textView1, context);
        ImageView imageView1 = (ImageView) tabTwo.findViewById(R.id.image_view);
        imageView1.setImageResource(R.drawable.ongoing_grey);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        View tabThree = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textView3 = (TextView) tabThree.findViewById(R.id.tab);
        textView3.setText("Leads");
        AppUtils.fontGotham_Book(textView3, context);
        ImageView imageView3 = (ImageView) tabThree.findViewById(R.id.image_view);
        imageView3.setImageResource(R.drawable.completed_grey);
        tabLayout.getTabAt(2).setCustomView(tabThree);

     /*   View tabFour = (View) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textView4 = (TextView) tabFour.findViewById(R.id.tab);
        textView4.setText("Chat");
        AppUtils.fontGotham_Book(textView4, context);
        ImageView imageView4 = (ImageView) tabFour.findViewById(R.id.image_view);
        imageView4.setImageResource(R.drawable.chat_grey);
        tabLayout.getTabAt(3).setCustomView(tabFour);*/

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentVendorRequest(), "Requests");
        adapter.addFrag(new Fragment_Chat(), "Chat");
        adapter.addFrag(new FragmentVendorLeads(), "Leads");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                VendorBidirectionalDashboard.this);

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
