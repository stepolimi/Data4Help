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

public class YesterdayFragment extends Fragment implements Runnable{

    private TextView minYesterdayBpm;
    private TextView averageYesterdayBmp;
    private TextView maxYesterdayBmp;

    private TextView minYesterdayPressure;
    private TextView maxYesterdayPressure;

    private TextView minYesterdayTemperature;
    private TextView maxYesterdayTemperature;

    private JSONObject authUser;
    private View view;

    public YesterdayFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_yesterday, container, false);
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
            minYesterdayBpm.setText(jsonObj.getString("minHeartBeat"));
            averageYesterdayBmp.setText( jsonObj.getString("avgHeartBeat"));
            maxYesterdayBmp.setText(jsonObj.getString("maxHeartBeat"));

            minYesterdayPressure.setText(jsonObj.getString("minMinPressure"));
            maxYesterdayPressure.setText(jsonObj.getString("maxMaxPressure"));

            minYesterdayTemperature.setText(jsonObj.getString("minTemperature"));
            maxYesterdayTemperature.setText(jsonObj.getString("maxTemperature"));
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
        minYesterdayBpm = view.findViewById(com.data4help.d4h_thirdparty.R.id.minYesterdayBpm);
        averageYesterdayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.averageYesterdayBpm);
        maxYesterdayBmp = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxYesterdayBpm);

        minYesterdayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.minYesterdayPressure);
        maxYesterdayPressure = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxYesterdayPressure);

        minYesterdayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.minYesterdayTemperature);
        maxYesterdayTemperature = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxYesterdayTemperature);
    }

    @Override
    public void run() {
        setAttributes(view);
        getParameters();
    }
}
