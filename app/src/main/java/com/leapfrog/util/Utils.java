package com.leapfrog.util;

//File Name: Utils.java
//Purpose: This file contains all the utilities used in this project
//Version: 1.0, Last Edit Date: 05/17/2020
//Author: Brian Chen, Omri Chashper
//Dependencies: N/A

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;
import static android.content.Context.MODE_PRIVATE;

public class Utils {
    /**
     * Checks if network is available
     * @param context Application context
     * @return Whether there is network or not
     */
    public static boolean hasNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Helper method to check if edittext is empty
     * @param editText The edittext to check
     * @return Whether the edittext is empty
     */
    public static boolean checkIfEditTextIsEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    /**
     * Encrypt password
     * @param password The password to encrypt
     * @return The encrypted password
     */
    public static String passwordEncryption(String password) {
        String key = "key";
        try {
            return AESCrypt.encrypt(key, password);
        }
        catch (GeneralSecurityException e) {
            //handle error
        }
        return password;
    }

    /**
     * Helper method to hide keyboard
     * @param activity The activity to hide keyboard from
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Helper method to change font size
     * @param context The Application context
     * @return The new application context
     */
    static Context adjustFontSize(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.fontScale = (float)(1 + PreferenceManager.getDefaultSharedPreferences(context).getInt("fontSize", 0) / 10.0);
        return context.createConfigurationContext(configuration);
    }
}
