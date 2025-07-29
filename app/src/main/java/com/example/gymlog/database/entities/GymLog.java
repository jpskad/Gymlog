package com.example.gymlog.database.entities;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlog.database.GymLogDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String exercise;
    private final double weight;
    private final int reps;
    private LocalDateTime date;
    private final int userID;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && userID == gymLog.userID && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, date, userID);
    }

    public GymLog(String exercise, double weight, int reps, int userID) {
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
        this.userID = userID;
        date = LocalDateTime.now();
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
                "%s\nweight: %.1f | reps: %d\n%s",
                exercise,
                weight,
                reps,
                date.format(formatter)
        );
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getExercise() {
        return exercise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public int getUserID() {
        return userID;
    }


}
