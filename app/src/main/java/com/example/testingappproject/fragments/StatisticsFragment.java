package com.example.testingappproject.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testingappproject.R;
import com.example.testingappproject.auxiliary.CustomizableLegend;
import com.example.testingappproject.auxiliary.NotEnoughDataException;
import com.example.testingappproject.data.AdditionalViewModel;
import com.example.testingappproject.data.MainViewModel;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsFragment extends Fragment implements CustomizableLegend {
    private AdditionalViewModel viewModel;
    private RadarChart pentagramChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AdditionalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_statistics, container, false);

       pentagramChart = view.findViewById(R.id.chart_pentagram);

        viewModel.getStatisticsLiveData().observe(getViewLifecycleOwner(), list -> startRadarChart(list));

        return view;
    }

    private void startRadarChart(int[] points) {
        pentagramChart.getDescription().setEnabled(false);
        // разметка: линии, по которым сетка строится
        pentagramChart.setWebColor(getContext().getResources().getColor(R.color.second_green));
        // сетка-паутина
        pentagramChart.setWebColorInner(getContext().getResources().getColor(R.color.second_green));
        settingRadarChart(pentagramChart, points);
    }

    private List<RadarEntry> convertToEntries(int[] list) {
        List<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            entries.add(new RadarEntry(list[i]));
        }
        return entries;
    }

    private void settingRadarChart(RadarChart chart, int[] points){
        // задаем описание
        Legend legendRadarChart = chart.getLegend();
        customizeLegend(legendRadarChart);

        RadarDataSet radarDataSet = new RadarDataSet(convertToEntries(points), getContext().getResources().getString(R.string.radar_label));
        radarDataSet.setColor(Color.WHITE);
        radarDataSet.setFillColor(Color.WHITE);
        radarDataSet.setDrawFilled(true);

        //RadarDataSet_ов может быть несколько, RadarData всегда одна
        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);
        radarData.setValueTextColor(Color.WHITE);
        Context context = getContext();
        String[] radarLabels = {context.getResources().getString(R.string.nutrition),
                context.getResources().getString(R.string.water),
                context.getResources().getString(R.string.sleep),
                context.getResources().getString(R.string.activity),
                context.getResources().getString(R.string.mood)};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(radarLabels));
        xAxis.setTextColor(Color.WHITE);
        chart.getYAxis().setTextColor(Color.WHITE);
        chart.setData(radarData);
    }
}