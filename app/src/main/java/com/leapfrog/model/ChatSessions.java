package com.leapfrog.model;


import androidx.annotation.NonNull;

public class ChatSessions {
    public String chatID;
    private String nickname = "";

    public void setChatID(String chatID){
        this.chatID = chatID;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @NonNull
    public String toString() {
        return nickname;
    }
}
