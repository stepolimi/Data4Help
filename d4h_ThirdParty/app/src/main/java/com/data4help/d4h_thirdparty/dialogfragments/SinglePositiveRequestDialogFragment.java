package com.data4help.d4h_thirdparty.dialogfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class SinglePositiveRequestDialogFragment extends DialogFragment{

    public SinglePositiveRequestDialogFragment(){}

    public boolean subscribed = false;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.single_positive_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.singleRefuseButton);
        TextView singlePositiveRequest = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.singlePositiveRequest);

        singlePositiveRequest.setText("The single user has accepted your request! If you want you can subscribe for receiving more data.");

        singleAcceptButton.setOnClickListener(v -> {
            getDialog().dismiss();
            subscribed = true;
            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");

        });

        singleRefuseButton.setOnClickListener(v -> {
            getDialog().dismiss();
            subscribed = false;
        });
        return dialogFragment;
    }
}
