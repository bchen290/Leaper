package com.leapfrog.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class UserWithMessage {
    @Embedded User user;

    @ColumnInfo(name = "chatsessions_chatid")
    Message message;
}
