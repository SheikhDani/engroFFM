package com.tallymarks.ffmapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.utils.CustomSeekBar;
import com.tallymarks.ffmapp.utils.ProgressItem;

import java.util.ArrayList;

public class Farmers extends Fragment {
    HalfGauge halfGauge;
    private CustomSeekBar seekbar1;
    private CustomSeekBar seekbar2;
    private float totalSpan = 1500;
    private float redSpan = 500;

    private float greenSpan = 500;
    private float yellowSpan = 500;

    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;
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
