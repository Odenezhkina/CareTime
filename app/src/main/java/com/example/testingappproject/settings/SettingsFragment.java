package com.example.testingappproject.settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.testingappproject.NotificationQuoteWorker;
import com.example.testingappproject.NotificationStateWorker;
import com.example.testingappproject.R;

import java.time.Duration;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String NOTIFICATIONS_CHANNEL_ID = "notificationsId";
    private SharedPreferences preferences;
    private static final int FLAG_QUOTE = 0;
    private static final int FLAG_STATE = 1;
    private static final String UNIQUE_WORK_NAME_QUOTE = "notificationQuoteWorker";
    private static final String UNIQUE_WORK_NAME_STATE = "notificationStateWorker";
    

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        Preference preferenceTheme = findPreference("dark_theme");
        assert preferenceTheme != null;
        preferenceTheme.setOnPreferenceChangeListener((preference1, newValue) -> {
            SharedPreferences.Editor e = preferences.edit();
            e.putBoolean("isDarkTheme", newValue.equals(true));
            e.apply();
            Toast.makeText(getContext(), requireContext().getResources().getString(R.string.please_restart), Toast.LENGTH_SHORT).show();
            return true;
        });
        
        
        Preference preferenceNotificationsState = findPreference("notifications_state");
        assert preferenceNotificationsState != null;
        preferenceNotificationsState.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                checkIfChannelIsCreatedAlready();
                showDialog(FLAG_STATE);
            } else {
                stopNotificationQuoteWorker(UNIQUE_WORK_NAME_STATE);
            }
            return true;
        });
        Preference preferenceNotificationsQuote = findPreference("notifications_quote");
        assert preferenceNotificationsQuote != null;
        preferenceNotificationsQuote.setOnPreferenceChangeListener((p, v) -> {
            if (v.equals(true)) {
                checkIfChannelIsCreatedAlready();
                showDialog(FLAG_QUOTE);
            } else {
                stopNotificationQuoteWorker(UNIQUE_WORK_NAME_QUOTE);
            }
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkIfChannelIsCreatedAlready() {
        if (!preferences.getBoolean("isChannelCreated", false)) {
            createNotificationChannel();
            SharedPreferences.Editor e = preferences.edit();
            e.putBoolean("isChannelCreated", true);
            e.apply();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDialog(int flag) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute1) -> startNotificationWorker(hourOfDay, minute1, flag);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                R.style.CustomDatePickerDialog,
                listener, hour, minute, true);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startNotificationWorker(int hours, int minutes, int flag) {
        Calendar c = Calendar.getInstance();
        int curHours = c.get(Calendar.HOUR_OF_DAY);
        int curMinutes = c.get(Calendar.MINUTE);
        int diffInMin = (hours - curHours) * 60 + minutes - curMinutes;
        Duration duration;
        if (diffInMin > 0) {
            duration = Duration.ofMinutes(diffInMin);
        } else {
            duration = Duration.ofMinutes(diffInMin + 24 * 60 * 60);
        }
        duration.minusSeconds(50);

        if(flag == FLAG_QUOTE){
            PeriodicWorkRequest wr = new PeriodicWorkRequest.Builder(NotificationQuoteWorker.class, 1, TimeUnit.DAYS)
                    .setInitialDelay(duration)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(UNIQUE_WORK_NAME_QUOTE, ExistingPeriodicWorkPolicy.REPLACE, wr);
        } else if(flag == FLAG_STATE){
            PeriodicWorkRequest wr = new PeriodicWorkRequest.Builder(NotificationStateWorker.class, 1, TimeUnit.DAYS)
                    .setInitialDelay(duration)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(UNIQUE_WORK_NAME_STATE, ExistingPeriodicWorkPolicy.REPLACE, wr);
        }
    }

    private void stopNotificationQuoteWorker(String uniqueName) {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(uniqueName);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATIONS_CHANNEL_ID, "notifications", importance);
        channel.setDescription("channel for notifications");
        NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
