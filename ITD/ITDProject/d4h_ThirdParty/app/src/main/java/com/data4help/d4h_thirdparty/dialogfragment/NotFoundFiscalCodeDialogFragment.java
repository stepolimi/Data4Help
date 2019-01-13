package com.data4help.d4h_thirdparty.dialogfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.data4help.d4h_thirdparty.R.*;

import java.util.Objects;

public class NotFoundFiscalCodeDialogFragment extends DialogFragment {

    public NotFoundFiscalCodeDialogFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialogFragment = inflater.inflate(layout.not_found_fiscal_code_popup, container, false);

        ImageView exitIcon = dialogFragment.findViewById(id.exitWrongFiscalCodeIcon);

        exitIcon.setOnClickListener(v -> {
            getDialog().dismiss();Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");
        });

        return dialogFragment;
    }

}
