package com.data4help.data4help1.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data4help.data4help1.AuthToken;

import static com.data4help.data4help1.Config.EMPTYFIELDS;
import static com.data4help.data4help1.Config.LOGINURL;
import static com.data4help.data4help1.Config.SERVERERROR;
import static com.data4help.data4help1.R.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView error;

    private String errorString;
    private boolean incompleteRequest = false;
    private JsonObjectRequest loginReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.login);

        email = findViewById(id.email);
        password = findViewById(id.password);
        error = findViewById(id.loginError);
        Button loginButton = findViewById(id.loginButton);
        View registerLink = findViewById(id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject credential = new JSONObject();
                        setCredential(credential);
                        System.out.println(credential.toString());
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        loginReq = new JsonObjectRequest(Request.Method.POST, LOGINURL, credential,
                                response -> {
                                },
                                volleyError -> {
                                }) {
                            @Override
                            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                                switch (response.statusCode) {
                                    case 200:
                                        try {
                                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                            new AuthToken(json);
                                            System.out.println(json);
                                        } catch (UnsupportedEncodingException e) {
                                            errorString = SERVERERROR;
                                            incompleteRequest = true;
                                        }
                                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                        break;
                                    //TODO
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
                            cancelReq(errorString);
                        else
                            queue.add(loginReq);
                    }
                }).start();
            }
        });

        registerLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        error.setText(errorString);
        loginReq.cancel();
    }

    /**
     * Sets the attributes in the JSONObject
     */
    private void setCredential(JSONObject credential) {
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
    @SuppressLint("SetTextI18n")
    private void checkValue(String field, String value,JSONObject personalDetails){
        if(value.isEmpty()){
            errorString = EMPTYFIELDS;
            incompleteRequest = true;
        }
        else {
            try {
                personalDetails.put(field, value);
            } catch (JSONException e) {
                errorString = SERVERERROR;
                incompleteRequest = true;
            }
        }
    }
}
