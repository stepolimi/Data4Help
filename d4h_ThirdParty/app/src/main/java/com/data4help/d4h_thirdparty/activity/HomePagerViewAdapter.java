package com.data4help.d4h_thirdparty.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.data4help.d4h_thirdparty.fragment.homepagerfragment.GroupRequestFragment;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.HomeSubscribedRequestFragment;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.WaitingUserAnswerFragment;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.SingleRequestFragment;

class HomePagerViewAdapter extends FragmentPagerAdapter{

    HomePagerViewAdapter(FragmentManager fm) { super(fm); }

    /**
     * @param position the id of the fragment
     * @return the fragment related to the selected id
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                System.out.println("home");
                return new HomeSubscribedRequestFragment();
            case 1:
                System.out.println("waiting");
                return new WaitingUserAnswerFragment();
            case 2:
                System.out.println("group");
                return new GroupRequestFragment();
            case 3:
                System.out.println("single");
                return new SingleRequestFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
