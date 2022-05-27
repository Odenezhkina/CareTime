package com.example.testingappproject.auxiliary;

import android.graphics.Color;

import com.github.mikephil.charting.components.Legend;

public interface CustomizableLegend {
    default void customizeLegend(Legend l) {
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setFormSize(10f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setTextColor(Color.WHITE);
        l.setYOffset(0f);
    }
}
