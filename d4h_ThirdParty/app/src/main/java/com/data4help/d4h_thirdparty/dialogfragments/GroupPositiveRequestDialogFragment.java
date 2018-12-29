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

public class GroupPositiveRequestDialogFragment extends DialogFragment{
    public boolean subscribed = false;

    public GroupPositiveRequestDialogFragment(){}

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.group_positive_request_popup, container, false);

        Button groupAcceptButton = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.groupAcceptButton);
        Button groupRefuseButton = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.groupRefuseButton);
        TextView groupPositiveRequest = dialogFragment.findViewById(com.data4help.d4h_thirdparty.R.id.groupPositiveRequest);

        groupPositiveRequest.setText("The app has found more than 1000 users which respect all request constraints.\n" +
        "The request has been accepted.\n" + "If you want you can subscribe for receiving more data.\n");

        groupAcceptButton.setOnClickListener(v -> {
            subscribed = true;
            getDialog().dismiss();
            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");
        });

        groupRefuseButton.setOnClickListener(v -> getDialog().dismiss());

        return dialogFragment;
    }

}
