package com.data4help.data4help1.dialogfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.data4help1.Config.ACCEPTORDENIEURL;

public class ThirdPartiesRequestDialogFragment extends DialogFragment{

    public ThirdPartiesRequestDialogFragment(){}

    public boolean subscribed = false;
    public boolean locked = false;
    public TextView singlePositiveRequest;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.third_party_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(id.singleRefuseButton);
        singlePositiveRequest = dialogFragment.findViewById(id.singlePositiveRequest);


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
            try {
                setAcceptOrNot();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendResponse();
            getDialog().dismiss();
            locked = false;
            //Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("SingleRequestFragment");

        });
    }

    /**
     * Sends the user's choice to the server
     */
    private void sendResponse() {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest groupUserRequest = new JsonObjectRequest(Request.Method.POST, ACCEPTORDENIEURL,  null,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        getDialog().dismiss();
                        locked = false;
                        break;
                    //TODO: codici d'errore
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
    }

    /**
     * @throws JSONException if something goes wrong
     *
     * Sets the JSONObject elements
     */
    private void setAcceptOrNot() throws JSONException {
        JSONObject acceptOrNot = new JSONObject();

        acceptOrNot.put("userId", AuthToken.getId());
        if(subscribed)
            acceptOrNot.put("accept", true);
        else
            acceptOrNot.put("accept", false);
    }
}
