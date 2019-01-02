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
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.dialogfragment.GroupNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragment.GroupPositiveRequestDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.GROUPREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEURL;
import static com.data4help.d4h_thirdparty.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupRequestFragment extends Fragment {

    private EditText minAge;
    private EditText maxAge;
    private EditText minWeight;
    private EditText maxWeight;
    private EditText minHeight;
    private EditText maxHeight;

    private EditText street;
    private EditText number;
    private EditText city;
    private EditText cap;
    private EditText region;
    private EditText country;

    private RadioButton male;
    private RadioButton female;

    private Button saveGroupRequestButton;

    private TextView errorGroupRequest;

    private JsonObjectRequest groupUserRequest;
    private RequestQueue queue;

    private GroupPositiveRequestDialogFragment positiveDialog;
    private GroupNegativeRequestDialogFragment negativeDialog;

    private String error;
    private boolean incompleteRequest = false;

    public GroupRequestFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_group_request, container, false);
        setAttributes(view);

        saveGroupRequestButton.setOnClickListener(v -> {
            JSONObject groupRequest = new JSONObject();

            try {
                setGroupRequest(groupRequest);
            } catch (JSONException e) {
                error = SERVERERROR;
                incompleteRequest = true;
            }

            queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            groupUserRequest = new JsonObjectRequest(Request.Method.POST, GROUPREQUESTURL, groupRequest,
                    response -> {},
                    volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    switch(response.statusCode){
                        case 200:
                            positiveDialog = new GroupPositiveRequestDialogFragment();
                            positiveDialog.show(Objects.requireNonNull(getFragmentManager()), "GroupPositiveRequestDialogFragment");
                            waitTheAnswer();
                            subscribeRequest();
                            break;
                        //TODO: codici d'errore
                        case 403:
                            negativeDialog = new GroupNegativeRequestDialogFragment();
                            negativeDialog.show(Objects.requireNonNull(getFragmentManager()), "GroupNegativeRequestDialogFragment");
                            break;
                        case 401:
                            break;
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            if(incompleteRequest) {
                cancelReq(error, groupUserRequest);
            }
            queue.add(groupUserRequest);
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
     * Sends the answer to given by the third party to the server
     */
    private void subscribeRequest() {
        JSONObject subscribeRequest = new JSONObject();
        try {
            subscribeRequest.put("thirdPartyId", AuthToken.getId());
            subscribeRequest.put("subscribed", positiveDialog.subscribed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest subscribeGroupReq = new JsonObjectRequest(Request.Method.POST, SUBSCRIBEURL, subscribeRequest,
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
            cancelReq(error, subscribeGroupReq);
        else
            queue.add(subscribeGroupReq);
    }

    /**
     * @param groupRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Fills the JSONObject
     */
    private void setGroupRequest(JSONObject groupRequest) throws JSONException {
        groupRequest.put("thirdPartyId", AuthToken.getId());
        groupRequest.put("minAge", Integer.parseInt(minAge.getText().toString()));
        groupRequest.put("maxAge", Integer.parseInt(maxAge.getText().toString()));
        groupRequest.put("minWeight", Integer.parseInt(minWeight.getText().toString()));
        groupRequest.put("maxWeight", Integer.parseInt(maxWeight.getText().toString()));
        groupRequest.put("minHeight", Integer.parseInt(minHeight.getText().toString()));
        groupRequest.put("maxHeight", Integer.parseInt(maxHeight.getText().toString()));

        groupRequest.put("male", male.isChecked());
        groupRequest.put("female", female.isChecked());

        groupRequest.put("street", street.getText().toString());
        groupRequest.put("number", Integer.parseInt(number.getText().toString()));
        groupRequest.put("city", city.getText().toString());
        groupRequest.put("cap", Integer.parseInt(cap.getText().toString()));
        groupRequest.put("region", region.getText().toString());
        groupRequest.put("country", country.getText().toString());
    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view) {
        minAge = view.findViewById(id.minAge);
        maxAge = view.findViewById(id.maxAge);
        minWeight = view.findViewById(id.minWeight);
        maxWeight = view.findViewById(id.maxWeight);
        minHeight = view.findViewById(id.minHeight);
        maxHeight = view.findViewById(id.maxHeight);

        street = view.findViewById(id.street);
        number = view.findViewById(id.number);
        city = view.findViewById(id.city);
        cap = view.findViewById(id.postalCode);
        region = view.findViewById(id.region);
        country = view.findViewById(id.country);

        male = view.findViewById(id.maleButton);
        female = view.findViewById(id.femaleButton);

        saveGroupRequestButton = view.findViewById(id.saveGroupRequestButton);
        errorGroupRequest = view.findViewById(id.errorGroupRequest);
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText, JsonObjectRequest request) {
        errorGroupRequest.setText(errorText);
        request.cancel();
    }

}
