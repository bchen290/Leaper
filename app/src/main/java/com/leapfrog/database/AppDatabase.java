package com.leapfrog.database;

import android.content.Context;


import com.leapfrog.model.ChatSessions;
import com.leapfrog.model.Message;
import com.leapfrog.util.Converters;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ChatSessions.class, Message.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatSessionDao chatSessionDao();
    public abstract MessageDao messageDao();

    private static volatile AppDatabase instance;

    public synchronized static AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "LeapFrogDatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return instance;
    }
}
