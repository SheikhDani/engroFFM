package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.VisitCustomerViewPagerAdapter;

public class VisitCustomerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    VisitCustomerViewPagerAdapter viewPagerAdapter;
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back,iv_location;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_customers);
        initView();


    }
    private void initView()
    {
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        iv_location = findViewById(R.id.img_location);
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loc = new Intent(VisitCustomerActivity.this,MapActivity.class);
                loc.putExtra("from","customer");
                startActivity(loc);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VisitCustomerActivity.this, MainActivity.class);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs_sales_plan);
        viewPager = (ViewPager) findViewById(R.id.viewPager_sales_plan);
        viewPagerAdapter = new VisitCustomerViewPagerAdapter(getSupportFragmentManager(),"customers");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("VISIT CUSTOMERS");
    }
}
