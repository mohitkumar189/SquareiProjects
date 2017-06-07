package com.app.justclap.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import com.app.justclap.fragment.Fragment_About;
import com.app.justclap.fragment.Fragment_Portfolio;
import com.app.justclap.fragment.Fragment_Reviews;
import com.app.justclap.models.ModelService;


/**
 * Created by admin on 05-02-2016.
 */
public class VendoDetailPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<ModelService> arrayList;

    public VendoDetailPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<ModelService> arrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.arrayList=arrayList;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                Fragment_About about = new Fragment_About();
                Bundle b = new Bundle();
                b.putString("profile", arrayList.get(0).getPersonalDetailsArray());
                about.setArguments(b);
                return about;

            case 1:

                Fragment_Reviews tab1 = new Fragment_Reviews();
                Bundle b1 = new Bundle();
                b1.putString("review", arrayList.get(0).getReviewArray());

                tab1.setArguments(b1);
                return tab1;

            case 2:

                Fragment_Portfolio tab2 = new Fragment_Portfolio();
                Bundle b2 = new Bundle();
                b2.putString("portfolio", arrayList.get(0).getPortfolioDetailsArray());
                tab2.setArguments(b2);
                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}