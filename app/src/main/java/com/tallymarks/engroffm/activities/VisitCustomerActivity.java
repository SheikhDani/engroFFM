package com.tallymarks.engroffm.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.VisitCustomerViewPagerAdapter;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.DialougeManager;
import com.tallymarks.engroffm.utils.GpsTracker;

public class VisitCustomerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    VisitCustomerViewPagerAdapter viewPagerAdapter;
    private TextView tvTopHeader;
    GpsTracker gpsTracker;
    EditText et_search;
    ImageView iv_menu, iv_back, iv_location;
    SharedPrefferenceHelper sHelper;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_customers);
        initView();


    }

    private void initView() {
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
                gpsTracker = new GpsTracker(VisitCustomerActivity.this);
                if (gpsTracker.canGetLocation()) {
                    String tabText = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
                    if (tabText.equals("TODAY PLANS")) {
                        sHelper.setString(Constants.PLAN_TYPE_MAP, "today");
                        Intent loc = new Intent(VisitCustomerActivity.this, TodayCustomerMap.class);
                        loc.putExtra("from", "customer");
                        startActivity(loc);
                    } else if (tabText.equals("ALL PLANS")) {
                        sHelper.setString(Constants.PLAN_TYPE_MAP, "all");
                        Intent loc = new Intent(VisitCustomerActivity.this, MapActivity.class);
                        loc.putExtra("from", "customer");
                        startActivity(loc);
                    } else if (tabText.equals("FUTURE PLANS")) {
                        Toast.makeText(VisitCustomerActivity.this, "No Customer Found", Toast.LENGTH_SHORT).show();
                    } else if (tabText.equals("PAST PLANS")) {
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
                } else {
                    DialougeManager.gpsNotEnabledPopup(VisitCustomerActivity.this);
                }
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
        viewPagerAdapter = new VisitCustomerViewPagerAdapter(getSupportFragmentManager(), "customers", et_search);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("VISIT CUSTOMERS");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("from");
            if (value.equals("today")) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
            }
            //The key argument here must match that used in the other activity
        }


        for (int i = 0; i < 4; i++) {
            if (tabLayout.getTabAt(i).getText().toString().equals("FUTURE PLANS")) {
                tabLayout.getTabAt(i).view.setEnabled(false);
                tabLayout.getTabAt(i).view.setSelected(false);
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_action_lock_outline);

            } else if (tabLayout.getTabAt(i).getText().toString().equals("PAST PLANS")) {
                tabLayout.getTabAt(i).view.setEnabled(false);
                tabLayout.getTabAt(i).view.setSelected(false);
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_action_lock_outline);
            } else {
                tabLayout.getTabAt(i).view.setEnabled(true);
            }
        }
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 2 || position == 3) {
                    //To disable specific tab and set the previuos tab
                    viewPager.setCurrentItem(1); //We cannot provide the position in setSelected(boolean) now. This was missed in above solution
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

        });
    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(VisitCustomerActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
