package com.app.justclap.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.justclap.fragment.Fragment_Chat;
import com.app.justclap.vendor_fragments.FragmentCompletedLeads;
import com.app.justclap.vendor_fragments.FragmentNewLead;
import com.app.justclap.vendor_fragments.FragmentOngoingLeads;


/**
 * Created by admin on 05-02-2016.
 */
public class VendorDashboardPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public VendorDashboardPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentNewLead tab1 = new FragmentNewLead();
                return tab1;
            case 1:
                FragmentOngoingLeads tab2 = new FragmentOngoingLeads();
                return tab2;
            case 2:
                FragmentCompletedLeads tab3 = new FragmentCompletedLeads();
                return tab3;
            case 3:
                Fragment_Chat tab4 = new Fragment_Chat();
                return tab4;
           /*    case 4:
                FragmentDashboardServices tab5 = new FragmentDashboardServices();
                return tab5;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}