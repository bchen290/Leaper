package com.leapfrog.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.Utils;
import com.leapfrogandroid.R;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final LinearLayout linearLayout = findViewById(R.id.registration_view);
        final EditText firstName = findViewById(R.id.First);
        final EditText lastName = findViewById(R.id.Last);
        final EditText username = findViewById(R.id.Username);
        final EditText password = findViewById(R.id.Password);
        final EditText passwordConfirmation = findViewById(R.id.Confirmation);
        final EditText email = findViewById(R.id.Email);

        Button submitRegistration = findViewById(R.id.btnRegister);
        submitRegistration.setOnClickListener(v -> {
            if (Utils.checkIfEditTextIsEmpty(firstName) || Utils.checkIfEditTextIsEmpty(lastName) || Utils.checkIfEditTextIsEmpty(username) || Utils.checkIfEditTextIsEmpty(password) || Utils.checkIfEditTextIsEmpty(email)) {
                Snackbar.make(linearLayout, "This field can not be blank", Snackbar.LENGTH_LONG);
            }

            if (password.getText().toString().equals(passwordConfirmation.getText().toString())){
                Snackbar.make(linearLayout, "Password doesn't match", Snackbar.LENGTH_LONG);
            }

            LeaperDatabase leaperDatabase = LeaperDatabase.getInstance(RegistrationActivity.this);

            if (leaperDatabase.getProfileTable().hasDuplicate(eq("Email", email.getText().toString()))) {
                Snackbar.make(linearLayout, "Email is already registered", Snackbar.LENGTH_LONG).show();
            } else if (leaperDatabase.getProfileTable().hasDuplicate(eq("Username", username.getText().toString()))) {
                Snackbar.make(linearLayout, "Username is already registered", Snackbar.LENGTH_LONG).show();
            } else {
                leaperDatabase.insertProfileData(firstName.getText().toString(),
                        lastName.getText().toString(), username.getText().toString(), password.getText().toString(),
                        email.getText().toString());

                Authentication.authenticate(this, username.getText().toString());

                Intent intent = new Intent(this, ConversationsActivity.class);
                startActivity(intent);
            }
        });

        TextView loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
