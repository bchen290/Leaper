package com.leapfrog.util;

//File Name: SettingsPreference.java
//Purpose: This file handles everything regarding the settings screen
//Version: 1.0, Last Edit Date: 05/29/2020
//Author: Brian Chen
//Dependencies: N/A

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class SettingsPreference {
    private static SharedPreferences getSettingsPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getColor(Context context) {
        SharedPreferences sharedPreferences = getSettingsPreference(context);
        int red = sharedPreferences.getInt("redText", 0);
        int green = sharedPreferences.getInt("greenText", 0);
        int blue = sharedPreferences.getInt("blueText", 0);

        return Color.rgb(red, green, blue);
    }

    public static boolean getColorBlindMode(Context context) {
        return getSettingsPreference(context).getBoolean("colorBlind", false);
    }
}
