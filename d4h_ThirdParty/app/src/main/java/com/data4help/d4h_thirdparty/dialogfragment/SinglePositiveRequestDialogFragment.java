package com.data4help.d4h_thirdparty.dialogfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.R.*;
import com.data4help.d4h_thirdparty.activity.ShowGroupDataActivity;
import com.data4help.d4h_thirdparty.activity.ShowSingleDataActivity;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.GroupRequestFragment;
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.WaitingUserAnswerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.POSITIVESINGLEREQUEST;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEGROUPURL;

public class SinglePositiveRequestDialogFragment extends DialogFragment{

    public SinglePositiveRequestDialogFragment(){}

    public boolean subscribed = false;

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View dialogFragment = inflater.inflate(layout.single_positive_request_popup, container, false);

        Button singleAcceptButton = dialogFragment.findViewById(id.singleAcceptButton);
        Button singleRefuseButton = dialogFragment.findViewById(id.singleRefuseButton);
        TextView singlePositiveRequest = dialogFragment.findViewById(id.singlePositiveRequest);

        singlePositiveRequest.setText(POSITIVESINGLEREQUEST);

        singleAcceptButton.setOnClickListener(v -> {
            getDialog().dismiss();
            subscribed = true;
            subscribeRequest();

        });

        singleRefuseButton.setOnClickListener(v ->{
            Intent intent= new Intent(getActivity(),ShowSingleDataActivity.class);
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
            subscribeRequest.put("id", WaitingUserAnswerFragment.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest subscribeGroupReq = new JsonObjectRequest(Request.Method.POST, SUBSCRIBEGROUPURL, subscribeRequest,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> VolleyLog.e("Error: " + volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch(response.statusCode){
                    case 200:
                        Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("WaitingUserAnswerFragment");
                        break;
                    //TODO: codici d'errore
                    case 403:
                        break;
                    case 401:
                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        GroupRequestFragment.queue.add(subscribeGroupReq);
    }
}
