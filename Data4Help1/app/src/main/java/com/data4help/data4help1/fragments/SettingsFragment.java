package com.data4help.data4help1.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.data4help1.Config.SERVERERROR;
import static com.data4help.data4help1.Config.SETTINGSURL;
import static com.data4help.data4help1.R.*;

public class SettingsFragment extends Fragment {

    private EditText weight;
    private EditText height;
    private EditText settingsError;

    private String errorString;
    private boolean incompleteRequest;
    private JsonObjectRequest settingsReq;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.content_settings, container, false);

        weight = view.findViewById(id.weight);
        height = view.findViewById(id.height);
        settingsError = view.findViewById(id.settingsError);
        Button saveButton = view.findViewById(id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject size = new JSONObject();
                try {
                    size.put("userId", AuthToken.getId());
                    size.put("weight", Integer.parseInt(weight.getText().toString()));
                    size.put("height", Integer.parseInt(height.getText().toString()));
                    System.out.println(size);
                    } catch (JSONException e) {
                        errorString = SERVERERROR;
                        incompleteRequest = true;
                }

                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                RequestQueue queue = Volley.newRequestQueue(context);
                settingsReq = new JsonObjectRequest(Request.Method.POST, SETTINGSURL, size,
                        response -> {},
                        volleyError -> {}){
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch (response.statusCode) {

                            case 200:
                                HomeFragment.height.setText("Height: " + height + "cm" );
                                HomeFragment.weight.setText("Height: " + weight + "kg" );
                                startActivity(new Intent(context, HomeFragment.class));
                                break;
                            //TODO: altri codici di error
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
                if(incompleteRequest)
                    cancelReq(errorString);
                else
                    queue.add(settingsReq);
            }
        });
        return view;
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        settingsError.setText(errorString);
        settingsReq.cancel();
    }

}






