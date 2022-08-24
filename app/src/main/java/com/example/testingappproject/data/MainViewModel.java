package com.example.testingappproject.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> data;

    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Callable<LiveData<List<TrackerDatePoint>>> callableTask = this::loadData;
        executor.submit(callableTask);
        Future<LiveData<List<TrackerDatePoint>>> future = executor.submit(callableTask);
        try {
            this.data = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return data;
    }

    private LiveData<List<TrackerDatePoint>> loadData() {
        AppDb database = App.getInstance().getDatabase();
        long lastDateId = database.dateDao().getLastDateId();
        LiveData<List<TrackerDatePoint>> data = database.trackerDatePointDao().getTrackerPoints(lastDateId);
        return data;
    }
}

