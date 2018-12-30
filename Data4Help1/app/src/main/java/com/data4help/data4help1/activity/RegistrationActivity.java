package com.data4help.data4help1.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.data4help.data4help1.R.*;


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
    private String errorString;
    private boolean incompleteRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.registration);

        setAttributes();
        registrationButton.setOnClickListener((v)-> {
                try {
                    setPersonalDetails(personalDetails);
                    setCredential(credential);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queue = Volley.newRequestQueue(RegistrationActivity.this);
                jobReq = new JsonObjectRequest(Request.Method.POST, url, credential,
                        token -> startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)),
                        volleyError -> VolleyLog.e("Error: ", volleyError.getMessage())){
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
                if(incompleteRequest)
                    cancelReq(errorString);
                else
                    queue.add(jobReq);

        });
        registrationButton.setOnClickListener((v) -> startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)));

    }



    /**
     * @param personalDetails is the object containing all personal details of the just created user
     */
    private void sendUserData(JSONObject personalDetails) {
        jobReq = new JsonObjectRequest(Request.Method.PUT, url, personalDetails,
                jsonObject -> {
                    System.out.println(jsonObject);
                    startActivity(new Intent(RegistrationActivity.this, MenuActivity.class)); },
                volleyError -> VolleyLog.e("Error: ", volleyError.getMessage())){
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
        if(!f && !m){
            errorString = "Some fields are empty. You must fill all of them!";
            incompleteRequest = true;
        }
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
        if(value.isEmpty()) {
            errorString = "Some fields are empty. You must fill all of them!";
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
                    errorString = "The fiscal code is incorrect!";
                    incompleteRequest = true;
                    return;
                }
            }
            personalDetails.put("fiscalCode", fc);
        }
        else{
            errorString = "The fiscal code is incorrect!";
            incompleteRequest = true;
        }

    }

    /**
     * @param accountDetails is the JSONObject that must be filled
     *
     * Puts the detected values in the accountDetails object
     */
    private void setCredential(JSONObject accountDetails) throws JSONException {

        checkValue("email", email.getText().toString(), accountDetails);
        checkPassword(accountDetails);
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
        if(psw.length() < 8 || psw.length() > 20){
            errorString = "The password must contain at least 8 elements.";
            incompleteRequest = true;
        }
        else
            accountDetails.put("password", password.getText().toString());
    }

    /**
     * Checks if the policy box has been selected or not.
     */
    @SuppressLint("SetTextI18n")
    private void checkPolicyBox(){
        boolean policyBox = acceptPolicy.isChecked();
        if (!policyBox){
            errorString = "Some fields are empty. You must fill all of them!";
            incompleteRequest = true;
        }
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        name = findViewById(id.name);
        surname = findViewById(id.surname);
        fiscalCode = findViewById(id.fiscalCode);
        dateOfBirth = findViewById(id.dateOfBirth);
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
        error = findViewById(id.error);
    }
}
