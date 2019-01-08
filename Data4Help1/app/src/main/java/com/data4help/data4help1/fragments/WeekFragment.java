package com.data4help.data4help1.fragments;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.data4help1.Config.WEEKLYHEALTHPARAMURL;
import static com.data4help.data4help1.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment implements Runnable {
    private TextView minWeekBpm;
    private TextView averageWeekBmp;
    private TextView maxWeekBmp;

    private TextView minWeekPressure;
    private TextView maxWeekPressure;

    private TextView minWeekTemperature;
    private TextView maxWeekTemperature;

    private JSONObject authUser;
    private View view;

    public WeekFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_week, container, false);

        run();

        return view;
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
     * Asks the daily health parameters of the user
     */
    private void getParameters() {
        setUserId();

        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, WEEKLYHEALTHPARAMURL, authUser ,
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
                        //TODO
                        break;
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
        queue.add(jobReq);
    }


    /**
     *
     * Sets all health parameters obtained from the response
     */
    private void setHealthParameters(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            minWeekBpm.setText(jsonObj.getString("minHeartBeat"));
            averageWeekBmp.setText( jsonObj.getString("avgHeartBeat"));
            maxWeekBmp.setText(jsonObj.getString("maxHeartBeat"));

            minWeekPressure.setText(jsonObj.getString("minMinPressure"));
            maxWeekPressure.setText(jsonObj.getString("maxMaxPressure"));

            minWeekTemperature.setText(jsonObj.getString("minTemperature"));
            maxWeekTemperature.setText(jsonObj.getString("maxTemperature"));
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
        minWeekBpm = view.findViewById(id.minWeekBpm);
        averageWeekBmp = view.findViewById(id.averageWeekBpm);
        maxWeekBmp = view.findViewById(id.maxWeekBpm);

        minWeekPressure = view.findViewById(id.minWeekPressure);
        maxWeekPressure = view.findViewById(id.maxWeekPressure);

        minWeekTemperature = view.findViewById(id.minWeekTemperature);
        maxWeekTemperature = view.findViewById(id.maxWeekTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        getParameters();

    }
}
