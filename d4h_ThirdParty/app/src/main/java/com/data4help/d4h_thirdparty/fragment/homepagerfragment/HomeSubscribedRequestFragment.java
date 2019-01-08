package com.data4help.d4h_thirdparty.fragment.homepagerfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.data4help.d4h_thirdparty.R;
import com.data4help.d4h_thirdparty.activity.ShowDataActivity;


public class HomeSubscribedRequestFragment extends Fragment {

    public HomeSubscribedRequestFragment() {
        // Required empty public constructor
    }


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_subscribed_request, container, false);

        Button example = view.findViewById(com.data4help.d4h_thirdparty.R.id.exampleButton);

        example.setOnClickListener((v -> {
            Intent intent = new Intent(getActivity(), ShowDataActivity.class);
            startActivity(intent);
        }));
        return view;
    }
}
