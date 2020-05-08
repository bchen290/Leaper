package com.leapfrog.database;

import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
class ProfileTable {
    private RemoteMongoDatabase mongoDatabase;
    private RemoteMongoCollection<Document> collections;
    private StitchAppClient remoteMongoClient;

    ProfileTable(RemoteMongoDatabase mongoDatabase, StitchAppClient remoteMongoClient) {
        this.mongoDatabase = mongoDatabase;
        this.remoteMongoClient = remoteMongoClient;

        collections = mongoDatabase.getCollection("Profile");
    }

    void deleteAll() {
        collections.deleteMany(new Document());
    }

    void insertProfileData(String first, String last, String username, String password, String email){
        final Document profileDocument = new Document("First", first)
                .append("Last", last)
                .append("Username", username)
                .append("Password", password)
                .append("Email", email);

        remoteMongoClient.getAuth().loginWithCredential(new AnonymousCredential())
                .continueWithTask(task -> collections.updateOne(null, profileDocument, new RemoteUpdateOptions().upsert(true)))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("ProfileTable", "Inserted profile document");
                    }
                });
    }
}
