package com.data4help.data4help1.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter{

    PagerViewAdapter(FragmentManager fm) { super(fm); }

    /**
     * @param position the id of the fragment
     * @return the fragment related to the selected id
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DayFragment();
            case 1:
                return new WeekFragment();
            case 2:
                return new MonthFragment();
            case 3:
                return new YearFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
