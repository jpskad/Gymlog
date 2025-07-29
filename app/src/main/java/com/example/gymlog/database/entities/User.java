package com.example.gymlog.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlog.database.GymLogDatabase;

import java.util.Objects;

@Entity(tableName = GymLogDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String username;
    private final String password;
    private boolean isAdmin;

    public User(String username,String password) {
        this.username = username;
        this.password = password;
        isAdmin = false;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
