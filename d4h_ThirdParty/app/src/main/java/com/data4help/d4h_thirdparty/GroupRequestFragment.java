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
import android.widget.RadioButton;
import android.widget.TextView;

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
    private TextView error;

    private JsonObjectRequest jobReq;

    public GroupRequestFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_request, container, false);
        setAttributes(view);

        saveGroupRequestButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick (View v){
                JSONObject groupRequest = new JSONObject();

                try {
                    setGroupRequest(groupRequest);
                } catch (JSONException e) {
                    error.setText("Server problem. Try again later.");
                }

                RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
                jobReq = new JsonObjectRequest(Request.Method.POST, url, groupRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                VolleyLog.v("Response:%n %s", response.toString()); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                VolleyLog.e("Error: "+ volleyError.getMessage()); }});

                queue.add(jobReq);
            }

        });

        return view;
    }

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
        minAge = view.findViewById(R.id.minAge);
        maxAge = view.findViewById(R.id.maxAge);
        minWeight = view.findViewById(R.id.minWeight);
        maxWeight = view.findViewById(R.id.maxWeight);
        minHeight = view.findViewById(R.id.minHeight);
        maxHeight = view.findViewById(R.id.maxHeight);

        street = view.findViewById(R.id.street);
        number = view.findViewById(R.id.number);
        city = view.findViewById(R.id.city);
        cap = view.findViewById(R.id.postalCode);
        region = view.findViewById(R.id.region);
        country = view.findViewById(R.id.country);

        male = view.findViewById(R.id.maleButton);
        female = view.findViewById(R.id.femaleButton);

        saveGroupRequestButton = view.findViewById(R.id.saveGroupRequestButton);
        error = view.findViewById(R.id.errorGroupRequest);
    }

}
