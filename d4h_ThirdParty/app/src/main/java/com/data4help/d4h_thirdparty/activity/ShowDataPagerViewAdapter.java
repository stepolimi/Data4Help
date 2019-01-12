package com.data4help.d4h_thirdparty.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.FiveDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.FourDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.SixDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.ThreeDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.TodayFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.TwoDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showdatapagerfragment.YesterdayFragment;

public class ShowDataPagerViewAdapter extends FragmentPagerAdapter {

    ShowDataPagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new TodayFragment();
            case 1:
                return new YesterdayFragment();
            case 2:
                return new TwoDaysAgoFragment();
            case 3:
                return new ThreeDaysAgoFragment();
            case 4:
                return new FourDaysAgoFragment();
            case 5:
                return new FiveDaysAgoFragment();
            case 6:
                return new SixDaysAgoFragment();
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return 7;
    }
}
