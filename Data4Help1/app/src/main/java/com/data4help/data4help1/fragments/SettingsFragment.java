package com.data4help.data4help1.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class SettingsFragment extends Fragment {

    private EditText weight;
    private EditText height;

    String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.data4help.data4help1.R.layout.content_settings, container, false);

        weight = view.findViewById(com.data4help.data4help1.R.id.weight);
        height = view.findViewById(com.data4help.data4help1.R.id.height);
        Button saveButton = view.findViewById(com.data4help.data4help1.R.id.saveButton);
        url = "http://192.168.0.143:8080/d4h_server-0.0.1-SNAPSHOT/api/users/settings";


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject size = new JSONObject();
                try {
                    size.put("authId", "id");
                    size.put("weight", weight.getText().toString());
                    size.put("height", height.getText().toString());
                    System.out.println(size);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.PUT, url, size,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                System.out.print("hi");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                VolleyLog.e("Error: " + volleyError.getMessage());
                            }
                        });
                queue.add(jobReq);
            }

        });
        return view;
    }

}






