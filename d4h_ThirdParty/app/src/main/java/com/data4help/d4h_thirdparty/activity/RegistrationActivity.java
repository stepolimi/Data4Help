package com.data4help.d4h_thirdparty.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.data4help.d4h_thirdparty.Config.EMPTYFIELDS;
import static com.data4help.d4h_thirdparty.Config.INCORRECTFISCALCODE;
import static com.data4help.d4h_thirdparty.Config.REGISTRATIONURL;
import static com.data4help.d4h_thirdparty.Config.SERVERERROR;
import static com.data4help.d4h_thirdparty.Config.SHORTPASSWORD;
import static com.data4help.d4h_thirdparty.R.*;


public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText typeSociety;
    private EditText fiscalCode;
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

    private TextView error;

    private JsonObjectRequest jobReq;
    private RequestQueue queue;

    private String errorString;
    private boolean incompleteRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.registration);

        setAttributes();
        registrationButton.setOnClickListener(v -> {
            JSONObject personalDetails = new JSONObject();
            JSONObject credential = new JSONObject();

            try {
                setPersonalDetails(personalDetails);
                setCredential(credential);
            } catch (JSONException e) {
                errorString = SERVERERROR;
                incompleteRequest = true;
            }

            queue = Volley.newRequestQueue(RegistrationActivity.this);
            jobReq = new JsonObjectRequest(Request.Method.POST, REGISTRATIONURL, credential,
                    response -> VolleyLog.v("Response:%n %s", response.toString()),
                    volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    switch (response.statusCode) {
                        case 200:
                            try {
                                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                new AuthToken(json);
                                personalDetails.put("userId", AuthToken.getId());
                            } catch (UnsupportedEncodingException | JSONException e) {
                                errorString = SERVERERROR;
                                incompleteRequest = true;
                            }
                            sendUserData(personalDetails);
                            break;
                        //TODO: altri errori
                        case 403:
                            System.out.println("The access has been denied. Try again.");
                            break;
                        case 401:
                            System.out.println("The given email is already in the DB. Change it or login.");
                            break;
                    }
                    finish();
                    return super.parseNetworkResponse(response);
                }
            };
            if(incompleteRequest)
                cancelReq(errorString);
            else
            queue.add(jobReq);
        });

        name.setOnClickListener(v -> error.setText(""));

    }

    /**
     * @param personalDetails is the object containing all personal details of the just created user
     */
    private void sendUserData(JSONObject personalDetails) {
        JsonObjectRequest userDataReq = new JsonObjectRequest(Request.Method.POST, Config.PERSONALDATAURL, personalDetails,
                response -> {},
                volleyError -> {}){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch(response.statusCode){
                    case 200:
                        startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                        break;
                    //TODO: codici d'errore
                    case 403:
                        cancelReq("The access has been denied. Try again.");
                        break;
                    case 401:
                        cancelReq("The given email is already in the DB. Change it or login.");
                        break;
                }
                finish();
                return super.parseNetworkResponse(response);
            }
        };
        if(incompleteRequest)
            cancelReq(errorString);
        else
            queue.add(userDataReq);
    }

    /**
     * @param personalDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the personalDetails object
     */
    private void setPersonalDetails(JSONObject personalDetails) throws JSONException {
        checkValue("name", name.getText().toString(), personalDetails);
        checkValue("typeOfSociety", typeSociety.getText().toString(), personalDetails);
        checkValue("pIva", pIva.getText().toString(), personalDetails);
        JSONObject address = new JSONObject();
        setAddress(address);
        personalDetails.put("address", address);
        checkFiscalCode(personalDetails);

        checkPolicyBox();
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
                errorString = SERVERERROR;
                incompleteRequest = true;
            }
        }
        else{
            errorString = EMPTYFIELDS;
            incompleteRequest = true;
        }
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        error.setText(errorString);
        deleteParam();
        jobReq.cancel();
    }

    /**
     * Clear all param in case of wrong answer
     */
    private void deleteParam() {
        email.getText().clear();
        password.getText().clear();
        name.getText().clear();
        typeSociety.getText().clear();
        fiscalCode.getText().clear();
        pIva.getText().clear();
        street.getText().clear();
        number.getText().clear();
        city.getText().clear();
        cap.getText().clear();
        country.getText().clear();
        region.getText().clear();
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
    private void checkValue(String field, String value,JSONObject personalDetails) throws JSONException {
        if(value.isEmpty()){
            errorString = EMPTYFIELDS;
            incompleteRequest = true;
        }
        else
            personalDetails.put(field, value);
    }

    /**
     * @param personalDetails is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the given fiscal code is correct for length and elements: if it is correct it will be put in the JSON;
     * if not an error string will be set.
     *
     */
    @SuppressLint("SetTextI18n")
    private void checkFiscalCode(JSONObject personalDetails) throws JSONException {
        String fc = fiscalCode.getText().toString();
        if( fc.length() == 16 ) {
            String fc2 = fc.toUpperCase();
            for (int i = 0; i < fc2.length(); i++) {
                int c = fc2.charAt(i);
                if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z')) {
                    errorString = INCORRECTFISCALCODE;
                    incompleteRequest = true;
                    return;
                }
            }
            personalDetails.put("fiscalCode", fc);
        }
        else{
            errorString = INCORRECTFISCALCODE;
            incompleteRequest = true;
        }

    }

    /**
     * @param credential is the JSONObject that must be filled
     *
     * Puts the detected values in the credential object
     */
    private void setCredential(JSONObject credential) throws JSONException {
        checkValue("email", email.getText().toString(), credential);
        checkPassword(credential);
    }

    /**
     * @param credential is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the password is in the length range.
     */
    @SuppressLint("SetTextI18n")
    private void checkPassword(JSONObject credential) throws JSONException {
        String psw = password.getText().toString();
        if(psw.length() < 8 || psw.length() > 20){
            errorString = SHORTPASSWORD;
            incompleteRequest = true;
        }
        else
            credential.put("password", password.getText().toString());
    }

    /**
     * Checks if the policy box has been selected or not.
     */
    private void checkPolicyBox(){
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox){
            errorString = EMPTYFIELDS;
            incompleteRequest = true;
        }
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(id.name);
        typeSociety = findViewById(id.typeSociety);
        fiscalCode = findViewById(id.fiscalCode);
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
        error = findViewById(id.error);
    }
}

