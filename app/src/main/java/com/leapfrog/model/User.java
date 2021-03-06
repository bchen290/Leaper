package com.leapfrog.model;

//File Name: User.java
//Purpose: This file handles everything regarding a user's profile
//Version: 1.0, Last Edit Date: 04/17/2020
//Author: Brian Chen
//Dependencies: N/A

/**
 * This class holds necessary components to allow the user to save and retrieve personal information
 */
public class User {
    private String nickname;
    private String profileUrl;
    private String userID;
    /**
     * Stores user information into private variables
     */
    public User(String nickname, String profileUrl, String userID) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.userID = userID;
    }
    /**
     * Gets user nickname
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Gets user profileURL
     */
    public String getProfileUrl() {
        return profileUrl;
    }
    /**
     * Gets userID
     */
    public String getUserID() {
        return userID;
    }
}
