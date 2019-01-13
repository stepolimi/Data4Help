package com.data4help.data4help1.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.data4help.data4help1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private Button day;
    private Button week;
    private Button month;
    private Button year;
    private static TextView height;
    private static TextView weight;

    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main, container, false);


        setAttributes(view);

        viewPager = view.findViewById(R.id.healthFragment);
        PagerViewAdapter pagerAdapter = new PagerViewAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.getItem(0);


        /*setItem(day, 0);
        setItem(week, 1);
        setItem(month, 2);
        setItem(year, 3);*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {}
            @Override
            public void onPageSelected(int position) { changeTab(position); }
            @Override
            public void onPageScrollStateChanged(int position) {}
        });
        return view;
    }


    /**
     * @param button is the selected textVIew
     * @param position is the fragment position associated to the selected text view
     *
     * Changes the fragment depending on the selected textView
     */
    private void setItem(Button button, int position) {
        button.setOnClickListener(v -> {
            changeTab(position);
            viewPager.setCurrentItem(position);});
    }
    /**
     * @param position is the int related to the chosen fragment
     *
     * Modifies the textView color in relation to the the chosen position
     */
    private void changeTab(int position) {
        switch (position){
            case 0:
                setColor(day, week, month, year);
                break;
            case 1:
                setColor(week, month, year, day);
                break;
            case 2:
                setColor(month, year, day, week);
                break;
            case 3:
                setColor(year, day, week, month);
                break;
            default:
                setColor(day, week, month, year);
                break;
        }
    }

    /**
     * @param selected is the textView related to the selected fragment
     * @param unselected1 is one of the textView not related to the fragment
     * @param unselected2 is one of the textView not related to the selected fragment
     * @param unselected3 is one of the textView not related to the selected fragment
     */
    @SuppressLint("ResourceAsColor")
    private void setColor(Button selected, Button unselected1, Button unselected2, Button unselected3){
        selected.setTextColor(getResources().getColor(R.color.colorAccent));
        unselected1.setTextColor(getResources().getColor(R.color.black));
        unselected2.setTextColor(getResources().getColor(R.color.black));
        unselected3.setTextColor(getResources().getColor(R.color.black));
    }


    /**
     * @param view is this
     *
     * Sets all attributes
     */
    private void setAttributes(View view) {
        day = view.findViewById(R.id.dayButton);
        week = view.findViewById(R.id.weekButton);
        month = view.findViewById(R.id.monthButton);
        year = view.findViewById(R.id.yearButton);

        weight = view.findViewById(R.id.homeWeight);
        height = view.findViewById(R.id.homeHeight);

        setDate(view);
    }

    /**
     * Sets the date in which the user is opening the home fragment
     */
    @SuppressLint("SimpleDateFormat")
    private void setDate(View view) {
        TextView date = view.findViewById(R.id.date);
        TextView hour = view.findViewById(R.id.hour);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");

        date.setText(formatter.format(todayDate));
        hour.setText(formatter1.format(todayDate));
    }


    public static void setHeightWeight(String heightGiven, String weightGiven){
        if (heightGiven != null) {
            String text = "Height: " + heightGiven + " cm";
            height.setText(text);
        }
        if (weightGiven != null) {
            String text = "Weight: " + weightGiven + " kg";
            weight.setText(text);
        }
    }
}
