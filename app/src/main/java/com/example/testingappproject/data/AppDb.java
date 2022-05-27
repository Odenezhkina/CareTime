package com.example.testingappproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

@Database(entities = {Tracker.class, Date.class, Point.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract TrackerDao trackerDao();

    public abstract DateDao dateDao();

    public abstract PointDao pointDao();

    public abstract TrackerDatePointDao trackerDatePointDao();

}
