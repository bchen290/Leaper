package com.leapfrog.model;

//File Name: User.java
//Purpose: This file handles everything regarding a user's profile
//Version: 1.0, Last Edit Date: 04/17/2020
//Author: Brian Chen
//Dependencies: N/A

public class User {
    private String nickname;
    private String profileUrl;
    private String userID;

    public User(String nickname, String profileUrl, String userID) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.userID = userID;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getUserID() {
        return userID;
    }
}
