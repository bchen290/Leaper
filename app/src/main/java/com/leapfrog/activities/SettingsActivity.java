package com.leapfrog.activities;

import android.os.Bundle;

import com.leapfrog.fragment.SettingsFragment;
import com.leapfrog.util.BaseActivity;
import com.leapfrogandroid.R;

public class SettingsActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
