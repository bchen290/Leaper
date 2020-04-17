package com.leapfrog.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatSessions {
    @PrimaryKey
    @NonNull
    public String chatID;

    public String nickname;

    public void setChatID(@NonNull String chatID){
        this.chatID = chatID;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @NonNull
    @Override
    public String toString() {
        return nickname;
    }
}
