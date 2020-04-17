package com.leapfrog.database;

import com.leapfrog.model.Message;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MessageDao {
    @Query("SELECT Message.*, ChatSessions.chatID AS chatsessions_chatid FROM Message INNER JOIN ChatSessions ON Message.chatSessionID = ChatSessions.chatID where Message.chatSessionID = :chatID AND Message.chatSessionIDCurrent = :currentUserID ORDER BY Message.timestamp DESC LIMIT 30")
    List<Message> getLast30Messages(String chatID, String currentUserID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Message... messages);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM Message")
    void deleteAll();
}
