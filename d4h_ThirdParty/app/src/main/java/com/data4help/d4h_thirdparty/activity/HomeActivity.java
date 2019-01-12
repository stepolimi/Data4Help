package com.data4help.d4h_thirdparty.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.data4help.d4h_thirdparty.R.*;

public class HomeActivity extends AppCompatActivity{

    private Button subscribedData;
    private Button waitingData;
    private Button singleRequest;
    private Button groupRequest;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.home);

        subscribedData = findViewById(id.subscribeData);
        waitingData = findViewById(id.waitingData);
        singleRequest = findViewById(id.singleRequest);
        groupRequest = findViewById(id.groupRequest);

        viewPager = findViewById(id.mainPage);
        HomePagerViewAdapter pagerAdapter = new HomePagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        setItem(subscribedData, 0);
        setItem(waitingData, 1);
        setItem(groupRequest, 2);
        setItem(singleRequest, 3);

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
                setColor(subscribedData, waitingData, singleRequest, groupRequest);
                break;
            case 1:
                setColor(waitingData, groupRequest, singleRequest, subscribedData);
                break;
            case 2:
                setColor(groupRequest, singleRequest, subscribedData, waitingData);
                break;
            case 3:
                setColor(singleRequest, subscribedData, waitingData, groupRequest);
        }
    }

    /**
     * @param selected is the textView related to the selected fragment
     * @param unselected1 is one of the textView not related to the fragment
     * @param unselected2 is one of the textView not related to the selected fragment
     */
    private void setColor(Button selected, Button unselected1, Button unselected2, Button unselected3){
        selected.setTextColor(getColor(color.colorAccent));
        unselected1.setTextColor(getColor(color.black));
        unselected2.setTextColor(getColor(color.black));
        unselected3.setTextColor(getColor(color.black));
    }

    /**
     * @param button is the selected textVIew
     * @param position is the fragment position associated to the selected text view
     *
     * Changes the fragment depending on the selected textView
     */
    private void setItem(final Button button, final int position ) {
        button.setOnClickListener(v -> {
            changeTab(position);
            viewPager.setCurrentItem(position);
        });

    }

}
