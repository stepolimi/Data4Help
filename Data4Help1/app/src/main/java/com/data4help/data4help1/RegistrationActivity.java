package com.data4help.data4help1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText fiscalCode;
    private EditText dateOfBirth;
    private EditText street;
    private EditText number;
    private EditText city;
    private EditText cap;
    private EditText region;
    private EditText country;
    private RadioButton male;
    private RadioButton female;
    private CheckBox acceptPolicy;

    private EditText email;
    private EditText password;

    private Button registrationButton;

    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";
    private TextView error;

    private JsonObjectRequest jobReq;
    private RequestQueue queue;

    private JSONObject personalDetails = new JSONObject();
    private JSONObject credential = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setAttributes();
      /*  registrationButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick (View v){
                try {
                    setPersonalDetails(personalDetails);
                    setCredential(credential);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queue = Volley.newRequestQueue(RegistrationActivity.this);
                jobReq = new JsonObjectRequest(Request.Method.POST, url, credential,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject token) {
                                startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) { VolleyLog.e("Error: ", volleyError.getMessage()); }}){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch(response.statusCode){
                            case 200:
                                try {
                                    personalDetails.put("authId", "credential id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sendUserData(personalDetails);
                                System.out.println("funziona");
                                break;
                            default:
                                break;
                        }
                        finish();
                        return super.parseNetworkResponse(response);
                    }
                };
                queue.add(jobReq);
            }

        });*/
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)); }
        });

    }



    /**
     * @param personalDetails is the object containing all personal details of the just created user
     */
    private void sendUserData(JSONObject personalDetails) {
        jobReq = new JsonObjectRequest(Request.Method.PUT, url, personalDetails,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        System.out.println(jsonObject);
                        startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)); }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) { VolleyLog.e("Error: ", volleyError.getMessage()); }}){
        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            switch(response.statusCode){
                case 200:
                    System.out.println("funziona!!!");
                    startActivity(new Intent(RegistrationActivity.this, MenuActivity.class));
                    break;
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
        queue.add(jobReq);
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
        dateOfBirth.getText().clear();
        street.getText().clear();
        number.getText().clear();
        city.getText().clear();
        cap.getText().clear();
        country.getText().clear();
        region.getText().clear();
    }

    /**
     * @param personalDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the personalDetails object
     */
    @SuppressLint("SetTextI18n")
    private void setPersonalDetails(JSONObject personalDetails) throws JSONException {
        checkValue("name", name.getText().toString(), personalDetails);
        checkValue("surname", surname.getText().toString(), personalDetails);
        checkValue("dateOfBirth", dateOfBirth.getText().toString(), personalDetails);
        JSONObject address = new JSONObject();
        setAddress(address);
        personalDetails.put("address", address);

        checkFiscalCode(personalDetails);
        checkSex(personalDetails);
        checkPolicyBox(personalDetails);
    }

    private void setAddress(JSONObject address) throws JSONException {
        checkValue("street", street.getText().toString(), address);
        checkValue("number", number.getText().toString(), address);
        checkValue("city", city.getText().toString(), address);
        checkValue("cap", cap.getText().toString(), address);
        checkValue("region", region.getText().toString(), address);
        checkValue("country", country.getText().toString(), address);
    }


    /**
     * @param personalDetails is the JSON object
     * @throws JSONException if some problems occur
     *
     * Checks if a sex is selected: if not it throws an error, if yes it adds the sex values to the JSON Object
     */
    private void checkSex(JSONObject personalDetails) throws JSONException{
        Boolean m = male.isChecked();
        Boolean f = female.isChecked();
        if(!f && !m)
            cancelReq("Some fields are empty. You must fill all of them!");
        else{
            personalDetails.put("male", m);
            personalDetails.put("female", f);
        }
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText) {
        error.setText(errorText);
        jobReq.cancel();
        deleteParam();
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
            cancelReq("Some fields are empty. You must fill all of them!");
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
                    cancelReq("The fiscal code is incorrect!");
                    return;
                }
            }
            personalDetails.put("fiscalCode", fc);
        }
        else
            cancelReq("The fiscal code is incorrect!");

    }

    /**
     * @param accountDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the accountDetails object
     */
    private void setCredential(JSONObject accountDetails) throws JSONException {

        checkValue("email", email.getText().toString(), accountDetails);
        checkPassword(accountDetails);

        System.out.println(accountDetails);
    }

    /**
     * @param accountDetails is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the password is in the length range.
     */
    @SuppressLint("SetTextI18n")
    private void checkPassword(JSONObject accountDetails) throws JSONException {
        String psw = password.getText().toString();
        if(psw.length() < 8 || psw.length() > 20)
            cancelReq("The password must contain at least 8 elements.");

        else
            accountDetails.put("password", password.getText().toString());
    }

    /**
     * @param accountDetails is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the policy box has been selected or not.
     */
    @SuppressLint("SetTextI18n")
    private void checkPolicyBox(JSONObject accountDetails) throws JSONException {
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox)
            cancelReq("Some fields are empty. You must fill all of them!");
        else
            accountDetails.put("acceptedPolicy", true);
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        fiscalCode = findViewById(R.id.fiscalCode);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        male = findViewById(R.id.maleButton);
        female = findViewById(R.id.femaleButton);

        street = findViewById(R.id.street);
        number = findViewById(R.id.number);
        city = findViewById(R.id.city);
        cap = findViewById(R.id.postalCode);
        region = findViewById(R.id.region);
        country = findViewById(R.id.country);

        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        acceptPolicy = findViewById(R.id.acceptPolicy);

        registrationButton = findViewById(R.id.registrationButton);
        error = findViewById(R.id.error);
    }
}
