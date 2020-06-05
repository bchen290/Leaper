package com.leapfrog.database;

//File Name: ProfileTable.java
//Purpose: This file creates and stores information into the Profile Table in MongoDB
//Version: 1.0, Last Edit Date: 05/29/2020
//Author: Brian Chen, Omri Chashper, Sarar Aseer
//Dependencies: N/A

import static com.mongodb.client.model.Filters.*;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;
/**
 * Holds all the components required to allow the user to save an account
 */
@SuppressWarnings({"unused"})
public class ProfileTable {
    private RemoteMongoDatabase mongoDatabase;
    private RemoteMongoCollection<Document> collections;
    private StitchAppClient remoteMongoClient;

    private Document currentUserDocument;

    ProfileTable(RemoteMongoDatabase mongoDatabase, StitchAppClient remoteMongoClient) {
        this.mongoDatabase = mongoDatabase;
        this.remoteMongoClient = remoteMongoClient;

        collections = mongoDatabase.getCollection("Profile");
    }

    /**
     * Creates new account and stores data in database
     */
    void insertProfileData(String first, String last, String username, String password, String email){
        final Document profileDocument = new Document("First", first)
                .append("Last", last)
                .append("Username", username)
                .append("Password", password)
                .append("Email", email)
                .append("Bio", "")
                .append("Profile Picture", "");

        currentUserDocument = profileDocument;
        collections.insertOne(profileDocument);
    }
    /**
     * Updates existing account information in database
     */
    public void updateBio(String username, String bio) {
        currentUserDocument.put("Bio", bio);
        collections.updateOne(eq("Username", username), currentUserDocument);
    }

    /**
     * Updates existing image in database
     */
    public void updateProfilePicture(String username, String profilePicture) {
        currentUserDocument.put("Profile Picture", profilePicture);
        collections.updateOne(eq("Username", username), currentUserDocument);
    }

    /**
     * Checks for duplicates
     */
    public boolean hasDuplicate(Bson filter){
        boolean hasDuplicate = false;

         Task result = collections.find(filter).first();

         while (!result.isComplete()) { }

        if (result.getResult() != null) {
            hasDuplicate = true;
        }

         return hasDuplicate;
    }

    /**
     * Checks if account already exists within database
     */
    boolean verifyData(String username, String password){
        boolean hasUser = false;

        Task result = collections.find(and(eq("Username", username), eq("Password", password))).first();

        while (!result.isComplete()) { }

        if (result.getResult() != null) {
            hasUser = true;
            currentUserDocument = (Document) result.getResult();
        }

        return hasUser;
    }

    /**
     * Gets a name based on username from database
     */
    public String getName(String username){
        return currentUserDocument.getString("First") + " " + currentUserDocument.getString("Last");
    }

    /**
     * Gets user information based on username from database
     */
    public String getBio(String username){
        return currentUserDocument.getString("Bio");
    }

    /**
     * Gets a name based on username from database
     */
    public String getProfilePicture(String username){
        return currentUserDocument.getString("Profile Picture");
    }
    /**
     * Gets an existing profile based on username from database
     */
    public void getProfile(String username) {
        if (currentUserDocument == null) {
            Task result = collections.find(eq("Username", username)).first();

            while (!result.isComplete()) { }

            if (result.getResult() != null) {
                currentUserDocument = (Document) result.getResult();
            }
        }
    }

    public void deleteAll() {
    }
}
