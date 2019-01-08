package com.data4help.d4h_thirdparty.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.data4help.d4h_thirdparty.Config.*;
import static com.data4help.d4h_thirdparty.R.*;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private String errorString;
    private boolean incompleteRequest = false;
    private JSONObject credential;
    private JsonObjectRequest jobReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login);

        email = findViewById(id.email);
        password = findViewById(id.password);
        Button loginButton = findViewById(id.loginButton);
        View registerLink = findViewById(id.registerLink);

        loginButton.setOnClickListener((v) -> {
                setCredential();
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                jobReq = new JsonObjectRequest(Request.Method.POST, LOGINURL, credential,
                        jsonObject -> System.out.print("hi"),
                        volleyError -> VolleyLog.e("Error: " + volleyError.getMessage())) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch (response.statusCode) {
                            case 200:
                                String json = null;
                                try {
                                    json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                new AuthToken(json);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                break;
                            //TODO
                            case 400:
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
                    cancelReq(errorString);
                else
                    queue.add(jobReq);
        });

        registerLink.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, HomeActivity.class)));

    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        LoginActivity.this.runOnUiThread(() -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage(errorString);
            alertDialogBuilder.setIcon(drawable.ic_exit);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.create().show();
        });
        incompleteRequest = false;
        jobReq.cancel();
    }

    /**
     * Sets the attributes in the JSONObject
     */
    private void setCredential() {
        credential = new JSONObject();
        checkValue("email", email.getText().toString(), credential);
        checkValue("password",password.getText().toString(), credential);
    }

    /**
     * @param field is the JSON field
     * @param value is the value that must be add in the filed
     * @param personalDetails is the JSON object
     *
     * checks if the String that must be insert are empty or not: if empty an error will be thrown, if
     * not it will be put in the JSON Object.
     */
    private void checkValue(String field, String value,JSONObject personalDetails){
        if(value.isEmpty()) {
            System.out.println("funziona?");
            setErrorString(EMPTYFIELDS);
        }
        else {
            if(field.equals("password") && (value.length()< 8 || value.length() > 20))
                setErrorString(SHORTPASSWORD);
            else if(field.equals("email") && !value.contains("@") && !value.contains(".") )
                setErrorString(WRONGEMAIL);
            try {
                personalDetails.put(field, value);
            } catch (JSONException e) {
                setErrorString(SERVERERROR);
            }
        }
    }

    /**
     * @param error is the error string
     *
     *              Sets the error label
     */
    private void setErrorString(String error) {
        switch (error) {
            case EMPTYFIELDS:
                errorString = EMPTYFIELDS;
                break;
            case SERVERERROR:
                errorString = SERVERERROR;
                break;
            case SHORTPASSWORD:
                errorString = SHORTPASSWORD;
                break;
            case WRONGEMAIL:
                errorString = WRONGEMAIL;
        }incompleteRequest = true;
    }
}

