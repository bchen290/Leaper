package com.leapfrog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leapfrog.model.Message;

import androidx.annotation.Nullable;

public class LeaperDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Leaper.db";

    private ProfileTable profileTable;
    private MessageTable messageTable;

    private static LeaperDatabase mInstance;

    public LeaperDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

        profileTable = new ProfileTable();
        messageTable = new MessageTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        profileTable.onCreate(db);
        messageTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        profileTable.onUpgrade(db, oldVersion, newVersion);
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
        profileTable.deleteAll(this.getWritableDatabase());
        messageTable.deleteAll(this.getWritableDatabase());
    }

    public boolean insertProfileData(String first, String last, String username, String password, String email) {
        return profileTable.insertProfileData(this.getWritableDatabase(), first, last, username, password, email);
    }

    public boolean insertMessageData(Message msg) {
        return messageTable.insertMessageData(this.getWritableDatabase(), msg.getMessage());
    }
}
