package com.leapfrog.database;

import android.database.Cursor;
import android.util.Log;


import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;


import static com.mongodb.client.model.Filters.*;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.sync.SyncUpdateOptions;

import org.bson.Document;
import org.bson.conversions.Bson;

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

        collections.insertOne(profileDocument);

        /*remoteMongoClient.getAuth().loginWithCredential(new AnonymousCredential())
                .continueWithTask(task -> collections.sync().updateOne(null, profileDocument, new SyncUpdateOptions().upsert(true)))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("ProfileTable", "Inserted profile document");
                    }
                });*/
    }
    public boolean verifyData(String un, String pass){

        RemoteFindIterable<Document> findIterable = collections.find(and(eq("Username", un),eq("Password",pass)));
        if(findIterable==null){
            return false;
        }
        return true;
    }

}
