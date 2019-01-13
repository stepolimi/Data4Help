package com.data4help.d4h_thirdparty.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.FiveDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.FourDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.SixDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.ThreeDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.TodaySingleUserFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.TwoDaysAgoFragment;
import com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment.YesterdayFragment;

public class ShowUserDataPagerViewAdapter extends FragmentPagerAdapter {

    ShowUserDataPagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new TodaySingleUserFragment();
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

