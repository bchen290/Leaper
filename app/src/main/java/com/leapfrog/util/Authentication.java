package com.leapfrog.util;

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
}
