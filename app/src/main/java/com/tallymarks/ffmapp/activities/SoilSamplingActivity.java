package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Commitment;
import com.tallymarks.ffmapp.models.SoilSamplingCrops;

import java.util.ArrayList;

public class SoilSamplingActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    AutoCompleteTextView auto_pre_rop,auto_current_crop,auto_current_crop2,auto_depth;
    ImageView iv_menu,iv_back;
    private TableLayout mTableLayout;
    ArrayList<SoilSamplingCrops> arraylist = new ArrayList<SoilSamplingCrops>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_sampling);
        initView();

    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        auto_pre_rop = findViewById(R.id.auto_pre_crop);
        auto_current_crop = findViewById(R.id.auto_cr_crop);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);

        auto_current_crop2 = findViewById(R.id.auto_current_crop_2);
        auto_depth= findViewById(R.id.auto_depth);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("SOIL SAMPLING");
        final String arraylist[]={"Male","female","other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        auto_pre_rop.setAdapter(arrayAdapter);
        auto_current_crop.setAdapter(arrayAdapter);
        auto_current_crop2.setAdapter(arrayAdapter);
        auto_depth.setAdapter(arrayAdapter);
        auto_depth.setCursorVisible(false);
        auto_current_crop2.setCursorVisible(false);
        auto_current_crop.setCursorVisible(false);
        auto_pre_rop.setCursorVisible(false);
        auto_pre_rop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_pre_rop.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_pre_rop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_pre_rop.showDropDown();
            }
        });
        auto_current_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_current_crop.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_current_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_current_crop.showDropDown();
            }
        });
        auto_current_crop2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_current_crop2.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_current_crop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_current_crop2.showDropDown();
            }
        });
        auto_depth.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_depth.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SoilSamplingActivity.this, FarmVisitActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        auto_depth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_depth.showDropDown();
            }
        });
        prepareRecommendationData();
        drawRecommendationTable();
    }
    public void drawRecommendationTable()
    {
        int cursorIndex=0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Plot/Acre#");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(12);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Block/Square#");
        column2.setTextSize(12);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Previous Crop");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(12);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Crop1");
        column4.setTextSize(12);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column5 = new TextView(this);
        column5.setText("Crop2");
        column5.setTextSize(12);
        column5.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column5.setPadding(2, 2, 2, 2);
        column5.setBackgroundColor(getResources().getColor(R.color.green));
        column5.setTextColor(getResources().getColor(R.color.colorPrimary));
        column5.setTypeface(Typeface.DEFAULT_BOLD);

        TextView column6 = new TextView(this);
        column6.setText("Depth");
        column6.setTextSize(12);
        column6.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column6.setPadding(2, 2, 2, 2);
        column6.setBackgroundColor(getResources().getColor(R.color.green));
        column6.setTextColor(getResources().getColor(R.color.colorPrimary));
        column6.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column2, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column3, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column4, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column5, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column6, new TableRow.LayoutParams(0, 150, 0.40f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for(int i=0;i<arraylist.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView acre = new TextView(this);
            acre.setText(arraylist.get(i).getAcre());
            acre.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            acre.setGravity(Gravity.CENTER);
            acre.setPadding(2, 2, 2, 2);
            TextView block = new TextView(this);
            block.setText(arraylist.get(i).getBlock());
            block.setGravity(Gravity.CENTER);
            block.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            block.setPadding(2, 2, 2, 2);

            TextView  precrop = new TextView(this);
            precrop.setText(arraylist.get(i).getPreviouscrop());
            precrop.setGravity(Gravity.CENTER);

            precrop.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            precrop.setPadding(2, 2, 2, 2);

            TextView crop1= new TextView(this);
            crop1.setText(arraylist.get(i).getCrop1());
            crop1.setGravity(Gravity.CENTER);
            crop1.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            crop1.setPadding(2, 2, 2, 2);

            TextView crop2= new TextView(this);
            crop2.setText(arraylist.get(i).getCrop2());
            crop2.setGravity(Gravity.CENTER);
            crop2.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            crop2.setPadding(2, 2, 2, 2);

            TextView depth= new TextView(this);
            depth.setText(arraylist.get(i).getCrop2());
            depth.setGravity(Gravity.CENTER);
            depth.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
           depth.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(acre, new TableRow.LayoutParams(0, 150, 0.80f));
            row2.addView(block, new TableRow.LayoutParams(0, 150, 0.80f));
            row2.addView(precrop, new TableRow.LayoutParams(0, 150, 0.80f));
            row2.addView(crop1, new TableRow.LayoutParams(0, 150, 0.40f));
            row2.addView(crop2, new TableRow.LayoutParams(0, 150, 0.40f));
            row2.addView(depth, new TableRow.LayoutParams(0, 150, 0.40f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }
    private void prepareRecommendationData() {
        SoilSamplingCrops prod = new  SoilSamplingCrops();
        prod .setAcre("2");
        prod .setBlock("10");
        prod .setCrop1("Wheat");
        prod .setCrop2("Rice");
        prod.setPreviouscrop("Cotton");
        prod.setDepth("0-6 ince");
        arraylist.add(prod);




        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
