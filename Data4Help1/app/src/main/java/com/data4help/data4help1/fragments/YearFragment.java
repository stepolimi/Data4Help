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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.data4help1.Config.YEARHEALTHPARAMURL;
import static com.data4help.data4help1.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearFragment extends Fragment implements Runnable {
    private TextView minYearBpm;
    private TextView averageYearBmp;
    private TextView maxYearBmp;

    private TextView minYearPressure;
    private TextView maxYearPressure;

    private TextView minYearTemperature;
    private TextView maxYearTemperature;

    private JSONObject authUser;
    private View view;

    public YearFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_year, container, false);
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

    private void getParameters() {
        setUserId();

        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, YEARHEALTHPARAMURL, authUser ,
                jsonObject -> System.out.print("hi"),
                volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
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
            minYearBpm.setText(jsonObj.getString("minHeartBeat"));
            averageYearBmp.setText( jsonObj.getString("avgHeartBeat"));
            maxYearBmp.setText(jsonObj.getString("maxHeartBeat"));

            minYearPressure.setText(jsonObj.getString("minMinPressure"));
            maxYearPressure.setText(jsonObj.getString("maxMaxPressure"));

            minYearTemperature.setText(jsonObj.getString("minTemperature"));
            maxYearTemperature.setText(jsonObj.getString("maxTemperature"));
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
        minYearBpm = view.findViewById(id.minYearBpm);
        averageYearBmp = view.findViewById(id.averageYearBpm);
        maxYearBmp = view.findViewById(id.maxYearBpm);

        minYearPressure = view.findViewById(id.minYearPressure);
        maxYearPressure = view.findViewById(id.maxYearPressure);

        minYearTemperature = view.findViewById(id.minYearTemperature);
        maxYearTemperature = view.findViewById(id.maxYearTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        getParameters();

    }
}
