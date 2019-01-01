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

import static com.data4help.data4help1.Config.MONTHLYHEALTHPARAM;
import static com.data4help.data4help1.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {
    private TextView minMonthBpm;
    private TextView averageMonthBmp;
    private TextView maxMonthBmp;

    private TextView minMonthPressure;
    private TextView maxMonthPressure;

    private TextView minMonthTemperature;
    private TextView maxMonthTemperature;

    private JSONObject authUser;

    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.fragment_month, container, false);

        setAttributes(view);
        getParameters();

        return view;
    }

    /**
     * Asks the daily health parameters of the user
     */
    private void getParameters() {
        setUserId();

        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, MONTHLYHEALTHPARAM, authUser ,
                jsonObject -> System.out.print("hi"),
                volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        String json = null;
                        try {
                            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        setHealthParameters(json);
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
     * Sets the JSONObject containing the user di
     */
    private void setUserId() {
        authUser = new JSONObject();
        try {
            authUser.put("userID", AuthToken.getId());
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
        //TODO
        minMonthBpm.setText("");
        averageMonthBmp.setText("");
        maxMonthBmp.setText("");

        minMonthPressure.setText("");
        maxMonthPressure.setText("");

        minMonthTemperature.setText("");
        maxMonthTemperature.setText("");
    }

    /**
     * @param view is this
     *
     * Sets all attributes
     */
    private void setAttributes(View view) {
        minMonthBpm = view.findViewById(id.minMonthBpm);
        averageMonthBmp = view.findViewById(id.averageMonthBpm);
        maxMonthBmp = view.findViewById(id.maxMonthBpm);

        minMonthPressure = view.findViewById(id.minMonthPressure);
        maxMonthPressure = view.findViewById(id.maxMonthPressure);

        minMonthTemperature = view.findViewById(id.minMonthTemperature);
        maxMonthTemperature = view.findViewById(id.maxMonthTemperature);
    }
}
