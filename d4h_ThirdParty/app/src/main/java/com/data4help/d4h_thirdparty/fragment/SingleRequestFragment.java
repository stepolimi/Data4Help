package com.data4help.d4h_thirdparty.fragment;


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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.dialogfragment.SingleNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragment.SinglePositiveRequestDialogFragment;
import com.data4help.d4h_thirdparty.R.*;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.INCORRECTFISCALCODE;
import static com.data4help.d4h_thirdparty.Config.INSERTNUMBER;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.SINGLEREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEURL;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleRequestFragment extends Fragment {

    private EditText userCountry;
    private EditText userFiscalCode;
    private EditText serviceDescription;

    private Button saveSingleRequestButton;
    private TextView errorSingleRequest;

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
                singleUserRequest = new JsonObjectRequest(Request.Method.POST, SINGLEREQUESTURL, singleRequest,
                        response -> VolleyLog.v("Response:%n %s", response.toString()),
                        volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch(response.statusCode){
                            case 200:
                                positiveDialog = new SinglePositiveRequestDialogFragment();
                                positiveDialog.show(Objects.requireNonNull(getFragmentManager()), "SinglePositiveRequestDialogFragment");
                                waitTheAnswer();
                                subscribeRequest();
                                break;
                            default:
                                negativeDialog = new SingleNegativeRequestDialogFragment();
                                negativeDialog.show(Objects.requireNonNull(getFragmentManager()), "SingleNegativeRequestDialogFragment");
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
     * A do-while loop which waits the third party answer
     */
    private void waitTheAnswer() {
        if (!positiveDialog.goesOnInSendingData) {
            do {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!positiveDialog.goesOnInSendingData);
        }
    }

    /**
     * Sends the chosen button to the user
     */
    private void subscribeRequest() {
        JSONObject subscribeRequest = new JSONObject();
        try {
            subscribeRequest.put("thirdPartyId", AuthToken.getId());
            subscribeRequest.put("subscribed", positiveDialog.subscribed);
        } catch (JSONException e) {
            error = SERVERERROR;
            incompleteRequest = true;
        }
        JsonObjectRequest subscribeSingleReq = new JsonObjectRequest(Request.Method.POST, SUBSCRIBEURL, subscribeRequest,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> VolleyLog.e("Error: " + volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch(response.statusCode){
                    case 200:
                        Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("HomeFragment");
                        break;
                    //TODO: codici d'errore
                    case 403:
                        break;
                    case 401:
                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest)
            cancelReq(error, subscribeSingleReq);
        else
            queue.add(subscribeSingleReq);
    }


    /**
     * @param singleRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Sets all attributes in the JSONObject that will be sent to the server
     */
    private void setSingleRequest(JSONObject singleRequest) throws JSONException {
        String country = userCountry.getText().toString().toLowerCase();
        singleRequest.put("state", country);

        if(country.equals("italy"))
            checkFiscalCode(singleRequest);
        else {
            if (userFiscalCode.getText().toString().isEmpty()) {
                error = INSERTNUMBER;
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
        userCountry = view.findViewById(id.userCountry);
        userFiscalCode = view.findViewById(id.userFiscalCode);
        serviceDescription = view.findViewById(id.serviceDescription);

        saveSingleRequestButton = view.findViewById(id.saveSingleRequestButton);
        errorSingleRequest = view.findViewById(id.errorSingleRequest);
    }

    /**
     * @param singleRequest is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the given fiscal code is correct for length and elements: if it is correct it will be put in the JSON;
     * if not an error string will be set.
     *
     */
    private void checkFiscalCode(JSONObject singleRequest) throws JSONException {
        String fc = userFiscalCode.getText().toString();
        if( fc.length() == 16 ) {
            String fc2 = fc.toUpperCase();
            for (int i = 0; i < fc2.length(); i++) {
                int c = fc2.charAt(i);
                if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z')) {
                    error = INCORRECTFISCALCODE;
                    incompleteRequest = true;
                    return;
                }
            }
            singleRequest.put("fiscalCode", fc);
        }
        else{
            error = INCORRECTFISCALCODE;
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
