package com.data4help.data4help1.dialogfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import com.data4help.data4help1.AuthToken;
import com.data4help.data4help1.R.*;
import com.data4help.data4help1.fragments.ThirdPartiesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.data4help1.Config.ACCEPTORDENIEURL;
import static com.data4help.data4help1.Config.BADREQUEST;
import static com.data4help.data4help1.Config.INTERNALSERVERERROR;
import static com.data4help.data4help1.Config.NOTFOUND;
import static com.data4help.data4help1.Config.SERVERERROR;
import static com.data4help.data4help1.Config.UNAUTHORIZED;

public class ThirdPartiesRequestDialogFragment extends DialogFragment{

    private boolean subscribed;
    private boolean incompleteRequest = false;
    private final FragmentManager fm = getFragmentManager();
    private JsonObjectRequest groupUserRequest;

    public ThirdPartiesRequestDialogFragment(){}

    public TextView singlePositiveRequest;
    private JSONObject acceptOrNot;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.third_party_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(id.singleRefuseButton);
        singlePositiveRequest = dialogFragment.findViewById(id.singlePositiveRequest);


        singlePositiveRequest.setText("The third party " + ThirdPartiesFragment.thirdPartyName + "would like to obtain your data for the following reason:\n+ " +
                ThirdPartiesFragment.description+"\n"+ "To accept it click on the accept button, to refuse on the other." );
        buttonClicked(singleAcceptButton, true);
        buttonClicked(singleRefuseButton, false);

        return dialogFragment;
    }

    /**
     * @param button is the chosen button
     *
     * Starts the process to send the chosen answer to the server
     */
    private void buttonClicked(Button button, boolean subscribed) {
        button.setOnClickListener(v -> {
            this.subscribed = subscribed;
            sendResponse();
            this.dismiss();
        });
    }

    /**
     * Sends the user's choice to the server
     */
    private void sendResponse() {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            setAcceptOrNot();
        } catch (JSONException e) {
              incompleteRequest = true;
        }
        groupUserRequest = new JsonObjectRequest(Request.Method.POST, ACCEPTORDENIEURL,  acceptOrNot,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> getVolleyError(volleyError.networkResponse.statusCode)){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
               if (response.statusCode== 200) 
                   getDialog().dismiss();
              
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest)
            cancelReq();
        else
            queue.add(groupUserRequest);
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq() {
        raiseGeneralDialogFragment(SERVERERROR);
        incompleteRequest = false;
        groupUserRequest.cancel();
    }

    /**
     * @param text is the text that must be shown in the dialog fragment
     *
     *             creates a new dialog fragment with the given text
     */
    private void raiseGeneralDialogFragment(String text) {
        GeneralDialogFragment dialogFragment = new GeneralDialogFragment();
        GeneralDialogFragment.setText(text);
        dialogFragment.show(Objects.requireNonNull(fm), "GeneralDialogFragment");
    }

    /**
     * @throws JSONException if something goes wrong
     *
     * Sets the JSONObject elements
     */
    private void setAcceptOrNot() throws JSONException {
         acceptOrNot = new JSONObject();

        acceptOrNot.put("userId", AuthToken.getId());
        acceptOrNot.put("id", ThirdPartiesFragment.requestId);
        if(subscribed)
            acceptOrNot.put("accepted", true);
        else
            acceptOrNot.put("accepted", false);
    }


    /**
     * @param statusCode is the status code sent from the server
     *
     *                   Creates the dialog with a particular error
     */
    public void getVolleyError(int statusCode){
        switch (statusCode){
            case 400:
                raiseGeneralDialogFragment(BADREQUEST);
                break;
            case 401:
                raiseGeneralDialogFragment(UNAUTHORIZED);
                break;
            case 404:
                raiseGeneralDialogFragment(NOTFOUND);
                break;
            case 500:
                raiseGeneralDialogFragment(INTERNALSERVERERROR);
                break;
            default:
                break;
        }
    }
}
