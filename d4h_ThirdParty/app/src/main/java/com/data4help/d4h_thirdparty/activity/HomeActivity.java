package com.data4help.d4h_thirdparty.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.data4help.d4h_thirdparty.R.*;

public class HomeActivity extends AppCompatActivity{

    private TextView data;
    private TextView singleRequest;
    private TextView groupRequest;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.home);

        data = findViewById(id.data);
        singleRequest = findViewById(id.singleRequest);
        groupRequest = findViewById(id.groupRequest);

        viewPager = findViewById(id.mainPage);
        PagerViewAdapter pagerAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        setItem(data, 0);
        setItem(groupRequest, 1);
        setItem(singleRequest, 2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {}

            @Override
            public void onPageSelected(int position) { changeTab(position); }

            @Override
            public void onPageScrollStateChanged(int position) {}
        });

    }

    /**
     * @param position is the int related to the chosen fragment
     *
     * Modifies the textView color in relation to the the chosen position
     */
    public void changeTab(int position) {
        switch (position){
            case 0:
                setColor(data, singleRequest, groupRequest);
                break;
            case 1:
                setColor(groupRequest, singleRequest, data);
                break;
            case 2:
                setColor(singleRequest, groupRequest, data);
                break;
            default:
                setColor(data, singleRequest, groupRequest);
                break;
        }
    }

    /**
     * @param selected is the textView related to the selected fragment
     * @param unselected1 is one of the textView not related to the fragment
     * @param unselected2 is one of the textView not related to the selected fragment
     */
    private void setColor(TextView selected, TextView unselected1, TextView unselected2){
        selected.setTextColor(getColor(color.colorAccent));
        unselected1.setTextColor(getColor(color.greyDark));
        unselected2.setTextColor(getColor(color.greyDark));
    }

    /**
     * @param textView is the selected textVIew
     * @param position is the fragment position associated to the selected text view
     *
     * Changes the fragment depending on the selected textView
     */
    private void setItem(final TextView textView, final int position ) {
        textView.setOnClickListener(v -> {
            changeTab(position);
            viewPager.setCurrentItem(position);
        });

    }

}
