package com.data4help.d4h_thirdparty.dialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.data4help.d4h_thirdparty.R.*;

import java.util.Objects;

public class SendSingleRequestDialogFragment extends DialogFragment {

    public SendSingleRequestDialogFragment() {
    }

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialogFragment = inflater.inflate(layout.send_single_request_popup, container, false);

        ImageView exitSingleIcon = dialogFragment.findViewById(id.exitSendIcon);

        exitSingleIcon.setOnClickListener(v -> {
            getDialog().dismiss();Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("DataFragment");
        });

        return dialogFragment;
    }

}
