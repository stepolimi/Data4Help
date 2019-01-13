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
import com.data4help.d4h_thirdparty.activity.ShowSingleSubUserDataActivity;

public class HomeSubscribedRequestFragment extends Fragment{


    private static Map<Integer, JSONObject> groupParams;
    private static Map<Integer, JSONObject> userParams;
    private static int buttonGroupId;
    private static int buttonUserId;
    private boolean incompleteRequest = false;

    private Button userButton1;
    private Button userButton2;
    private Button userButton3;
    private Button userButton4;
    private Button userButton5;
    private Button userButton6;
    private Button userButton7;
    private Button userButton8;
    private Button userButton9;
    private Button userButton10;
    private Button userButton11;
    private Button userButton0;

    private Button groupButton1;
    private Button groupButton2;
    private Button groupButton3;
    private Button groupButton4;
    private Button groupButton5;
    private Button groupButton6;
    private Button groupButton7;
    private Button groupButton8;
    private Button groupButton9;
    private Button groupButton10;
    private Button groupButton11;
    private Button groupButton0;

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
        setAttributes(view);
        doGroupRequest();
        doSingleUserRequest();

        activateButtons();
        return view;
    }

    private void activateButtons() {
        onClickButton(userButton1);
        onClickButton(userButton2);
        onClickButton(userButton3);
        onClickButton(userButton4);
        onClickButton(userButton5);
        onClickButton(userButton6);
        onClickButton(userButton7);
        onClickButton(userButton8);
        onClickButton(userButton9);
        onClickButton(userButton10);
        onClickButton(userButton11);
        onClickButton(userButton0);

        onClickGroupButton(groupButton1);
        onClickGroupButton(groupButton2);
        onClickGroupButton(groupButton3);
        onClickGroupButton(groupButton4);
        onClickGroupButton(groupButton5);
        onClickGroupButton(groupButton6);
        onClickGroupButton(groupButton7);
        onClickGroupButton(groupButton8);
        onClickGroupButton(groupButton9);
        onClickGroupButton(groupButton10);
        onClickGroupButton(groupButton11);
        onClickGroupButton(groupButton0);

    }

    private void onClickGroupButton(Button groupButton) {
        groupButton.setOnClickListener((v)->{
            if(!groupButton.getText().toString().isEmpty()) {
                buttonGroupId = Integer.parseInt(groupButton.getText().toString());
                Intent intent = new Intent(getActivity(), ShowGroupSubDataActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ResourceType")
    private void onClickButton(Button userButton) {
        userButton.setOnClickListener((v)->{
            if(!userButton.getText().toString().isEmpty()) {
                buttonUserId = Integer.parseInt(userButton.getText().toString());
                Intent intent = new Intent(getActivity(), ShowSingleSubUserDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAttributes(View view) {
        userButton1 = view.findViewById(id.userButton1);
        userButton2 = view.findViewById(id.userButton2);
        userButton3 = view.findViewById(id.userButton3);
        userButton4 = view.findViewById(id.userButton4);
        userButton5 = view.findViewById(id.userButton5);
        userButton6 = view.findViewById(id.userButton6);
        userButton7 = view.findViewById(id.userButton7);
        userButton8 = view.findViewById(id.userButton8);
        userButton9 = view.findViewById(id.userButton9);
        userButton10 = view.findViewById(id.userButton10);
        userButton11 = view.findViewById(id.userButton11);
        userButton0 = view.findViewById(id.userButton0);


        groupButton1 = view.findViewById(id.groupButton1);
        groupButton2 = view.findViewById(id.groupButton2);
        groupButton3 = view.findViewById(id.groupButton3);
        groupButton4 = view.findViewById(id.groupButton4);
        groupButton5 = view.findViewById(id.groupButton5);
        groupButton6 = view.findViewById(id.groupButton6);
        groupButton7 = view.findViewById(id.groupButton7);
        groupButton8 = view.findViewById(id.groupButton8);
        groupButton9 = view.findViewById(id.groupButton9);
        groupButton10 = view.findViewById(id.groupButton10);
        groupButton11 = view.findViewById(id.groupButton11);
        groupButton0 = view.findViewById(id.groupButton0);
    }

    private void activateUserButton() {
        for(int i = 0; i < subscribedUserButtons.getChildCount(); i++){
            buttonUserId = subscribedUserButtons.getChildAt(i).getId();
            subscribedUserButtons.getChildAt(i).setOnClickListener((v) -> {
                Intent intent = new Intent(getActivity(), ShowGroupSubDataActivity.class);
                startActivity(intent);
            });
        }
    }

    private void activateGroupButton() {
        for(int i = 0; i < subscribedGroupButtons.getChildCount(); i++){
            buttonGroupId = subscribedGroupButtons.getChildAt(i).getId();
            subscribedGroupButtons.getChildAt(i).setOnClickListener((v) -> {
                Intent intent = new Intent(getActivity(), ShowGroupSubDataActivity.class);
                startActivity(intent);
            });
        }
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
        //activateUserButton();
    }

    @SuppressLint("SetTextI18n")
    private void createUserButtons(int i) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            switch(i){
                case 0:
                    userButton0.setText(String.valueOf(i) );
                    break;
                case 1:
                    userButton1.setText(String.valueOf(i));
                    break;
                case 2:
                    userButton2.setText(String.valueOf(i));
                    break;
                case 3:
                    userButton3.setText(String.valueOf(i));
                    break;
                case 4:
                    userButton4.setText(String.valueOf(i));
                    break;
                case 5:
                    userButton5.setText(String.valueOf(i));
                    break;
                case 6:
                    userButton6.setText(String.valueOf(i));
                    break;
                case 7:
                    userButton7.setText(String.valueOf(i));
                    break;
                case 8:
                    userButton8.setText(String.valueOf(i));
                    break;
                case 9:
                    userButton9.setText(String.valueOf(i));
                    break;
                case 10:
                    userButton10.setText(String.valueOf(i));
                    break;
                case 11:
                    userButton11.setText(String.valueOf(i));
                    break;
                default:
                    Button button = new Button(getActivity());
                    button.setText("User" + String.valueOf(i));

                    subscribedUserButtons.addView(button);
                    break;
            }

        });
    }

    /**
     * Calls the server to get all data of all subscribed group of users requests
     */
    private void doGroupRequest() {
        JSONObject obj= setAuthTokenObject();
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
                        System.out.println(json);
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

        //activateGroupButton();
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
                    switch(i) {
                        case 0:
                            groupButton0.setText(String.valueOf(i));
                            break;
                        case 1:
                            groupButton1.setText(String.valueOf(i));
                            break;
                        case 2:
                            groupButton2.setText(String.valueOf(i));
                            break;
                        case 3:
                            groupButton3.setText(String.valueOf(i));
                            break;
                        case 4:
                            groupButton4.setText(String.valueOf(i));
                            break;
                        case 5:
                            groupButton5.setText(String.valueOf(i));
                            break;
                        case 6:
                            groupButton6.setText(String.valueOf(i));
                            break;
                        case 7:
                            groupButton7.setText(String.valueOf(i));
                            break;
                        case 8:
                            groupButton8.setText(String.valueOf(i));
                            break;
                        case 9:
                            groupButton9.setText(String.valueOf(i));
                            break;
                        case 10:
                            groupButton10.setText(String.valueOf(i));
                            break;
                        case 11:
                            groupButton11.setText(String.valueOf(i));
                            break;
                        default:
                            Button button = new Button(getActivity());
                            button.setGravity(Gravity.CENTER_HORIZONTAL);
                            button.setId(i);
                            button.setText(String.valueOf(i));
                            button.setBackground(Drawable.createFromPath("drawable-v24/health_param_shape.xml"));
                            subscribedGroupButtons.addView(button);
                            break;
                    }

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
