package com.data4help.d4h_thirdparty;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleRequestFragment extends Fragment {

    private EditText userCountry;
    private EditText userFiscalCode;
    private EditText serviceDescription;

    private Button saveSingleRequestButton;
    private TextView errorSingleRequest;

    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";

    private JsonObjectRequest jobReq;

    public SingleRequestFragment() {
        // Required empty public constructor
    }


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_request, container, false);
        setAttributes(view);

        saveSingleRequestButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick (View v){
                JSONObject singleRequest = new JSONObject();

                try {
                    setSingleRequest(singleRequest);
                } catch (JSONException e) {
                    errorSingleRequest.setText("Server problem. Try again later.");
                }

                RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
                jobReq = new JsonObjectRequest(Request.Method.POST, url, singleRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                VolleyLog.v("Response:%n %s", response.toString()); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                VolleyLog.e("Error: "+ volleyError.getMessage()); }}){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch(response.statusCode){
                            case 200:

                                break;
                            default:
                                break;
                        }
                        return super.parseNetworkResponse(response);
                    }
                };
                queue.add(jobReq);
            }

        });

        return view;
    }

    private void setSingleRequest(JSONObject singleRequest) throws JSONException {
        String country = userCountry.getText().toString().toLowerCase();
        singleRequest.put("country", country);

        if(country.equals("italy"))
            checkFiscalCode(singleRequest);
        else
            singleRequest.put("fiscalCode", userFiscalCode.getText().toString());

        singleRequest.put("description", serviceDescription.getText().toString());

    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view){
        userCountry = view.findViewById(R.id.userCountry);
        userFiscalCode = view.findViewById(R.id.userFiscalCode);
        serviceDescription = view.findViewById(R.id.serviceDescription);

        saveSingleRequestButton = view.findViewById(R.id.saveSingleRequestButton);
        errorSingleRequest = view.findViewById(R.id.errorSingleRequest);
    }

    /**
     * @param singleRequest is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the given fiscal code is correct for length and elements: if it is correct it will be put in the JSON;
     * if not an error string will be set.
     *
     */
    @SuppressLint("SetTextI18n")
    private void checkFiscalCode(JSONObject singleRequest) throws JSONException {
        String fc = userFiscalCode.getText().toString();
        if( fc.length() == 16 ) {
            String fc2 = fc.toUpperCase();
            for (int i = 0; i < fc2.length(); i++) {
                int c = fc2.charAt(i);
                if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z')) {
                    cancelReq("The fiscal code is incorrect!");
                    return;
                }
            }
            singleRequest.put("fiscalCode", fc);
        }
        else
            cancelReq("The fiscal code is incorrect!");
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText) {
        errorSingleRequest.setText(errorText);
        jobReq.cancel();
    }

}
