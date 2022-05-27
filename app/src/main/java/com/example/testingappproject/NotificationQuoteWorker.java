package com.example.testingappproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationQuoteWorker extends Worker {
    private static final String NOTIFICATIONS_CHANNEL_ID = "notificationsId";
    private static final String KEY_QUOTE = "quote";
    private static final String KEY_QUOTE_AUTHOR = "quote-author";

    public NotificationQuoteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        showNotification();
        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String quoteWithAuthor = preferences.getString(KEY_QUOTE, "The dream was always running ahead of me. To catch up, to live for a moment" +
                " in unison with it, that always was the miracle".concat(".") );
        quoteWithAuthor += " " + preferences.getString(KEY_QUOTE_AUTHOR, "");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATIONS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_favorite_24)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.notification_quote_title))
                .setContentText(quoteWithAuthor)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(quoteWithAuthor))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }


    @Override
    public void onStopped() {
        super.onStopped();
    }
}
