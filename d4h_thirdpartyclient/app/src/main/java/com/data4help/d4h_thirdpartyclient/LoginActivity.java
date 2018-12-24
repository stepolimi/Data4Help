package com.data4help.d4h_thirdpartyclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

        private EditText email;
        private EditText password;
        private String url = "http://jsonplaceholder.typicode.com/posts";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        View registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jObject = new JSONObject();
                try {
                    jObject.put("email", email.getText().toString());
                    jObject.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, url, jObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) { System.out.print("hi"); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) { VolleyLog.e("Error: ", volleyError.getMessage()); }});
                queue.add(jobReq);
            }
        });


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)); }
        });
    }
}
