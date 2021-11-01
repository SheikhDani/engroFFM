package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.VisitCustomerViewPagerAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;

public class VisitFarmerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    VisitCustomerViewPagerAdapter viewPagerAdapter;
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back,iv_location;
    GpsTracker gpsTracker;
    EditText et_search;
    SharedPrefferenceHelper sHelper;
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
        et_search = findViewById(R.id.et_Search);
        sHelper = new SharedPrefferenceHelper(VisitFarmerActivity.this);
        iv_location = findViewById(R.id.img_location);
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GpsTracker(VisitFarmerActivity.this);
                if (gpsTracker.canGetLocation()) {

                }
                String tabText = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
                if (tabText.equals("TODAY PLANS")) {
                    sHelper.setString(Constants.PLAN_TYPE_MAP_FARMER, "TODAY");
                    Intent loc = new Intent(VisitFarmerActivity.this, MapActivity.class);
                    loc.putExtra("from", "farmer");
                    startActivity(loc);
                } else if (tabText.equals("ALL PLANS")) {
                    sHelper.setString(Constants.PLAN_TYPE_MAP_FARMER, "ALL");
                    Intent loc = new Intent(VisitFarmerActivity.this, MapActivity.class);
                    loc.putExtra("from", "farmer");
                    startActivity(loc);
                }
                else
                    {
                        DialougeManager.gpsNotEnabledPopup(VisitFarmerActivity.this);
                }
            }

        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VisitFarmerActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs_sales_plan);
        viewPager = (ViewPager) findViewById(R.id.viewPager_sales_plan);
        viewPagerAdapter = new VisitCustomerViewPagerAdapter(getSupportFragmentManager(),"farmers", et_search);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("VISIT Farmers");
    }
}
