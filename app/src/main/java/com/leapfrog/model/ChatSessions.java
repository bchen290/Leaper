package com.leapfrog.model;


public class ChatSessions {
    public String chatID;
    public String nickname;

    public void setChatID(String chatID){
        this.chatID = chatID;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String toString() {
        return nickname;
    }
}
