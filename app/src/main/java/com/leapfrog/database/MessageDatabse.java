package com.leapfrog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MessageDatabse extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Messages.db";
    public static final String TABLE_NAME = "Messages";
    public static final String COL1 = "msgID";
    public static final String COL2 = "usrID";
    public static final String COL3 = "msg";

    public MessageDatabse(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(msgID INTEGER PRIMARY KEY AUTOINCREMENT, usrID INTEGER, msg TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
