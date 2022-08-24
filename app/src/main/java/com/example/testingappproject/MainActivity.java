package com.example.testingappproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.testingappproject.fragments.HomeFragment;
import com.example.testingappproject.fragments.ItemFragment;
import com.example.testingappproject.fragments.QuoteFragment;
import com.example.testingappproject.fragments.StatisticsFragment;
import com.example.testingappproject.settings.SettingsFragment;
import com.example.testingappproject.viewpager.DepthViewPagerTransformer;
import com.example.testingappproject.viewpager.StarterPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentSendDataListener {
    private static final String keyIsColdBoot = "isColdBoot";
    public static ViewPager2 mPager;
    private FragmentManager fragmentManager;
    public static FrameLayout fr;
    public static BottomNavigationView bnv;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        fr = findViewById(R.id.frame_layout_content);
        bnv = findViewById(R.id.bottom_navigation_view);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean("isDarkTheme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (preferences.getBoolean(keyIsColdBoot, true)) {
            createViewPager();
            startMainWorker();
            saveIsColdBoot();
        }

        setListenerOnBottomNavigationView();
    }

    private void createViewPager() {
        fr.setVisibility(View.GONE);
        bnv.setVisibility(View.GONE);
        mPager = findViewById(R.id.view_pager);
        mPager.setVisibility(View.VISIBLE);
        StarterPagerAdapter pagerAdapter = new StarterPagerAdapter(this, this);
        mPager.setPageTransformer(new DepthViewPagerTransformer());
        mPager.setAdapter(pagerAdapter);
    }

    private void saveIsColdBoot() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(keyIsColdBoot, false);
        editor.apply();
    }

    private void startMainWorker() {
        Long delayTimeUntilMidnight = getTimeUntilMidnight();
        PeriodicWorkRequest wr = new PeriodicWorkRequest
                .Builder(MainWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(delayTimeUntilMidnight, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "main worker",
                ExistingPeriodicWorkPolicy.REPLACE,
                wr);
    }

    private Long getTimeUntilMidnight() {
        java.util.Date currentTime = new java.util.Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String[] timeText = timeFormat.format(currentTime).split(":");
        return 24 * 60 * 60 * 1000 - Long.parseLong(timeText[0]) * 60 * 60 * 1000 - Long.parseLong(timeText[1]) * 60 * 1000 - Long.parseLong(timeText[2]) * 1000;
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setListenerOnBottomNavigationView() {
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        final Fragment fragmentHome = new HomeFragment();
        final Fragment fragmentSettings = new SettingsFragment();
        final Fragment fragmentQuote = new QuoteFragment();
        final Fragment fragmentStatistics = new StatisticsFragment();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    openFragment(fragmentHome);
                    return true;
                case R.id.settings:
                    openFragment(fragmentSettings);
                    return true;
                case R.id.quote:
                    openFragment(fragmentQuote);
                    return true;
                case R.id.statistics:
                    openFragment(fragmentStatistics);
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public void onSendData(int position) {
        ItemFragment itemFragment = new ItemFragment();
        openFragment(itemFragment);
        itemFragment.setSelectedItem(position);
    }
}
