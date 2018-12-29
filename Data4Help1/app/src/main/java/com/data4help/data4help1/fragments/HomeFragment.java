package com.data4help.data4help1.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private Button day;
    private Button week;
    private Button month;
    private Button year;

    private TextView date;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(com.data4help.data4help1.R.layout.content_main, container, false);

        setAttributes(view);

        buttonAction(day);
        buttonAction(week);
        buttonAction(month);
        buttonAction(year);
        return view;

    }

    /**
     * @param healthParamPeriod is the pressed button
     *
     * Calls the server to get the health values of the chosen period
     */
    private void buttonAction(Button healthParamPeriod) {
        healthParamPeriod.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                   }
                               }
        );
    }

    private void setAttributes(View view) {
        day = view.findViewById(com.data4help.data4help1.R.id.dayButton);
        week = view.findViewById(com.data4help.data4help1.R.id.weekButton);
        month = view.findViewById(com.data4help.data4help1.R.id.monthButton);
        year = view.findViewById(com.data4help.data4help1.R.id.yearButton);

        date = view.findViewById(com.data4help.data4help1.R.id.date);
        setDate();
    }

    /**
     * Sets the date in which the user is opening the home fragment
     */
    @SuppressLint("SimpleDateFormat")
    private void setDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-mm-yyyy");
        date.setText(formatter.format(todayDate));
    }
}
