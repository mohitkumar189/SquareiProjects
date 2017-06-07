package com.app.justclap.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.justclap.fragments.FragmentUserRequests;
import com.app.justclap.fragments.Fragment_Chat;
import com.app.justclap.fragments.Fragment_Notification;
import com.app.justclap.fragments.HomeFragment;


/**
 * Created by admin on 05-02-2016.
 */
public class DashboardPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DashboardPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                FragmentUserRequests tab2 = new FragmentUserRequests();
                return tab2;
            case 2:
                Fragment_Chat tab3 = new Fragment_Chat();
                return tab3;
            case 3:
                Fragment_Notification tab4 = new Fragment_Notification();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}