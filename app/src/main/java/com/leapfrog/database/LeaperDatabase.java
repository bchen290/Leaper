package com.leapfrog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LeaperDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Leaper.db";
    public static final String TABLE_NAME = "Profile";
    public static final String COL1 = "ID";
    public static final String COL2 = "First";
    public static final String COL3 = "Last";
    public static final String COL4 = "Username";
    public static final String COL5 = "Password";
    public static final String COL6 = "Email";
    private static LeaperDatabase mInstance;

    public LeaperDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, First TEXT, Last TEXT, Username TEXT, Password TEXT, Email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static LeaperDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new LeaperDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public boolean insertData(String First,String Last, String Username, String Password, String Email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, First);
        contentValues.put(COL3, Last);
        contentValues.put(COL4, Username);
        contentValues.put(COL5, Password);
        contentValues.put(COL6, Email);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
}
