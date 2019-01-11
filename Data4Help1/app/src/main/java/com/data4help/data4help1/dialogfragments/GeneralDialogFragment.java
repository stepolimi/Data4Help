package com.data4help.data4help1.dialogfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.data4help.data4help1.R;

public class GeneralDialogFragment extends DialogFragment {

    public GeneralDialogFragment(){}

    public ImageView exit;
    public static TextView text;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState){

        final View dialogFragment = inflater.inflate(R.layout.general_dialog_popup, container, false);

        exit = dialogFragment.findViewById(R.id.exitIcon);
        text = dialogFragment.findViewById(R.id.alertText);

        exit.setOnClickListener((v)-> getDialog().dismiss());

        return dialogFragment;
    }

    public static void setText(String newText){
        text.setText(newText);
    }
}
