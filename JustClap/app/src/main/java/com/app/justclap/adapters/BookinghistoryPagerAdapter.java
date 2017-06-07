package com.app.justclap.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.justclap.fragment.FragmentCancelledBooking;
import com.app.justclap.fragment.FragmentCompletedBooking;
import com.app.justclap.fragment.FragmentOnGoingBooking;


/**
 * Created by admin on 05-02-2016.
 */
public class BookinghistoryPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public BookinghistoryPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentOnGoingBooking tab1 = new FragmentOnGoingBooking();

                return tab1;
            case 1:
                FragmentCompletedBooking  tab2 = new FragmentCompletedBooking();

                return tab2;
            case 2:

                FragmentCancelledBooking  tab3 = new FragmentCancelledBooking();

                return tab3;
         /*   case 3:
                Fragment_CheckboxTypeQues tab4 = new Fragment_CheckboxTypeQues();
                return tab4;
            case 4:
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