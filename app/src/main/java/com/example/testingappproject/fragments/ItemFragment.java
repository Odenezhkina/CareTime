package com.example.testingappproject.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testingappproject.App;
import com.example.testingappproject.R;
import com.example.testingappproject.auxiliary.CustomizableLegend;
import com.example.testingappproject.data.MainViewModel;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.TrackerDatePoint;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment implements CustomizableLegend {
    private TextView tvHeadline;
    private ProgressBar progressBar;
    private TextView tvPoints;
    private TextView tvStatsDates;
    private ProgressBar pbLoadingGraph;
    private int position;
    private int currentStateOfPoints;
    private int maxStateOfPoints;
    private boolean wasChanged = false;
    private Handler handler;
    private volatile long tracker_id;
    private long date_id;
    private MainViewModel viewModel;
    private BarChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        handler = new Handler();

        try {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
            bottomNavigationView.setVisibility(View.GONE);
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
        }

        return inflater.inflate(R.layout.fragment_item, container, false);

    }

    @Override
    public void onDestroyView() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        chart = view.findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        Legend legendBarChart = chart.getLegend();
        customizeLegend(legendBarChart);

        tvHeadline = view.findViewById(R.id.tv_headline);
        progressBar = view.findViewById(R.id.progressBar);
        tvPoints = view.findViewById(R.id.tv_points);
        tvStatsDates = view.findViewById(R.id.tv_dates_stats);
        pbLoadingGraph = view.findViewById(R.id.pb_loading_graph);

        ImageButton btnPlus = view.findViewById(R.id.btn_plus);
        ImageButton btnMinus = view.findViewById(R.id.btn_minus);

        viewModel.getTrackerDatePointLiveData().observe(getViewLifecycleOwner(), trackerDatePoints -> {
            TrackerDatePoint tracker = trackerDatePoints.get(position);
            tracker_id = tracker.tracker_id;
            date_id = tracker.date_id;
            currentStateOfPoints = tracker.points;
            maxStateOfPoints = tracker.max_points;
            tvHeadline.setText(tracker.headline);
            setTvPoints(tracker.points);
            setProgressPoints(currentStateOfPoints, maxStateOfPoints);
        });

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.btn_plus:
                    if (currentStateOfPoints < maxStateOfPoints) {
                        currentStateOfPoints++;
                        setProgressPoints(currentStateOfPoints, maxStateOfPoints);
                        setTvPoints(currentStateOfPoints);
                    }
                    break;
                case R.id.btn_minus:
                    if (currentStateOfPoints > 0) {
                        currentStateOfPoints--;
                        setProgressPoints(currentStateOfPoints, maxStateOfPoints);
                        setTvPoints(currentStateOfPoints);
                    }
            }
            wasChanged = true;
            Thread thread = new Thread(this::getAllLinkedPointsAndDates);
            thread.start();
        };

        btnPlus.setOnClickListener(onClickListener);
        btnMinus.setOnClickListener(onClickListener);


        Thread thread = new Thread(this::getAllLinkedPointsAndDates);
        thread.start();

        super.onViewCreated(view, savedInstanceState);
    }

    public void setSelectedItem(int position) {
        this.position = position;
    }

    private void setProgressPoints(int points, int max_points) {
        progressBar.setProgress((int) Math.round(points * 100.0 / max_points));
    }

    private void setTvPoints(int newPoints) {
        tvPoints.setText(getString(R.string.points, newPoints, maxStateOfPoints));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wasChanged) {
            updateDatabase();
        }
    }

    private void updateDatabase() {
        Thread thread = new Thread(() -> {
            long point_id = App.getInstance().getDatabase().trackerDatePointDao().getPointId(tracker_id, date_id);
            App.getInstance().getDatabase().pointDao().updatePoint(new Point(point_id, tracker_id, date_id, currentStateOfPoints));
        });
        thread.start();
    }

    private void getAllLinkedPointsAndDates() {

        while (tracker_id == 0) {
        }
        List<Point> linkedPoints = App.getInstance().getDatabase().pointDao().getPointsLinkedToTracker(tracker_id);
        List<Date> allDates = App.getInstance().getDatabase().dateDao().getAllDates();

        if (linkedPoints.get(linkedPoints.size() - 1).getPoints() != currentStateOfPoints) {
            linkedPoints.remove(linkedPoints.size() - 1);
            linkedPoints.add(new Point(currentStateOfPoints));
        }
        while (linkedPoints.size() < 7) {
            linkedPoints.add(new Point(0));
        }
        if (linkedPoints.size() > 7) {
            linkedPoints = linkedPoints.subList(linkedPoints.size() - 7, linkedPoints.size());
        }
        if (allDates.size() > 7) {
            allDates = allDates.subList(allDates.size() - 7, allDates.size());
        }

        List<Date> dates = allDates;
        List<Integer> points = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            linkedPoints.forEach(x -> points.add(x.getPoints()));
        }

        handler.post(() -> {
            displayMonitoringDates(dates);
            setDataToBarChart(points, dates);
        });
    }


    private void displayMonitoringDates(List<Date> allDates) {
        String firstDateMonth = monthToString(allDates.get(0).getMonth());
        String lastDateMonth = monthToString(allDates.get(allDates.size() - 1).getMonth());
        tvStatsDates.setText(getString(R.string.monitoring_dates, allDates.get(0).getDay(), firstDateMonth,
                allDates.get(allDates.size() - 1).getDay(), lastDateMonth));
    }

    private String monthToString(int month) {
        return getContext().getResources().getStringArray(R.array.months)[month - 1];
    }

    private void setDataToBarChart(List<Integer> linkedPoints, List<Date> allDates) {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < linkedPoints.size(); i++) {
            values.add(new BarEntry(i, linkedPoints.get(i)));
        }
        BarDataSet dataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            dataSet = (BarDataSet) chart.getData().getDataSetByIndex(0);
            dataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {
            dataSet = new BarDataSet(values, tvHeadline.getText() + " ");
            dataSet.setColor(Color.WHITE);
            final ArrayList<String> xAxisLabel = new ArrayList<>();
            for (int i = 0; i < allDates.size(); i++) {
                xAxisLabel.add(allDates.get(i).getDay() + " " + monthToString(allDates.get(i).getMonth()));
            }
            XAxis xAxis = chart.getXAxis();
            xAxis.setTextSize(0.9f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            BarData data = new BarData(dataSets);
            chart.setData(data);
            pbLoadingGraph.setVisibility(View.GONE);
            chart.setVisibility(View.VISIBLE);
        }
    }

}
