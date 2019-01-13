package com.data4help.d4h_thirdparty.fragment.homepagerfragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.data4help.d4h_thirdparty.R;
import com.data4help.d4h_thirdparty.dialogfragment.SinglePositiveRequestDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.BADREQUEST;
import static com.data4help.d4h_thirdparty.Config.GETPENDINGREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOSINGLEUSERREQUESTS;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingUserAnswerFragment extends Fragment {
    public static String id;

    private LinearLayout singleUserRequestsButtons;
    private View view;

    private boolean incompleteRequest = false;

    public WaitingUserAnswerFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_waiting_user_answer, container, false);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // fragment is visible and have created
        if (isVisibleToUser && isResumed()) loadData();
    }


    private void loadData(){
        singleUserRequestsButtons = view.findViewById(R.id.singleUserRequestsButtons);
        getPendingRequests();
    }


    private void getPendingRequests() {

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            JSONObject authId = new JSONObject();
            try {
                authId.put("id", AuthToken.getId());
            } catch (JSONException e) {
                createDialog(SERVERERROR);
            }
            RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            JsonObjectRequest seeAcceptedSingleRequest = new JsonObjectRequest(Request.Method.POST, GETPENDINGREQUESTURL, authId,
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
                            createButtons(json);
                        } catch (UnsupportedEncodingException e) {
                            createDialog(SERVERERROR);
                        }
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            if (incompleteRequest)
                cancelReq(seeAcceptedSingleRequest);
            else
                queue.add(seeAcceptedSingleRequest);
        });selectButton();
    }

    /**
     * Gives the action based on a button click
     */
    private void selectButton() {
        for (int i = 0; i < singleUserRequestsButtons.getChildCount(); i++) {
            id = String.valueOf(singleUserRequestsButtons.getChildAt(i).getId());

            singleUserRequestsButtons.getChildAt(i).setOnClickListener((v) -> {
                FragmentManager fm = getFragmentManager();
                SinglePositiveRequestDialogFragment dialogFragment = new SinglePositiveRequestDialogFragment();
                dialogFragment.setIdRequest(id);

                dialogFragment.show(Objects.requireNonNull(fm), "SinglePositiveRequestDialogFragment");

            });

        }
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
     * @param json is the given string
     *
     *             Creates a new button for each JSONObject in the string
     *
     */
    private void createButtons(String json){

        Objects.requireNonNull(getActivity()).runOnUiThread(()-> {
            try {
                JSONArray thirdPartyRequest = new JSONArray(json);
                if (thirdPartyRequest.length() == 0) createDialog(NOSINGLEUSERREQUESTS);
                else {
                    for (int i = 0; i < thirdPartyRequest.length(); i++) {

                        JSONObject jsonObject = thirdPartyRequest.getJSONObject(i);

                        Button button = new Button(getActivity());
                        button.setGravity(Gravity.CENTER_HORIZONTAL);

                        button.setId(Integer.parseInt(jsonObject.getString("requestId")));
                        button.setText(jsonObject.getString("fiscalCode"));
                        button.setBackground(Drawable.createFromPath("drawable-v24/health_param_shape.xml"));

                        singleUserRequestsButtons.addView(button);
                    }
                }
            } catch (JSONException e) {
                createDialog(SERVERERROR);
            }
        });
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
