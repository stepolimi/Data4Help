package com.data4help.d4h_thirdparty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";
    private TextView error;

    private JsonObjectRequest jobReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setAttributes();
        registrationButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick (View v){
                JSONObject personalDetails = new JSONObject();
                JSONObject credential = new JSONObject();

                try {
                    setPersonalDetails(personalDetails);
                    setCredential(credential);
                    System.out.println(credential);
                    System.out.println("string : " + credential.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                jobReq = new JsonObjectRequest(Request.Method.POST, url, credential,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("ci entra??");
                                VolleyLog.v("Response:%n %s", response.toString()); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("bo");
                                VolleyLog.e("Error: "+ volleyError.getMessage()); }});

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
        checkValue("typeOfSociety", typeSociety.getText().toString(), personalDetails);
        checkValue("pIva", pIva.getText().toString(), personalDetails);
        JSONObject address = new JSONObject();
        setAddress(address);
        personalDetails.put("address", address);

        checkPolicyBox(personalDetails);
        checkFiscalCode(personalDetails);
    }

    /**
     * @param address is the jsonObject which will contain the address
     * @throws JSONException if something goes wrong
     *
     * Puts all address parameters
     */
    private void setAddress(JSONObject address) throws JSONException {
        checkValue("street", street.getText().toString(), address);
        checkValue("number", number.getText().toString(), address);
        checkValue("city", city.getText().toString(), address);
        checkValue("cap", cap.getText().toString(), address);
        checkValue("region", region.getText().toString(), address);
        checkValue("country", country.getText().toString(), address);
    }


    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorText) {
        error.setText(errorText);
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
        if(psw.length() < 8 || psw.length() > 20)
            cancelReq("The password must contain at least 8 elements.");

        else
            credential.put("password", password.getText().toString());
    }

    /**
     * @param credential is the JSONObject in which the fiscal code must be put
     * @throws JSONException if some problems occur
     *
     * Checks if the policy box has been selected or not.
     */
    @SuppressLint("SetTextI18n")
    private void checkPolicyBox(JSONObject credential) throws JSONException {
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox)
            cancelReq("Some fields are empty. You must fill all of them!");
        else
            credential.put("acceptedPolicy", true);
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(R.id.name);
        typeSociety = findViewById(R.id.typeSociety);
        fiscalCode = findViewById(R.id.fiscalCode);
        pIva = findViewById(R.id.pIva);
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

