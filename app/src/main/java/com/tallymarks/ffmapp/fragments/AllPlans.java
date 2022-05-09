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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.DealersInsightActivity;
import com.tallymarks.ffmapp.activities.FarmersStartActivity;
import com.tallymarks.ffmapp.activities.StartActivity;
import com.tallymarks.ffmapp.adapters.AllPlanAdapter;
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
    TextView txt_no_data;
    FusedLocationProviderClient fusedLocationProviderClient;


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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mydb = new MyDatabaseHandler(getActivity());
        db = new DatabaseHandler(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        txt_no_data = view.findViewById(R.id.empty_view);
        sHelper = new SharedPrefferenceHelper(getActivity());
        planList.clear();
        gps = new GpsTracker(getActivity());
        if (gps.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location1) {
                    currentlat = String.valueOf(location1.getLatitude());
                    currentlng = String.valueOf(location1.getLongitude());
                    if(activity.equals("customers")) {
                        sHelper.setString(Constants.PLAN_TYPE, "all");
                        getAllCustomerJourneyPlan();
                        if (planList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            txt_no_data.setVisibility(View.VISIBLE);

                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            txt_no_data.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        prepareMovieData(activity);
                        if (planList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            txt_no_data.setVisibility(View.VISIBLE);
                            txt_no_data.setText(getResources().getString(R.string.no_farmer_available));

                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            txt_no_data.setVisibility(View.GONE);
                        }
                    }
                    AllPlanAdapter adapter = new AllPlanAdapter(planList, activity);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.setClickListener(AllPlans.this);
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
                }});
//            currentlat = String.valueOf(gps.getLatitude());
//            currentlng = String.valueOf(gps.getLongitude());
        }
        else
        {
            DialougeManager.gpsNotEnabledPopup(getActivity());
        }


    }
    private void getAllCustomerJourneyPlan()
    {
        String  customerCode="" , customerName="", customerCategory="" , customerLat="", customerLng="" ,customersalesPoint="" , customerVisit="", customerID= "",customerDayid="",custoemrjourneyplanID="",locationStatus="";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CATEGORY, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LOCATION_STATUS, "");
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
                customerCategory = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CATEGORY)));
                customerName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));
                customersalesPoint = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME)));
                customerVisit = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED)));
                customerLat = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE));
                customerLng = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE));
                locationStatus = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LOCATION_STATUS));
                plan.setCustomercode(customerCode);
                plan.setMemebrship(customerVisit);
                plan.setTitle(customerName);
                plan.setSalespoint(customersalesPoint);
                plan.setLocationStauts(locationStatus);
                plan.setTime("9:00 AM");
                plan.setCustomercategory(customerCategory);
                plan.setCustomerJourneyPlanID(custoemrjourneyplanID);
                plan.setCustomerDayID(customerDayid);
                plan.setLatitude(customerLat);
                plan.setLongitude(customerLng);

                plan.setCustomerID(customerID);
                if(!customerLat.equals("NA") && !customerLng.equals("NA")) {
                    float distance = getMeterFromLatLong(Float.parseFloat(currentlat), Float.parseFloat(currentlng), Float.parseFloat(customerLat), Float.parseFloat(customerLng));

                    float totaldistance = distance / 1000;
                    String radius = "500";
                    int totalb = (int) Math.round(totaldistance);
                    int c = (int) Math.round(distance);
                    boolean isWithinradius = c <= Integer.parseInt(radius) + 50;
                    if (isWithinradius) {
                        plan.setDistance("Reached");
                    } else {
                        plan.setDistance(totalb + " Km away");
                    }
                }
                else

                {
                    plan.setDistance("NA");
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
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, "");
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
        final TodayPlan plan = planList.get(position);
        // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if (activity.equals("customers")) {
            if(planList.get(position).getMemebrship().equals("Not Visited")) {
                gps = new GpsTracker(getActivity());
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(@NonNull Location location1) {
                            currentlat = String.valueOf(location1.getLatitude());
                            currentlng = String.valueOf(location1.getLongitude());
                        }});
//                    currentlat = String.valueOf(gps.getLatitude());
//                    currentlng = String.valueOf(gps.getLongitude());
                    if (plan.getLatitude().equals("NA") || plan.getLatitude() == "NA" && plan.getLongitude().equals("NA") || plan.getLongitude() == "NA") {
                        Helpers.alertWarning(getActivity(),"Location info not available","Warning",null,null);
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                        alertDialogBuilder
//                                .setMessage("Location info not available")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.cancel();
//
//                                                //Toast.makeText(ShopStatusActivity.this, "You are "+totalb+" Km away from the shop ", Toast.LENGTH_SHORT).show();
//                                            }
////                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        dialog.cancel();
////                                    }
//                                        }
//                                );
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();

                    } else {
                        float distance = getMeterFromLatLong(Float.parseFloat(currentlat), Float.parseFloat(currentlng), Float.parseFloat(plan.getLatitude()), Float.parseFloat(plan.getLongitude()));
                        float totaldistance = distance / 1000;
                        String radius = "450";
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
                            Intent i = new Intent(getActivity(), DealersInsightActivity.class);
                            startActivity(i);
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder
                                    .setMessage("You are " + totalb + " Km" + "(" + totalmeters + " Meters" + ")" + " away from the shop. ")
                                    .setCancelable(false)
                                    .setIcon(R.drawable.ic_baseline_warning_24)
                                    .setTitle("Warning")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
//                                        sHelper.setString(Constants.CUSTOMER_ID, plan.getCustomerID());
//                                        sHelper.setString(Constants.CUSTOMER_CODE, plan.getCustomercode());
//                                        sHelper.setString(Constants.CUSTOMER_NAME, plan.getTitle());
//                                        sHelper.setString(Constants.CUSTOMER_LAT, plan.getLatitude());
//                                        sHelper.setString(Constants.PLAN_TYPE, "all");
//                                        sHelper.setString(Constants.CUSTOMER_LNG, plan.getLongitude());
//                                        sHelper.setString(Constants.CUSTOMER_DAY_ID, plan.getCustomerDayID());
//                                        sHelper.setString(Constants.CUSTOMER_JOURNEYPLAN_ID, plan.getCustomerJourneyPlanID());
//                                        sHelper.setString(Constants.CUSTOMER_SALES_POINT_NAME, plan.getSalespoint());
//                                        Intent i = new Intent(getActivity(), StartActivity.class);
//                                        startActivity(i);
                                                    //Toast.makeText(ShopStatusActivity.this, "You are "+totalb+" Km away from the shop ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
//                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                }
                                    );
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    }
                }
                else {
                    DialougeManager.gpsNotEnabledPopup(getActivity());
                }
            }
            else
            {
                Toast.makeText(getActivity(), "You Already performed The Activity against "+plan.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(planList.get(position).getMemebrship().equals("Not Visited")) {
                //sHelper.clearPreferenceStore();
                gps = new GpsTracker(getActivity());
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(@NonNull Location location1) {
                            currentlat = String.valueOf(location1.getLatitude());
                            currentlng = String.valueOf(location1.getLongitude());
                        }});
//                    currentlat = String.valueOf(gps.getLatitude());
//                    currentlng = String.valueOf(gps.getLongitude());
                    if (plan.getLatitude().equals("NA") || plan.getLatitude() == "NA" && plan.getLongitude().equals("NA") || plan.getLongitude() == "NA") {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage("Location info not available, Do you want to proceed?")
                                .setCancelable(false)
                                .setTitle("Warning")
                                .setIcon(R.drawable.ic_baseline_warning_24)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
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
                                        gameData.putString(Constants.PLAN_TYPE_FARMER, "ALL");
                                        i.putExtras(gameData);
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
                else {

                        DialougeManager.gpsNotEnabledPopup(getActivity());

                }

                // set farmers details
//                Constants.FARMER_ID = planList.get(position).getLocation();
//                Constants.JOURNEY_PLAN_ID = planList.get(position).getCustomercode();
//                sHelper.setString(Constants.S_FARMER_ID, planList.get(position).getLocation());
//                sHelper.setString(Constants.S_JOURNEY_PLAN_ID, planList.get(position).getCustomercode());
//                sHelper.setString(Constants.FARMER_NAME, planList.get(position).getFatherName());
//                sHelper.setString(Constants.MOBIL_NO, planList.get(position).getMobilenumber());
//                sHelper.setString(Constants.SALES_POINT, planList.get(position).getSalespoint());
//                sHelper.setString(Constants.FARMER_LAT, currentlat);
//                sHelper.setString(Constants.FARMER_LONG, currentlng);
//                sHelper.setString(Constants.PLAN_TYPE_FARMER, "ALL");
//
//                Intent i = new Intent(getActivity(), FarmersStartActivity.class);
//                Bundle gameData = new Bundle();
//                gameData.putString(Constants.PLAN_TYPE_FARMER,"ALL");
//                i.putExtras(gameData);
//                startActivity(i);
            }
            else
            {
                Toast.makeText(getActivity(), "You Already performed The Activity against "+planList.get(position).getFatherName(), Toast.LENGTH_SHORT).show();
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
