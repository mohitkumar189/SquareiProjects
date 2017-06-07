package com.app.lunavista.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.lunavista.Fragments.FragmentPlayList;
import com.app.lunavista.Fragments.FragmentPopularSongList;
import com.app.lunavista.Fragments.FragmentSongList;
import com.app.lunavista.Fragments.FragmentallSongs;


/**
 * Created by admin on 05-02-2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HomePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentallSongs tab3 = new FragmentallSongs();
                return tab3;
            case 1:
                FragmentSongList tab1 = new FragmentSongList();
                return tab1;
            case 2:
                FragmentPlayList tab2 = new FragmentPlayList();
                return tab2;
            case 3:
                FragmentPopularSongList tab4 = new FragmentPopularSongList();
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