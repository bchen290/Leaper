package com.leapfrog.database;

import com.google.android.gms.tasks.Task;
import com.leapfrog.model.Message;
import com.leapfrog.model.User;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;

import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

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

    ArrayList<Message> getMessages(User current, User other) {
        ArrayList<Document> documentArrayList = new ArrayList<>();
        ArrayList<Message> messageArrayList = new ArrayList<>();

        RemoteFindIterable<Document> documentFound = collections.find(or(and(eq("From", current.getUserID()), eq("To", other.getUserID())), and(eq("From", other.getUserID()), eq("To", current.getUserID()))));

        Task task = documentFound.into(documentArrayList);

        while(!task.isComplete()) {}

        for (Document document : documentArrayList) {
            messageArrayList.add(new Message(document.getLong("Timestamp"), document.getString("Message"), current, other));
        }

        return messageArrayList;
    }
}
