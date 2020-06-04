package com.leapfrog.activities;

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
