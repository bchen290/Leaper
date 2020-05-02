package com.leapfrog.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

@SuppressWarnings("unused")
class ProfileTable {
    private static final String TABLE_NAME = "Profile";
    private static final String COL1 = "ID";
    private static final String COL2 = "First";
    private static final String COL3 = "Last";
    private static final String COL4 = "Username";
    private static final String COL5 = "Password";
    private static final String COL6 = "Email";

    void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, First TEXT, Last TEXT, Username TEXT, Password TEXT, Email TEXT)");
    }

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    void deleteAll(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    boolean insertProfileData(SQLiteDatabase db, String first, String last, String username, String password, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, first);
        contentValues.put(COL3, last);
        contentValues.put(COL4, username);
        contentValues.put(COL5, password);
        contentValues.put(COL6, email);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }
}
