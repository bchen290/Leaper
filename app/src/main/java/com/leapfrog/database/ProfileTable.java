package com.leapfrog.database;

import static com.mongodb.client.model.Filters.*;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

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

    void deleteAll() {
        //collections.deleteMany(new Document());
    }

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

    public void updateBio(String username, String bio) {
        currentUserDocument.put("Bio", bio);
        collections.updateOne(eq("Username", username), currentUserDocument);
    }

    public void updateProfilePicture(String username, String profilePicture) {
        currentUserDocument.put("Profile Picture", profilePicture);
        collections.updateOne(eq("Username", username), currentUserDocument);
    }


    public boolean hasDuplicate(Bson filter){
        boolean hasDuplicate = false;

         Task result = collections.find(filter).first();

         while (!result.isComplete()) { }

        if (result.getResult() != null) {
            hasDuplicate = true;
        }

         return hasDuplicate;
    }

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

    public String getName(String username){
        return currentUserDocument.getString("First") + " " + currentUserDocument.getString("Last");
    }

    public String getBio(String username){
        return currentUserDocument.getString("Bio");
    }

    public String getProfilePicture(String username){
        return currentUserDocument.getString("Profile Picture");
    }

    public void getProfile(String username) {
        if (currentUserDocument == null) {
            Task result = collections.find(eq("Username", username)).first();

            while (!result.isComplete()) { }

            if (result.getResult() != null) {
                currentUserDocument = (Document) result.getResult();
            }
        }
    }
}
