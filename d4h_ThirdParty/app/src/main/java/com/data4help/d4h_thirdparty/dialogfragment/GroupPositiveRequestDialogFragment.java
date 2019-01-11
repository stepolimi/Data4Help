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
import com.data4help.d4h_thirdparty.fragment.homepagerfragment.GroupRequestFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.POSITIVEGROUPREQUEST;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEGROUPURL;

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
            subscribeRequest.put("id", GroupRequestFragment.groupRequestID);
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
                        Objects.requireNonNull(getActivity()).getFragmentManager().findFragmentByTag("GroupRequestFragment");
                        break;
                    //TODO: codici d'errore
                    case 401:
                        break;
                    case 500:
                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        GroupRequestFragment.queue.add(subscribeGroupReq);
    }

}
