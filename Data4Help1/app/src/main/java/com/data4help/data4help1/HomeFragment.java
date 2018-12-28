package com.data4help.data4help1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private Button day;
    private Button week;
    private Button month;
    private Button year;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.content_main, container, false);
        setButtons(view);

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

    private void setButtons(View view) {
        day = view.findViewById(R.id.dayButton);
        week = view.findViewById(R.id.weekButton);
        month = view.findViewById(R.id.monthButton);
        year = view.findViewById(R.id.yearButton);
    }
}
