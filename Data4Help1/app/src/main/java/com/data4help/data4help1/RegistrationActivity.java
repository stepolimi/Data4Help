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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText fiscalCode;
    private EditText dateOfBirth;
    private EditText address;
    private RadioButton male;
    private RadioButton female;

    private EditText email;
    private EditText password;
    private CheckBox acceptPolicy;

    private Button registrationButton;

    private String url = " http://example.com";
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setAttributes();
        registrationButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick (View v){
                JSONArray registrationData = new JSONArray();
                JSONObject personalDetails = new JSONObject();
                JSONObject accountDetails = new JSONObject();

                registrationData.put(personalDetails);
                registrationData.put(accountDetails);

                try {
                    setPersonalDetails(personalDetails);
                    setAccountDetails(accountDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, url, registrationData,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray registrationData) {
                                startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) { VolleyLog.e("Error: ", volleyError.getMessage()); }});
                queue.add(jobReq);
            }

        });

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
        checkValue("address", address.getText().toString(), personalDetails);

        checkFiscalCode(personalDetails);
        checkSex(personalDetails);
    }


    /**
     * @param personalDetails is the JSON object
     * @throws JSONException if some problems occur
     *
     * Checks if a sex is selected: if not it throws an error, if yes it adds the sex values to the JSON Object
     */
    @SuppressLint("SetTextI18n")
    private void checkSex(JSONObject personalDetails) throws JSONException{
        Boolean m = male.isChecked();
        Boolean f = female.isChecked();
        if(!f && !m)
            error.setText("Some fields are empty. You must fill all of them!");
        else{
            personalDetails.put("male", m);
            personalDetails.put("female", f);
        }
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
        if(value.length() == 0 )
            error.setText("Some fields are empty. You must fill all of them!");
        else {
            error.setText("");
            personalDetails.put(field, value);
        }
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
        if( fc.length() != 16 ) {
            error.setText("The fiscal code is incorrect!");
            return;
        }
        String fc2 = fc.toUpperCase();
        for( int i = 0; i < fc2.length(); i++ ){
            int c = fc2.charAt(i);
            if( ! ( c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' ) ) {
                error.setText("The fiscal code is incorrect!");
                return;
            }
        }
        error.setText("");
        personalDetails.put("fiscalCode", fc);

    }

    /**
     * @param accountDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the accountDetails object
     */
    private void setAccountDetails(JSONObject accountDetails) throws JSONException {

        checkValue("email", email.getText().toString(), accountDetails);
        checkPassword(accountDetails);
        checkPolicyBox(accountDetails);
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
            error.setText("The password must contain at least 8 elements.");
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
            error.setText("Some fields are empty. You must fill all of them!");
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
        address = findViewById(R.id.address);
        male = findViewById(R.id.maleButton);
        female = findViewById(R.id.femaleButton);

        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        acceptPolicy = findViewById(R.id.acceptPolicy);

        registrationButton = findViewById(R.id.registrationButton);
        error = findViewById(R.id.error);
    }
}
