package com.leapfrog.util;

//File Name: Authentication.java
//Purpose: This file checks if a users profile info is authentic/not used prior to
//Version: 1.0, Last Edit Date: 05/15/2020
//Author: Brian Chen
//Dependencies: N/A

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Authentication {
    private static SharedPreferences getAuthenticationPreference(Context context) {
        return context.getSharedPreferences("Authentication", MODE_PRIVATE);
    }

    public static boolean isAuthenticated(Context context) {
        return getAuthenticationPreference(context).getBoolean("IsAuthenticated", false);
    }

    public static String getUsername(Context context) {
        return getAuthenticationPreference(context).getString("Username", "");
    }

    public static void authenticate(Context context, String username) {
        getAuthenticationPreference(context).edit()
                .putBoolean("IsAuthenticated", true)
                .putString("Username", username)
                .apply();
    }

    public static void unauthenticate(Context context) {
        getAuthenticationPreference(context).edit()
                .putBoolean("IsAuthenticated", false)
                .putString("Username", "")
                .apply();
    }
}
