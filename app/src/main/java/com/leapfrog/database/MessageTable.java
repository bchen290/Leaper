package com.leapfrog.database;

import com.leapfrog.model.Message;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;

import org.bson.Document;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
class MessageTable {
    private RemoteMongoDatabase mongoDatabase;
    private RemoteMongoCollection<Document> collections;
    private StitchAppClient remoteMongoClient;

    MessageTable(RemoteMongoDatabase mongoDatabase, StitchAppClient remoteMongoClient) {
        this.mongoDatabase = mongoDatabase;
        this.remoteMongoClient = remoteMongoClient;

        collections = mongoDatabase.getCollection("Message");
    }

    void deleteAll() {
        //collections.deleteMany(new Document());
    }

    void insertMessageData(Message message){
        final Document profileDocument = new Document("Timestamp", message.getCreatedAt())
                .append("From", message.getSender().getUserID())
                .append("To", message.getReceiver().getUserID())
                .append("Message", message.getMessage());

        collections.insertOne(profileDocument);
    }
}
