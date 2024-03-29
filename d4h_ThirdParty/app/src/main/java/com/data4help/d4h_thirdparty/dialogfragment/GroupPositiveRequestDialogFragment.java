package com.data4help.d4h_thirdparty.dialogfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.R.*;
import com.data4help.d4h_thirdparty.activity.ShowGroupDataActivity;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.GroupRequestFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.BADREQUEST;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.POSITIVEGROUPREQUEST;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEGROUPURL;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;

public class GroupPositiveRequestDialogFragment extends DialogFragment{
    private boolean subscribed = false;

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
            subscribed = true;
            subscribeRequest();
            getDialog().dismiss();
        });

        groupRefuseButton.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(),ShowGroupDataActivity.class);

            startActivity(intent);
            getDialog().dismiss();
        });

        return dialogFragment;
    }

    /**
     * Sends the answer to given by the third party to the server
     */
    private void subscribeRequest() {
        JSONObject subscribeRequest = new JSONObject();
        try {
            subscribeRequest.put("thirdPartyId", AuthToken.getId());
            subscribeRequest.put("subscribed", subscribed);
            subscribeRequest.put("id", GroupRequestFragment.groupRequestId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        JsonObjectRequest subscribeGroupReq = new JsonObjectRequest(Request.Method.POST, SUBSCRIBEGROUPURL, subscribeRequest,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError ->
                { if(volleyError.networkResponse != null)
                    getVolleyError(volleyError.networkResponse.statusCode);}){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch(response.statusCode){
                    case 200:
                        Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("HomeSubscribedRequestFragment");
                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(subscribeGroupReq);
    }

    /**
     * @param statusCode is the code sent byt the server
     *
     *                   Checks the code sent by the server and show a different error depending on it.
     */
    private void getVolleyError(int statusCode) {
        {switch (statusCode){
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
        }}
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

}
