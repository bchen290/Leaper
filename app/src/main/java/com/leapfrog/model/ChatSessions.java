package com.leapfrog.model;


import java.util.Objects;

import androidx.annotation.NonNull;

public class ChatSessions {
    public String chatID;
    private String nickname = "";

    public ChatSessions() {}

    public ChatSessions(String chatID, String nickname) {
        this.chatID = chatID;
        this.nickname = nickname;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatSessions that = (ChatSessions) o;
        return Objects.equals(chatID, that.chatID) &&
                Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatID, nickname);
    }
}
