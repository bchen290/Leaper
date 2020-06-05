package com.leapfrog.util;

//File Name: Authentication.java
//Purpose: This file checks if a users profile info is authentic/not used prior to
//Version: 1.0, Last Edit Date: 05/15/2020
//Author: Brian Chen
//Dependencies: N/A

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class does the authentication for the app
 */
public class Authentication {
    /**
     * Gets the preferences for authentication
     * @param context context for getting authentication preference
     * @return returns the preference for authentication
     */
    private static SharedPreferences getAuthenticationPreference(Context context) {
        return context.getSharedPreferences("Authentication", MODE_PRIVATE);
    }

    /**
     * Checks if the context is authenticated
     * @param context the context to be authenticated
     * @return returns true if it is authenticated, false if not
     */
    public static boolean isAuthenticated(Context context) {
        return getAuthenticationPreference(context).getBoolean("IsAuthenticated", false);
    }

    /**
     * Retrives the username of the user
     * @param context context for username
     * @return returns the username
     */
    public static String getUsername(Context context) {
        return getAuthenticationPreference(context).getString("Username", "");
    }

    /**
     * Authenticates the username for the context
     * @param context context of the user
     * @param username the username
     */
    public static void authenticate(Context context, String username) {
        getAuthenticationPreference(context).edit()
                .putBoolean("IsAuthenticated", true)
                .putString("Username", username)
                .apply();
    }

    /**
     * Unauthenticates the user
     * @param context context of user
     */
    public static void unauthenticate(Context context) {
        getAuthenticationPreference(context).edit()
                .putBoolean("IsAuthenticated", false)
                .putString("Username", "")
                .apply();
    }
}
