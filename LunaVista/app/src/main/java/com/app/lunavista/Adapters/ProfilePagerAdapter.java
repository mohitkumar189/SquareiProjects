package com.app.lunavista.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.lunavista.Fragments.FragmentDuetSong;
import com.app.lunavista.Fragments.FragmentPlayList;
import com.app.lunavista.Fragments.FragmentSongList;
import com.app.lunavista.Fragments.FragmentUserRecording;
import com.app.lunavista.Fragments.FragmentallSongs;


/**
 * Created by admin on 05-02-2016.
 */
public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                FragmentUserRecording tab3 = new FragmentUserRecording();
                return tab3;

            case 1:
                FragmentDuetSong tab1 = new FragmentDuetSong();
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}