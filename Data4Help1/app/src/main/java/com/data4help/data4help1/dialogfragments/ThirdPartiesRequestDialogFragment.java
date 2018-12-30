package com.data4help.data4help1.dialogfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.data4help.data4help1.R.*;

import java.util.Objects;

public class ThirdPartiesRequestDialogFragment extends DialogFragment{

    public ThirdPartiesRequestDialogFragment(){}

    public boolean subscribed = false;
    public TextView singlePositiveRequest;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.third_party_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(id.singleRefuseButton);
        singlePositiveRequest = dialogFragment.findViewById(id.singlePositiveRequest);

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
