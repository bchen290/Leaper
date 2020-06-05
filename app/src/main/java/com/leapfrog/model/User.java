package com.leapfrog.model;
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
