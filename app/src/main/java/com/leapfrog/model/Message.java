package com.leapfrog.model;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ChatSessions.class,
                                  parentColumns = "chatID",
                                  childColumns = "chatSessionID"))
public class Message {
    @ColumnInfo(name = "timestamp")
    @PrimaryKey
    @NotNull
    public long createdAt;

    @ColumnInfo(name = "message")
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
