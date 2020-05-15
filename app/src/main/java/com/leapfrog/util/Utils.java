package com.leapfrog.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;
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
}
