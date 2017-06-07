package com.app.justclap.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import com.app.justclap.models.ModelService;
import com.app.justclap.vendor_fragments.FragmentProfile;
import com.app.justclap.vendor_fragments.Fragment_BusinessInfo;
import com.app.justclap.vendor_fragments.Fragment_VendorPortfolio;


/**
 * Created by admin on 05-02-2016.
 */
public class VendorProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<ModelService> arrayList=new ArrayList<>();

    public VendorProfilePagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<ModelService> arrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.arrayList=arrayList;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentProfile tab1 = new FragmentProfile();
                Bundle b = new Bundle();
                b.putString("profile", arrayList.get(0).getPersonalDetailsArray());
                tab1.setArguments(b);
                return tab1;
            case 1:
                Fragment_BusinessInfo tab2 = new Fragment_BusinessInfo();
                Bundle b1 = new Bundle();
                b1.putString("business", arrayList.get(0).getBussinessDetailsArray());
                b1.putString("cert", arrayList.get(0).getCertificateDetailsArray());
                tab2.setArguments(b1);
                return tab2;
            case 2:
                Fragment_VendorPortfolio tab3 = new Fragment_VendorPortfolio();
                Bundle b2 = new Bundle();
                b2.putString("portfolio", arrayList.get(0).getPortfolioDetailsArray());
                tab3.setArguments(b2);
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