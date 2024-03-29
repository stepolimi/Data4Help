package com.data4help.data4help1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;
import com.data4help.data4help1.R.*;
import com.data4help.data4help1.dialogfragments.ThirdPartiesRequestDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.data4help1.Config.BADREQUEST;
import static com.data4help.data4help1.Config.INTERNALSERVERERROR;
import static com.data4help.data4help1.Config.NOREQUESTS;
import static com.data4help.data4help1.Config.NOTFOUND;
import static com.data4help.data4help1.Config.SERVERERROR;
import static com.data4help.data4help1.Config.THIRDPARTYNOTIFICATIONURL;
import static com.data4help.data4help1.Config.UNAUTHORIZED;

public class ThirdPartiesFragment  extends Fragment {

    public static String requestId;
    public static String thirdPartyName;
    public static String description;
    
    private boolean incompleteRequest = false;
    private JsonObjectRequest groupUserRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(com.data4help.data4help1.R.layout.content_third_party, container, false);

        Button notificationButton = view.findViewById(id.notificationButton);

        setOnButtonCLick(notificationButton);

        return view;
    }

    private void setOnButtonCLick(Button notificationButton) {

        notificationButton.setOnClickListener((v)->{
            JSONObject authId = new JSONObject();
            try {
                authId.put("id", AuthToken.getId());
            } catch (JSONException e) {
                incompleteRequest = false;
            }
            Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            groupUserRequest = new JsonObjectRequest(Request.Method.POST, THIRDPARTYNOTIFICATIONURL,  authId,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError ->{
                        if(volleyError.networkResponse != null)
                            getVolleyError(volleyError.networkResponse.statusCode);}){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode == 200) {
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            createDialogFragment(json);
                        } catch (UnsupportedEncodingException e) {
                            createDialogFragment(SERVERERROR);
                        }
                    }
                    return super.parseNetworkResponse(response);
                }
                };
            if(incompleteRequest)
                cancelReq();
            else
                queue.add(groupUserRequest);
        });
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq() {
        createDialog(SERVERERROR);
        incompleteRequest = false;
        groupUserRequest.cancel();
    }


    /**
     * @param json is the string coming from the server
     *             
     *             Creates an error dialog if the string is empty; if not create a new dialog for every single request coming
     *             from the server
     */
    private void createDialogFragment(String json) {

        try {
            JSONArray thirdPartyRequest = new JSONArray(json);
            if(thirdPartyRequest.length() == 0)
                createDialog(NOREQUESTS);
            else {
                for (int i = 0; i < thirdPartyRequest.length(); i++) {
                    JSONObject jsonObject = thirdPartyRequest.getJSONObject(i);

                    requestId = jsonObject.getString("requestId");
                    thirdPartyName = jsonObject.getString("senderName");
                    description = jsonObject.getString("description");

                    System.out.println(requestId);
                    System.out.println(thirdPartyName);
                    System.out.println(description);
                    FragmentManager fm = getFragmentManager();
                    ThirdPartiesRequestDialogFragment dialog = new ThirdPartiesRequestDialogFragment();
                    dialog.show(Objects.requireNonNull(fm), "ThirdPartiesRequestDialogFragment");
                }
            }
        } catch (JSONException e) {
            createDialog(SERVERERROR);
        }

    }

    /**
     *
     * Shows a dialog with the occurred error
     */
    private void createDialog(String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setMessage(error);
        alertDialogBuilder.setIcon(drawable.ic_exit);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    /**
     * @param statusCode is the code sent byt the server
     *
     *                   Checks the code sent by the server and show a different error depending on it.
     */
    private void getVolleyError(int statusCode) {
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
