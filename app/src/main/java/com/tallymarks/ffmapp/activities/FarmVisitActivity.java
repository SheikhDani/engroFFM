package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Recommendations;

import java.util.ArrayList;

public class FarmVisitActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    private TableLayout mTableLayout;
    AutoCompleteTextView auto_crop,auto_main_product;
    ImageView iv_menu,iv_back,img_add_recommendations;
    Button btn_proceed,btn_back;
    ArrayList<Recommendations> arraylist = new ArrayList<Recommendations>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_visit);
        initView();



    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        auto_crop = findViewById(R.id.auto_crop);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmVisitActivity.this,FarmersStartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent soil = new Intent(FarmVisitActivity.this, SoilSamplingActivity.class);
                startActivity(soil);
            }
        });

        auto_main_product = findViewById(R.id.auto_main_product);
        img_add_recommendations = findViewById(R.id.img_add_recommendations);
        img_add_recommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecommendations();
            }
        });

        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("FARM VISIT");
        prepareRecommendationData();
        drawRecommendationTable();
        final String arraylist[]={"Male","female","other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        auto_crop.setAdapter(arrayAdapter);
        auto_main_product.setAdapter(arrayAdapter);

        auto_crop.setCursorVisible(false);
        auto_main_product.setCursorVisible(false);

        auto_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_crop.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_crop.showDropDown();
            }
        });
        auto_main_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_main_product.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_main_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_main_product.showDropDown();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmVisitActivity.this,FarmersStartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    public void addRecommendations() {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialoige_add_recommednations, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);


        Button btnYes = promptsView.findViewById(R.id.btn_add_recommendations);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }
    public void drawRecommendationTable()
    {
        int cursorIndex=0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Crop");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(16);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Fert App Type");
        column2.setTextSize(16);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("product");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(16);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Dosage");
        column4.setTextSize(16);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 100, 0.15f));
        row.addView(column2, new TableRow.LayoutParams(0, 100, 0.37f));
        row.addView(column3, new TableRow.LayoutParams(0, 100, 0.23f));
        row.addView(column4, new TableRow.LayoutParams(0, 100, 0.25f));


       mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for(int i=0;i<arraylist.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView crop = new TextView(this);
            crop.setText(arraylist.get(i).getCrop());
            crop.setTextSize(16);
            //startDate.setBackgroundResource(R.drawable.table_row);
            crop.setGravity(Gravity.CENTER);
            crop.setPadding(2, 2, 2, 2);
            TextView fert = new TextView(this);
            fert.setText(arraylist.get(i).getFert());
            fert.setGravity(Gravity.CENTER);
            fert.setTextSize(16);
            //name.setBackgroundResource(R.drawable.table_row);
            fert.setPadding(2, 2, 2, 2);

            TextView product = new TextView(this);
            product.setText(arraylist.get(i).getProduct());
            product.setGravity(Gravity.CENTER);

            product.setTextSize(16);
            //status.setBackgroundResource(R.drawable.table_row);
            product.setPadding(2, 2, 2, 2);
            TextView dosage= new TextView(this);
            dosage.setText(arraylist.get(i).getDosage());
            dosage.setGravity(Gravity.CENTER);
            dosage.setTextSize(16);
            //srNo.setBackgroundResource(R.drawable.table_row);
            dosage.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(crop, new TableRow.LayoutParams(0, 100, 0.15f));
            row2.addView(fert, new TableRow.LayoutParams(0, 100, 0.37f));
            row2.addView(product, new TableRow.LayoutParams(0, 100, 0.23f));
            row2.addView(dosage, new TableRow.LayoutParams(0, 100, 0.25f));


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
       Recommendations plan = new Recommendations();
        plan.setCrop("Wheat");
        plan.setDosage("10");
        plan.setFert("Trop Dressing");
        plan.setProduct("Engro DAP");
        arraylist.add(plan);

        Recommendations plan2 = new Recommendations();
        plan2.setCrop("Wheat");
        plan2.setDosage("10");
        plan2.setFert("Trop Dressing");
        plan2.setProduct("Engro DAP");
        arraylist.add(plan2);

        Recommendations plan3 = new Recommendations();
        plan3.setCrop("Wheat");
        plan3.setDosage("10");
        plan3.setFert("Trop Dressing");
        plan3.setProduct("Engro DAP");
        arraylist.add(plan3);

        Recommendations plan4 = new Recommendations();
        plan4.setCrop("Wheat");
        plan4.setDosage("10");
        plan4.setFert("Trop Dressing");
        plan4.setProduct("Engro DAP");
        arraylist.add(plan4);

        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
