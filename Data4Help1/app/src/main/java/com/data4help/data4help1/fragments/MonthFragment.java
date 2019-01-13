package com.data4help.data4help1.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.text.DecimalFormat;
import java.util.Objects;

import static com.data4help.data4help1.Config.BADREQUEST;
import static com.data4help.data4help1.Config.INTERNALSERVERERROR;
import static com.data4help.data4help1.Config.MONTHLYHEALTHPARAMURL;
import static com.data4help.data4help1.Config.NOTFOUND;
import static com.data4help.data4help1.Config.UNAUTHORIZED;
import static com.data4help.data4help1.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment implements Runnable {
    private TextView minMonthBpm;
    private TextView averageMonthBmp;
    private TextView maxMonthBmp;

    private TextView minMonthPressure;
    private TextView maxMonthPressure;

    private TextView minMonthTemperature;
    private TextView maxMonthTemperature;

    private JSONObject authUser;
    private View view;

    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_month, container, false);
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
     * Asks the daily health parameters of the user
     */
    private void getParameters() {
        setUserId();

        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, MONTHLYHEALTHPARAMURL, authUser ,
                jsonObject -> {},
                volleyError ->  {
                    if(volleyError.networkResponse != null)
                        getVolleyError(volleyError.networkResponse.statusCode);}){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            setHealthParameters(json);
                        } catch (UnsupportedEncodingException e) {
                            createDialog(INTERNALSERVERERROR);
                        }
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            try {
                JSONObject jsonObj = new JSONObject(json);
                DecimalFormat df = new DecimalFormat("#.##");
                minMonthBpm.setText(jsonObj.getString("minHeartBeat"));
                averageMonthBmp.setText(jsonObj.getString("avgHeartBeat"));
                maxMonthBmp.setText(jsonObj.getString("maxHeartBeat"));

                minMonthPressure.setText(jsonObj.getString("minMinPressure"));
                maxMonthPressure.setText(jsonObj.getString("maxMaxPressure"));
                if(!(Float.parseFloat(jsonObj.getString("minTemperature")) == 0.0) || !(Float.parseFloat(jsonObj.getString("maxTemperature"))== 0.0)) {
                    minMonthTemperature.setText(String.valueOf(df.format(Float.parseFloat(jsonObj.getString("minTemperature")))));
                    maxMonthTemperature.setText(String.valueOf(df.format(Float.parseFloat(jsonObj.getString("maxTemperature")))));
                } else{
                    minMonthTemperature.setText(jsonObj.getString("minTemperature"));
                    maxMonthTemperature.setText(jsonObj.getString("maxTemperature"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
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

    @Override
    public void run() {
        setAttributes(view);
        getParameters();
    }



    /**
     * @param errorString is the error that must be shown in the dialog
     *
     * Shows a dialog with the occurred error
     */
    private void createDialog(String errorString) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setMessage(errorString);
        alertDialogBuilder.setIcon(drawable.ic_exit);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    /**
     * @param statusCode is the code sent byt the server
     *
     *                   Checks the code sent by the server and show a different error depending on it.
     */
    private void getVolleyError(int statusCode) {
        switch (statusCode){
            case 400:
                createDialog(BADREQUEST);
                break;
            case 401:
                createDialog(UNAUTHORIZED);
                break;
            case 404:
                createDialog(NOTFOUND);
                break;
            case 500:
                createDialog(INTERNALSERVERERROR);
                break;
            default:
                break;
        }
    }
}
