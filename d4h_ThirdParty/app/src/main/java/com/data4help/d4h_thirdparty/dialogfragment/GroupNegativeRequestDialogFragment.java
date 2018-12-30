package com.data4help.d4h_thirdparty.dialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.data4help.d4h_thirdparty.R;

import java.util.Objects;

public class GroupNegativeRequestDialogFragment extends DialogFragment {

    public GroupNegativeRequestDialogFragment() {}

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialogFragment = inflater.inflate(R.layout.group_negative_request_popup, container, false);

        ImageView exitGroupIcon = dialogFragment.findViewById(R.id.exitGroupIcon);

        exitGroupIcon.setOnClickListener(v -> {
            getDialog().dismiss();
            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");
        });

        return dialogFragment;
    }
}
