package com.data4help.d4h_thirdparty.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.data4help.d4h_thirdparty.fragments.GroupRequestFragment;
import com.data4help.d4h_thirdparty.fragments.HomeFragment;
import com.data4help.d4h_thirdparty.fragments.SingleRequestFragment;

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
                return new HomeFragment();
            case 1:
                return new GroupRequestFragment();
            case 2:
                return new SingleRequestFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
