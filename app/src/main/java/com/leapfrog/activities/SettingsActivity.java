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
/**
 * This class holds all the necessary components to allow the user to enter settings page
 */
public class SettingsActivity extends BaseActivity {
    /**
     * Sets up Settings activity
     * Initialize toolbar and settings layout
     * Initialize fragment manager support
     * @param savedInstanceState Information saved about current activity
     */
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
