package com.leapfrog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leapfrog.model.Message;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase;

import androidx.annotation.Nullable;

public class LeaperDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Leaper.db";

    private ProfileTable profileTable;
    private MessageTable messageTable;

    private final StitchAppClient client = Stitch.initializeAppClient("leaper-oumlj");
    private RemoteMongoClient mongoClient;

    private static LeaperDatabase mInstance;

    private LeaperDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        RemoteMongoDatabase database = mongoClient.getDatabase("Leaper");

        profileTable = new ProfileTable(database, client);
        messageTable = new MessageTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        messageTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        messageTable.onUpgrade(db, oldVersion, newVersion);
        onCreate(db);
    }

    public static LeaperDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new LeaperDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public void deleteAll(){
        profileTable.deleteAll();
        messageTable.deleteAll(this.getWritableDatabase());
    }

    public void insertProfileData(String first, String last, String username, String password, String email) {
        profileTable.insertProfileData(first, last, username, password, email);
    }

    public void insertMessageData(Message msg) {
        messageTable.insertMessageData(this.getWritableDatabase(), msg.getMessage());
    }
}
