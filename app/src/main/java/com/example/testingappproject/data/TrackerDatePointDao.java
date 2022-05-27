package com.example.testingappproject.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

@Dao
public interface TrackerDatePointDao {

    @Query("SELECT id FROM points " +
            "WHERE tracker_id LIKE :tracker_id AND date_id LIKE :date_id")
    long getPointId(long tracker_id, long date_id);

    @Query("SELECT points.date_id, points.tracker_id, headline, max_points, img_res, points FROM trackers, points" +
            " WHERE points.tracker_id LIKE :tracker_id AND points.date_id LiKE :last_date_id")
    TrackerDatePoint getTrackerPoint(long last_date_id, long tracker_id);

    @Query("SELECT * " +
            "FROM trackers LEFT JOIN points " +
            "ON trackers.id = points.tracker_id WHERE points.date_id = :last_date_id")
    LiveData<List<TrackerDatePoint>> getTrackerPoints(long last_date_id);

    @Query("SELECT * " +
            "FROM trackers LEFT JOIN points " +
            "ON trackers.id = points.tracker_id WHERE points.date_id = :last_date_id")
    List<TrackerDatePoint> getTrackerPointsAsList(long last_date_id);
}
