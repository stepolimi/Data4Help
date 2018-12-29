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
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.dialogfragments.GroupNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragments.GroupPositiveRequestDialogFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


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

    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";
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
                error = "Server problem. Try again later.";
                incompleteRequest = true;
            }

            queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            groupUserRequest = new JsonObjectRequest(Request.Method.POST, url, groupRequest,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError -> {
                        positiveDialog = new GroupPositiveRequestDialogFragment();
                        negativeDialog = new GroupNegativeRequestDialogFragment();
                        final FragmentManager fm = getFragmentManager();
                        positiveDialog.show(Objects.requireNonNull(fm), "GroupNegativeRequestDialogFragment");
                        try {
                            subscribeRequest();
                        } catch (JSONException e) {
                            error = "Server problem. Try again later.";
                            incompleteRequest = true;
                        }
                        VolleyLog.e("Error: "+ volleyError.getMessage()); });
            if(incompleteRequest) {
                cancelReq(error, groupUserRequest);
            }
            queue.add(groupUserRequest);
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
     * @param groupRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Fills the JSONObject
     */
    private void setGroupRequest(JSONObject groupRequest) throws JSONException {
        groupRequest.put("authId", "id");
        groupRequest.put("minAge", minAge.getText().toString());
        groupRequest.put("maxAge", maxAge.getText().toString());
        groupRequest.put("minWeight", minWeight.getText().toString());
        groupRequest.put("maxWeight", maxWeight.getText().toString());
        groupRequest.put("minHeight", minHeight.getText().toString());
        groupRequest.put("maxHeight", maxHeight.getText().toString());

        groupRequest.put("male", male.isChecked());
        groupRequest.put("female", female.isChecked());

        groupRequest.put("street", street.getText().toString());
        groupRequest.put("number", number.getText().toString());
        groupRequest.put("city", city.getText().toString());
        groupRequest.put("cap", cap.getText().toString());
        groupRequest.put("region", region.getText().toString());
        groupRequest.put("country", country.getText().toString());
    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view) {
        minAge = view.findViewById(com.data4help.d4h_thirdparty.R.id.minAge);
        maxAge = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxAge);
        minWeight = view.findViewById(com.data4help.d4h_thirdparty.R.id.minWeight);
        maxWeight = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxWeight);
        minHeight = view.findViewById(com.data4help.d4h_thirdparty.R.id.minHeight);
        maxHeight = view.findViewById(com.data4help.d4h_thirdparty.R.id.maxHeight);

        street = view.findViewById(com.data4help.d4h_thirdparty.R.id.street);
        number = view.findViewById(com.data4help.d4h_thirdparty.R.id.number);
        city = view.findViewById(com.data4help.d4h_thirdparty.R.id.city);
        cap = view.findViewById(com.data4help.d4h_thirdparty.R.id.postalCode);
        region = view.findViewById(com.data4help.d4h_thirdparty.R.id.region);
        country = view.findViewById(com.data4help.d4h_thirdparty.R.id.country);

        male = view.findViewById(com.data4help.d4h_thirdparty.R.id.maleButton);
        female = view.findViewById(com.data4help.d4h_thirdparty.R.id.femaleButton);

        saveGroupRequestButton = view.findViewById(com.data4help.d4h_thirdparty.R.id.saveGroupRequestButton);
        errorGroupRequest = view.findViewById(com.data4help.d4h_thirdparty.R.id.errorGroupRequest);
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText, JsonObjectRequest request) {
        errorGroupRequest.setText(errorText);
        request.cancel();
    }

}
