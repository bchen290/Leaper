package com.leapfrog.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrogandroid.R;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final EditText firstName = findViewById(R.id.First);
        final EditText lastName = findViewById(R.id.Last);
        final EditText userName = findViewById(R.id.Username);
        final EditText passWord = findViewById(R.id.Password);
        final EditText email = findViewById(R.id.Email);

        Button submitRegistration = findViewById(R.id.btnRegister);
        submitRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaperDatabase.getInstance(RegistrationActivity.this).insertProfileData(firstName.getText().toString(),
                        lastName.getText().toString(), userName.getText().toString(), passWord.getText().toString(),
                        email.getText().toString());
            }
        });
    }
}
