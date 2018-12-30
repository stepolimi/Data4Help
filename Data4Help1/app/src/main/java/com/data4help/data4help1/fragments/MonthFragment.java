package com.data4help.data4help1.fragments;


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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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

    private void getParameters() {
        JSONObject authUser = new JSONObject();
        try {
            authUser.put("authid", "authID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String url = "";
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.GET, url, authUser ,
                jsonObject -> System.out.print("hi"),
                volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        System.out.println("funziona!!!");
                        setHealthParameters();
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

    private void setHealthParameters() {
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
