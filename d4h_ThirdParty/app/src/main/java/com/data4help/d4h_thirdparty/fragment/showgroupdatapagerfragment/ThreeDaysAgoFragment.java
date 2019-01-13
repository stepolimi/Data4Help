package com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

import com.data4help.d4h_thirdparty.R.*;
import static com.data4help.d4h_thirdparty.Config.DAYTHREE;
import static com.data4help.d4h_thirdparty.Config.DAYTHREE;

public class ThreeDaysAgoFragment extends Fragment implements Runnable{

    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    private View view;

    public ThreeDaysAgoFragment(){}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(layout.fragment_day, container, false);
        return view;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            Runnable runnable = this;
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    /**
     *
     * Sets all health parameters obtained from the response
     */
    private void setHealthParameters() {
        HashMap<String,String> data = selectData();
        minDayBpm.setText(data.get("minHeartBeat"));
        averageDayBmp.setText(data.get("avgHeartBeat"));
        maxDayBmp.setText(data.get("maxHeartBeat"));

        minDayPressure.setText(data.get("minMinPressure"));
        maxDayPressure.setText(data.get("maxMaxPressure"));

        DecimalFormat df = new DecimalFormat("#.##");
        if(!(Float.parseFloat(Objects.requireNonNull(data.get("minTemperature"))) == 0.0) || !(Float.parseFloat(Objects.requireNonNull(data.get("maxTemperature")))== 0.0)) {
            minDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(Objects.requireNonNull(data.get("minTemperature"))))));
            maxDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(Objects.requireNonNull(data.get("maxTemperature"))))));
        }
        else{
            minDayTemperature.setText(data.get("minTemperature"));
            maxDayTemperature.setText(data.get("maxTemperature"));
        }
    }

    /**
     * @return the hashmap related tot he correct type of fragment which has been raised up from the third party
     */
    private HashMap<String, String> selectData() {
        if(TodayGroupFragment.sevenDaysOfARequest != null)
            return new HashMap<>(Objects.requireNonNull(TodayGroupFragment.sevenDaysOfARequest.get(DAYTHREE)));
        else if(TodayGroupSubFragment.sevenDaysOfARequest != null)
            return new HashMap<>(Objects.requireNonNull(TodayGroupSubFragment.sevenDaysOfARequest.get(DAYTHREE)));
        else if(TodaySingleUserFragment.sevenDaysOfARequest != null)
            return new HashMap<>(Objects.requireNonNull(TodaySingleUserFragment.sevenDaysOfARequest.get(DAYTHREE)));
        else if(TodaySingleSubUserFragment.sevenDaysOfARequest != null)
            return new HashMap<>(Objects.requireNonNull(TodaySingleSubUserFragment.sevenDaysOfARequest.get(DAYTHREE)));
        else
            return null;
    }

    /**
     * @param view is this
     *
     * Sets all attributes
     */
    private void setAttributes(View view) {
        minDayBpm = view.findViewById(id.minDayBpm);
        averageDayBmp = view.findViewById(id.averageDayBpm);
        maxDayBmp = view.findViewById(id.maxDayBpm);

        minDayPressure = view.findViewById(id.minDayPressure);
        maxDayPressure = view.findViewById(id.maxDayPressure);

        minDayTemperature = view.findViewById(id.minDayTemperature);
        maxDayTemperature = view.findViewById(id.maxDayTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        setHealthParameters();
    }
}
