package com.data4help.d4h_thirdparty;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity{

    private TextView data;
    private TextView singleRequest;
    private TextView groupRequest;
    private ViewPager viewPager;

    private PagerViewAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        data = findViewById(R.id.data);
        singleRequest = findViewById(R.id.singleRequest);
        groupRequest = findViewById(R.id.groupRequest);

        viewPager = findViewById(R.id.mainPage);
        pagerAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        setItem(data, 0);
        setItem(groupRequest, 1);
        setItem(singleRequest, 2);

    }

    private void setItem(final TextView textView, final int item) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(item);
            }
        });

    }
}
