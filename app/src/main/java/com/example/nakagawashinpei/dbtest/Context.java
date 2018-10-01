package com.example.nakagawashinpei.dbtest;

import android.util.Log;

public class Context {

    public enum State{
        Grade,
        Gym
    }

    private ChartState chartState = null;
    private final String TAG = "nakagawa";

    public Context(){
        setState(State.Grade);
        chartState = LineChart.getInstance();
        Log.d(TAG, "***Context***");
    }

    public void setState(Context.State state){
        if(state == State.Grade){
            chartState = LineChart.getInstance();
        }
        else if (state == State.Gym){
            chartState = BarChart.getInstance();
        }
    }

    public void makeChart(){
        Log.d(TAG, "***makechart***");
        chartState.makeChart();
    }



}
