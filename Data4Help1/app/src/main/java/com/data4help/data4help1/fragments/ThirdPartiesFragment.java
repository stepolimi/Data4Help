package com.data4help.data4help1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import static com.data4help.data4help1.Config.THIRDPARTYNOTIFICATIONURL;

public class ThirdPartiesFragment  extends Fragment {

    public static String requestId;
    public static String thirdPartyName;
    public static String description;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(com.data4help.data4help1.R.layout.content_third_party, container, false);

        Button notificationButton = view.findViewById(id.notificationButton);

        //TODO: si potrebbe fare un get all third party associated to the user
        setOnButtonCLick(notificationButton);


        return view;
    }

    private void setOnButtonCLick(Button notificationButton) {

        notificationButton.setOnClickListener((v)->{
            //TODO: devo sistemare tutto bene
            JSONObject authId = new JSONObject();
            try {
                authId.put("id", AuthToken.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest groupUserRequest = new JsonObjectRequest(Request.Method.POST, THIRDPARTYNOTIFICATIONURL,  authId,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    switch (response.statusCode) {
                        case 200:
                            try {
                                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                createDialogFragment(json);
                            } catch (UnsupportedEncodingException e) {
                                //TODO
                            }
                            break;
                        //TODO
                        case 403:
                            System.out.println("The access has been denied. Try again.");
                            break;
                        case 401:
                            System.out.println("The given email is already in the DB. Change it or login.");
                            break;
                    }
                    return super.parseNetworkResponse(response);
                }
                };
            queue.add(groupUserRequest);
        });
    }

    private void createDialogFragment(String json) {
        System.out.println(json);
        try {
            JSONArray thirdPartyRequest = new JSONArray(json);
            if(thirdPartyRequest.length() == 0){
                //TODO dialog diverso
            }
            else {
                for (int i = 0; i < thirdPartyRequest.length(); i++) {
                    JSONObject jsonObject = thirdPartyRequest.getJSONObject(i);
                    requestId = jsonObject.getString("requestId");
                    thirdPartyName = jsonObject.getString("senderName");
                    description = jsonObject.getString("description");

                    ThirdPartiesRequestDialogFragment dialog = new ThirdPartiesRequestDialogFragment();
                    final FragmentManager fm = getFragmentManager();
                    dialog.show(Objects.requireNonNull(fm), "ThirdPartiesRequestDialogFragment");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param thirdParty is the JSONObject
     *
     * Sets textView for ech third party related to the user
     */
    private void setTextViewThirdParty(JSONObject thirdParty) {
        //TODO
    }
}
