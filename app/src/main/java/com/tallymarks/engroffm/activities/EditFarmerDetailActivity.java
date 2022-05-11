package com.tallymarks.engroffm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.AllPlanAdapter;
import com.tallymarks.engroffm.adapters.TodayPlanAdapter;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.MyDatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.TodayPlan;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditFarmerDetailActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    TextView txt_no_data;
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    EditText et_Search;
    TodayPlanAdapter adapter;
    static {
        System.loadLibrary("native-lib");
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfarmer_list);
        initView();

    }
    private void initView()
    {
        mydb = new MyDatabaseHandler(EditFarmerDetailActivity.this);
        db = new DatabaseHandler(EditFarmerDetailActivity.this);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        et_Search = findViewById(R.id.et_Search);
        sHelper = new SharedPrefferenceHelper(EditFarmerDetailActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("EDIT FARMER");
        txt_no_data = findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Intent i = new Intent(EditFarmerDetailActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        prepareMovieData();
        if (planList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            txt_no_data.setVisibility(View.VISIBLE);
            txt_no_data.setText(getResources().getString(R.string.no_farmer_available));

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt_no_data.setVisibility(View.GONE);
        }
        AllPlanAdapter adapter = new AllPlanAdapter(planList, "editfarmer");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditFarmerDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        et_Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(et_Search.hasFocus()) {
                    adapter.filter(cs.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void prepareMovieData() {
        planList.clear();
        mydb = new MyDatabaseHandler(EditFarmerDetailActivity.this);
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, "");
        map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "");
        map.put(mydb.KEY_TODAY_JOURNEY_IS_EDITED, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, "");
        //map.put(db.KEY_TODAY_JOURNEY_FARMER_NAME, "");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_PLAN_TYPE, "ALL");
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                TodayPlan plan = new TodayPlan();
                plan.setSalespoint("" + Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME))));
                plan.setTitle("" + Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))));
                plan.setLocation(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID)));
                plan.setMemebrship("" + Helpers.clean(cursor.getString((cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_IS_EDITED)))));
                plan.setFatherName("" + Helpers.clean(cursor.getString((cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME)))));
                plan.setMobilenumber(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_MOBILE_NO)));
                plan.setCustomercode("");
                plan.setLatitude(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)));
                plan.setLongitude(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)));
                planList.add(plan);

            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data

    }



    @Override
    public void onClick(View view, int position) {
        final TodayPlan city = planList.get(position);
        // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if(city.getMemebrship().equals("Not Edited")) {

            Intent i = new Intent(EditFarmerDetailActivity.this, EditFarmerActivity.class);
            sHelper.setString(Constants.EDIT_FARMER_NAME, city.getTitle());
            sHelper.setString(Constants.EDIT_FARMER_MOBILENUBMER, city.getMobilenumber());
            sHelper.setString(Constants.EDIT_FARMER_ID, city.getLocation());
            startActivity(i);
        }
        else
        {

                Toast.makeText(EditFarmerDetailActivity.this, "You Already Edit This Farmer", Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
