package com.data4help.data4help1.fragments;

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
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4help.data4help1.AuthToken;
import com.data4help.data4help1.activity.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Objects;

import static com.data4help.data4help1.Config.BADREQUEST;
import static com.data4help.data4help1.Config.DAILYHEALTHPARAMURL;
import static com.data4help.data4help1.Config.INTERNALSERVERERROR;
import static com.data4help.data4help1.Config.NOTFOUND;
import static com.data4help.data4help1.Config.UNAUTHORIZED;
import static com.data4help.data4help1.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment implements Runnable{

    private TextView minDayBpm;
    private TextView averageDayBmp;
    private TextView maxDayBmp;

    private TextView minDayPressure;
    private TextView maxDayPressure;

    private TextView minDayTemperature;
    private TextView maxDayTemperature;

    private JSONObject authUser;
    private View view;

    public DayFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_day, container, false);


        Runnable runnable = this;
        Thread thread = new Thread(runnable);
        thread.start();

        return view;
    }


    /**
     * Asks the daily health parameters of the user
     */
    private void getParameters() {
        setUserId();


        JsonObjectRequest dailyHealthParamReq = new JsonObjectRequest(Request.Method.POST, DAILYHEALTHPARAMURL, authUser ,
                jsonObject -> {},
                volleyError -> {
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
                            e.printStackTrace();
                        }
                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        MenuActivity.queue.add(dailyHealthParamReq);
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

                minDayBpm.setText(jsonObj.getString("minHeartBeat"));
                averageDayBmp.setText(jsonObj.getString("avgHeartBeat"));
                maxDayBmp.setText(jsonObj.getString("maxHeartBeat"));

                minDayPressure.setText(jsonObj.getString("minMinPressure"));
                maxDayPressure.setText(jsonObj.getString("maxMaxPressure"));

                DecimalFormat df = new DecimalFormat("#.##");
                if(!(Float.parseFloat(jsonObj.getString("minTemperature")) == 0.0) || !(Float.parseFloat(jsonObj.getString("maxTemperature"))== 0.0)) {
                    minDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(jsonObj.getString("minTemperature")))));
                    maxDayTemperature.setText(String.valueOf(df.format(Float.parseFloat(jsonObj.getString("maxTemperature")))));
                }
                else{
                    minDayTemperature.setText(jsonObj.getString("minTemperature"));
                    maxDayTemperature.setText(jsonObj.getString("maxTemperature"));
                }

                HomeFragment.setHeightWeight(jsonObj.getString("height"), jsonObj.getString("weight"));
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
