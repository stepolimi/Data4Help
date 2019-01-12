package com.data4help.d4h_thirdparty.fragment.showdatapagerfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Objects;

import com.data4help.d4h_thirdparty.R.*;
import static com.data4help.d4h_thirdparty.Config.DAYTHREE;

public class TwoDaysAgoFragment extends Fragment implements Runnable{

    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    private View view;

    public TwoDaysAgoFragment(){}

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
            System.out.println("giorno 2");
            this.run();
        }
    }


    /**
     *
     * Sets all health parameters obtained from the response
     */
    private void setHealthParameters() {
        HashMap<String,String> data = new HashMap<>(Objects.requireNonNull(TodayFragment.sevenDaysOfARequest.get(DAYTHREE)));
        minDayBpm.setText(data.get("minHeartBeat"));
        averageDayBmp.setText(data.get("avgHeartBeat"));
        maxDayBmp.setText(data.get("maxHeartBeat"));

        minDayPressure.setText(data.get("minMinPressure"));
        maxDayPressure.setText(data.get("maxMaxPressure"));

        minDayTemperature.setText(data.get("minTemperature"));
        maxDayTemperature.setText(data.get("maxTemperature"));
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
        //setHealthParameters();
    }
}
