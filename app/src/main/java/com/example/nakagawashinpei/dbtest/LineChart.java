package com.example.nakagawashinpei.dbtest;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nakagawashinpei.dbtest.Chart.BLOWN;
import static com.example.nakagawashinpei.dbtest.Chart.PURPLE;

public class LineChart implements ChartState {

    private static final String stateName = "ConcreteStateA";
    private final String TAG = "LineChart";

    /* [Singleton] パターンを適用 */
    private static ChartState lineChart = new LineChart();
    private LineChart(){}
    public static ChartState getInstance(){
        return lineChart;
    }

    @Override
    public void makeChart() {
        Log.d(TAG, "***Linechart***");

    }
}
