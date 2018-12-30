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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import static com.data4help.data4help1.R.*;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView error;

    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users";

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
        error = findViewById(id.loginError);
        Button loginButton = findViewById(id.loginButton);
        View registerLink = findViewById(id.registerLink);

        loginButton.setOnClickListener((v)-> {
            setCredential();
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            jobReq = new JsonObjectRequest(Request.Method.GET, url, credential,
                    jsonObject -> System.out.print("hi"),
                    volleyError -> VolleyLog.e("Error: "+ volleyError.getMessage())){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    switch (response.statusCode) {
                        case 200:
                            System.out.println("funziona!!!");
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            break;
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

        registerLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MenuActivity.class)));
    }

    /**
     * set text in the error label and cancel the request
     */
    private void cancelReq(String errorString) {
        error.setText(errorString);
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
    @SuppressLint("SetTextI18n")
    private void checkValue(String field, String value,JSONObject personalDetails){
        if(value.isEmpty()){
            errorString = "Some fields are empty. You must fill all of them!";
            incompleteRequest = true;
        }
        else {
            try {
                personalDetails.put(field, value);
            } catch (JSONException e) {
                errorString = "Server problem. Try again later.";
                incompleteRequest = true;
            }
        }
    }
}
