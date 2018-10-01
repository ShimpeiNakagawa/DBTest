package com.example.nakagawashinpei.dbtest;

import android.content.Context;
import android.util.Log;

public class BarChart implements ChartState{
    private static final String stateName = "BarCharrt";
    private final String TAG = "BarChart";

    /* [Singleton] パターンを適用 */
    private static ChartState barChart = new BarChart();
    private BarChart(){}
    public static ChartState getInstance(){
        return barChart;
    }

    @Override
    public void makeChart() {
            Log.d(TAG, "***Barchart***");
    }
}
