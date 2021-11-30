package com.tallymarks.ffmapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.utils.CustomSeekBar;
import com.tallymarks.ffmapp.utils.MyDecimalFormator;
import com.tallymarks.ffmapp.utils.ProgressItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Dealers extends Fragment {
    HalfGauge halfGauge;
    private CustomSeekBar seekbar1;
    private CustomSeekBar seekbar2;
    private float totalSpan = 1500;
    private float redSpan = 500;

    private float greenSpan = 500;
    private float yellowSpan = 500;

    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;

    private PieChart chart;
    String[] products = {"Industry", "Urea"};
    int[] percent = {60,40};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_dashoard, container, false);
        return rootView;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        halfGauge = (HalfGauge) view.findViewById(R.id.half);
        seekbar1 = ((CustomSeekBar) view.findViewById(R.id.seekBar0));
        seekbar2 = ((CustomSeekBar) view.findViewById(R.id.seekBar1));
        chart = view.findViewById(R.id.piechart);
        com.ekn.gruzer.gaugelibrary.Range range = new Range();
        range.setColor(Color.parseColor("#ce0000"));
        range.setFrom(0.0);
        range.setTo(50.0);

        com.ekn.gruzer.gaugelibrary.Range range2 = new Range();
        range2.setColor(Color.parseColor("#E3E500"));
        range2.setFrom(50.0);
        range2.setTo(100.0);

        com.ekn.gruzer.gaugelibrary.Range range3 = new Range();
        range3.setColor(Color.parseColor("#00b20b"));
        range3.setFrom(100.0);
        range3.setTo(150.0);


        //set min max and current value
        halfGauge.setMinValue(0.0);
        halfGauge.setMaxValue(150.0);
        halfGauge.setValue(60.0);

        //add color ranges to gauge
        halfGauge.addRange(range);
        halfGauge.addRange(range2);
        halfGauge.addRange(range3);

        initDataToSeekbar();
        seekbar1.setProgress(70);
        seekbar2.setProgress(80);

        drawPieChart();


    }

    private void drawPieChart() {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        ArrayList<PieEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < products.length; i++) {
            dataEntries.add(new PieEntry(percent[i] , products[i]));
        }
        PieDataSet dataSet = new PieDataSet(dataEntries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(R.color.green);
        colors.add(R.color.yellow);


//        ArrayList percentEntries = new ArrayList();
//        for (int i = 0; i < percent.length; i++) {
//            percentEntries.add((percent[i]));
//        }
//
//        dataSet.setColors(percentEntries);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(50f);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        dataSet.setColors(new int[]{ContextCompat.getColor(getActivity(), R.color.green),
                ContextCompat.getColor(getActivity(), R.color.yellow)});
       // dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setDrawSliceText(false);

        chart.animateXY(5000, 5000);

    }

    private void initDataToSeekbar() {
        progressItemList = new ArrayList<ProgressItem>();
        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = ((redSpan / totalSpan) * 100);

        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);

        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (greenSpan / totalSpan) * 100;
        mProgressItem.color = R.color.green;
        progressItemList.add(mProgressItem);
        // yellow span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = (yellowSpan / totalSpan) * 100;
        mProgressItem.color = R.color.yellow;
        progressItemList.add(mProgressItem);


        seekbar1.initData(progressItemList);
        seekbar1.invalidate();

        seekbar2.initData(progressItemList);
        seekbar2.invalidate();
    }

}
