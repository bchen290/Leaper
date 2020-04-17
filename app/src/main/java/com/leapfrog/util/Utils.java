package com.leapfrog.util;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    private static Gson gson;

    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static Gson getGson() {
        if(gson == null){
            gson = new Gson();
        }

        return gson;
    }
}
