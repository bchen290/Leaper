package com.leapfrog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class Utils {
    public boolean hasNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean checkIfEditTextIsEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }
    public static String passwordEncryption(String password){
        String key = "key";
        try {
            return AESCrypt.encrypt(key, password);
        }catch (GeneralSecurityException e){
            //handle error
        }
        return password;
    }
}
