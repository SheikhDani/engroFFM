package com.tallymarks.ffmapp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.FarmersStartActivity;
import com.tallymarks.ffmapp.activities.MainActivity;
import com.tallymarks.ffmapp.activities.StartActivity;
import com.tallymarks.ffmapp.adapters.DealersAdapter;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.tasks.GetCompanHeldBrandBasicList;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodaysPlan extends Fragment implements ItemClickListener {

    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String ARG_TEXT = "activityname";
    private String activity;
    DatabaseHandler db;
    GpsTracker gps;
    String currentlat;
    String  currentlng;
    SharedPrefferenceHelper sHelper;
    static EditText et_search_plan;




    public static TodaysPlan newInstance(String activity, EditText et_search) {
        TodaysPlan fragment = new TodaysPlan();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, activity);
        et_search_plan = et_search;
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
            sHelper.setString(Constants.PLAN_TYPE, "today");
            getTodayCustomerJourneyPlan();
        }
        else
        {
            prepareMovieData(activity);
        }


        TodayPlanAdapter adapter = new TodayPlanAdapter(planList, activity);
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
    }
    private void getTodayCustomerJourneyPlan()
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
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "today");
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
        if (activity.equals("customers")) {
            com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
            plan.setCustomercode("Customer Code: 8134560");
            plan.setMemebrship("Visited");
            plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan.setTime("9:00 AM");
            plan.setTitle("Aqib Ali");
            planList.add(plan);

            com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan2.setCustomercode("Customer Code: 8134560");
            plan2.setMemebrship("Not Visited");
            plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan2.setTime("9:00 AM");
            plan2.setTitle("786 Fertilizer");
            planList.add(plan2);

            com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan3.setCustomercode("Customer code: 8134560");
            plan3.setMemebrship("Not Visited");
            plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan3.setTime("9:00 AM");
            plan3.setTitle("Aamir Behzad");
            planList.add(plan3);

            com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan4.setCustomercode("Customer Code: 8134560");
            plan4.setMemebrship("Visited");
            plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan4.setTime("9:00 AM");
            plan4.setTitle("Aamir Shafiq and Brothers");
            planList.add(plan4);

            com.tallymarks.ffmapp.models.TodayPlan plan5 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan5.setCustomercode("Customer Code: 8134560");
            plan5.setMemebrship("Not Visited");
            plan5.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan5.setTime("9:00 AM");
            plan5.setTitle("illahi Bukhsh");
            planList.add(plan5);

            com.tallymarks.ffmapp.models.TodayPlan plan6 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan6.setCustomercode("Customer Code: 8134560");
            plan6.setMemebrship("Visited");
            plan6.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan6.setTime("9:00 AM");
            plan6.setTitle("Abid Hussain &  Brothers");
            planList.add(plan6);

            com.tallymarks.ffmapp.models.TodayPlan plan7 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan7.setCustomercode("Customer Code: 8134560");
            plan7.setMemebrship("Not Visited");
            plan7.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan7.setTime("9:00 AM");
            plan7.setTitle("Abid Hussain &  Brothers");
            planList.add(plan7);
            com.tallymarks.ffmapp.models.TodayPlan plan8 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan8.setCustomercode("Customer Code: 8134560");
            plan8.setMemebrship("Visited");
            plan8.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan8.setTime("9:00 AM");
            plan8.setTitle("Abid Hussain &  Brothers");
            planList.add(plan8);
        } else {

            com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
            plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan.setTitle("Dr. Athar Rasheed");
            plan.setLocation("Lahore");
            plan.setMemebrship("Visited");
            plan.setMobilenumber("023123421312");
            planList.add(plan);

            com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan2.setTitle("Dr. Athar Rasheed");
            plan2.setMemebrship("Visited");
            plan2.setLocation("Lahore");
            plan2.setMobilenumber("023123421312");
            planList.add(plan2);

            com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan3.setTitle("Dr. Athar Rasheed");
            plan3.setLocation("Lahore");
            plan3.setMemebrship("Visited");
            plan3.setMobilenumber("023123421312");
            planList.add(plan3);

            com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan4.setTitle("Dr. Athar Rasheed");
            plan4.setLocation("Lahore");
            plan4.setMemebrship("Visited");
            plan4.setMobilenumber("023123421312");
            planList.add(plan4);


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
                        sHelper.setString(Constants.PLAN_TYPE, "today");
                        sHelper.setString(Constants.CUSTOMER_LAT, plan.getLatitude());
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
                                        sHelper.setString(Constants.PLAN_TYPE, "today");
                                        sHelper.setString(Constants.CUSTOMER_NAME, plan.getTitle());
                                        sHelper.setString(Constants.CUSTOMER_LAT, plan.getLatitude());
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
                Toast.makeText(getActivity(), "You Already performed The Activity for That Customer", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Intent i = new Intent(getActivity(), FarmersStartActivity.class);
            startActivity(i);
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
