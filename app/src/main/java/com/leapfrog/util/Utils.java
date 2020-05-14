package com.leapfrog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static boolean hasNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean checkIfEditTextIsEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    public static boolean checkCachedInternet(Context context) {
        return context.getSharedPreferences("InternetConnectivity", MODE_PRIVATE).getBoolean("HasInternet", true);
    }
}
