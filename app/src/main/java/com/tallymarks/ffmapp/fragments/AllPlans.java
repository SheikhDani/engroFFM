package com.tallymarks.ffmapp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.FarmersStartActivity;
import com.tallymarks.ffmapp.activities.StartActivity;
import com.tallymarks.ffmapp.adapters.AllPlanAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllPlans extends Fragment implements ItemClickListener {
    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String ARG_TEXT = "activityname";
    private String activity;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    GpsTracker gps;
    String currentlat;
    String  currentlng;
    SharedPrefferenceHelper sHelper;
    static EditText et_search_plan;


    public static AllPlans newInstance(String activity,EditText et) {
        AllPlans fragment = new AllPlans();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, activity);
        et_search_plan = et;
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            activity = getArguments().getString(ARG_TEXT);

        }
        mydb = new MyDatabaseHandler(getActivity());
        db = new DatabaseHandler(getActivity());
        sHelper = new SharedPrefferenceHelper(getActivity());
        planList.clear();
        gps = new GpsTracker(getActivity());
        if (gps.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            currentlat = String.valueOf(gps.getLatitude());
            currentlng = String.valueOf(gps.getLongitude());
        }
        else
        {
            DialougeManager.gpsNotEnabledPopup(getActivity());
        }
        if(activity.equals("customers")) {
            sHelper.setString(Constants.PLAN_TYPE, "all");
            getAllCustomerJourneyPlan();
        }
        else
        {
            prepareMovieData(activity);
        }
        AllPlanAdapter adapter = new AllPlanAdapter(planList, activity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        if(activity.equals("customers")) {
            et_search_plan.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if(et_search_plan.hasFocus()) {
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
        else {
            et_search_plan.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if(et_search_plan.hasFocus()) {
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

    }
    private void getAllCustomerJourneyPlan()
    {
        String  customerCode="" , customerName="" , customerLat="", customerLng="" ,customersalesPoint="" , customerVisit="", customerID= "",customerDayid="",custoemrjourneyplanID="";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                TodayPlan plan = new TodayPlan();
                customerCode = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE));
                customerDayid = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID));
                custoemrjourneyplanID = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID));
                customerID = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                customerName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));
                customersalesPoint = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME)));
                customerVisit = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED)));
                customerLat = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE));
                customerLng = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE));
                plan.setCustomercode(customerCode);
                plan.setMemebrship(customerVisit);
                plan.setTitle(customerName);
                plan.setSalespoint(customersalesPoint);
                plan.setTime("9:00 AM");
                plan.setCustomerJourneyPlanID(custoemrjourneyplanID);
                plan.setCustomerDayID(customerDayid);
                plan.setLatitude(customerLat);
                plan.setLongitude(customerLng);
                plan.setCustomerID(customerID);
                float distance = getMeterFromLatLong(Float.parseFloat(currentlat), Float.parseFloat(currentlng), Float.parseFloat(customerLat), Float.parseFloat(customerLng));
                float totaldistance = distance / 1000;
                String radius = "500";
                int totalb = (int) Math.round(totaldistance);
                int c = (int) Math.round(distance);
                boolean isWithinradius = c <= Integer.parseInt(radius) + 50;
                if (isWithinradius) {
                    plan.setDistance("Reached");
                }
                else {
                    plan.setDistance(totalb + " Km away");
                }
                planList.add(plan);


            }
            while (cursor.moveToNext());
        }
    }
    private void prepareMovieData(String activity) {
        planList.clear();
        mydb = new MyDatabaseHandler(getActivity());
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, "");
        map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, "");
        map.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, "");
        //map.put(db.KEY_TODAY_JOURNEY_FARMER_NAME, "");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_PLAN_TYPE, "ALL");
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
                plan.setSalespoint("" + Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME))));
                plan.setTitle("" + Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))));
                plan.setLocation(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID)));
                plan.setMemebrship("" + Helpers.clean(cursor.getString((cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_IS_VISITED)))));
                plan.setFatherName("" + Helpers.clean(cursor.getString((cursor.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME)))));
                plan.setMobilenumber(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_MOBILE_NO)));
                plan.setCustomercode("");
                planList.add(plan);

            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    @Override
    public void onClick(View view, int position) {
        final TodayPlan plan = planList.get(position);
        // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if (activity.equals("customers")) {
            if(planList.get(position).getMemebrship().equals("Not Visited")) {
                gps = new GpsTracker(getActivity());
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    currentlat = String.valueOf(gps.getLatitude());
                    currentlng = String.valueOf(gps.getLongitude());
                    float distance = getMeterFromLatLong(Float.parseFloat(currentlat), Float.parseFloat(currentlng), Float.parseFloat(plan.getLatitude()), Float.parseFloat(plan.getLongitude()));
                    float totaldistance = distance / 1000;
                    String radius = "50";
                    int totalmeters = (int) Math.round(distance);
                    int totalb = (int) Math.round(totaldistance);
                    int c = (int) Math.round(distance);
                    boolean isWithinradius = c <= Integer.parseInt(radius) + 50;
                    if (isWithinradius) {
                        sHelper.setString(Constants.CUSTOMER_ID, plan.getCustomerID());
                        sHelper.setString(Constants.CUSTOMER_CODE, plan.getCustomercode());
                        sHelper.setString(Constants.CUSTOMER_NAME, plan.getTitle());
                        sHelper.setString(Constants.CUSTOMER_LAT, plan.getLatitude());
                        sHelper.setString(Constants.PLAN_TYPE, "all");
                        sHelper.setString(Constants.CUSTOMER_LNG, plan.getLongitude());
                        sHelper.setString(Constants.CUSTOMER_DAY_ID, plan.getCustomerDayID());
                        sHelper.setString(Constants.CUSTOMER_JOURNEYPLAN_ID, plan.getCustomerJourneyPlanID());
                        sHelper.setString(Constants.CUSTOMER_SALES_POINT_NAME, plan.getSalespoint());
                        Intent i = new Intent(getActivity(), StartActivity.class);
                        startActivity(i);
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage("You are " + totalb + " Km" + "(" + totalmeters + " Meters" + ")" + " away from the shop. ")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sHelper.setString(Constants.CUSTOMER_ID, plan.getCustomerID());
                                        sHelper.setString(Constants.CUSTOMER_CODE, plan.getCustomercode());
                                        sHelper.setString(Constants.CUSTOMER_NAME, plan.getTitle());
                                        sHelper.setString(Constants.CUSTOMER_LAT, plan.getLatitude());
                                        sHelper.setString(Constants.PLAN_TYPE, "all");
                                        sHelper.setString(Constants.CUSTOMER_LNG, plan.getLongitude());
                                        sHelper.setString(Constants.CUSTOMER_DAY_ID, plan.getCustomerDayID());
                                        sHelper.setString(Constants.CUSTOMER_JOURNEYPLAN_ID, plan.getCustomerJourneyPlanID());
                                        sHelper.setString(Constants.CUSTOMER_SALES_POINT_NAME, plan.getSalespoint());
                                        Intent i = new Intent(getActivity(), StartActivity.class);
                                        startActivity(i);
                                        //Toast.makeText(ShopStatusActivity.this, "You are "+totalb+" Km away from the shop ", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }
                        );
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(), "You Already Performed the Activity for that Customer", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(planList.get(position).getMemebrship().equals("Not Visited")) {
                //sHelper.clearPreferenceStore();
                gps = new GpsTracker(getActivity());
                if (gps.canGetLocation()) {
//                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
                    currentlat = String.valueOf(gps.getLatitude());
                    currentlng = String.valueOf(gps.getLongitude());

                }

                // set farmers details
                Constants.FARMER_ID = planList.get(position).getLocation();
                Constants.JOURNEY_PLAN_ID = planList.get(position).getCustomercode();
                sHelper.setString(Constants.S_FARMER_ID, planList.get(position).getLocation());
                sHelper.setString(Constants.S_JOURNEY_PLAN_ID, planList.get(position).getCustomercode());
                sHelper.setString(Constants.FARMER_NAME, planList.get(position).getFatherName());
                sHelper.setString(Constants.MOBIL_NO, planList.get(position).getMobilenumber());
                sHelper.setString(Constants.SALES_POINT, planList.get(position).getSalespoint());
                sHelper.setString(Constants.FARMER_LAT, currentlat);
                sHelper.setString(Constants.FARMER_LONG, currentlng);
                sHelper.setString(Constants.PLAN_TYPE_FARMER, "ALL");

                Intent i = new Intent(getActivity(), FarmersStartActivity.class);
                Bundle gameData = new Bundle();
                gameData.putString(Constants.PLAN_TYPE_FARMER,"ALL");
                i.putExtras(gameData);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getActivity(), "You Already performed The Activity for That Farmer", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public static float getMeterFromLatLong(float lat1, float lng1, float lat2, float lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }
}
