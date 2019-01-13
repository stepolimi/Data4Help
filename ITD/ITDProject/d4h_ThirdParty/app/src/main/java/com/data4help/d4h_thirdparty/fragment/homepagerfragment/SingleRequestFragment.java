package com.data4help.d4h_thirdparty.fragment.homepagerfragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.d4h_thirdparty.AuthToken;
import com.data4help.d4h_thirdparty.dialogfragment.NotFoundFiscalCodeDialogFragment;
import com.data4help.d4h_thirdparty.dialogfragment.SendSingleRequestDialogFragment;
import com.data4help.d4h_thirdparty.R.*;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.data4help.d4h_thirdparty.Config.INCORRECTFISCALCODE;
import static com.data4help.d4h_thirdparty.Config.INSERTNUMBER;
import static com.data4help.d4h_thirdparty.Config.INTERNALSERVERERROR;
import static com.data4help.d4h_thirdparty.Config.NOTFOUND;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.SINGLEREQUESTURL;
import static com.data4help.d4h_thirdparty.Config.UNAUTHORIZED;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleRequestFragment extends Fragment implements Runnable {

    private EditText userCountry;
    private EditText userFiscalCode;
    private EditText serviceDescription;

    private Button saveSingleRequestButton;

    private JsonObjectRequest singleUserRequest;
    private  RequestQueue queue;

    private String error;
    private boolean incompleteRequest = false;
    private View view;

    public SingleRequestFragment() {
        // Required empty public constructor
    }


    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(com.data4help.d4h_thirdparty.R.layout.fragment_single_request, container, false);


        Runnable runnable = this;
        Thread thread = new Thread(runnable);
        thread.start();

        return view;
    }




    /**
     * @param singleRequest is the JSONObject that must be filled
     * @throws JSONException is something goes wrong
     *
     * Sets all attributes in the JSONObject that will be sent to the server
     */
    private void setSingleRequest(JSONObject singleRequest) throws JSONException {
        String country = userCountry.getText().toString().toLowerCase();
        singleRequest.put("thirdPartyId", AuthToken.getId());

        if(country.equals("italy"))
            checkFiscalCode(singleRequest);
        else {
            if (userFiscalCode.getText().toString().isEmpty()) setErrorString(INSERTNUMBER);
            else
                singleRequest.put("fiscalCode", userFiscalCode.getText().toString());
        }
        System.out.println(serviceDescription.getText().toString());
        singleRequest.put("description", serviceDescription.getText().toString());

    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes(View view){
        userCountry = view.findViewById(id.userCountry);
        userFiscalCode = view.findViewById(id.userFiscalCode);
        serviceDescription = view.findViewById(id.serviceDescription);

        saveSingleRequestButton = view.findViewById(id.saveSingleRequestButton);
    }

    /**
     * @param singleRequest is the JSONObject in which the fiscal code must be put
     *
     * Checks if the given fiscal code is correct for length and elements: if it is correct it will be put in the JSON;
     * if not an error string will be set.
     *
     */
    private void checkFiscalCode(JSONObject singleRequest){
        String fc = userFiscalCode.getText().toString().toLowerCase();
        if (fc.length() == 16) {
            String validFiscalCode = "[a-zA-Z]{6}"+"[0-9]{2}"+"[a-zA-Z]"+"[0-9]{2}"+"[a-zA-Z]"+"[0-9]{3}"+"[a-zA-Z]";
            Matcher matcher= Pattern.compile(validFiscalCode).matcher(fc);
            if(matcher.matches()) {
                try {
                    singleRequest.put("fiscalCode", fc);
                } catch (JSONException e) {
                    setErrorString(SERVERERROR);
                }
            }
            else setErrorString(INCORRECTFISCALCODE);
        } else setErrorString(INCORRECTFISCALCODE);

    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText, JsonObjectRequest request) {
        setAlertDialog(errorText);
        incompleteRequest = false;
        request.cancel();
    }

    /**
     * @param error is the error string
     *
     *              Sets the error label
     */
    private void setErrorString(String error){
        switch (error) {
            case SERVERERROR:
                this.error = SERVERERROR;
                break;
            case INCORRECTFISCALCODE:
                this.error = INCORRECTFISCALCODE;
                break;
            case INSERTNUMBER:
                this.error = INSERTNUMBER;
                break;
        }
        incompleteRequest = true;
        }

    @Override
    public void run() {
        setAttributes(view);
        saveSingleRequestButton.setOnClickListener((v) -> {
            JSONObject singleRequest = new JSONObject();

            try {
                setSingleRequest(singleRequest);
            } catch (JSONException e) {
                setErrorString(SERVERERROR);
            }

            queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
            singleUserRequest = new JsonObjectRequest(Request.Method.POST, SINGLEREQUESTURL, singleRequest,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError ->
                    {
                        if(volleyError.networkResponse != null)
                        getVolleyError(volleyError.networkResponse.statusCode);}){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    FragmentManager fm = Objects.requireNonNull(getFragmentManager());
                    if (response.statusCode == 200){
                        try{
                            String singleRequestId = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            } catch ( UnsupportedEncodingException e ) {
                                setAlertDialog(SERVERERROR);
                            }
                            SendSingleRequestDialogFragment sendDialog = new SendSingleRequestDialogFragment();
                            sendDialog.show(fm ,"SendSingleRequestDialogFragment" );
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            if(incompleteRequest)
                cancelReq(error, singleUserRequest);
            else
                queue.add(singleUserRequest);
        });

    }

    /**
     * Creates an alert dialog on all TextEdit which are not available yet
     */
    private void setAlertDialog(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setIcon(drawable.ic_exit);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }


    /**
     * @param statusCode is the code sent byt the server
     *
     *                   Checks the code sent by the server and show a different error depending on it.
     */
    private void getVolleyError(int statusCode) {
        switch (statusCode){
            case 400:
                NotFoundFiscalCodeDialogFragment fiscalCodeDialog = new NotFoundFiscalCodeDialogFragment();
                fiscalCodeDialog.show(Objects.requireNonNull(getFragmentManager()), "NotFoundFiscalCodeDialogFragment");
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
