package com.leapfrog.util;


import com.leapfrog.model.User;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String fromUser(User user){
        return Utils.getGson().toJson(user);
    }

    @TypeConverter
    public static User toUser(String json){
        return Utils.getGson().fromJson(json, User.class);
    }
}
