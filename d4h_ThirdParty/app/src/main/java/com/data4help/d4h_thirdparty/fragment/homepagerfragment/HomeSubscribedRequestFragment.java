package com.data4help.d4h_thirdparty.fragment.homepagerfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.BADREQUEST;
import static com.data4help.d4h_thirdparty.Config.GETSUBSCRIBEDGROUPDATAURL;
import static com.data4help.d4h_thirdparty.Config.GETSUBSCRIBEDUSERURL;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;

import com.data4help.d4h_thirdparty.R.*;
import com.data4help.d4h_thirdparty.activity.ShowGroupSubDataActivity;

public class HomeSubscribedRequestFragment extends Fragment{


    private static Map<Integer, JSONObject> groupParams;
    private static Map<Integer, JSONObject> userParams;
    private static int buttonGroupId;
    private static int buttonUserId;
    private boolean incompleteRequest = false;

    public static int getButtonGroupId() {
        return buttonGroupId;
    }

    public static Map<Integer, JSONObject> getGroupParams() {
        return groupParams;
    }

    public static Map<Integer, JSONObject> getUserParams() {
        return userParams;
    }

    private LinearLayout subscribedGroupButtons;
    private LinearLayout subscribedUserButtons;

    public HomeSubscribedRequestFragment() {
        // Required empty public constructor
    }

    public static int getButtonUserId() {
        return buttonUserId;
    }


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.fragment_home_subscribed_request, container, false);

        subscribedGroupButtons = view.findViewById(id.subscribedGroupButtons);
        subscribedUserButtons = view.findViewById(id.subscribedUserButtons);

        doGroupRequest();
        doSingleUserRequest();

        return view;
    }

    private void activateUserButton() {
        for(int i = 0; i < subscribedUserButtons.getChildCount(); i++){
            buttonUserId = subscribedUserButtons.getChildAt(i).getId();
            subscribedUserButtons.getChildAt(i).setOnClickListener((v) -> {
                Intent intent = new Intent(getActivity(), ShowGroupSubDataActivity.class);
                startActivity(intent);
            });
        }
        activateGroupButton();
    }

    private void activateGroupButton() {
        for(int i = 0; i < subscribedGroupButtons.getChildCount(); i++){
            buttonGroupId = subscribedGroupButtons.getChildAt(i).getId();
            subscribedGroupButtons.getChildAt(i).setOnClickListener((v) -> {
                Intent intent = new Intent(getActivity(), ShowGroupSubDataActivity.class);
                startActivity(intent);
            });
        }
        activateUserButton();
    }

    /**
     * Calls the server to get all data of all subscribed users requests
     */
    private void doSingleUserRequest() {
        JSONObject obj= setAuthTokenObject();
        System.out.println(obj.toString());
        RequestQueue queue1 = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        JsonObjectRequest getSubscribedUserRequest = new JsonObjectRequest(Request.Method.POST, GETSUBSCRIBEDUSERURL, obj,
                response -> {
                },
                volleyError -> {
                    if (volleyError.networkResponse != null)
                        getVolleyError(volleyError.networkResponse.statusCode);
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 200) {
                    try {
                        String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        saveUsersParams(json);
                    } catch (UnsupportedEncodingException e) {
                        createDialog(SERVERERROR);
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest) {
            cancelReq(getSubscribedUserRequest);
        }
        queue1.add(getSubscribedUserRequest);
    }

    @SuppressLint("UseSparseArrays")
    private void saveUsersParams(String json) {
        userParams = new HashMap<>();
        try {
            JSONArray allUsers = new JSONArray(json);
            for(int i = 0; i < allUsers.length(); i++){
                JSONObject user = allUsers.getJSONObject(i);
                userParams.put(i, user);
                createUserButtons(i);
            }
        } catch (JSONException e) {
            createDialog(SERVERERROR);
        }
    }

    @SuppressLint("SetTextI18n")
    private void createUserButtons(int i) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Button button = new Button(getActivity());
            button.setGravity(Gravity.CENTER_HORIZONTAL);

            button.setId(i);
            button.setText("User "+ String.valueOf(i));
            button.setBackground(Drawable.createFromPath("drawable-v24/health_param_shape.xml"));

            subscribedUserButtons.addView(button);
        });
    }

    /**
     * Calls the server to get all data of all subscribed group of users requests
     */
    private void doGroupRequest() {
        JSONObject obj= setAuthTokenObject();

        System.out.println(obj.toString());
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        JsonObjectRequest getSubscribedGroupRequest = new JsonObjectRequest(Request.Method.POST, GETSUBSCRIBEDGROUPDATAURL, obj,
                response -> {
                },
                volleyError -> {
                    if (volleyError.networkResponse != null)
                        getVolleyError(volleyError.networkResponse.statusCode);
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 200) {
                    try {
                        String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        saveGroupsParam(json);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest) {
            cancelReq(getSubscribedGroupRequest);
        }
        queue.add(getSubscribedGroupRequest);
    }

    @SuppressLint("UseSparseArrays")
    private void saveGroupsParam(String json) {
        groupParams = new HashMap<>();
        try {
            JSONArray allGroups = new JSONArray(json);
            for(int i = 0; i < allGroups.length(); i++){
                JSONObject group = allGroups.getJSONObject(i);
                groupParams.put(i, group);
                createGroupButtons(i);
            }
        } catch (JSONException e) {
            createDialog(SERVERERROR);
        }
    }

    /**
     * Creates a new button for each JSONObject in the string
     */
    @SuppressLint("SetTextI18n")
    private void createGroupButtons(int i) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Button button = new Button(getActivity());
            button.setGravity(Gravity.CENTER_HORIZONTAL);

            button.setId(i);
            button.setText("Group "+ String.valueOf(i));
            button.setBackground(Drawable.createFromPath("drawable-v24/health_param_shape.xml"));

            subscribedGroupButtons.addView(button);
        });
    }


    /**
     * Creates the JSONObject with the user id
     */
    private JSONObject setAuthTokenObject(){
        JSONObject authId = new JSONObject();
        try {
            authId.put("id", AuthToken.getId());
        } catch (JSONException e) {
            createDialog(SERVERERROR);
            incompleteRequest = true;
        }
        return authId;
    }

    /**
     * @param request is the JsonObject request that must be cancel
     *
     *                Deletes the request and create an error dialog
     */
    private void cancelReq(JsonObjectRequest request) {
        incompleteRequest = false;
        createDialog(SERVERERROR);
        request.cancel();
    }


    /**
     * @param text is the error
     */
    private void createDialog(String text){
        Objects.requireNonNull(getActivity()).runOnUiThread(()-> {
            if (!((Objects.requireNonNull(getActivity())).isFinishing())) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                alertDialogBuilder.setMessage(text);
                alertDialogBuilder.setIcon(com.data4help.d4h_thirdparty.R.drawable.ic_exit);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.create().show();
            }
        });

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
