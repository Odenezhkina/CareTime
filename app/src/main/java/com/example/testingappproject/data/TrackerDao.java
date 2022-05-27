package com.example.testingappproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.testingappproject.model.Tracker;

import java.util.List;

@Dao
public interface TrackerDao {

    @Query("SELECT * FROM trackers")
    List<Tracker> getAllTrackersAsList();

    @Query("SELECT COUNT(headline) FROM trackers")
    int getTrackersCount();

    @Query("DELETE FROM trackers")
    void clearTrackers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTracker(Tracker tracker);



}
