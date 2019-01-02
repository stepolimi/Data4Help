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

import static com.data4help.d4h_thirdparty.Config.POSITIVESINGLEREQUEST;

public class SinglePositiveRequestDialogFragment extends DialogFragment{

    public SinglePositiveRequestDialogFragment(){}


    public boolean goesOnInSendingData = false;
    public boolean subscribed = false;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.single_positive_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(id.singleRefuseButton);
        TextView singlePositiveRequest = dialogFragment.findViewById(id.singlePositiveRequest);

        singlePositiveRequest.setText(POSITIVESINGLEREQUEST);

        singleAcceptButton.setOnClickListener(v -> {
            getDialog().dismiss();
            goesOnInSendingData = true;
            subscribed = true;
            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");

        });

        singleRefuseButton.setOnClickListener(v -> {
            getDialog().dismiss();
            goesOnInSendingData = true;
            subscribed = false;
        });
        return dialogFragment;
    }
}
