package com.leapfrog.model;

public class Message {
    private long createdAt;
    private String message;
    private User sender;
    private User receiver;

    public Message() {
        createdAt = 0L;
        message = "";
        sender = null;
        receiver = null;
    }

    public Message(long createdAt, String message, User sender, User receiver) {
        this.createdAt = createdAt;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
