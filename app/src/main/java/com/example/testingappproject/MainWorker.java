package com.example.testingappproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.testingappproject.auxiliary.QuoteOfTheDay;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


// WorkManager is the recommended solution for persistent work.
// Work is persistent when it remains scheduled through app restarts
// and system reboots. Because most background processing is best
// accomplished through persistent work, WorkManager is the primary
// recommended API for background processing
public class MainWorker extends Worker {
    private static final String KEY_QUOTE = "quote";
    private static final String KEY_QUOTE_AUTHOR = "quote-author";

    public MainWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("toradora", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        // unlike Service here we do not have to create a new Thread: Worker has his own thread
        Calendar calendar = new GregorianCalendar();
        Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        addNewDateToBd(currentDate);
        manageDailyQuote();
        return Result.success();
    }

    private void addNewDateToBd(Date newDate) {
        long newDateId = App.getInstance().getDatabase().dateDao().insertDate(newDate);
        List<Tracker> trackerList = App.getInstance().getDatabase().trackerDao().getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
            App.getInstance().getDatabase().pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
        }
    }

    private void manageDailyQuote() {
        QuoteOfTheDay quoteOfTheDay = new QuoteOfTheDay();
        String[] quoteWithAuthor = quoteOfTheDay.getQuote();
        saveDailyQuote(quoteWithAuthor[0], quoteWithAuthor[1]);
    }

    private void saveDailyQuote(String quote, String author) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_QUOTE, quote);
        editor.putString(KEY_QUOTE_AUTHOR, author);
        editor.apply();
    }
}
