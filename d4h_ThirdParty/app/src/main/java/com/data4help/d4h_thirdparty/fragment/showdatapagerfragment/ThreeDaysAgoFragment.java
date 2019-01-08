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
import com.data4help.d4h_thirdparty.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ThreeDaysAgoFragment extends Fragment implements Runnable{

    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    private JSONObject authUser;
    private View view;

    public ThreeDaysAgoFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_today, container, false);
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
            minDayBpm.setText(jsonObj.getString("minHeartBeat"));
            averageDayBmp.setText( jsonObj.getString("avgHeartBeat"));
            maxDayBmp.setText(jsonObj.getString("maxHeartBeat"));

            minDayPressure.setText(jsonObj.getString("minMinPressure"));
            maxDayPressure.setText(jsonObj.getString("maxMaxPressure"));

            minDayTemperature.setText(jsonObj.getString("minTemperature"));
            maxDayTemperature.setText(jsonObj.getString("maxTemperature"));
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
        minDayBpm = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayBpm);
        averageDayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.averageDayBpm);
        maxDayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayBpm);

        minDayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayPressure);
        maxDayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayPressure);

        minDayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.minDayTemperature);
        maxDayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxDayTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        getParameters();
    }
}
