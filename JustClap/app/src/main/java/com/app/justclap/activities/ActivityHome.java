package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;

public class ActivityHome extends AppCompatActivity {


    ViewPager pager;
    CustomPagerAdapter mCustomPagerAdapter;
    SharedPreferences.Editor ed;
    TextView btn_skip;
    String is_Vendor = "0";
    Context context;
    TextView text_title, text_view1;
    ImageView image_right;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    private BroadcastReceiver broadcastReceiver;
    int[] mResources = {R.drawable.screen1, R.drawable.screen2, R.drawable.screen3, R.drawable.screen4};
    int lastPosition;
    com.app.justclap.interfaces.ConnectionDetector cd;
    JSONArray contactsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        init();

        Intent in = getIntent();
        if (in.hasExtra("is_Vendor")) {
            is_Vendor = in.getExtras().getString("is_Vendor");
        }

        mCustomPagerAdapter = new CustomPagerAdapter(this);
        pager.setAdapter(mCustomPagerAdapter);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        setUiPageViewController();
        pager.setCurrentItem(0);

        setListener();

       /* mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);*/
        Log.e("GMRegistrationKey", "*********" + AppUtils.getGcmRegistrationKey(this));

    }



    private void init() {




        this.overridePendingTransition(R.anim.enter,
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
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        cd = new ConnectionDetector(context);
        pager = (ViewPager) findViewById(R.id.pager);
        text_title = (TextView) findViewById(R.id.text_title);
        text_view1 = (TextView) findViewById(R.id.text_view1);
        image_right = (ImageView) findViewById(R.id.image_right);
        SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
        ed = pref.edit();
        ed.putBoolean("Deactivate", true);
        ed.commit();

    }




    private void setListener() {

        btn_skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (is_Vendor.equalsIgnoreCase("1")) {

                    finish();
                } else if (is_Vendor.equalsIgnoreCase("2")) {

                    finish();
                } else if (is_Vendor.equalsIgnoreCase("0")) {

                    Intent i = new Intent(getBaseContext(), DashBoardActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastPosition != pager.getAdapter().getCount() - 1) {
                    pager.setCurrentItem(lastPosition + 1);
                } else {
                    Intent i = new Intent(getBaseContext(), DashBoardActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
                try {

                    if (position >= dotsCount) {
                        position = (position % dotsCount);
                    }
                    Log.e("position", "*" + position);

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }

                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("pagerCount", "*" + pager.getAdapter().getCount());
                if (position == pager.getAdapter().getCount() - 1) {

                    btn_skip.setVisibility(View.GONE);
                    image_right.setImageResource(R.drawable.tick_inside_circle);

                } else {
                    btn_skip.setVisibility(View.VISIBLE);
                    image_right.setImageResource(R.drawable.rightarrow);

                }

                if (position == 0) {
                    text_title.setText("User Dashboard");
                    text_view1.setText("We connect you to providers and users using bidirectional logic!  Match your needs with trusted professionals and users");
                } else if (position == 1) {
                    text_title.setText("Request Page");
                    text_view1.setText("We Match your requirement with right professional and user to get response and chat for guaranteed service");
                } else if (position == 2) {
                    text_title.setText("Lead Dashboard");
                    text_view1.setText("Customers are looking providers like you! Connect with customers to get hired");
                } else if (position == 3) {
                    text_title.setText("Bi-Directional Services");
                    text_view1.setText("Connect users to users and users to providers on single platform for Share Ride");
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUiPageViewController() {

        dotsCount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dotwhite));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //   params.gravity = Gravity.RIGHT;
            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dotwhite));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item,
                    container, false);

            ImageView imageView = (ImageView) itemView
                    .findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


}
