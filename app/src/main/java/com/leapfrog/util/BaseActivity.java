package com.leapfrog.util;

import android.content.Context;
import android.os.Bundle;

import com.leapfrogandroid.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Utils.adjustFontSize(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.adjustFontSize(this);
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupToolbarNoUp() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setupToolbar(String title) {
        setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setupToolbarNoUp(String title) {
        setupToolbarNoUp();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
