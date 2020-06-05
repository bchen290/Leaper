package com.leapfrog.activities;

//File Name: SettingsActivity.java
//Purpose: This file allows user to handle their proffered account settings
//Version: 1.0, Last Edit Date: 05/29/2020
//Author: Brian Chen
//Dependencies: AndroidManifest.xml, activity_settings.xml, SettingsFragment.java, BaseActivity.java

import android.os.Bundle;

import com.leapfrog.fragment.SettingsFragment;
import com.leapfrog.util.BaseActivity;
import com.leapfrogandroid.R;

public class SettingsActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbar("Settings");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
