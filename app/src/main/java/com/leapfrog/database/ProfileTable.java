package com.leapfrog.database;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;

@SuppressWarnings({"unused", "ConstantConditions"})
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
                .append("Email", email);

        collections.insertOne(profileDocument);
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
}
