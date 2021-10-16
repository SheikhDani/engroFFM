package com.tallymarks.ffmapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubordinatListActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinates_list);
        initView();

    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubordinatListActivity.this, SubOrdinatesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tvTopHeader.setText("Riaz Ul Haq"+ " Customers");






        prepareMovieData();
        TodayPlanAdapter adapter = new TodayPlanAdapter(planList, "subordinate");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SubordinatListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }
    private void prepareMovieData() {

        com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
        plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan.setTitle("Dr. Athar Rasheed");
        plan.setLocation("Location Code: Lahore");
        plan.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan);

        com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan2.setTitle("Dr. Athar Rasheed");

        plan2.setLocation("Location Code: Lahore");
        plan2.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan2);

        com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan3.setTitle("Dr. Athar Rasheed");
        plan3.setLocation("Location Code: Lahore");
        plan3.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan3);

        com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan4.setTitle("Dr. Athar Rasheed");
        plan4.setLocation("Location Code: Lahore");
        plan4.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan4);


        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    @Override
    public void onClick(View view, int position) {
        final TodayPlan city = planList.get(position);
        // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(SubordinatListActivity.this, SuperVisorSnapShotActivity.class);
            i.putExtra("tradername",city.getTitle());
            startActivity(i);

    }

}
