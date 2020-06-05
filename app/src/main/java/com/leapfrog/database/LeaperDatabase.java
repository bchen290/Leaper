package com.leapfrog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leapfrog.model.ChatSessions;
import com.leapfrog.model.Message;
import com.leapfrog.model.User;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import java.util.ArrayList;

import androidx.annotation.Nullable;
/**
 * This class holds all the necessary components to allow the system to save data
 */
public class LeaperDatabase{
    private static final String DATABASE_NAME = "Leaper.db";

    private ProfileTable profileTable;
    private MessageTable messageTable;

    private StitchAppClient client;
    private RemoteMongoClient mongoClient;

    private static LeaperDatabase mInstance;
    /**
     * Initializes Database and connects to Mongo
     * Creates databases for Profiles and Messages
     */
    private LeaperDatabase(@Nullable Context context) {

        client = Stitch.initializeAppClient("leaper-oumlj");
        client.getAuth().loginWithCredential(new AnonymousCredential());
        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        RemoteMongoDatabase database = mongoClient.getDatabase("Leaper");

        profileTable = new ProfileTable(database, client);
        messageTable = new MessageTable(database, client);
    }

    /**
     * Gets instance of class
     */
    public static LeaperDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new LeaperDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Deleted all data of profiles and messages from database
     */
    public void deleteAll(){
        profileTable.deleteAll();
        messageTable.deleteAll();
    }

    /**
     * Inserts new profile data into database
     */
    public void insertProfileData(String first, String last, String username, String password, String email) {
        profileTable.insertProfileData(first, last, username, password, email);
    }

    /**
     * Inserts new message data into database
     */
    public void insertMessageData(Message msg) {
        messageTable.insertMessageData(msg);
    }

    /**
     * Gets existing profile from database
     */
    public ProfileTable getProfileTable() {
        return profileTable;
    }

    /**
     * Checks if user information already exists in database
     */
    public boolean verifyData(String username, String password) {
       return profileTable.verifyData(username, password);
    }

    /**
     * Returns existing messages within database
     */
    public ArrayList<Message> getMessages(User current, User other) {
        return messageTable.getMessages(current, other);
    }

    /**
     * Returns existing chats between users within database
     */
    public ArrayList<ChatSessions> getChatSessions(User current) {
        return messageTable.getChatSessions(current);
    }
}
