package com.example.nakagawashinpei.dbtest;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyXAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d("nakagawa", "value:" + value);
        return mValues[(int) value];
    }

    /** this is only needed if numbers are returned, else return 0 */
    //@Override

    public int getDecimalDigits() { return 0; }
}
