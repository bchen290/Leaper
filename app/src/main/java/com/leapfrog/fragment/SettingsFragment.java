package com.leapfrog.fragment;

//File Name: SettingsFragment.java
//Purpose: This file the actual settings screen
//Version: 1.0, Last Edit Date: 05/17/2020
//Author: Brian Chen
//Dependencies: N/A

import android.os.Bundle;
import android.widget.Toast;

import com.leapfrogandroid.R;

import java.util.Objects;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

/**
 * Holds the fragment for the settings screen
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    /**
     * Sets up settings screen from xml
     * @param savedInstanceState Saved application data
     * @param rootKey The key to use for SharedPreference
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        bindPreferenceSummaryToValue(Objects.requireNonNull(findPreference("fontSize")));
    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, newValue) -> {
        Toast.makeText(preference.getContext(), "Please restart app to apply changes", Toast.LENGTH_LONG).show();
        preference.setSummary(newValue.toString());
        return true;
    };

    /**
     * Listens to change in settings
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference.getKey().equals("fontSize")){
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getInt(preference.getKey(), 0));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }
    }
}
