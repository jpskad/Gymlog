package com.example.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlog.database.entities.GymLog;

import java.util.List;

@Dao
public interface GymLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymLog);
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " order by date DESC")
    List<GymLog> getAllRecords();
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userID = :loggedInUserID ORDER BY date DESC")
    List<GymLog> getRecordsByUserID(int loggedInUserID);
    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userID = :loggedInUserID ORDER BY date DESC")
    LiveData<List<GymLog>> getAllLogsByUserIDLiveData(int loggedInUserID);
}
