package com.data4help.d4h_thirdparty.fragment.showdatapagerfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.R.*;
import com.data4help.d4h_thirdparty.activity.ShowGroupDataActivity;
import com.data4help.d4h_thirdparty.activity.ShowSingleDataActivity;

public class TodayFragment extends Fragment implements Runnable{


    public static Map<Integer, Map<String, String>> sevenDaysOfARequest;
    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    private JSONObject authUser;
    private View view;

    public TodayFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_day, container, false);
        System.out.println("oggi");
        this.run();

        return view;
    }


    /**
     * Asks the daily health parameters of the user
     */
    private void getParameters() {
        setUserId();


        JsonObjectRequest dailyHealthParamReq = new JsonObjectRequest(Request.Method.POST, null, authUser ,
                jsonObject -> {},
                volleyError -> {}){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            setHealthParameters(json);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    //TODO
                    case 403:
                        System.out.println("The access has been denied. Try again.");
                        break;
                    case 401:
                        System.out.println("The given email is already in the DB. Change it or login.");
                        break;

                }
                return super.parseNetworkResponse(response);
            }
        };
        //ShowGroupDataActivity.queue.add(dailyHealthParamReq);
    }

    /**
     * Sets the JSONObject containing the user di
     */
    private void setUserId() {
        authUser = new JSONObject();
        try {
            authUser.put("id", AuthToken.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param json is the response
     *
     * Sets all health parameters obtained from the response
     */
    @SuppressLint("UseSparseArrays")
    private void setHealthParameters(String json) {
        sevenDaysOfARequest = new HashMap<>();
        try {
            JSONArray sevenDaysHealthParam = new JSONArray(json);
            for (int i = 0; i < sevenDaysHealthParam.length(); i++) {
                JSONObject param = sevenDaysHealthParam.getJSONObject(i);
                Map<String, String> data = new HashMap<>();
                switch (i) {
                    case 0:
                        setContextParam(param);
                        break;
                    default:
                        if(i == 1)
                            setTodayFragmentParam(param);
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
     * @param param is the JSONObject that contains all personal detail
     * @throws JSONException is something wrong occurs
     *
     * calls a method in the main activity
     */
    private void setContextParam(JSONObject param) throws JSONException {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        if(context instanceof ShowGroupDataActivity)
            ShowGroupDataActivity.setRequestParam(param);
        else if (context instanceof ShowSingleDataActivity)
            ShowSingleDataActivity.setRequestParam(param);
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

        minDayTemperature.setText(param.getString("minTemperature"));
        maxDayTemperature.setText(param.getString("maxTemperature"));
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
        getParameters();
    }
}
