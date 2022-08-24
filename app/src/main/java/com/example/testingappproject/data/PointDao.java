package com.example.testingappproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testingappproject.model.Point;

import java.util.List;

@Dao
public interface PointDao {

    @Query("SELECT * FROM points WHERE date_id LIKE :date_id")
    List<Point> getPointsLinkedToDate(long date_id);

    @Query("SELECT * FROM points WHERE tracker_id LIKE :tracker_id")
    List<Point> getPointsLinkedToTracker(long tracker_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPoint(Point point);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePoint(Point point);
}
