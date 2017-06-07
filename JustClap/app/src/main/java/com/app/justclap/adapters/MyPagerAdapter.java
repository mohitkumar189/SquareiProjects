package com.app.justclap.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.justclap.fragment.FragmentDashboardServices;
import com.app.justclap.fragment.FragmentRequest;
import com.app.justclap.fragment.FragmentServiceRequest;
import com.app.justclap.fragment.Fragment_Chat;
import com.app.justclap.fragment.Fragment_Notification;


/**
 * Created by admin on 05-02-2016.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentDashboardServices tab1 = new FragmentDashboardServices();
                return tab1;
            case 1:
                FragmentServiceRequest tab2 = new FragmentServiceRequest();
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