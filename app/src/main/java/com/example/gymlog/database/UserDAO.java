package com.example.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlog.database.entities.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Query("DELETE from " + GymLogDatabase.USER_TABLE) void deleteAll();

    @Query("SELECT * FROM " + GymLogDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + GymLogDatabase.USER_TABLE + " WHERE id == :userID")
    LiveData<User> getUserByUserID(int userID);
}
