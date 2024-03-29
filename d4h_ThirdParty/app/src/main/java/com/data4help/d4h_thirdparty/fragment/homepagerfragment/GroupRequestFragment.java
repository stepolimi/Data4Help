package com.data4help.d4h_thirdparty.fragment.homepagerfragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.dialogfragment.GroupNegativeRequestDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragment.GroupPositiveRequestDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static com.data4help.d4h_thirdparty.Config.GROUPREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.NOTIMPLEMENTED;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;
import static com.data4help.d4h_thirdparty.R.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupRequestFragment extends Fragment implements Runnable {

    private EditText minAge;
    private EditText maxAge;
    private EditText minWeight;
    private EditText maxWeight;
    private EditText minHeight;
    private EditText maxHeight;

    private EditText street;
    private EditText number;
    private EditText city;
    private EditText cap;
    private EditText region;
    private EditText country;

    private RadioButton male;
    private RadioButton female;

    private Button saveGroupRequestButton;

    private JsonObjectRequest groupUserRequest;
    public RequestQueue queue;

    private GroupPositiveRequestDialogFragment positiveDialog;

    private String error;
    private boolean incompleteRequest = false;
    private View view;

    public static String groupRequestId;

    public GroupRequestFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(layout.fragment_group_request, container, false);

        Runnable runnable = this;
        Thread thread = new Thread(runnable);
        thread.start();
        return view;
    }


    /**
     * @param groupRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Fills the JSONObject
     */
    private void setGroupRequest(JSONObject groupRequest) throws JSONException {
        groupRequest.put("thirdPartyId", AuthToken.getId());

        JSONObject attributes = new JSONObject();
        setAttributesObject(attributes);
        groupRequest.put("attributes", attributes);

    }

    /**
     * @param attributes is th JSONObject
     * @throws JSONException when something bad happens
     *
     * Sets all attributes
     */
    private void setAttributesObject(JSONObject attributes) throws JSONException {
        checkValue("minAge", minAge.getText().toString(), attributes);
        checkValue("maxAge", maxAge.getText().toString(), attributes);
        checkValue("minWeight", minWeight.getText().toString(), attributes);
        checkValue("maxWeight", maxWeight.getText().toString(), attributes);
        checkValue("minHeight", minHeight.getText().toString(), attributes);
        checkValue("maxHeight", maxHeight.getText().toString(), attributes);

        checkSex(attributes);

        JSONObject addressRange = new JSONObject();
        setAddressRange(addressRange);
        attributes.put("addressRange", addressRange);
    }

    /**
     * @param addressRange is the JSONObject
     * @throws JSONException if something bad occurs
     *
     * Puts the address in the JSONObject
     */
    private void setAddressRange(JSONObject addressRange) throws JSONException {
        addressRange.put("region", region.getText().toString());
        addressRange.put("state", country.getText().toString());
    }

    /**
     * @param field is the JSONObject field
     * @param value is the string taken from the TextEdit
     * @param groupRequest is the JSONObject
     * @throws JSONException is something bad occurs
     *
     * Checks if the value is empty or not
     */
    private void checkValue(String field, String value, JSONObject groupRequest) throws JSONException {
        if(!value.isEmpty())
            groupRequest.put(field, Integer.parseInt(value));
    }


    /**
     * @param groupRequest is the JSONObject that must be filled
     * @throws JSONException is something bad occurs
     *
     * Checks if the sex radio buttons have been checked or not
     */
    private void checkSex(JSONObject groupRequest) throws JSONException {
        boolean m = male.isChecked();
        boolean f = female.isChecked();
        if(m && !f)
            groupRequest.put("sex", "male");
        else if (!m && f)
            groupRequest.put("sex", "female");
    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view) {
        minAge = view.findViewById(id.minAge);
        maxAge = view.findViewById(id.maxAge);
        minWeight = view.findViewById(id.minWeight);
        maxWeight = view.findViewById(id.maxWeight);
        minHeight = view.findViewById(id.minHeight);
        maxHeight = view.findViewById(id.maxHeight);

        street = view.findViewById(id.street);
        number = view.findViewById(id.number);
        city = view.findViewById(id.city);
        cap = view.findViewById(id.postalCode);
        region = view.findViewById(id.region);
        country = view.findViewById(id.country);

        male = view.findViewById(id.maleButton);
        female = view.findViewById(id.femaleButton);

        saveGroupRequestButton = view.findViewById(id.saveGroupRequestButton);
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText, JsonObjectRequest request) {
        setAlertDialog(errorText);
        incompleteRequest= false;
        request.cancel();
    }

    @Override
    public void run() {
        setAttributes(view);
        saveGroupRequestButton.setOnClickListener(v -> {
            JSONObject groupRequest = new JSONObject();

            try {
                setGroupRequest(groupRequest);
            } catch (JSONException e) {
                error = SERVERERROR;
                incompleteRequest = true;
            }
            //
            queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            groupUserRequest = new JsonObjectRequest(Request.Method.POST, GROUPREQUESTURL, groupRequest,
                    response -> {},
                    volleyError ->  {
                        if(volleyError.networkResponse != null)
                            getVolleyError(volleyError.networkResponse.statusCode);
                    }){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode == 200) {
                        try {
                            groupRequestId = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        positiveDialog = new GroupPositiveRequestDialogFragment();
                        positiveDialog.show(Objects.requireNonNull(getFragmentManager()), "GroupPositiveRequestDialogFragment");
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            if(incompleteRequest) {
                cancelReq(error, groupUserRequest);
            }
            queue.add(groupUserRequest);
        });

        street.setOnClickListener(v -> setAlertDialog(NOTIMPLEMENTED));
        cap.setOnClickListener(v -> setAlertDialog(NOTIMPLEMENTED));
        number.setOnClickListener(v -> setAlertDialog(NOTIMPLEMENTED));
        city.setOnClickListener(v -> setAlertDialog(NOTIMPLEMENTED));
    }

    /**
     * Creates an alert dialog on all TextEdit which are not available yet
     */
    private void setAlertDialog(String text) {
        Objects.requireNonNull(getActivity()).runOnUiThread(()-> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialogBuilder.setMessage(text);
            alertDialogBuilder.setIcon(drawable.ic_exit);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.create().show();
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
                GroupNegativeRequestDialogFragment negativeDialog = new GroupNegativeRequestDialogFragment();
                negativeDialog.show(Objects.requireNonNull(getFragmentManager()), "GroupNegativeRequestDialogFragment");
                break;
            case 401:
                setAlertDialog(UNAUTHORIZED);
                break;
            case 404:
                setAlertDialog(NOTFOUND);
                break;
            case 500:
                setAlertDialog(INTERNALSERVERERROR);
                break;
            default:
                break;
        }
    }
}
