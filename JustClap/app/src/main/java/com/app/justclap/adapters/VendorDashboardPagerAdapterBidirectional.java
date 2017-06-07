package com.app.justclap.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.justclap.fragment.FragmentDashboardServices;
import com.app.justclap.fragment.FragmentOnGoingBooking;
import com.app.justclap.fragment.Fragment_Chat;
import com.app.justclap.vendor_fragments.FragmentBidirectionalLeads;
import com.app.justclap.vendor_fragments.FragmentVendorRequests;
import com.app.justclap.vendor_fragments.FragmentVendorServiceRequests;


/**
 * Created by admin on 05-02-2016.
 */
public class VendorDashboardPagerAdapterBidirectional extends FragmentPagerAdapter {
    int mNumOfTabs;

    public VendorDashboardPagerAdapterBidirectional(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentVendorServiceRequests tab1 = new FragmentVendorServiceRequests();
                return tab1;
            case 2:
                FragmentBidirectionalLeads tab5 = new FragmentBidirectionalLeads();
                return tab5;
            case 1:
                Fragment_Chat tab4 = new Fragment_Chat();
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