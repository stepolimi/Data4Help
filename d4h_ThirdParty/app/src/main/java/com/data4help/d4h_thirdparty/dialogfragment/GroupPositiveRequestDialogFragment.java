package com.data4help.d4h_thirdparty.dialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.data4help.d4h_thirdparty.R.*;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.POSITIVEGROUPREQUEST;

public class GroupPositiveRequestDialogFragment extends DialogFragment{
    public boolean subscribed = false;
    public boolean goesOnInSendingData = false;

    public GroupPositiveRequestDialogFragment(){}

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.group_positive_request_popup, container, false);

        Button groupAcceptButton = dialogFragment.findViewById(id.groupAcceptButton);
        Button groupRefuseButton = dialogFragment.findViewById(id.groupRefuseButton);
        TextView groupPositiveRequest = dialogFragment.findViewById(id.groupPositiveRequest);

        groupPositiveRequest.setText(POSITIVEGROUPREQUEST);

        groupAcceptButton.setOnClickListener(v -> {
            goesOnInSendingData = true;
            subscribed = true;
            getDialog().dismiss();
            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");
        });

        groupRefuseButton.setOnClickListener(v -> {
            goesOnInSendingData = true;
            getDialog().dismiss();
        });

        return dialogFragment;
    }

}
