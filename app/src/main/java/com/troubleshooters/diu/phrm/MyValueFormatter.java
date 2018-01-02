package com.troubleshooters.diu.phrm;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Arif on 07-12-17.
 */

public class MyValueFormatter implements IValueFormatter{

    private DecimalFormat mFormate;
    public MyValueFormatter()
    {
        mFormate=new DecimalFormat("######.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormate.format(value)+"$";
    }
}
