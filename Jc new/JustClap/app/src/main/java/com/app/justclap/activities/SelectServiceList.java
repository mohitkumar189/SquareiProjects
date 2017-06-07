package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.fragments.FragmentBiServiceList;
import com.app.justclap.fragments.FragmentUniServiceList;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceList extends AppCompatActivity {

    private TabLayout tabLayout;
    private Context context;
    private ViewPager mViewPager;
    private TextView text_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_list);

        context = this;
        init();
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        text_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppUtils.getBiSelectedServices(context).equalsIgnoreCase("")) {
                    setResult(511);
                    finish();
                } else {
                    Toast.makeText(context, "Please select service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    text_continue.setVisibility(View.GONE);
                } else {
                    text_continue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 52) {
            setResult(512);
            finish();
        }
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Uni directional");
        tabLayout.getTabAt(1).setText("Bi directional");

    }


    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Services");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.container);
        text_continue = (TextView) findViewById(R.id.text_continue);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentUniServiceList(), "Uni directional");
        adapter.addFrag(new FragmentBiServiceList(), "Bi directional");
        viewPager.setAdapter(adapter);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


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
}
