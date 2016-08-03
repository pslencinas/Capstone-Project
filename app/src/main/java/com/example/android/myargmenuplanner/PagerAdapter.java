package com.example.android.myargmenuplanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter{

    int     mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentLW tab1 = new TabFragmentLW();
                return tab1;
            case 1:
                TabFragmentTW tab2 = new TabFragmentTW();
                return tab2;
            case 2:
                TabFragmentNW tab3 = new TabFragmentNW();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
