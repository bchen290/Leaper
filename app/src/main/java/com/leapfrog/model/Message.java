package com.leapfrog.model;

//File Name: Message.java
//Purpose: This file handles everything regarding a message
//Version: 1.0, Last Edit Date: 05/14/2020
//Author: Brian Chen
//Dependencies: N/A

/**
 * This class holds necessary components to allow the user to format messages
 */
public class Message {
    private long createdAt;
    private String message;
    private User sender;
    private User receiver;
    /**
     * Stores message components into private variables
     */
    public Message() {
        createdAt = 0L;
        message = "";
        sender = null;
        receiver = null;
    }
    /**
     * Stores message components into private variables
     */
    public Message(long createdAt, String message, User sender, User receiver) {
        this.createdAt = createdAt;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }
    /**
     * Get message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Set message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Get sender
     */
    public User getSender() {
        return sender;
    }
    /**
     * Set message
     */
    public void setSender(User sender) {
        this.sender = sender;
    }
    /**
     * Get timestamp for when the message was created
     */
    public long getCreatedAt() {
        return createdAt;
    }
    /**
     * Set timestamp for when the message was created
     */
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * Get receiver
     */
    public User getReceiver() {
        return receiver;
    }
    /**
     * Set receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
