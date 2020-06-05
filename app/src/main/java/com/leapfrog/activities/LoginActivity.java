package com.leapfrog.activities;

//File Name: LoginActivity.java
//Purpose: This file allows users to Log In
//Version: 1.0, Last Edit Date: 05/17/2020
//Author: Brian Chen, Omri Chashper, Sarar Aseer
//Dependencies: AndroidManifest.xml, activity_login.xml, LeaperDatabase.java, BaseActivity.java, Utils.java, Authentication.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.util.BaseActivity;
import com.leapfrog.util.Utils;
import com.leapfrog.util.Authentication;
import com.leapfrogandroid.R;

/**
 * Holds all the components required to allow the user to login or redirect to registration activity
 */
public class LoginActivity extends BaseActivity {
    /**
     * Creates layout
     * Set on click listener to allow login or redirect to {@link RegistrationActivity}
     * @param savedInstanceState Information saved about current activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView username = findViewById(R.id.Username);
        final EditText password = findViewById(R.id.Password);
        final TextView wrong = findViewById(R.id.wrong);
        final TextView registerLink = findViewById(R.id.lnkRegister);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });


        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener(v -> {
            Utils.hideKeyboard(this);

            boolean validUser = LeaperDatabase.getInstance(LoginActivity.this).verifyData(username.getText().toString(), Utils.passwordEncryption(password.getText().toString()));

            if (validUser) {
                Authentication.authenticate(this, username.getText().toString());
                Intent intent = new Intent(this, ConversationsActivity.class);
                startActivity(intent);
            }else{
                wrong.setText(R.string.wrong_username_or_password);
                wrong.setTextSize(25);
            }
        });
    }
}
