package com.data4help.data4help1.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import static com.data4help.data4help1.Config.*;
import static com.data4help.data4help1.R.*;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements Runnable {

    private EditText name;
    private EditText surname;
    private EditText fiscalCode;
    private EditText yearOfBirth;
    private EditText street;
    private EditText number;
    private EditText city;
    private EditText cap;
    private EditText region;
    private EditText country;
    private RadioButton male;
    private RadioButton female;

    private EditText email;
    private EditText password;
    private CheckBox acceptPolicy;

    private Button registrationButton;

    private String errorString;
    private boolean incompleteRequest;
    private ProgressDialog dialog;

    private JsonObjectRequest registrationReq;
    private RequestQueue queue;

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
        dialog = new ProgressDialog(RegistrationActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * @param personalDetails is the object containing all personal details of the just created user
     */
    private void sendUserData(JSONObject personalDetails) {
        JsonObjectRequest userDataReq = new JsonObjectRequest(Request.Method.POST, PERSONALDATAURL, personalDetails,
                response -> {
                },
                volleyError -> {
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                switch (response.statusCode) {
                    case 200:
                        dialog.dismiss();
                        startActivity(new Intent(RegistrationActivity.this, MenuActivity.class));
                        break;
                    //TODO: codici d'errore
                    case 400:
                        //BAD REQUEST
                        break;
                    case 401:
                        //UNAUTHORIZED
                        System.out.println("The given email is already in the DB. Change it or login.");
                        break;

                }
                finish();
                return super.parseNetworkResponse(response);
            }
        };
        if (incompleteRequest)
            cancelReq(errorString, userDataReq);
        else
            queue.add(userDataReq);
    }

    /**
     * Clear all param in case of wrong answer
     */
    private void deleteParam() {
        email.getText().clear();
        password.getText().clear();
        name.getText().clear();
        surname.getText().clear();
        fiscalCode.getText().clear();
        yearOfBirth.getText().clear();
        street.getText().clear();
        number.getText().clear();
        city.getText().clear();
        cap.getText().clear();
        country.getText().clear();
        region.getText().clear();
    }

    /**
     * @param personalDetails is the JSONObject that must be filled
     *                        <p>
     *                        Puts the detected values in the personalDetails object
     */
    private void setPersonalDetails(JSONObject personalDetails) throws JSONException {
        checkValue("name", name.getText().toString(), personalDetails);
        checkValue("surname", surname.getText().toString(), personalDetails);
        checkNumber("yearOfBirth", yearOfBirth.getText().toString(), personalDetails);

        JSONObject address = new JSONObject();
        setAddress(address);
        personalDetails.put("address", address);

        checkFiscalCode(personalDetails);
        checkSex(personalDetails);
        checkPolicyBox();
    }

    /**
     * @param field   is the JSON field
     * @param value   is the value obtained by the related TextView
     * @param address is the JSONObject
     *                <p>
     *                Checks if the number fields are empty or not
     */
    private void checkNumber(String field, String value, JSONObject address) {
        if (!value.isEmpty()) {
            try {
                address.put(field, Integer.parseInt(value));
            } catch (JSONException e) {
                setErrorString(SERVERERROR);
            }
        } else setErrorString(EMPTYFIELDS);
    }

    /**
     * @param address is the jsonObject which will contain the address
     * @throws JSONException if something goes wrong
     *                       <p>
     *                       Puts all address parameters
     */
    private void setAddress(JSONObject address) throws JSONException {
        checkValue("street", street.getText().toString(), address);
        checkNumber("number", number.getText().toString(), address);
        checkValue("city", city.getText().toString(), address);
        checkNumber("cap", cap.getText().toString(), address);
        checkRegion(address);
        checkValue("state", country.getText().toString(), address);
    }

    /**
     * @param address is the JSONObject that must be filled
     * @throws JSONException when something goes wrong
     *
     * Checks if the insert region is equal to an italian one named in english, the whole app talks english
     */
    private void checkRegion(JSONObject address) throws JSONException {
        String reg = region.getText().toString().toLowerCase();
        ArrayList<String> regions = new ArrayList<>();
        Collections.addAll(regions,"lombardy","aosta valley", "liguria","piedmont", "emilia-romagna", "friuli-venezia-giulia",
                "trentino-south tyrol", "veneto","lazio", "marche", "tuscany","umbria", "abruzzo", "apulia","basilicata","calabria,",
                        "campania", "molise","sardinia","sicily");
        for(String region: regions)
            if(reg.equals(region)) {
                address.put("region", reg);
                return;
            }
        setErrorString(INCORRECTREGION);
    }

    /**
     * @param personalDetails is the JSON object
     * @throws JSONException if some problems occur
     *                       <p>
     *                       Checks if a sex is selected: if not it throws an error, if yes it adds the sex values to the JSON Object
     */
    private void checkSex(JSONObject personalDetails) throws JSONException {
        Boolean m = male.isChecked();
        Boolean f = female.isChecked();
        if (!f && !m)
            setErrorString(EMPTYFIELDS);
        else if (f && !m)
            personalDetails.put("sex", "female");
        else
            personalDetails.put("sex", "male");
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString, JsonObjectRequest request) {
        RegistrationActivity.this.runOnUiThread(() -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
            alertDialogBuilder.setMessage(errorString);
            alertDialogBuilder.setIcon(drawable.ic_exit);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.create().show();
        });
        request.cancel();
        dialog.dismiss();
        incompleteRequest = false;
        deleteParam();
    }

    /**
     * @param field           is the JSON field
     * @param value           is the value that must be add in the filed
     * @param personalDetails is the JSON object
     * @throws JSONException if some problems occur
     *                       <p>
     *                       checks if the String that must be insert are empty or not: if empty an error will be thrown, if
     *                       not it will be put in the JSON Object.
     */
    @SuppressLint("SetTextI18n")
    private void checkValue(String field, String value, JSONObject personalDetails) throws JSONException {
        if (value.isEmpty())
            setErrorString(EMPTYFIELDS);
        else if(field.equals("state") && !value.toLowerCase().equals("italy"))
            setErrorString(INCORRECTCOUNTRY);
        else if(value.matches("[^A-Za-z]"))
            setErrorString(PRESENCENUMBERORSYMBOLS);
        else
            personalDetails.put(field, value);
    }

    /**
     * @param personalDetails is the JSONObject in which the fiscal code must be put
     *                       <p>
     *                       Checks if the given fiscal code is correct for length and elements: if it is correct it will be put in the JSON;
     *                       if not an error string will be set.
     */
    @SuppressLint("SetTextI18n")
    private void checkFiscalCode(JSONObject personalDetails){
        String fc = fiscalCode.getText().toString().toLowerCase();
        if (fc.length() == 16) {
            String validFiscalCode = "[a-zA-Z]{6}"+"[0-9]{2}"+"[a-zA-Z]"+"[0-9]{2}"+"[a-zA-Z]"+"[0-9]{3}"+"[a-zA-Z]";
            Matcher matcher= Pattern.compile(validFiscalCode).matcher(fc);
            if(matcher.matches()) {
                try {
                    personalDetails.put("fiscalCode", fc);
                } catch (JSONException e) {
                    setErrorString(SERVERERROR);
                }
            }
            else setErrorString(INCORRECTFISCALCODE);
        } else setErrorString(INCORRECTFISCALCODE);

    }

    /**
     * @param credential is the JSONObject that must be filled
     *                       <p>
     *                       Puts the detected values in the credential object
     */
    private void setCredential(JSONObject credential){
        try {
            checkEmail(credential);
            checkPassword(credential);
        } catch (JSONException e) {
            setErrorString(SERVERERROR);
        }
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
        else
            setErrorString(WRONGEMAIL);
        }
        else setErrorString(EMPTYFIELDS);
    }

    /**
     * @param accountDetails is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *                       <p>
     *                       Checks if the password is in the length range.
     */
    private void checkPassword(JSONObject accountDetails) throws JSONException {
        String psw = password.getText().toString();
        if (psw.length() < 8 || psw.length() > 20) setErrorString(SHORTPASSWORD);
        else
            accountDetails.put("password", password.getText().toString());
    }

    /**
     * Checks if the policy box has been selected or not.
     */
    private void checkPolicyBox() {
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox) setErrorString(EMPTYFIELDS);
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
            case INCORRECTFISCALCODE:
                errorString = INCORRECTFISCALCODE;
                break;
            case WRONGEMAIL:
                errorString = WRONGEMAIL;
                break;
            case PRESENCENUMBERORSYMBOLS:
                errorString = PRESENCENUMBERORSYMBOLS;
                break;
            case INCORRECTCOUNTRY:
                errorString = INCORRECTCOUNTRY;
                break;
            case INCORRECTREGION:
                errorString = INCORRECTREGION;
        }
        incompleteRequest = true;
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(id.name);
        surname = findViewById(id.surname);
        fiscalCode = findViewById(id.fiscalCode);
        yearOfBirth = findViewById(id.dateOfBirth);
        male = findViewById(id.maleButton);
        female = findViewById(id.femaleButton);

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

    @Override
    public void run() {
        setAttributes();


        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressDialog();
                final JSONObject personalDetails = new JSONObject();
                JSONObject credential = new JSONObject();

                try {
                    setPersonalDetails(personalDetails);
                    setCredential(credential);
                } catch (JSONException e) {
                    setErrorString(SERVERERROR);
                }


                queue = Volley.newRequestQueue(RegistrationActivity.this);
                registrationReq = new JsonObjectRequest(Request.Method.POST, REGISTRATIONURL, credential,
                        response -> {
                        },
                        volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch (response.statusCode) {
                            case 200:
                                try {
                                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                    new AuthToken(json);
                                    personalDetails.put("userId", AuthToken.getId());
                                    System.out.println(json);

                                } catch (UnsupportedEncodingException | JSONException e) {
                                    setErrorString(SERVERERROR);
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
                if (incompleteRequest)
                    cancelReq(errorString, registrationReq);
                else
                    queue.add(registrationReq);
            }
        });
    }
}

