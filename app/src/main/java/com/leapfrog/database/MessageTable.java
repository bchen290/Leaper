package com.leapfrog.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

class MessageTable {
    private static final String TABLE_NAME = "Messages";

    private static final String COL1 = "MessageID";
    private static final String COL2 = "UserID";
    private static final String COL3 = "Message";

    void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(MessageID INTEGER PRIMARY KEY AUTOINCREMENT, UserID TEXT, Message TEXT)");
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    void deleteAll(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    boolean insertMessageData(SQLiteDatabase db, String msg) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, msg);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }
}
