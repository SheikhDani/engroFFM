package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.VisitCustomerViewPagerAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.utils.Constants;

public class VisitCustomerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    VisitCustomerViewPagerAdapter viewPagerAdapter;
    private TextView tvTopHeader;
    EditText et_search;
    ImageView iv_menu,iv_back,iv_location;
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
        et_search = findViewById(R.id.et_Search);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        sHelper = new SharedPrefferenceHelper(VisitCustomerActivity.this);
        iv_location = findViewById(R.id.img_location);
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tabText=tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
               if(tabText.equals("TODAY PLANS"))
               {
                   sHelper.setString(Constants.PLAN_TYPE_MAP,"today");
                            Intent loc = new Intent(VisitCustomerActivity.this,MapActivity.class);
                            loc.putExtra("from","customer");
                            startActivity(loc);
               }
               else if(tabText.equals("ALL PLANS"))
               {
                   sHelper.setString(Constants.PLAN_TYPE_MAP,"all");
                   Intent loc = new Intent(VisitCustomerActivity.this,MapActivity.class);
                   loc.putExtra("from","customer");
                   startActivity(loc);
               }
               else if(tabText.equals("FUTURE PLANS"))
               {
                   Toast.makeText(VisitCustomerActivity.this, "No Customer Found", Toast.LENGTH_SHORT).show();
               }
               else if(tabText.equals("PAST PLANS"))
               {
                   Toast.makeText(VisitCustomerActivity.this, "No Customer Found", Toast.LENGTH_SHORT).show();
               }
//                PopupMenu popup = new PopupMenu(VisitCustomerActivity.this,iv_location);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if(item.getTitle().equals("Today")) {
//                            sHelper.setString(Constants.PLAN_TYPE_MAP,"today");
//                            Intent loc = new Intent(VisitCustomerActivity.this,MapActivity.class);
//                            loc.putExtra("from","customer");
//                            startActivity(loc);
//                        }
//                        else {
//                            sHelper.setString(Constants.PLAN_TYPE_MAP,"all");
//                            Intent loc = new Intent(VisitCustomerActivity.this,MapActivity.class);
//                            loc.putExtra("from","customer");
//                            startActivity(loc);
//                        }
//                        return true;
//                    }
//                });
//
//                popup.show();//showing popup menu
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
        viewPagerAdapter = new VisitCustomerViewPagerAdapter(getSupportFragmentManager(),"customers",et_search);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("VISIT CUSTOMERS");

    }

}
