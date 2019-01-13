package com.data4help.d4h_thirdparty.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.CheckBox;
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
import com.data4help.d4h_thirdparty.Config;
import com.data4help.d4h_thirdparty.TypeOfSociety;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.data4help.d4h_thirdparty.Config.*;
import static com.data4help.d4h_thirdparty.R.*;


public class RegistrationActivity extends AppCompatActivity implements Runnable {

    private EditText name;
    private EditText typeSociety;
    private EditText pIva;
    private EditText street;
    private EditText number;
    private EditText city;
    private EditText cap;
    private EditText region;
    private EditText country;

    private EditText email;
    private EditText password;
    private CheckBox acceptPolicy;

    private Button registrationButton;

    private JsonObjectRequest registrationReq;
    private RequestQueue queue;
    private ProgressDialog dialog;

    private String errorString;
    private boolean incompleteRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.registration);

        run();
    }

    /**
     * Sets a progress dialog to show to the user that the app is verifying the credentials and the
     * personal details added by the user
     */
    private void setProgressDialog() {
        RegistrationActivity.this.runOnUiThread(() -> {
            dialog = new ProgressDialog(RegistrationActivity.this);
            dialog.setMessage(PLEASEWAIT);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    /**
     * @param personalDetails is the object containing all personal details of the just created user
     */
    private void sendUserData(JSONObject personalDetails) {
        dialog.dismiss();
        JsonObjectRequest thirdPartyDataReq = new JsonObjectRequest(Request.Method.POST, Config.PERSONALDATAURL, personalDetails,
                response -> {},
                volleyError ->
                { if(volleyError.networkResponse != null)
                        getVolleyError(volleyError.networkResponse.statusCode);}){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 200) {
                    dialog.dismiss();
                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                }
                finish();
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest)
            cancelReq(errorString, thirdPartyDataReq);
        else
            queue.add(thirdPartyDataReq);
    }

    /**
     * @param personalDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the personalDetails object
     */
    private void setPersonalDetails(JSONObject personalDetails) throws JSONException {
        checkValue("name", name.getText().toString(), personalDetails);
        checkTypeSociety(typeSociety.getText().toString(), personalDetails);
        JSONObject address = new JSONObject();
        setAddress(address);
        personalDetails.put("address", address);
        checkValue("pIva", pIva.getText().toString(), personalDetails);

        checkPolicyBox();
    }

    /**
     * @param typeOfSociety is the string written by the user
     * @param personalDetails is the JSON object that must be filled
     * @throws JSONException is something goes wrong
     *
     * Checks if the type of society is one of the requested ones
     */
    private void checkTypeSociety(String typeOfSociety, JSONObject personalDetails) throws JSONException {
        if(TypeOfSociety.evaluate(typeOfSociety))
            personalDetails.put("typeOfSociety", typeOfSociety);
        else
            setErrorString(EMPTYFIELDS);
    }

    /**
     * @param address is the jsonObject which will contain the address
     * @throws JSONException if something goes wrong
     *
     * Puts all address parameters
     */
    private void setAddress(JSONObject address) throws JSONException {
        checkValue("street", street.getText().toString(), address);
        checkNumber("number", number.getText().toString(), address);
        checkValue("city", city.getText().toString(), address);
        checkNumber("cap", cap.getText().toString(), address);
        checkValue("region", region.getText().toString(), address);
        checkValue("country", country.getText().toString(), address);
    }

    /**
     * @param field is the JSON field
     * @param value is the value obtained by the related TextView
     * @param address is the JSONObject
     *
     * Checks if the number fields are empty or not
     */
    private void checkNumber(String field, String value, JSONObject address) {
        if(!value.isEmpty()) {
            try {
                address.put(field, Integer.parseInt(value));
            } catch (JSONException e) {
                setErrorString(SERVERERROR);
            }
        }
        else
            setErrorString(EMPTYFIELDS);
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString, JsonObjectRequest request) {
        dialog.dismiss();
        deleteParam(errorString);
        incompleteRequest = false;
        request.cancel();
    }

    /**
     * @param field is the JSON field
     * @param value is the value that must be add in the filed
     * @param personalDetails is the JSON object
     * @throws JSONException if some problems occur
     *
     * checks if the String that must be insert are empty or not: if empty an error will be thrown, if
     * not it will be put in the JSON Object.
     */
    @SuppressLint("SetTextI18n")
    private void checkValue(String field, String value,JSONObject personalDetails) throws JSONException {
        if(value.isEmpty())
            setErrorString(EMPTYFIELDS);
        else if (value.matches("[^A-Za-z]"))
            setErrorString(PRESENCENUMBERORSYMBOLS);
        else
            personalDetails.put(field, value);
    }

    /**
     * @param credential is the JSONObject that must be filled
     *
     * Puts the detected values in the credential object
     */
    private void setCredential(JSONObject credential) throws JSONException {
            checkEmail(credential);
            checkPassword(credential);

    }

    /**
     * @param credentials is the JSONObject which must be filled with th email
     *
     * Checks if the given email is correct or not
     */
    @SuppressLint("SetTextI18n")
    private void checkEmail(JSONObject credentials) throws JSONException {
        String mail = email.getText().toString();
        if(!mail.isEmpty()){
            String validEmail = "[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

            Matcher matcher= Pattern.compile(validEmail).matcher(mail);
            if(matcher.matches())
                    credentials.put("email", mail);

            else setErrorString(WRONGEMAIL);
        }
        else setErrorString(EMPTYFIELDS);
    }

    /**
     * @param credential is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the password is in the length range.
     */
    private void checkPassword(JSONObject credential) throws JSONException {
        String psw = password.getText().toString();
        if(psw.length() < 8 || psw.length() > 20) setErrorString(SHORTPASSWORD);
        else
            credential.put("password", password.getText().toString());
    }

    /**
     * Checks if the policy box has been selected or not.
     */
    private void checkPolicyBox(){
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox)
            setErrorString(EMPTYFIELDS);
    }

    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(id.name);
        typeSociety = findViewById(id.typeSociety);
        pIva = findViewById(id.pIva);
        street = findViewById(id.street);
        number = findViewById(id.number);
        city = findViewById(id.city);
        cap = findViewById(id.postalCode);
        region = findViewById(id.region);
        country = findViewById(id.country);

        email = findViewById(id.regEmail);
        password = findViewById(id.regPassword);
        acceptPolicy = findViewById(id.acceptPolicy);

        registrationButton = findViewById(id.registrationButton);
    }

    /**
     * @param error is the error string
     *
     *              Sets the error label
     */
    private void setErrorString(String error){
        switch (error){
            case EMPTYFIELDS:
                errorString = EMPTYFIELDS;
                break;
            case SERVERERROR:
                errorString = SERVERERROR;
                break;
            case SHORTPASSWORD:
                errorString = SHORTPASSWORD;
                break;
            case INCORRECTPIVA:
                errorString = INCORRECTPIVA;
                break;
            case WRONGEMAIL:
                errorString = WRONGEMAIL;
                break;
            case PRESENCENUMBERORSYMBOLS:
                errorString = PRESENCENUMBERORSYMBOLS;
                break;
        }
        incompleteRequest = true;
    }

    @Override
    public void run() {
        setAttributes();
        registrationButton.setOnClickListener(v -> {
            JSONObject personalDetails = new JSONObject();
            JSONObject credential = new JSONObject();

            try {
                setPersonalDetails(personalDetails);
                setCredential(credential);
            } catch (JSONException e) {
                setErrorString(SERVERERROR);
            }

            setProgressDialog();
            queue = Volley.newRequestQueue(RegistrationActivity.this);
            registrationReq = new JsonObjectRequest(Request.Method.POST, REGISTRATIONURL, credential,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError -> {
                        if(volleyError.networkResponse != null)
                            getVolleyError(volleyError.networkResponse.statusCode);
                    }){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    switch (response.statusCode ) {
                        case 200:
                        try {
                            dialog.dismiss();
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            new AuthToken(json);
                            personalDetails.put("thirdPartyId", AuthToken.getId());
                        } catch (UnsupportedEncodingException | JSONException e) {
                            createDialog(SERVERERROR);
                            }
                            sendUserData(personalDetails);
                        break;
                    }
                    finish();
                    return super.parseNetworkResponse(response);
                }
            };
            if(incompleteRequest)
                cancelReq(errorString, registrationReq);
            else
                queue.add(registrationReq);
        });
    }

    /**
     * Clear all param in case of wrong answer
     */
    private void deleteParam(String errorString) {
        RegistrationActivity.this.runOnUiThread(() -> {
            email.getText().clear();
            password.getText().clear();
            name.getText().clear();
            typeSociety.getText().clear();
            pIva.getText().clear();
            street.getText().clear();
            number.getText().clear();
            city.getText().clear();
            cap.getText().clear();
            country.getText().clear();
            region.getText().clear();
            createDialog(errorString);
        });
    }

    /**
     * @param errorString is the error that must be shown in the dialog
     *
     * Shows a dialog with the occurred error
     */
    private void createDialog(String errorString) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
        alertDialogBuilder.setMessage(errorString);
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

