package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.fragments.Information;
import com.tallymarks.ffmapp.fragments.Insights;
import com.tallymarks.ffmapp.fragments.Relationship;

public class DealersInsightActivity extends AppCompatActivity {
   private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    Button btn_checkin;
    ImageView iv_menu, iv_back;
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_insight);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager_dealers_insight);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        btn_checkin = findViewById(R.id.btn_checkin);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_dealers_insight);
        tabLayout.setupWithViewPager(mViewPager);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DealersInsightActivity.this, VisitCustomerActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startActivity  =  new Intent(DealersInsightActivity.this,StartActivity.class);
                startActivity(startActivity);
            }
        });

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Insights.newInstance();
                case 1:
                    return Relationship.newInstance();
                    default:
                    return new Information();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Insights";
                case 1:
                    return "Relationship";
                case 2:
                    return "Information";
            }
            return null;
        }
    }
}
