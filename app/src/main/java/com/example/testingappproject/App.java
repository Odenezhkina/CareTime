package com.example.testingappproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testingappproject.data.AppDb;
import com.example.testingappproject.data.TrackerDao;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class App extends Application {
    private static final String DB_NAME = "database";
    private static App instance;
    private AppDb database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Thread thread = new Thread(() -> insertToDb());
                thread.start();
            }
        };

        database = Room.databaseBuilder(this, AppDb.class, DB_NAME).addCallback(callback).build();
        Thread thread = new Thread(() -> database.dateDao().getAllDates());
        thread.start();
    }

    public AppDb getDatabase() {
        return database;
    }

    public void insertToDb() {
        AppDb database = instance.getDatabase();
        TrackerDao trackerDao = App.getInstance().getDatabase().trackerDao();
        trackerDao.insertTracker(new Tracker(getApplicationContext().getResources().getString(R.string.nutrition), R.drawable.nutrition, 10));
        trackerDao.insertTracker(new Tracker(getApplicationContext().getResources().getString(R.string.water), R.drawable.water, 10));
        trackerDao.insertTracker(new Tracker(getApplicationContext().getResources().getString(R.string.sleep), R.drawable.sleep, 10));
        trackerDao.insertTracker(new Tracker(getApplicationContext().getResources().getString(R.string.activity), R.drawable.activity, 10));
        trackerDao.insertTracker(new Tracker(getApplicationContext().getResources().getString(R.string.mood), R.drawable.mood, 10));

        Calendar calendar = new GregorianCalendar();
        Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        long newDateId = database.dateDao().insertDate(currentDate);
        List<Tracker> trackerList = trackerDao.getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            database.pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
        }
    }
}
