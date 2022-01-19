package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;

import java.sql.Timestamp;
import java.util.HashMap;

public class FarmersStartActivity extends AppCompatActivity {
    private TextView tvTopHeader, txt_checkin, tvNotAvailable, txt_salescall, txt_fas, txt_prospecting, txt_complaint_handling;
    ImageView iv_menu,iv_back;
    Button btn_next,btn_back;
    LinearLayout li_status, LiNotAvaliable,li_saleCall, li_fas,li_prospecting,  li_complaintHandling;
    String checkinLayout = "";
    String objectivelayout = "";
    SharedPrefferenceHelper sHelper;
    MyDatabaseHandler mydb;
    GpsTracker gps;
    double checkinlat = 0.0;
    double checkinlng = 0.0;
    String status = "";
    String objective = "";
    String planType = "";
    String journeytype;
    DatabaseHandler db;
    String rolename = "";
    ExtraHelper extraHelper;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_start);
        initView();

    }
    private void initView()
    {
        Bundle data = getIntent().getExtras();
        if (data != null)
        {
            planType = data.getString(Constants.PLAN_TYPE_FARMER);

        }
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        btn_next = findViewById(R.id.btn_next);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        li_status = findViewById(R.id.li_status);
        li_saleCall = findViewById(R.id.li_saleCall);
        li_fas = findViewById(R.id.li_fas);
        li_prospecting = findViewById(R.id.li_prospecting);
        li_complaintHandling = findViewById(R.id.li_complaintHandling);
        LiNotAvaliable = findViewById(R.id.LiNotAvaliable);

        txt_checkin = findViewById(R.id.txt_checkin);
        tvNotAvailable = findViewById(R.id.tvNotAvailable);
        txt_salescall = findViewById(R.id.txt_salescall);
        txt_fas = findViewById(R.id.txt_fas);
        txt_prospecting = findViewById(R.id.txt_prospecting);
        txt_complaint_handling = findViewById(R.id.txt_complaint_handling);

        btn_back = findViewById(R.id.back);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("START ACTIVITY");
        mydb = new MyDatabaseHandler(FarmersStartActivity.this);
        db = new DatabaseHandler(FarmersStartActivity.this);
        sHelper = new SharedPrefferenceHelper(this);
        extraHelper = new ExtraHelper(this);
        if(sHelper!=null)
        {

            if(sHelper.getString(Constants.PLAN_TYPE_FARMER).equals("TODAY"))
            {
                journeytype = "TODAY";
            }
            else
            {
                journeytype = "ALL";
            }
        }
        loadRoles();
        loadActivity();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sHelper.getString(Constants.ACTIVITY_STATUS_FARMER) != null && !sHelper.getString(Constants.ACTIVITY_STATUS_FARMER).equals("") && sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE) != null && !sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE).equals("")
                        && !status.equals("") && status !=null && objective!=null && !objective.equals("")){
                    gps = new GpsTracker(FarmersStartActivity.this);
                    if (gps.canGetLocation()) {
                        if (ActivityCompat.checkSelfPermission(FarmersStartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FarmersStartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        checkinlat = gps.getLatitude();
                        checkinlng = gps.getLongitude();
                        if (isUNpostedDataExsists()) {
                            updateActivity();
                        } else {
                            addActivity();
                        }
                    //    Intent n = new Intent(FarmersStartActivity.this, FloorStockActivity.class);
                    //    startActivity(n);
                    //    addDataInCheckInTable();
                        if(rolename.equals("Field Force Team") || rolename.equals("FieldAssistant") ) {
                            Intent n = new Intent(FarmersStartActivity.this, FarmVisitRoleWiseActivity.class);
                            Bundle data = new Bundle();
                            data.putString(Constants.PLAN_TYPE_FARMER, planType);
                            n.putExtras(data);
                            startActivity(n);
                            addDataInCheckInTable();
                        }
                        else
                        {
                            Intent n = new Intent(FarmersStartActivity.this, FarmVisitActivity.class);
                            Bundle data = new Bundle();
                            data.putString(Constants.PLAN_TYPE_FARMER, planType);
                            n.putExtras(data);
                            startActivity(n);
                            addDataInCheckInTable();
                        }
                    }
                    else {
                        DialougeManager.gpsNotEnabledPopup(FarmersStartActivity.this);
                    }
                }else{
                    final AlertDialog actions;
                    AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmersStartActivity.this);
                    categoryAlert.setTitle("Select objective and status");
                    categoryAlert.setMessage("You must select objective and status");
                    categoryAlert.setCancelable(false);
                    categoryAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    actions = categoryAlert.create();
                    actions.show();
                }
            }
        });
        txt_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextViewDrawableColor(txt_checkin, R.color.red);
                setTextViewDrawableColor(tvNotAvailable, R.color.green);
                status = "1";
                sHelper.setString(Constants.ACTIVITY_STATUS_FARMER, "1");

            }
        });
        tvNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextViewDrawableColor(txt_checkin, R.color.green);
                setTextViewDrawableColor(tvNotAvailable, R.color.red);
                status = "0";
                sHelper.setString(Constants.ACTIVITY_STATUS_FARMER, "3");
                gps = new GpsTracker(FarmersStartActivity.this);
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(FarmersStartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FarmersStartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    checkinlat = gps.getLatitude();
                    checkinlng = gps.getLongitude();
                    saveFarmerNoAvailale();
                    Intent n = new Intent(FarmersStartActivity.this,VisitFarmerActivity.class);
                    startActivity(n);

                }

            }
        });
        txt_salescall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextViewDrawableColor(txt_salescall, R.color.red);
                setTextViewDrawableColor(txt_fas, R.color.green);
                setTextViewDrawableColor(txt_prospecting, R.color.green);
                setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                objective = txt_salescall.getText().toString();
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME, txt_salescall.getText().toString());
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE, "1");
            }
        });
        txt_fas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTextViewDrawableColor(txt_salescall, R.color.green);
                setTextViewDrawableColor(txt_fas, R.color.red);
                setTextViewDrawableColor(txt_prospecting, R.color.green);
                setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                objective = txt_fas.getText().toString();
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME, txt_fas.getText().toString());
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE, "2");
            }
        });
        txt_prospecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextViewDrawableColor(txt_salescall, R.color.green);
                setTextViewDrawableColor(txt_fas, R.color.green);
                setTextViewDrawableColor(txt_prospecting, R.color.red);
                setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                objective = txt_prospecting.getText().toString();
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME, txt_prospecting.getText().toString());
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE, "3");
            }
        });
        txt_complaint_handling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextViewDrawableColor(txt_salescall, R.color.green);
                setTextViewDrawableColor(txt_fas, R.color.green);
                setTextViewDrawableColor(txt_prospecting, R.color.green);
                setTextViewDrawableColor(txt_complaint_handling, R.color.red);
                objective = txt_complaint_handling.getText().toString();
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME, txt_complaint_handling.getText().toString());
                sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE, "4");
            }
        });

//        li_status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                li_status.setBackgroundColor(getResources().getColor(R.color.selected));
//                LiNotAvaliable.setBackgroundColor(getResources().getColor(R.color.white));
//                status = "CheckIn";
//            }
//        });
//        LiNotAvaliable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LiNotAvaliable.setBackgroundColor(getResources().getColor(R.color.selected));
//                li_status.setBackgroundColor(getResources().getColor(R.color.white));
//                status = "Not CheckIn";
//            }
//        });
//        li_saleCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                li_saleCall.setBackgroundColor(getResources().getColor(R.color.selected));
//                li_fas.setBackgroundColor(getResources().getColor(R.color.white));
//                li_prospecting.setBackgroundColor(getResources().getColor(R.color.white));
//                li_complaintHandling.setBackgroundColor(getResources().getColor(R.color.white));
//                objective_of_visit = "Sale Call";
//            }
//        });
//        li_fas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                li_fas.setBackgroundColor(getResources().getColor(R.color.selected));
//
//                li_saleCall.setBackgroundColor(getResources().getColor(R.color.white));
//                li_prospecting.setBackgroundColor(getResources().getColor(R.color.white));
//                li_complaintHandling.setBackgroundColor(getResources().getColor(R.color.white));
//                objective_of_visit = "FAS";
//
//            }
//        });
//        li_prospecting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                li_prospecting.setBackgroundColor(getResources().getColor(R.color.selected));
//
//                li_saleCall.setBackgroundColor(getResources().getColor(R.color.white));
//                li_fas.setBackgroundColor(getResources().getColor(R.color.white));
//                li_complaintHandling.setBackgroundColor(getResources().getColor(R.color.white));
//                objective_of_visit = "Prospecting";
//            }
//        });
//        li_complaintHandling.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                li_complaintHandling.setBackgroundColor(getResources().getColor(R.color.selected));
//
//                li_saleCall.setBackgroundColor(getResources().getColor(R.color.white));
//                li_fas.setBackgroundColor(getResources().getColor(R.color.white));
//                li_prospecting.setBackgroundColor(getResources().getColor(R.color.white));
//                objective_of_visit = "Complaint handling";
//            }
//        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmersStartActivity.this,  VisitFarmerActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmersStartActivity.this, VisitFarmerActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void saveFarmerNoAvailale() {
        HashMap<String, String> headerParams = new HashMap<>();
        long time = System.currentTimeMillis();

        headerParams.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, String.valueOf(time));
        headerParams.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, sHelper.getString(Constants.ACTIVITY_STATUS_FARMER));
        headerParams.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        if(planType=="TODAY") {
            headerParams.put(mydb.KEY_PLAN_TYPE, "TODAY");
        }
        else
        {
            headerParams.put(mydb.KEY_PLAN_TYPE, "ALL");
        }
        headerParams.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, String.valueOf(checkinlat));
        headerParams.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, String.valueOf(checkinlng));
        mydb.addData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, headerParams);
        updateOutletStatus("Not Available");
        addCheckoutLocation();
    }
    private void updateOutletStatus(String status) {
        HashMap<String, String> params = new HashMap<>();
        params.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, status);
        params.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        filter.put(mydb.KEY_PLAN_TYPE, planType);
        mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN, params, filter);
    }
    private void addCheckoutLocation()
    {
        long time = System.currentTimeMillis();
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, String.valueOf(checkinlat));
        headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, String.valueOf(checkinlng));
        headerParams.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        headerParams.put(mydb.KEY_PLAN_TYPE, journeytype);
        headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP,String.valueOf(time));
        mydb.addData(mydb.TODAY_JOURNEY_PLAN_POST_DATA,headerParams);
    }

    private void addDataInCheckInTable() {
        mydb = new MyDatabaseHandler(getApplicationContext());
        HashMap<String, String> map = new HashMap<>();
        long time = System.currentTimeMillis();

        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, Constants.FARMER_ID);
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID, null);
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, Constants.JOURNEY_PLAN_ID);
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, sHelper.getString(Constants.FARMER_NAME));
        map.put(mydb.KEY_TODAY_LATITUTE, "0.0");
        map.put(mydb.KEY_TODAY_LONGITUTE, "0.0");
        map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, sHelper.getString(Constants.MOBIL_NO));
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, sHelper.getString(Constants.SALES_POINT));
        map.put(mydb.KEY_TODAY_DISTANCE, "0");
        map.put(mydb.KEY_TODAY_VISIT_OBJECTIVE, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME));
        map.put(mydb.KEY_TODAY_STATUS, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE));
        map.put(mydb.KEY_TODAY_OUTLET_STATUS_ID, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE));
        map.put(mydb.KEY_TODAY_CHECK_IN_TIMESTAMP, String.valueOf(time));
        map.put(mydb.KEY_TODAY_CHECKIN_LATITUTE, sHelper.getString(Constants.FARMER_LAT));
        map.put(mydb.KEY_TODAY_CHECKIN_LONGITUTE, sHelper.getString(Constants.FARMER_LONG));
        map.put(mydb.KEY_TODAY_CEHCKOUT_TIMESTMAP, String.valueOf(time));
        map.put(mydb.KEY_TODAY_CHECKOUT_LATITUTE, "0.0");
        map.put(mydb.KEY_TODAY_CHECKOUT_LONGITUTE, "0.0");
        map.put(mydb.KEY_PLAN_TYPE, planType);

        mydb.addData(mydb.TODAY_FARMER_CHECKIN, map);
    }

    public void addActivity(){
        mydb = new MyDatabaseHandler(getApplicationContext());
        HashMap<String, String> map = new HashMap<>();
        long time = System.currentTimeMillis();

        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, String.valueOf(time));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, sHelper.getString(Constants.ACTIVITY_STATUS_FARMER));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME));
        map.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, String.valueOf(checkinlat));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, String.valueOf(checkinlng));
        mydb.addData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, map);
    }

    public void updateActivity(){
        mydb = new MyDatabaseHandler(this);
        HashMap<String, String> map = new HashMap<>();
        long time = System.currentTimeMillis();

        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, String.valueOf(time));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, sHelper.getString(Constants.ACTIVITY_STATUS_FARMER));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE, sHelper.getString(Constants.ACTIVITY_FARMER_OBJECTIVE_NAME));
        map.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, String.valueOf(checkinlat));
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, String.valueOf(checkinlng));

        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, map, filter);

    }

    public boolean isUNpostedDataExsists(){
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, map, filer);
        if (cursor.getCount() >0 ) {
            cursor.moveToFirst();
            do{
                flag = true;
            }while (cursor.moveToNext());
        }else{
            flag = false;
        }
        return flag;
    }
    public void  loadRoles()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ROLE_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ROLES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                rolename = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ROLE_NAME)));
                while (cursor.moveToNext()) ;
            }
            while (cursor.moveToNext());


        }
        else
        {
            rolename =  extraHelper.getString(Constants.ROLE);
        }



    }

    public void loadActivity(){
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS, "");
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, map, filer);
        if (cursor.getCount() >0 ) {
            cursor.moveToFirst();
            do{
                checkinLayout = cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS));
                objectivelayout = cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS));

            }while (cursor.moveToNext());
            if (checkinLayout.equals("1")){
                setTextViewDrawableColor(txt_checkin, R.color.red);
                setTextViewDrawableColor(tvNotAvailable, R.color.green);
                sHelper.setString(Constants.ACTIVITY_STATUS_FARMER,"1");
                status = "1";
            }else if (checkinLayout.equals("3")){
                setTextViewDrawableColor(txt_checkin, R.color.green);
                setTextViewDrawableColor(tvNotAvailable, R.color.red);
                sHelper.setString(Constants.ACTIVITY_STATUS_FARMER,"3");
                status = "3";
            }
            if (objectivelayout!=null){
                if (objectivelayout.equals("1")){
                    setTextViewDrawableColor(txt_salescall, R.color.red);
                    setTextViewDrawableColor(txt_fas, R.color.green);
                    setTextViewDrawableColor(txt_prospecting, R.color.green);
                    setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                    sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE,"1");
                    objective = "1";
                }else if(objectivelayout.equals("2")){
                    setTextViewDrawableColor(txt_salescall, R.color.green);
                    setTextViewDrawableColor(txt_fas, R.color.red);
                    setTextViewDrawableColor(txt_prospecting, R.color.green);
                    setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                    sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE,"2");
                    objective = "2";
                }else if(objectivelayout.equals("3")){
                    setTextViewDrawableColor(txt_salescall, R.color.green);
                    setTextViewDrawableColor(txt_fas, R.color.green);
                    setTextViewDrawableColor(txt_prospecting, R.color.red);
                    setTextViewDrawableColor(txt_complaint_handling, R.color.green);
                    sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE,"3");
                    objective = "3";
                }else if(objectivelayout.equals("4")){
                    setTextViewDrawableColor(txt_salescall, R.color.green);
                    setTextViewDrawableColor(txt_fas, R.color.green);
                    setTextViewDrawableColor(txt_prospecting, R.color.green);
                    setTextViewDrawableColor(txt_complaint_handling, R.color.red);
                    sHelper.setString(Constants.ACTIVITY_FARMER_OBJECTIVE,"4");
                    objective = "4";
                }
            }
        }else{
            setTextViewDrawableColor(txt_salescall, R.color.green);
            setTextViewDrawableColor(txt_fas, R.color.green);
            setTextViewDrawableColor(txt_prospecting, R.color.green);
            setTextViewDrawableColor(txt_complaint_handling, R.color.green);
            setTextViewDrawableColor(tvNotAvailable, R.color.green);
            setTextViewDrawableColor(txt_checkin, R.color.green);

        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }


}
