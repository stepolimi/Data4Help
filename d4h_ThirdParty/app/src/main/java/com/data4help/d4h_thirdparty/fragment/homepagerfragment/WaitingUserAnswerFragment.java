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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.R;
import com.data4help.d4h_thirdparty.dialogfragment.SingleNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragment.SinglePositiveRequestDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.GETPENDINGREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.NOSINGLEUSERREQUESTS;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.SUBSCRIBEUSERURL;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingUserAnswerFragment extends Fragment {
    public static String id;

    private LinearLayout singleUserRequestsButtons;
    private View view;

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
            if (isVisibleToUser && isResumed()) { // fragment is visible and have created
                loadData();
            }
        }


        private void loadData(){
        System.out.println("blabla");

        Objects.requireNonNull(getActivity()).runOnUiThread(()->{
                singleUserRequestsButtons = view.findViewById(R.id.singleUserRequestsButtons);

                getPendingRequests();

                for (int i = 0; i < singleUserRequestsButtons.getChildCount(); i++) {
                    String id = String.valueOf(singleUserRequestsButtons.getChildAt(i).getId());
                    singleUserRequestsButtons.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            JSONObject seeAccepted = new JSONObject();
                            try {
                                seeAccepted.put("thirdPartyId", AuthToken.getId());
                                seeAccepted.put("id", id);
                            } catch (JSONException e) {
                                //TODO
                                //error = SERVERERROR;
                                //incompleteRequest = true;
                            }
                            RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
                            JsonObjectRequest seeAcceptedSingleRequest = new JsonObjectRequest(Request.Method.POST, SUBSCRIBEUSERURL, seeAccepted,
                                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                                    volleyError -> VolleyLog.e("Error: " + volleyError.getMessage())) {
                                @Override
                                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                                    FragmentManager fm = Objects.requireNonNull(getFragmentManager());
                                    switch (response.statusCode) {
                                        case 200:
                                            SinglePositiveRequestDialogFragment positiveDialog = new SinglePositiveRequestDialogFragment();
                                            positiveDialog.show(fm, "SinglePositiveRequestDialogFragment");

                                            break;
                                        default:
                                            SingleNegativeRequestDialogFragment negativeDialog = new SingleNegativeRequestDialogFragment();
                                            negativeDialog.show(fm, "SingleNegativeRequestDialogFragment");

                                            break;
                                    }
                                    return super.parseNetworkResponse(response);
                                }
                            };
                            queue.add(seeAcceptedSingleRequest);
                        }
                    });
                }
        });
    }

    private void getPendingRequests() {
        JSONObject authId = new JSONObject();
        try {
            authId.put("id", AuthToken.getId());
        } catch (JSONException e) {
            createDialog(SERVERERROR);
        }
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest seeAcceptedSingleRequest = new JsonObjectRequest(Request.Method.POST, GETPENDINGREQUESTURL, authId,
                response -> VolleyLog.v("Response:%n %s", response.toString()),
                volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                FragmentManager fm = Objects.requireNonNull(getFragmentManager());
                switch(response.statusCode){
                    case 200:
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            createButtons(json);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        SingleNegativeRequestDialogFragment negativeDialog = new SingleNegativeRequestDialogFragment();
                        negativeDialog.show(fm, "SingleNegativeRequestDialogFragment");

                        break;
                }
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(seeAcceptedSingleRequest);
    }


    private void createButtons(String json){
        System.out.println(json);
        try {
            JSONArray thirdPartyRequest = new JSONArray(json);
            if(thirdPartyRequest.length() == 0) createDialog(NOSINGLEUSERREQUESTS);
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

    }

    private void createDialog(String text){
        if(!((Objects.requireNonNull(getActivity())).isFinishing())) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialogBuilder.setMessage(text);
            alertDialogBuilder.setIcon(com.data4help.d4h_thirdparty.R.drawable.ic_exit);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.create().show();
        }

    }
}
