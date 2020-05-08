package com.leapfrog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leapfrog.database.LeaperDatabase;
import com.leapfrogandroid.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final TextView userName = findViewById(R.id.Username);
        final EditText password = findViewById(R.id.Password);

        final TextView registerLink = findViewById(R.id.lnkRegister);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });

        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener(v -> {
            boolean validUser = LeaperDatabase.getInstance(LoginActivity.this).verifyData(userName.getText().toString(), password.getText().toString());
        });
    }
}
