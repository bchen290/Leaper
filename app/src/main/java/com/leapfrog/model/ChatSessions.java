package com.leapfrog.model;

//File Name: ChatSessions.java
//Purpose: This file holds values and data regarding the chat screen
//Version: 1.0, Last Edit Date: 05/29/2020
//Author: Brian Chen
//Dependencies: N/A

import androidx.annotation.NonNull;

import java.util.Objects;
/**
 * This class holds necessary components to allow the user to format chats
 */
public class ChatSessions {
    public String chatID;
    private String nickname = "";

    public ChatSessions() {}
    /**
     * Stores chat components into private variables
     */
    public ChatSessions(String chatID, String nickname) {
        this.chatID = chatID;
        this.nickname = nickname;
    }
    /**
     * Set chatid
     */
    public void setChatID(String chatID){
        this.chatID = chatID;
    }
    /**
     * set nickname
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    /**
     * get nickname
     */
    @NonNull
    public String toString() {
        return nickname;
    }
    /**
     * Compare chats to see if they are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatSessions that = (ChatSessions) o;
        return Objects.equals(chatID, that.chatID) &&
                Objects.equals(nickname, that.nickname);
    }
    /**
     * Hash chatid for security
     */
    @Override
    public int hashCode() {
        return Objects.hash(chatID, nickname);
    }
}
