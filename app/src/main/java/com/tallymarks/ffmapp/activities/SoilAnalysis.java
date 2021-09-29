package com.tallymarks.ffmapp.activities;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SoilAnalysisAdapter;

import java.util.ArrayList;
import java.util.List;

public class SoilAnalysis extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private TextView tvTopHeader;
    List<String> categories;
    RecyclerView recyclerView;
    private List<com.tallymarks.ffmapp.models.SoilAnalysis> planList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_analysis);
        initView();


    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("SOIL ANALYSIS");
        categories = new ArrayList<String>();
        categories.add("This Year");
        categories.add("Past Year");
        categories.add("Next Year");
        categories.add("After Year");
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        prepareMovieData();

        SoilAnalysisAdapter adapter = new SoilAnalysisAdapter(planList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SoilAnalysis.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),categories.get(i) , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void prepareMovieData() {
       com.tallymarks.ffmapp.models.SoilAnalysis plan = new com.tallymarks.ffmapp.models.SoilAnalysis();
        plan.setTitle("Parvez Mughal");
        plan.setMemebrship("REPORT PEDING");
        plan.setSampleno("234567");
        plan.setDate("12 Sep 2022 . 4:15 AM");
        planList.add(plan);

        com.tallymarks.ffmapp.models.SoilAnalysis plan2 = new com.tallymarks.ffmapp.models.SoilAnalysis();
        plan2.setTitle("Parvez Mughal");
        plan2.setMemebrship("Completed");
        plan2.setSampleno("234567");
        plan2.setDate("12 Sep 2022 . 4:15 AM");
        planList.add(plan2);

        com.tallymarks.ffmapp.models.SoilAnalysis plan3 = new com.tallymarks.ffmapp.models.SoilAnalysis();
        plan3.setTitle("Parvez Mughal");
        plan3.setMemebrship("REPORT PEDING");
        plan3.setSampleno("234567");
        plan3.setDate("12 Sep 2022 . 4:15 AM");
        planList.add(plan3);


        com.tallymarks.ffmapp.models.SoilAnalysis plan4 = new com.tallymarks.ffmapp.models.SoilAnalysis();
        plan4.setTitle("Parvez Mughal");
        plan4.setMemebrship("REPORT PEDING");
        plan4.setSampleno("234567");
        plan4.setDate("12 Sep 2022 . 4:15 AM");
        planList.add(plan4);


        com.tallymarks.ffmapp.models.SoilAnalysis plan5 = new com.tallymarks.ffmapp.models.SoilAnalysis();
        plan5.setTitle("Parvez Mughal");
        plan5.setMemebrship("REPORT PEDING");
        plan5.setSampleno("234567");
        plan5.setDate("12 Sep 2022 . 4:15 AM");
        planList.add(plan5);

        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
