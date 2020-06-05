package com.leapfrog.util;

//File Name: User.java
//Purpose: This file checks for and handles everything involving device connectivity
//Version: 1.0, Last Edit Date: 05/14/2020
//Author: Brian Chen
//Dependencies: N/A

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class InternetConnectivity {
    private static SharedPreferences getInternetConnectivityPreference(Context context) {
        return context.getSharedPreferences("InternetConnectivity", MODE_PRIVATE);
    }

    public static void cacheInternetState(Context context, boolean hasInternet) {
        getInternetConnectivityPreference(context).edit().putBoolean("HasInternet", hasInternet).apply();
    }

    public static boolean checkCachedInternet(Context context) {
        return getInternetConnectivityPreference(context).getBoolean("HasInternet", false);
    }
}
