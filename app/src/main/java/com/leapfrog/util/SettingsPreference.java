package com.leapfrog.util;

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
}
