package com.leapfrog.util;

//File Name: User.java
//Purpose: This file checks for and handles everything involving device connectivity
//Version: 1.0, Last Edit Date: 05/14/2020
//Author: Brian Chen
//Dependencies: N/A

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Checks the internet connectivity
 */
public class InternetConnectivity {
    /**
     * Gets the internet connectivity preference
     * @param context context for preference
     * @return returns the internet connectivity preference
     */
    private static SharedPreferences getInternetConnectivityPreference(Context context) {
        return context.getSharedPreferences("InternetConnectivity", MODE_PRIVATE);
    }

    /**
     * Caches the current internet state
     * @param context context to be cached
     * @param hasInternet boolean for if user has internet connectivity
     */
    public static void cacheInternetState(Context context, boolean hasInternet) {
        getInternetConnectivityPreference(context).edit().putBoolean("HasInternet", hasInternet).apply();
    }

    /**
     * checks the cached internet  preference
     * @param context context to get the cached internet
     * @return returns the cached internet preferences
     */
    public static boolean checkCachedInternet(Context context) {
        return getInternetConnectivityPreference(context).getBoolean("HasInternet", false);
    }
}
