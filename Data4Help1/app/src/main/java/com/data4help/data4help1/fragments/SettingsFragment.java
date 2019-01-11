package com.data4help.data4help1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.data4help1.Config.BADREQUEST;
import static com.data4help.data4help1.Config.INTERNALSERVERERROR;
import static com.data4help.data4help1.Config.NOTFOUND;
import static com.data4help.data4help1.Config.SERVERERROR;
import static com.data4help.data4help1.Config.SETTINGSURL;
import static com.data4help.data4help1.Config.UNAUTHORIZED;
import static com.data4help.data4help1.R.*;

public class SettingsFragment extends Fragment {

    private EditText weight;
    private EditText height;
    private TextView settingsError;

    private String errorString;
    private boolean incompleteRequest;
    private JsonObjectRequest settingsReq;

    private static String setHeight = null;
    private static String setWeight = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.content_settings, container, false);

        weight = view.findViewById(id.weight);
        height = view.findViewById(id.height);
        settingsError = view.findViewById(id.settingsError);
        Button saveButton = view.findViewById(id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject size = new JSONObject();
                try {
                    size.put("userId", AuthToken.getId());
                    size.put("weight", Integer.parseInt(weight.getText().toString()));
                    size.put("height", Integer.parseInt(height.getText().toString()));
                    System.out.println(size);
                    } catch (JSONException e) {
                        errorString = SERVERERROR;
                        incompleteRequest = true;
                }

                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                RequestQueue queue = Volley.newRequestQueue(context);
                settingsReq = new JsonObjectRequest(Request.Method.POST, SETTINGSURL, size,
                        response -> {},
                        volleyError -> getVolleyError(volleyError.networkResponse.statusCode)){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        if (response.statusCode == 200) {
                            setHeight = height.getText().toString();
                            setWeight = weight.getText().toString();
                            Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("HomeFragment");
                        }

                        return super.parseNetworkResponse(response);
                    }
                };
                if(incompleteRequest)
                    cancelReq(errorString);
                else
                    queue.add(settingsReq);
            }
        });
        return view;
    }

    /**
     * @param errorString is the error that must be shown in the dialog
     *
     * Shows a dialog with the occurred error
     */
    private void createDialog(String errorString) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setMessage(errorString);
        alertDialogBuilder.setIcon(drawable.ic_exit);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        settingsError.setText(errorString);
        incompleteRequest = false;
        settingsReq.cancel();
    }

    public static String getSetHeight() {
        return setHeight;
    }

    public static String getSetWeight() {
        return setWeight;
    }

    /**
     * @param statusCode is the status code sent from the server
     *
     *                   Creates the dialog with a particular error
     */
    public void getVolleyError(int statusCode){
        switch (statusCode){
            case 400:
                createDialog(BADREQUEST);
                break;
            case 401:
                createDialog(UNAUTHORIZED);
                break;
            case 404:
                createDialog(NOTFOUND);
                break;
            case 500:
                createDialog(INTERNALSERVERERROR);
                break;
            default:
                break;
        }
    }

}






