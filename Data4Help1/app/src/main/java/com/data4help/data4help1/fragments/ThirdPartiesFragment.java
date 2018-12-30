package com.data4help.data4help1.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.R.*;
import com.data4help.data4help1.dialogfragments.ThirdPartiesRequestDialogFragment;

import java.util.Objects;

public class ThirdPartiesFragment  extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(com.data4help.data4help1.R.layout.content_third_party, container, false);

        Button notificationButton = view.findViewById(id.notificationButton);
        String url = "http://example.com";

        notificationButton.setOnClickListener((v)->{
            RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());

            //TODO: devo sistemare tutto bene
            JsonObjectRequest groupUserRequest = new JsonObjectRequest(Request.Method.GET, url,  null,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError ->{
                        ThirdPartiesRequestDialogFragment dialog = new ThirdPartiesRequestDialogFragment();
                        final FragmentManager fm = getFragmentManager();
                        dialog.show(Objects.requireNonNull(fm), "ThirdPartiesRequestDialogFragment");
                VolleyLog.e("Error: "+ volleyError.getMessage());});
            queue.add(groupUserRequest);
        });


        return view;
    }
}
