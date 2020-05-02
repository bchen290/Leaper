package com.leapfrog.model;

public class Message {
    public long createdAt;
    private String message;
    private User sender;

    public String chatSessionID;
    public String chatSessionIDCurrent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getChatSessionID(){
        return chatSessionID;
    }

    public void setChatSessionID(String chatSessionID){
        this.chatSessionID = chatSessionID;
    }

    public String getChatSessionIDCurrent(){
        return chatSessionIDCurrent;
    }

    public void setChatSessionIDCurrent(String chatSessionIDCurrent){
        this.chatSessionIDCurrent = chatSessionIDCurrent;
    }
}
