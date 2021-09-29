package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;

public class FarmerMeetingActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    AutoCompleteTextView attendance,farmers;
    Button btn_back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_meeting);
        initView();

    }
    private void initView()
    {
        attendance = findViewById(R.id.attendance);
        farmers= findViewById(R.id.farmers);
        btn_back = findViewById(R.id.back);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("FARMER MEETING");
        final String arraylist[]={"Male","female","other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);

        attendance.setAdapter(arrayAdapter);
        farmers.setAdapter(arrayAdapter);
        attendance.setCursorVisible(false);
        farmers.setCursorVisible(false);
        attendance.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                attendance.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerMeetingActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerMeetingActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                attendance.showDropDown();
            }
        });
        farmers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                farmers.showDropDown();
                String selection = arraylist[position];
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        farmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                farmers.showDropDown();
            }
        });
    }
}
