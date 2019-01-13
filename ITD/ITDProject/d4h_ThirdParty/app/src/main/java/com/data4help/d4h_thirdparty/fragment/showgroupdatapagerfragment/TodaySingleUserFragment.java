package com.data4help.d4h_thirdparty.fragment.showgroupdatapagerfragment;

import android.annotation.SuppressLint;
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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.activity.ShowSingleDataActivity;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.WaitingUserAnswerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.BADREQUEST;
import static com.data4help.d4h_thirdparty.Config.GETUSERDATAURL;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;

import com.data4help.d4h_thirdparty.R.*;

public class TodaySingleUserFragment extends Fragment implements Runnable{


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
    private boolean subscribed = true;

    public TodaySingleUserFragment(){}

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
        if(subscribed) {
            subscribed = false;
            setUserId();
            RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            JsonObjectRequest dailyHealthParamReq = new JsonObjectRequest(Request.Method.POST, GETUSERDATAURL, authUser,
                    jsonObject -> {
                    },
                    volleyError -> {
                        if (volleyError.networkResponse != null)
                            getVolleyError(volleyError.networkResponse.statusCode);
                    }) {
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
            queue.add(dailyHealthParamReq);
        }
    }

    /**
     * Sets the JSONObject containing the user di
     */
    private void setUserId() {
        authUser = new JSONObject();
        try {
            authUser.put("thirdPartyId", AuthToken.getId());
            authUser.put("id", (WaitingUserAnswerFragment.id));
            System.out.println(authUser);
        } catch (JSONException e) {
            createDialog(SERVERERROR);
        }
    }

    /**
     * @param json is the response
     *
     * Sets all health parameters obtained from the response
     */
    @SuppressLint("UseSparseArrays")
    private void setHealthParameters(String json) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            System.out.println(json);
            sevenDaysOfARequest = new HashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray sevenDaysHealthParam = jsonObject.getJSONArray("healthParametersSents");

                setContextParam(jsonObject.getString("name"), jsonObject.getString("surname"), jsonObject.getInt("yearOfBirth"),
                        jsonObject.getInt("height"), jsonObject.getInt("weight"), jsonObject.getString("sex"),
                        jsonObject.getString("fiscalCode"));
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
                    sevenDaysOfARequest.put(i, data);
                }
            } catch (JSONException e1) {
                createDialog(SERVERERROR);
            }
        });
    }

    /**
     *
     * calls a method in the main activity
     */
    private void setContextParam(String name, String surname,int yearOfBirth, int height, int weight, String sex, String fiscalCode){
        ((ShowSingleDataActivity)Objects.requireNonNull(getActivity())).setRequestParam(name, surname, yearOfBirth, height, weight, sex, fiscalCode);
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
