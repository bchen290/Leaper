package com.leapfrog.model;

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
