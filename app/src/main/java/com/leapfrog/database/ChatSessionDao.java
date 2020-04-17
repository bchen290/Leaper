package com.leapfrog.database;


import com.leapfrog.model.ChatSessions;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ChatSessionDao {
    @Query("SELECT * FROM chatsessions")
    List<ChatSessions> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatSessions... sessions);

    @Delete
    void delete(ChatSessions session);

    @Query("DELETE FROM ChatSessions")
    void deleteAll();
}
