package com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.data4help.d4h_thirdparty.activity.ShowSingleDataActivity;
import com.data4help.d4h_thirdparty.activity.ShowSingleSubUserDataActivity;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.HomeSubscribedRequestFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TodaySingleSubUserFragment  extends Fragment implements Runnable {


    public static Map<Integer, Map<String, String>> sevenDaysOfARequest;

    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    public TodaySingleSubUserFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_day, container, false);

        setAttributes(view);
        Runnable runnable = this;
        Thread thread = new Thread(runnable);
        thread.start();
        return view;
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void run() {

        JSONObject userData = HomeSubscribedRequestFragment.getUserParams().get(HomeSubscribedRequestFragment.getButtonUserId());
        sevenDaysOfARequest = new HashMap<>();
        try {
            JSONArray sevenDaysHealthParam = Objects.requireNonNull(userData).getJSONArray("healthParametersSents");
            setContextParam(userData.getString("name"), userData.getString("surname"), userData.getInt("yearOfBirth"),
                    userData.getInt("height"), userData.getInt("weight"), userData.getString("sex"),
                    userData.getString("fiscalCode"));

            for (int i = 0; i < sevenDaysHealthParam.length(); i++) {
                JSONObject param = sevenDaysHealthParam.getJSONObject(i);
                Map<String, String> data = new HashMap<>();
                switch (i) {
                    case 0:
                        setTodayFragmentParam(param);
                        setData(param, data);
                        break;
                    default:
                        setData(param, data);
                        break;
                }
                sevenDaysOfARequest.put(i,data);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }


    /**
     *
     * calls a method in the main activity
     */
    private void setContextParam(String name, String surname,int yearOfBirth, int height, int weight, String sex, String fiscalCode){
        System.out.println(name);
        System.out.println(surname);
        ((ShowSingleSubUserDataActivity)Objects.requireNonNull(getActivity())).setRequestParam(name, surname, yearOfBirth, height, weight, sex, fiscalCode);
    }

    /**
     * @param param is the JSONObject that contains all health params
     * @throws JSONException is something wrong occurs
     *
     * sets all param in the textViews
     */
    private void setTodayFragmentParam(JSONObject param) throws JSONException {
        minDayBpm.setText(param.getString("minHeartBeat"));
        averageDayBmp.setText( param.getString("avgHeartBeat"));
        maxDayBmp.setText(param.getString("maxHeartBeat"));

        minDayPressure.setText(param.getString("minMinPressure"));
        maxDayPressure.setText(param.getString("maxMaxPressure"));

        DecimalFormat df = new DecimalFormat("#.##");
        if(!(Float.parseFloat(param.getString("minTemperature")) == 0.0) || !(Float.parseFloat(param.getString("maxTemperature"))== 0.0)) {
            minDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(param.getString("minTemperature")))));
            maxDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(param.getString("maxTemperature")))));
        }
        else{
            minDayTemperature.setText(param.getString("minTemperature"));
            maxDayTemperature.setText(param.getString("maxTemperature"));
        }
    }

    /**
     * @param param is the JSONObject that contains all params
     * @param data is the HashMap that must be filled
     *
     *              Sets all health params of a day in a map
     */
    private void setData(JSONObject param, Map<String, String> data) throws JSONException {
        data.put("minHeartBeat", param.getString("minHeartBeat"));
        data.put("avgHeartBeat", param.getString("avgHeartBeat"));
        data.put("maxHeartBeat", param.getString("maxHeartBeat"));
        data.put("minMinPressure", param.getString("minMinPressure"));
        data.put("maxMaxPressure", param.getString("maxMaxPressure"));
        data.put("minTemperature", param.getString("avgHeartBeat"));
        data.put("maxTemperature", param.getString("maxTemperature"));
    }

    /**
     * @param view is this
     *
     * Sets all attributes
     */
    private void setAttributes(View view) {
        minDayBpm = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayBpm);
        averageDayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.averageDayBpm);
        maxDayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayBpm);

        minDayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayPressure);
        maxDayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayPressure);

        minDayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayTemperature);
        maxDayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayTemperature);
    }
}
