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

        collections.insertOne(profileDocument);
    }

    public void updateBio(String username, String bio) {
        collections.updateOne(eq("Username", username), new Document().append("Bio", bio));
    }

    public void updatePP(String username, String PP) {
        collections.updateOne(eq("Username", username), new Document().append("Profile Picture", PP));
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
        }

        return hasUser;
    }

    public String getName(String username){
        String full_name = "";

        Task<Document> result = collections.find(eq("Username", username)).first();

        while (!result.isComplete()) { }

        if (result.getResult() != null) {
            full_name = result.getResult().getString("First") + " " + result.getResult().getString("Last");
        }

        return full_name;
    }

    public String getBio(String username){
        String bio = "";

        Task<Document> result = collections.find(eq("Username", username)).first();

        while (!result.isComplete()) { }

        if (result.getResult() != null) {
            bio = result.getResult().getString("Bio");
        }

        return bio;
    }

    public String getPP(String username){
        String PP = "";

        Task<Document> result = collections.find(eq("Username", username)).first();

        while (!result.isComplete()) { }

        if (result.getResult() != null) {
            PP = result.getResult().getString("Profile Picture");
        }

        return PP;
    }
}
