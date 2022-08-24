package com.example.testingappproject.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdditionalViewModel extends ViewModel {
    private LiveData<int[]> statsLiveData;
    private final MutableLiveData<int[]> mutableLiveData = new MutableLiveData<>();

    public LiveData<int[]> getStatisticsLiveData() {
        if (statsLiveData == null) {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Callable<int[]> callableTask = this::loadData;
            executor.submit(callableTask);
            Future<int[]> future = executor.submit(callableTask);
            try {
                mutableLiveData.setValue(future.get());
                statsLiveData = mutableLiveData;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
        return statsLiveData;
    }

    private int[] loadData() {
        AppDb database = App.getInstance().getDatabase();
        List<Date> lastSevenDates = database.dateDao().getLastSevenDates();
        int[] averagePoints = new int[database.trackerDao().getTrackersCount()];
        for (int i = 0; i < lastSevenDates.size(); i++) {
            List<Point> pointList = database.pointDao().getPointsLinkedToDate(lastSevenDates.get(i).id);
            for (int j = 0; j < pointList.size(); j++) {
                averagePoints[(int) (pointList.get(j).trackerId) - 1] += pointList.get(j).getPoints();
            }
        }
        for (int i = 0; i < averagePoints.length; i++) {
            averagePoints[i] = (int) Math.round(averagePoints[i] / 7.00);
        }
        return averagePoints;
    }
}
