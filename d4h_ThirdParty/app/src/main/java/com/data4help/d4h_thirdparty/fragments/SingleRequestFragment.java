package com.data4help.d4h_thirdparty.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.dialogfragments.SingleNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragments.SinglePositiveRequestDialogFragment;

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

    private JsonObjectRequest singleUserRequest;
    private  RequestQueue queue;

    //Dialog
    private SinglePositiveRequestDialogFragment positiveDialog;
    private SingleNegativeRequestDialogFragment negativeDialog;

    private String error;
    private boolean incompleteRequest = false;

    public SingleRequestFragment() {
        // Required empty public constructor
    }


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_single_request, container, false);
        setAttributes(view);

        saveSingleRequestButton.setOnClickListener((v) -> {
                JSONObject singleRequest = new JSONObject();

                try {
                    setSingleRequest(singleRequest);
                } catch (JSONException e) {
                    error = "Server problem. Try again later.";
                    incompleteRequest = true;
                }

                queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
                singleUserRequest = new JsonObjectRequest(Request.Method.POST, url, singleRequest,
                        response -> VolleyLog.v("Response:%n %s", response.toString()),
                        volleyError -> {
                            positiveDialog = new SinglePositiveRequestDialogFragment();
                            negativeDialog = new SingleNegativeRequestDialogFragment();
                            final FragmentManager fm = getFragmentManager();
                            negativeDialog.show(Objects.requireNonNull(fm), "SingleNegativeRequestDialogFragment");
                            try {
                                subscribeRequest();
                            } catch (JSONException e) {
                                error = "Server problem. Try again later.";
                                incompleteRequest = true;
                            }
                            VolleyLog.e("Error: "+ volleyError.getMessage()); }){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch(response.statusCode){
                            case 200:
                                positiveDialog = new SinglePositiveRequestDialogFragment();
                                //TODO
                                break;
                            default:
                                negativeDialog = new SingleNegativeRequestDialogFragment();
                                //TODO
                                break;
                        }
                        return super.parseNetworkResponse(response);
                    }
                };
            if(incompleteRequest)
                cancelReq(error, singleUserRequest);
            else
                queue.add(singleUserRequest);
        });

        return view;
    }

    /**
     * @throws JSONException is something goes wrong
     *
     * Creates a PUT
     */
    private void subscribeRequest() throws JSONException {
        JSONObject subscribeRequest = new JSONObject();
        subscribeRequest.put("id", "id");
        subscribeRequest.put("subscribed", positiveDialog.subscribed);
        //TODO
        JsonObjectRequest subscribeReq = new JsonObjectRequest(Request.Method.PUT, url, subscribeRequest,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> VolleyLog.e("Error: " + volleyError.getMessage()));
        if(incompleteRequest)
            cancelReq(error, subscribeReq);
        else
            queue.add(subscribeReq);
    }


    /**
     * @param singleRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Sets all attributes in the JSONObject that will be sent to the server
     */
    private void setSingleRequest(JSONObject singleRequest) throws JSONException {
        String country = userCountry.getText().toString().toLowerCase();
        singleRequest.put("country", country);

        if(country.equals("italy"))
            checkFiscalCode(singleRequest);
        else {
            if (userFiscalCode.getText().toString().isEmpty()) {
                error = "A number must be insert in the fiscal code area.";
                incompleteRequest = true;
            }
            else
                singleRequest.put("fiscalCode", userFiscalCode.getText().toString());
        }
        singleRequest.put("description", serviceDescription.getText().toString());

    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view){
        userCountry = view.findViewById(com.data4help.d4h_thirdparty.R.id.userCountry);
        userFiscalCode = view.findViewById(com.data4help.d4h_thirdparty.R.id.userFiscalCode);
        serviceDescription = view.findViewById(com.data4help.d4h_thirdparty.R.id.serviceDescription);

        saveSingleRequestButton = view.findViewById(com.data4help.d4h_thirdparty.R.id.saveSingleRequestButton);
        errorSingleRequest = view.findViewById(com.data4help.d4h_thirdparty.R.id.errorSingleRequest);
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
                    error = "The fiscal code is incorrect!";
                    incompleteRequest = true;
                    return;
                }
            }
            singleRequest.put("fiscalCode", fc);
        }
        else{
            error = "The fiscal code is incorrect!";
            incompleteRequest = true;
        }
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText, JsonObjectRequest request) {
        errorSingleRequest.setText(errorText);
        request.cancel();
    }
}
