package com.leapfrog.activities;

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
        final EditText passWord = findViewById(R.id.Password);

        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean real=LeaperDatabase.getInstance(LoginActivity.this).verifyData(userName.getText().toString(),passWord.getText().toString());

            }
        });
    }
}
