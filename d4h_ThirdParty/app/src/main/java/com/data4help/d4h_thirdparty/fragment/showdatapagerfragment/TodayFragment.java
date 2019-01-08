package com.data4help.d4h_thirdparty.fragment.showdatapagerfragment;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.R.*;

public class TodayFragment extends Fragment implements Runnable{

    private TextView minTodayBpm;
    private TextView averageTodayBmp;
    private TextView maxTodayBmp;

    private TextView minTodayPressure;
    private TextView maxTodayPressure;

    private TextView minTodayTemperature;
    private TextView maxTodayTemperature;

    private JSONObject authUser;
    private View view;

    public TodayFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_today, container, false);
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
        //ShowDataActivity.queue.add(dailyHealthParamReq);
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
    private void setHealthParameters(String json) {

        try {
            JSONObject jsonObj = new JSONObject(json);
            minTodayBpm.setText(jsonObj.getString("minHeartBeat"));
            averageTodayBmp.setText( jsonObj.getString("avgHeartBeat"));
            maxTodayBmp.setText(jsonObj.getString("maxHeartBeat"));

            minTodayPressure.setText(jsonObj.getString("minMinPressure"));
            maxTodayPressure.setText(jsonObj.getString("maxMaxPressure"));

            minTodayTemperature.setText(jsonObj.getString("minTemperature"));
            maxTodayTemperature.setText(jsonObj.getString("maxTemperature"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view is this
     *
     * Sets all attributes
     */
    private void setAttributes(View view) {
        minTodayBpm = view.findViewById(id.minTodayBpm);
        averageTodayBmp = view.findViewById(id.averageTodayBpm);
        maxTodayBmp = view.findViewById(id.maxTodayBpm);

        minTodayPressure = view.findViewById(id.minTodayPressure);
        maxTodayPressure = view.findViewById(id.maxTodayPressure);

        minTodayTemperature = view.findViewById(id.minTodayTemperature);
        maxTodayTemperature = view.findViewById(id.maxTodayTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        getParameters();
    }
}
