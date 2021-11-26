package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Recommendations;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.SoilSamplingCrops;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Activity;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.FarmerCheckIn;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Recommendation;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Sampling;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SoilSamplingActivity extends AppCompatActivity {
    private TextView tvTopHeader, acres, blocks, txt_lat, txt_reference,txt_lng;
    TextView txtacre, txtblock,txtpreviouscrop, txtcurrentcrop,txtdepth,txtsavelocation;
    AutoCompleteTextView auto_pre_rop,auto_current_crop,auto_current_crop2,auto_depth;
    ImageView iv_menu,iv_back;
    private TableLayout mTableLayout;
    private HttpHandler httpHandler;
    Button btn_add_sampling, btn_save, btn_lcoation;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    String planType = "";
    GpsTracker gps;
    double soilSamplingLat ;
    double soilSamplingLong ;
    SharedPrefferenceHelper sHelper;
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> depthArraylist = new ArrayList<>();
    ArrayList<String> depthIDArraylist = new ArrayList<>();
    ArrayList<SoilSamplingCrops> arraylistSoilSampling = new ArrayList<SoilSamplingCrops>();
    HashMap<String, String> soilhashmap = new HashMap<>();
    FarmerCheckIn farmerCheckIn;
    Double checkoutlat = null, checkoutlng = null;
    String checkinlat;
    String checkinlong;
    ExtraHelper extraHelper;
    String rolename="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_sampling);
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
        extraHelper = new ExtraHelper(SoilSamplingActivity.this);
        auto_pre_rop = findViewById(R.id.auto_pre_crop);
        txtacre = findViewById(R.id.txt_acre);
        txtblock = findViewById(R.id.txt_block);
        txtpreviouscrop = findViewById(R.id.txt_prev_crop);
        txtcurrentcrop = findViewById(R.id.txt_curr_crop);
        txtdepth = findViewById(R.id.txt_depth);
        txtsavelocation = findViewById(R.id.txt_location);
        btn_save = findViewById(R.id.btn_save);
        auto_current_crop = findViewById(R.id.auto_cr_crop);
        db = new DatabaseHandler(SoilSamplingActivity.this);
        acres = findViewById(R.id.acres);
        blocks = findViewById(R.id.blocks);
        txt_reference = findViewById(R.id.txt_reference);
        txt_lat = findViewById(R.id.txt_lat);
        txt_lng = findViewById(R.id.txt_lng);



        btn_add_sampling = findViewById(R.id.btn_add_sampling);
        btn_lcoation = findViewById(R.id.btn_lcoation);
        mydb = new MyDatabaseHandler(SoilSamplingActivity.this);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        farmerCheckIn = new FarmerCheckIn();

        sHelper = new SharedPrefferenceHelper(getApplicationContext());
        auto_current_crop2 = findViewById(R.id.auto_current_crop_2);
        auto_depth= findViewById(R.id.auto_depth);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("SOIL SAMPLING");
//        Intent intent = getIntent();
//        if(intent.getExtras()!=null) {
//            if (intent.getExtras().getString("soillat") != null &&
//                    !intent.getExtras().getString("soillat").equals("") &&
//                    intent.getExtras().getString("soillng") != null &&
//                    !intent.getExtras().getString("soillng").equals("")
//            ) {
//                soilSamplingLat = Double.parseDouble(intent.getExtras().getString("soillat"));
//                soilSamplingLong = Double.parseDouble(intent.getExtras().getString("soillng"));
//                txt_lat_lng.setText("Selected Lat,Log: " + soilSamplingLat + " , " + soilSamplingLong);
//                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, String.valueOf(soilSamplingLat));
//                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf(soilSamplingLong));
//            }
//        }
//         else{
//                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, "0.0");
//                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, "0.0");
//            }


//        if(sHelper!=null)
//        {
//            if(sHelper.getString(Constants.CUSTOM_LNG_SOIL)!=null &&
//                    !sHelper.getString(Constants.CUSTOM_LNG_SOIL).equals("") &&
//                    sHelper.getString(Constants.CUSTOM_LAT_SOIL)!=null &&
//                    !sHelper.getString(Constants.CUSTOM_LAT_SOIL).equals("")
//            )
//            {
//                soilSamplingLat = Double.parseDouble(sHelper.getString(Constants.CUSTOM_LAT_SOIL));
//                soilSamplingLong = Double.parseDouble(sHelper.getString(Constants.CUSTOM_LNG_SOIL));
//                txt_lat_lng.setText("Selected Lat,Log: " +  soilSamplingLat + " , " + soilSamplingLong);
//                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, String.valueOf(soilSamplingLat));
//                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf(soilSamplingLong));
//
//            }
//            else{
//                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, "0.0");
//                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, "0.0");
//            }
//
//        }
        final String arraylist[]={"Male","female","other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        //auto_pre_rop.setAdapter(arrayAdapter);
        //auto_current_crop.setAdapter(arrayAdapter);
        //auto_current_crop2.setAdapter(arrayAdapter);
        //auto_depth.setAdapter(arrayAdapter);
        auto_depth.setCursorVisible(false);
        auto_current_crop2.setCursorVisible(false);
        auto_current_crop.setCursorVisible(false);
        auto_pre_rop.setCursorVisible(false);


        SpannableStringBuilder tvAcre = setStarToLabel("Plot/Acre#");
        SpannableStringBuilder tvBlock= setStarToLabel("Block/Square#");
        SpannableStringBuilder tvPreviousCrop = setStarToLabel("Previous Crop");
        SpannableStringBuilder tvCurrentCrop = setStarToLabel("Current Crop");
        SpannableStringBuilder tvDepth = setStarToLabel("Depth");
        SpannableStringBuilder tvSvelocation = setStarToLabel("Save Location");

        txtacre.setText(tvAcre);
        txtblock.setText(tvBlock);
        txtpreviouscrop.setText(tvPreviousCrop);
        txtcurrentcrop.setText(tvCurrentCrop);
        txtdepth.setText(tvDepth);
        txtsavelocation.setText(tvSvelocation);



        //getCropfromDatabase();
        loadRoles();
        getDepthfromDatabase();
        loadCheckInLocation();
        if (isthisFarmerSamplingDataAlreadyExists()){
            // fill the fields from database

            HashMap<String, String> map = new HashMap<>();

            map.put(mydb.KEY_TODAY_FARMMMER_ID, "");
            map.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, "");
            map.put(mydb.KEY_TODAY_DEPTH_ID, "");
            map.put(mydb.KEY_TODAY_CROP1_ID, "");
            map.put(mydb.KEY_TODAY_CROP2_ID, "");
            map.put(mydb.KEY_TODAY_PLOT_NUMBER, "");
            map.put(mydb.KEY_TODAY_BLOCK_NUMBER, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_FARMMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            Cursor cursor = mydb.getData(mydb.TODAY_FARMER_SAMPLING, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    SoilSamplingCrops mysampling = new SoilSamplingCrops();
                    mysampling.setBlock(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_BLOCK_NUMBER)));
                    mysampling.setAcre(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_PLOT_NUMBER)));
                    mysampling.setPreviouscrop(getCropData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_PREVIOUSCROP_ID))));
                    mysampling.setCrop1(getCropData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROP1_ID))));
                    mysampling.setCrop2(getCropData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROP2_ID))));
                    mysampling.setDepth(depthArraylist.get(Integer.parseInt(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_DEPTH_ID)))-1));
                    arraylistSoilSampling.add(mysampling);

                }
                while (cursor.moveToNext());
            }
            drawRecommendationTable();
            gps = new GpsTracker(SoilSamplingActivity.this);
            if (gps.canGetLocation()) {
                if (ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                soilSamplingLat = gps.getLatitude();
                soilSamplingLong = gps.getLongitude();
            }
            txt_lat.setText("Selected Lat: " +  soilSamplingLat);
            txt_lng.setText("Selected Long: " +  soilSamplingLong);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        auto_current_crop.setAdapter(adapter);
        auto_current_crop2.setAdapter(adapter);
        auto_pre_rop.setAdapter(adapter);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, depthArraylist);
        auto_depth.setAdapter(adapter1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        txt_reference.setText(getUsernamefromDatabase() + "-" + currentDateandTime);
        auto_pre_rop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_pre_rop.showDropDown();
//                String selection = cropArraylist.get(position);
//                soilhashmap.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, cropIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT);

            }
        });

        auto_pre_rop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_pre_rop , "precrop",soilhashmap);
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//                        soilhashmap.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, cropIDArraylist.get(which));
//                        auto_pre_rop.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(SoilSamplingActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_pre_rop.showDropDown();

            }
        });
        auto_current_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_current_crop.showDropDown();
//                String selection = cropArraylist.get(position);
//                soilhashmap.put(mydb.KEY_TODAY_CROP1_ID, cropIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT);

            }
        });

        auto_current_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_current_crop, "currentcrop",soilhashmap);
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//                        soilhashmap.put(mydb.KEY_TODAY_CROP1_ID, cropIDArraylist.get(which));
//                        auto_current_crop.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(SoilSamplingActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_current_crop.showDropDown();
            }
        });
        auto_current_crop2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_current_crop2.showDropDown();
//                String selection = cropArraylist.get(position);
//                soilhashmap.put(mydb.KEY_TODAY_CROP2_ID, cropIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT);

            }
        });

        auto_current_crop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_current_crop2, "currentcropsecond",soilhashmap);
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//                        soilhashmap.put(mydb.KEY_TODAY_CROP2_ID, cropIDArraylist.get(which));
//                        auto_current_crop2.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(SoilSamplingActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_current_crop2.showDropDown();
            }
        });
        auto_depth.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_depth.showDropDown();
                String selection = depthArraylist.get(position);
                soilhashmap.put(mydb.KEY_TODAY_DEPTH_ID, depthIDArraylist.get(position));
                auto_depth.setText(selection);

                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_depth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final AlertDialog actions;
                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //goto category list with which as the category
                        String selection = depthArraylist.get(which);
                        soilhashmap.put(mydb.KEY_TODAY_DEPTH_ID, depthIDArraylist.get(which));
                        auto_depth.setText(selection);

                    }
                };

                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(SoilSamplingActivity.this);
                categoryAlert.setTitle("Depth List");

                categoryAlert.setItems(depthArraylist.toArray(new String[0]), actionListener);
                actions = categoryAlert.create();
                actions.show();

                auto_depth.showDropDown();
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(rolename.equals("Field Force Team") || rolename.equals("FieldAssistant")) {
                    Intent i = new Intent(SoilSamplingActivity.this, FarmVisitRoleWiseActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else
                {
                    Intent i = new Intent(SoilSamplingActivity.this, FarmVisitActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        btn_lcoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loc = new Intent(SoilSamplingActivity.this, CustomMap.class);
                loc.putExtra("from", "soil");
                startActivity(loc);

               gps = new GpsTracker(SoilSamplingActivity.this);
               if (gps.canGetLocation()) {
                  if (ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                      return;
                  }
                  soilSamplingLat = gps.getLatitude();
                  soilSamplingLong = gps.getLongitude();
             }
                txt_lat.setText("Selected Lat: " +  soilSamplingLat);
                txt_lng.setText("Selected Long: " +  soilSamplingLong);

                if(gps.getLatitude() != 0.0 && gps.getLongitude() != 0.0){
                   soilhashmap.put(mydb.KEY_TODAY_LATITUTE, String.valueOf(soilSamplingLat));
                   soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf(soilSamplingLong));
               }else{
                  soilhashmap.put(mydb.KEY_TODAY_LATITUTE, "0.0");
                    soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, "0.0");
                }
            }
        });
        btn_add_sampling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();


            }
        });
        prepareRecommendationData();
        drawRecommendationTable();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Helpers.isEmptyTextview(getApplicationContext(), acres))
                        && !(Helpers.isEmptyTextview(getApplicationContext(), blocks))
                        && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_pre_rop))
                        && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_current_crop))
                        && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_depth))
                        && soilSamplingLat!=0.0 && soilSamplingLong!=0.0
                ) {
                    // Checkout logic here

                    // Mapping for activity
                    mydb = new MyDatabaseHandler(getBaseContext());
                    long time = System.currentTimeMillis();

//                HashMap<String, String> map = new HashMap<>();
//                map.put(mydb.KEY_TODAY_CHECKOUT_LATITUTE, String.valueOf(gps.getLatitude()));
//                map.put(mydb.KEY_TODAY_CHECKOUT_LONGITUTE, String.valueOf(gps.getLongitude()));
//                map.put(mydb.KEY_TODAY_CEHCKOUT_TIMESTMAP, String.valueOf(time));
//
//                HashMap<String, String> filter = new HashMap<>();
//                filter.put(mydb.KEY_TODAY_FARMER_FARMER_ID, Constants.FARMER_ID);
//                mydb.updateData(mydb.TODAY_FARMER_CHECKIN, map, filter);

                    addCheckOut();
                    updateOutletStatus("Visited");

                    Toast.makeText(getApplicationContext(), "Farmer Saved", Toast.LENGTH_SHORT).show();
                    //sHelper.clearPreferenceStore();

                    Intent farmvisit = new Intent(SoilSamplingActivity.this, VisitFarmerActivity.class);
                    startActivity(farmvisit);
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SoilSamplingActivity.this);
                    alertDialogBuilder
                            .setMessage(getResources().getString(R.string.field_required_message))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();


                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
    }
    private String getCropData(String cropid) {

        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_CROP_ID, cropid);
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME)));

            }
            while (cursor.moveToNext());
        }
        return productName;

    }

    public void selectDialouge(AutoCompleteTextView autoProduct,String from, HashMap<String, String> soilhashmap ) {
        LayoutInflater li = LayoutInflater.from(SoilSamplingActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SoilSamplingActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> companyList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);
        final EditText search = promptsView.findViewById(R.id.et_Search);
        final ImageView ivClsoe = promptsView.findViewById(R.id.iv_Close);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

            title.setText("Select Crop");

        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        prepareCropData(companyList);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList,"salescall");

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SaelsPoint companyname = companyList.get(position);
                alertDialog.dismiss();
                autoProduct.setText(companyname.getPoint());
                if(from.equals("precrop")) {
                    soilhashmap.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, companyname.getId());
                }
                else if(from.equals("currentcrop"))
                {
                    soilhashmap.put(mydb.KEY_TODAY_CROP1_ID, companyname.getId());
                }
                else if(from.equals("currentcropsecond"))
                {
                    soilhashmap.put(mydb.KEY_TODAY_CROP2_ID, companyname.getId());
                }

                // Toast.makeText(getApplicationContext(), movie.getPoint() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(search.hasFocus()) {
                    mAdapter.filter(cs.toString());
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

        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }
    private void prepareCropData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID));
                companyname.setPoint(productName);
                companyname.setId(productID);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }






        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }
    private void validateInputs() {
        if (!(Helpers.isEmptyTextview(getApplicationContext(), acres))
                && !(Helpers.isEmptyTextview(getApplicationContext(), blocks))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_pre_rop))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_current_crop))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_depth))
                && soilSamplingLat!=0.0 && soilSamplingLong!=0.0
        ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentDateandTime = sdf.format(new Date());
            txt_reference.setText(getUsernamefromDatabase() + "-" + currentDateandTime);
            SoilSamplingCrops prod = new  SoilSamplingCrops();

            if(acres != null || !acres.equals(null)){
                int acre = Integer.parseInt( acres.getText().toString() );
                prod.setAcre(String.valueOf(acre));
                soilhashmap.put(mydb.KEY_TODAY_PLOT_NUMBER, String.valueOf(acre));
            }else{
                prod.setAcre("");
                soilhashmap.put(mydb.KEY_TODAY_PLOT_NUMBER, "");
            }
            if (blocks != null || !blocks.equals(null)){
                int block = Integer.parseInt( blocks.getText().toString() );
                prod.setBlock(String.valueOf(block));
                soilhashmap.put(mydb.KEY_TODAY_BLOCK_NUMBER, String.valueOf(block));
            }else{
                prod.setBlock("");
                soilhashmap.put(mydb.KEY_TODAY_BLOCK_NUMBER, "");
            }
            if (txt_reference!=null || !txt_reference.equals(null)){
                soilhashmap.put(mydb.KEY_TODAY_REFRENCE, txt_reference.getText().toString());
            }else{
                soilhashmap.put(mydb.KEY_TODAY_REFRENCE, "");
            }

            soilhashmap.put(mydb.KEY_TODAY_FARMMMER_ID, sHelper.getString(Constants.S_FARMER_ID));

            if (auto_pre_rop != null || !auto_pre_rop.equals(null)){
                prod.setPreviouscrop(auto_pre_rop.getText().toString());
            }else{prod.setPreviouscrop("");}

            if (auto_current_crop != null || !auto_current_crop.equals(null)){
                prod.setCrop1(auto_current_crop.getText().toString());
            }else{prod.setCrop1("");}

            if (auto_current_crop2 != null || !auto_current_crop2.equals(null)){
                prod.setCrop2(auto_current_crop2.getText().toString());
            }else{prod.setCrop2("");}

            if (auto_depth != null || !auto_depth.equals(null)){
                prod.setDepth(auto_depth.getText().toString());
            }else{prod.setDepth("");}

            if(!soilhashmap.equals("0.0")){
                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, String.valueOf(soilSamplingLat));
                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf(soilSamplingLong));
            }else{
                soilhashmap.put(mydb.KEY_TODAY_LATITUTE, "0.0");
                soilhashmap.put(mydb.KEY_TODAY_LONGITUTE, "0.0");
            }

            soilhashmap.put(mydb.KEY_PLAN_TYPE, planType);

            arraylistSoilSampling.add(prod);
            mydb.addData(mydb.TODAY_FARMER_SAMPLING, soilhashmap);

            drawRecommendationTable();
            txt_lat.setText("Selected Lat" +  "");
            txt_lng.setText("Selected Long" +  "");
            acres.setText("");
            blocks.setText("");
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SoilSamplingActivity.this);
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.field_required_message))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    private void updateOutletStatus(String visited) {
            HashMap<String, String> params = new HashMap<>();
            params.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, visited);
            params.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            filter.put(mydb.KEY_PLAN_TYPE, sHelper.getString(Constants.PLAN_TYPE_FARMER));
            mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN, params, filter);
            mydb.close();
    }

    private void addCheckOut() {
        mydb = new MyDatabaseHandler(SoilSamplingActivity.this);

        gps = new GpsTracker(SoilSamplingActivity.this);
        if (gps.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            checkoutlat = gps.getLatitude();
            checkoutlng = gps.getLongitude();
        }

            long time = System.currentTimeMillis();
        HashMap<String, String> headerParams = new HashMap<>();
        if(checkoutlat != null || checkoutlng != null){
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, String.valueOf(checkoutlat));
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, String.valueOf(checkoutlng));
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE,String.valueOf(getMeterFromLatLong(Float.parseFloat(String.valueOf(checkoutlat)), Float.parseFloat(String.valueOf(checkoutlng)), Float.parseFloat(String.valueOf(checkinlat)), Float.parseFloat(String.valueOf(checkinlong)))));

        }else{
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, String.valueOf(0.0));
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, String.valueOf(0.0));
            headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE,String.valueOf(0));
        }
        //headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, String.valueOf(checkoutlat));
        //headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, String.valueOf(checkoutlng));
        headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP, String.valueOf(time));
        headerParams.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        //headerParams.put(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE,String.valueOf(getMeterFromLatLong(Float.parseFloat(String.valueOf(checkoutlat)), Float.parseFloat(String.valueOf(checkoutlng)), Float.parseFloat(String.valueOf(checkinlat)), Float.parseFloat(String.valueOf(checkinlong)))));
        headerParams.put(mydb.KEY_PLAN_TYPE,planType);
        mydb.addData(mydb.TODAY_JOURNEY_PLAN_POST_DATA,headerParams);
    }

    public void loadCheckInLocation() {
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, "");
        map.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_TODAY_FARMER_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                checkinlat = cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE));
                checkinlong = cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE));

            }
            while (cursor.moveToNext());
        }
    }

    private boolean isthisFarmerSamplingDataAlreadyExists() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_SAMPLING, map, filer);
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

    public int saveCheckOutLatAndLongAndDistance() {
        int totalb = 0;
        gps = new GpsTracker(SoilSamplingActivity.this);
        if (gps.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SoilSamplingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return 0;
            }
            checkoutlat = gps.getLatitude();
            checkoutlng = gps.getLongitude();
            float distance = getMeterFromLatLong(Float.parseFloat(String.valueOf(checkoutlat)), Float.parseFloat(String.valueOf(checkoutlng)), Float.parseFloat(Constants.FARMER_LAT), Float.parseFloat(Constants.FARMER_LONG));
            float totaldistance = distance / 1000;
            totalb = (int) Math.round(totaldistance);
            //addCheckoutLocation(totalb);
            return totalb;
        } else {
            DialougeManager.gpsNotEnabledPopup(SoilSamplingActivity.this);
        }
        return 0;
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

    public void getCropfromDatabase(){
        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_CROP_NAME, "");
        map.put(db.KEY_CROP_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cropArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME)));
                cropIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID)));
            }
            while (cursor.moveToNext());
        }
        for (int i=0;i<cropArraylist.size();i++){
            cropArraylist.get(i).replace("%20" , " ");
            cropArraylist.set(i, cropArraylist.get(i).replace("%20" , " "));
        }
    }
    public void getDepthfromDatabase(){
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_SOIL_NAME, "");
        map.put(db.KEY_SOIL_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.SOIL_DEPTHS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                depthArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_SOIL_NAME)));
                depthIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_SOIL_ID)));
            }
            while (cursor.moveToNext());
        }
        for (int i=0;i<depthArraylist.size();i++){
            depthArraylist.get(i).replace("%20" , " ");
            depthArraylist.set(i, depthArraylist.get(i).replace("%20" , " "));
        }
    }
    public String getUsernamefromDatabase(){
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> UserMap = new HashMap<>();

        String username = "";
        map.put(db.KEY_USER_NAME, "");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_IS_LOGGED_IN, "1");
        Cursor cursor = db.getData(db.LOGIN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                username = cursor.getString(cursor.getColumnIndex(db.KEY_USER_NAME));
                //UserMap.put(cursor.getString(cursor.getColumnIndex(db.KEY_USER_NAME)), cursor.getString(cursor.getColumnIndex(db.KEY_IS_LOGGED_IN)));
            }
            while (cursor.moveToNext());
        }
        else
        {
            username = extraHelper.getString(Constants.USER_NAME);
        }
        return username;
    }

    public void drawRecommendationTable()
    {
        int cursorIndex=0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Plot/Acre#");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(12);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Block/Square#");
        column2.setTextSize(12);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Previous Crop");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(12);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Crop1");
        column4.setTextSize(12);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column5 = new TextView(this);
        column5.setText("Crop2");
        column5.setTextSize(12);
        column5.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column5.setPadding(2, 2, 2, 2);
        column5.setBackgroundColor(getResources().getColor(R.color.green));
        column5.setTextColor(getResources().getColor(R.color.colorPrimary));
        column5.setTypeface(Typeface.DEFAULT_BOLD);

        TextView column6 = new TextView(this);
        column6.setText("Depth");
        column6.setTextSize(12);
        column6.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column6.setPadding(2, 2, 2, 2);
        column6.setBackgroundColor(getResources().getColor(R.color.green));
        column6.setTextColor(getResources().getColor(R.color.colorPrimary));
        column6.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column2, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column3, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column4, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column5, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column6, new TableRow.LayoutParams(0, 150, 0.40f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for(int i=0;i<arraylistSoilSampling.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView acre = new TextView(this);
            acre.setText(arraylistSoilSampling.get(i).getAcre());
            acre.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            acre.setGravity(Gravity.CENTER);
            acre.setPadding(2, 2, 2, 2);
            TextView block = new TextView(this);
            block.setText(arraylistSoilSampling.get(i).getBlock());
            block.setGravity(Gravity.CENTER);
            block.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            block.setPadding(2, 2, 2, 2);

            TextView  precrop = new TextView(this);
            precrop.setText(arraylistSoilSampling.get(i).getPreviouscrop());
            precrop.setGravity(Gravity.CENTER);

            precrop.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            precrop.setPadding(2, 2, 2, 2);

            TextView crop1= new TextView(this);
            crop1.setText(arraylistSoilSampling.get(i).getCrop1());
            crop1.setGravity(Gravity.CENTER);
            crop1.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            crop1.setPadding(2, 2, 2, 2);

            TextView crop2= new TextView(this);
            crop2.setText(arraylistSoilSampling.get(i).getCrop2());
            crop2.setGravity(Gravity.CENTER);
            crop2.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            crop2.setPadding(2, 2, 2, 2);

            TextView depth= new TextView(this);
            depth.setText(arraylistSoilSampling.get(i).getCrop2());
            depth.setGravity(Gravity.CENTER);
            depth.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            depth.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(acre, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.80f));
            row2.addView(block, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.80f));
            row2.addView(precrop, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.80f));
            row2.addView(crop1, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.40f));
            row2.addView(crop2, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.40f));
            row2.addView(depth, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.40f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

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
    private void prepareRecommendationData() {
        SoilSamplingCrops prod = new  SoilSamplingCrops();
//        prod .setAcre("2");
//        prod .setBlock("10");
//        prod .setCrop1("Wheat");
//        prod .setCrop2("Rice");
//        prod.setPreviouscrop("Cotton");
//        prod.setDepth("0-6 ince");
//        arraylistSoilSampling.add(prod);




        // notify adapter about data set changes
        // so that it will render the list with new data
        // i added this in dev
    }
    @NonNull
    private SpannableStringBuilder setStarToLabel(String text) {
        String simple = text;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}
