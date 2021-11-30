package com.tallymarks.ffmapp.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyDecimalFormator extends PercentFormatter {

    /******Created by                   robi kumar tomar             ********/
    /****** For more tutorials visit    http://mobiappdeveloper.com   *******/
    /****** Contact me at                robi.tomar72@gmail.com         *******/





    protected DecimalFormat mFormat;

    public MyDecimalFormator(DecimalFormat format) {
        this.mFormat = format;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " ";
    }
}