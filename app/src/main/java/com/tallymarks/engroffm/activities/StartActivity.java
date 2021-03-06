package com.tallymarks.engroffm.activities;

import android.Manifest;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.DialougeManager;
import com.tallymarks.engroffm.utils.GpsTracker;
import com.tallymarks.engroffm.utils.Helpers;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    private TextView tvTopHeader, tvCheckin, tvNotAvailable;
    private TextView tvSalesCall, tvOrderTracking;
    private TextView tvInventory, tvMarketignIntelligence, tvComplainhandling;
    Button btn_next, btn_back;
    ImageView iv_menu, iv_back;
    DatabaseHandler db;
    private String checkinLayout = "", objectiveLayout = "";
    SharedPrefferenceHelper shelper;
    GpsTracker gps;
    String status = "";
    String objective = "";
    double checkinlat;
    double checkinlng;
    String journeytype = "";
    static {
        System.loadLibrary("native-lib");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
    }

    private void initView() {
        db = new DatabaseHandler(StartActivity.this);
        shelper = new SharedPrefferenceHelper(StartActivity.this);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        btn_next = findViewById(R.id.btn_next);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.back);
        tvCheckin = findViewById(R.id.txt_checkin);
        tvNotAvailable = findViewById(R.id.txt_notavailable);
        tvOrderTracking = findViewById(R.id.txt_ordertaking);
        tvSalesCall = findViewById(R.id.txt_salescall);
        tvInventory = findViewById(R.id.txt_inventory);
        tvMarketignIntelligence = findViewById(R.id.txt_market_intelligence);
        tvComplainhandling = findViewById(R.id.txt_complain);
        if(shelper!=null)
        {
            if(shelper.getString(Constants.PLAN_TYPE).equals("today"))
            {
                journeytype = "today";
            }
            else
                {
                journeytype = "all";
            }
        }
        loadActivity();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, VisitCustomerActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        tvTopHeader.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, VisitCustomerActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shelper.getString(Constants.ACTIVITY_STATUS) != null && !shelper.getString(Constants.ACTIVITY_STATUS).equals("") && !status.equals("")
                        && status !=null && objective!=null && !objective.equals("")
                        && shelper.getString(Constants.ACTIVITY_OBJECTIVE) != null && !shelper.getString(Constants.ACTIVITY_OBJECTIVE).equals("")) {
                      gps = new GpsTracker(StartActivity.this);
                        if (gps.canGetLocation()) {
                            if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            checkinlat = gps.getLatitude();
                            checkinlng = gps.getLongitude();
                            if (isUnpostedDataExist()) {
                                updateActivity();
                            } else {
                                addActivity();
                            }
                            if (shelper.getString(Constants.ACTIVITY_OBJECTIVE).equals("3") || shelper.getString(Constants.ACTIVITY_OBJECTIVE).equals("5")) {
                                Intent n = new Intent(StartActivity.this, FloorStockActivity.class);
                                startActivity(n);
                            }
                            else
                            {
                                Intent n = new Intent(StartActivity.this, QualityofSalesCallActivity.class);
                              //  n.putExtra("startfrom", "start");
                                startActivity(n);
                            }
                        }
                        else {
                            DialougeManager.gpsNotEnabledPopup(StartActivity.this);
                        }
                } else {
                    Helpers.alertWarning(StartActivity.this,"You must Select Objective and Status","Warning",null,null);
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
//                    alertDialogBuilder.setTitle(R.string.message)
//                            .setMessage("You must Select Objective and Status")
//                            .setCancelable(false)
//                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();

                }
            }
        });
        tvTopHeader.setText("START ACTIVITY");
        tvCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvCheckin, R.color.red);
                setTextViewDrawableColor(tvNotAvailable, R.color.green);
                status = "1";
                shelper.setString(Constants.ACTIVITY_STATUS, "1");

            }
        });
        tvNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvCheckin, R.color.green);
                setTextViewDrawableColor(tvNotAvailable, R.color.red);
                status = "0";
                shelper.setString(Constants.ACTIVITY_STATUS, "3");
                gps = new GpsTracker(StartActivity.this);
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    checkinlat = gps.getLatitude();
                    checkinlng = gps.getLongitude();
                    saveCustomerNoAvailale();
                    Intent n = new Intent(StartActivity.this, VisitCustomerActivity.class);
                    startActivity(n);

                }
            }
        });
        tvOrderTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvOrderTracking, R.color.red);
                setTextViewDrawableColor(tvComplainhandling, R.color.green);
                setTextViewDrawableColor(tvSalesCall, R.color.green);
                setTextViewDrawableColor(tvInventory, R.color.green);
                objective = tvOrderTracking.getText().toString();
                setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                shelper.setString(Constants.ACTIVITY_OBJECTIVE_NAME, tvOrderTracking.getText().toString());
                shelper.setString(Constants.ACTIVITY_OBJECTIVE, "2");

            }
        });
        tvComplainhandling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvOrderTracking, R.color.green);
                setTextViewDrawableColor(tvComplainhandling, R.color.red);
                setTextViewDrawableColor(tvSalesCall, R.color.green);
                setTextViewDrawableColor(tvInventory, R.color.green);
                objective = tvComplainhandling.getText().toString();
                shelper.setString(Constants.ACTIVITY_OBJECTIVE_NAME, tvComplainhandling.getText().toString());
                setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                shelper.setString(Constants.ACTIVITY_OBJECTIVE, "5");

            }
        });
        tvMarketignIntelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvOrderTracking, R.color.green);
                setTextViewDrawableColor(tvComplainhandling, R.color.green);
                setTextViewDrawableColor(tvSalesCall, R.color.green);
                setTextViewDrawableColor(tvInventory, R.color.green);
                objective = tvMarketignIntelligence.getText().toString();
                setTextViewDrawableColor(tvMarketignIntelligence, R.color.red);
                shelper.setString(Constants.ACTIVITY_OBJECTIVE_NAME, tvMarketignIntelligence.getText().toString());
                shelper.setString(Constants.ACTIVITY_OBJECTIVE, "4");

            }
        });
        tvSalesCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvOrderTracking, R.color.green);
                setTextViewDrawableColor(tvComplainhandling, R.color.green);
                setTextViewDrawableColor(tvSalesCall, R.color.red);
                setTextViewDrawableColor(tvInventory, R.color.green);
                objective = tvSalesCall.getText().toString();
                setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                shelper.setString(Constants.ACTIVITY_OBJECTIVE_NAME, tvSalesCall.getText().toString());
                shelper.setString(Constants.ACTIVITY_OBJECTIVE, "1");

            }
        });
        tvInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextViewDrawableColor(tvOrderTracking, R.color.green);
                setTextViewDrawableColor(tvComplainhandling, R.color.green);
                setTextViewDrawableColor(tvSalesCall, R.color.green);
                setTextViewDrawableColor(tvInventory, R.color.red);
                objective = tvInventory.getText().toString();
                setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                shelper.setString(Constants.ACTIVITY_OBJECTIVE, "3");
                shelper.setString(Constants.ACTIVITY_OBJECTIVE_NAME, tvInventory.getText().toString());


            }
        });
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public void addActivity() {

        HashMap<String, String> headerParams = new HashMap<>();
        long time = System.currentTimeMillis();

        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, String.valueOf(time));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBJECTIVE_STATUS, shelper.getString(Constants.ACTIVITY_OBJECTIVE));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, shelper.getString(Constants.ACTIVITY_STATUS));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE, shelper.getString(Constants.ACTIVITY_OBJECTIVE_NAME));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        if(journeytype=="today") {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "today");
        }
        else
        {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        }
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, String.valueOf(checkinlat));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, String.valueOf(checkinlng));
        db.addData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, headerParams);
        //sHelper.setString(Constants.SURVEY_ID,String.valueOf(totalscore));
    }

//    public void addObjective() {
//        HashMap<String, String> headerParams = new HashMap<>();
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_VISIT_OBJECTIVE, objective);
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_OUTLET_STATUS_ID, status);
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKIN_LATITUDE, String.valueOf(checkinlat));
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKIN_LONGITUDE, String.valueOf(checkinlng));
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_STATUS, status);
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
//        db.addData(db.TODAY_JOURNEY_PLAN_POST_DATA, headerParams);
//        //sHelper.setString(Constants.SURVEY_ID,String.valueOf(totalscore));
//    }
//
//    private void updateObjective() {
//        HashMap<String, String> headerParams = new HashMap<>();
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_VISIT_OBJECTIVE, objective);
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_OUTLET_STATUS_ID, status);
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKIN_LATITUDE, String.valueOf(checkinlat));
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKIN_LONGITUDE, String.valueOf(checkinlng));
//        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_STATUS, status);
//        HashMap<String, String> filter = new HashMap<>();
//        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
//        db.updateData(db.TODAY_JOURNEY_PLAN_POST_DATA, headerParams, filter);
//
//    }

    private void updateActivity() {
        long time = System.currentTimeMillis();
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, String.valueOf(time));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBJECTIVE_STATUS, shelper.getString(Constants.ACTIVITY_OBJECTIVE));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, shelper.getString(Constants.ACTIVITY_STATUS));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE, shelper.getString(Constants.ACTIVITY_OBJECTIVE_NAME));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, String.valueOf(checkinlat));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, String.valueOf(checkinlng));
        if(journeytype=="today") {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "today");
        }
        else
        {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        }
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeytype);
        db.updateData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, headerParams, filter);
    }

    private boolean isUnpostedDataExist() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBJECTIVE_STATUS, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, journeytype);
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }
//    private boolean isUnpostedObjective() {
//
//        boolean flag = false;
//        HashMap<String, String> map = new HashMap<>();
//        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_VISIT_OBJECTIVE, "");
//        HashMap<String, String> filters = new HashMap<>();
//        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
//        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_POST_DATA, map, filters);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            do {
//                flag = true;
//            }
//            while (cursor.moveToNext());
//        } else {
//            flag = false;
//        }
//        return flag;
//    }

    public void loadActivity() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBJECTIVE_STATUS, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, journeytype);
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                checkinLayout = cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS));
                objectiveLayout = cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBJECTIVE_STATUS));

            }
            while (cursor.moveToNext());
            if (checkinLayout.equals("1")) {
                setTextViewDrawableColor(tvCheckin, R.color.red);
                setTextViewDrawableColor(tvNotAvailable, R.color.green);
                shelper.setString(Constants.ACTIVITY_STATUS,"1");
                status = "1";
            } else if (checkinLayout.equals("3")) {
                setTextViewDrawableColor(tvNotAvailable, R.color.red);
                setTextViewDrawableColor(tvCheckin, R.color.green);
                shelper.setString(Constants.ACTIVITY_STATUS,"3");
                status = "3";
            }
            if(objectiveLayout!=null) {
                if (objectiveLayout.equals("1")) {
                    setTextViewDrawableColor(tvOrderTracking, R.color.green);
                    setTextViewDrawableColor(tvComplainhandling, R.color.green);
                    setTextViewDrawableColor(tvSalesCall, R.color.red);
                    setTextViewDrawableColor(tvInventory, R.color.green);
                    setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                    shelper.setString(Constants.ACTIVITY_OBJECTIVE,"1");
                    objective = "1";

                } else if (objectiveLayout.equals("2")) {
                    setTextViewDrawableColor(tvOrderTracking, R.color.red);
                    setTextViewDrawableColor(tvComplainhandling, R.color.green);
                    setTextViewDrawableColor(tvSalesCall, R.color.green);
                    setTextViewDrawableColor(tvInventory, R.color.green);
                    setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                    shelper.setString(Constants.ACTIVITY_OBJECTIVE,"2");
                    objective = "2";
                } else if (objectiveLayout.equals("3")) {
                    setTextViewDrawableColor(tvOrderTracking, R.color.green);
                    setTextViewDrawableColor(tvComplainhandling, R.color.green);
                    setTextViewDrawableColor(tvSalesCall, R.color.green);
                    setTextViewDrawableColor(tvInventory, R.color.red);
                    setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                    shelper.setString(Constants.ACTIVITY_OBJECTIVE,"3");
                    objective = "3";
                } else if (objectiveLayout.equals("4")) {
                    setTextViewDrawableColor(tvOrderTracking, R.color.green);
                    setTextViewDrawableColor(tvComplainhandling, R.color.green);
                    setTextViewDrawableColor(tvSalesCall, R.color.green);
                    setTextViewDrawableColor(tvInventory, R.color.green);
                    setTextViewDrawableColor(tvMarketignIntelligence, R.color.red);
                    shelper.setString(Constants.ACTIVITY_OBJECTIVE,"4");
                    objective = "4";
                } else if (objectiveLayout.equals("5")) {
                    setTextViewDrawableColor(tvOrderTracking, R.color.green);
                    setTextViewDrawableColor(tvComplainhandling, R.color.red);
                    setTextViewDrawableColor(tvSalesCall, R.color.green);
                    setTextViewDrawableColor(tvInventory, R.color.green);
                    setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
                    shelper.setString(Constants.ACTIVITY_OBJECTIVE,"5");
                    objective = "5";
                }
            }
        } else {
            setTextViewDrawableColor(tvOrderTracking, R.color.green);
            setTextViewDrawableColor(tvComplainhandling, R.color.green);
            setTextViewDrawableColor(tvSalesCall, R.color.green);
            setTextViewDrawableColor(tvInventory, R.color.green);
            setTextViewDrawableColor(tvMarketignIntelligence, R.color.green);
            setTextViewDrawableColor(tvCheckin, R.color.green);
            setTextViewDrawableColor(tvNotAvailable, R.color.green);
        }
    }
    private void saveCustomerNoAvailale()
    {
        HashMap<String, String> headerParams = new HashMap<>();
        long time = System.currentTimeMillis();

        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, String.valueOf(time));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, shelper.getString(Constants.ACTIVITY_STATUS));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        if(journeytype=="today") {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "today");
        }
        else
        {
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        }
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, String.valueOf(checkinlat));
        headerParams.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, String.valueOf(checkinlng));
        db.addData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, headerParams);
        updateOutletStatus("Not Available");
        addCheckoutLocation();
        //s
    }
    private void updateOutletStatus(String status) {
        HashMap<String, String> params = new HashMap<>();
        params.put(db.KEY_TODAY_JOURNEY_IS_VISITED, status);
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED_INTERNET_AVAILALE, "0");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeytype);
        db.updateData(db.TODAY_JOURNEY_PLAN, params, filter);
    }
    private void addCheckoutLocation()
    {
        long time = System.currentTimeMillis();
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE, "");
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE, "");
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, shelper.getString(Constants.CUSTOMER_ID));
        headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, journeytype);
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP,String.valueOf(time));
        db.addData(db.TODAY_JOURNEY_PLAN_POST_DATA,headerParams);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(StartActivity.this, VisitCustomerActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
