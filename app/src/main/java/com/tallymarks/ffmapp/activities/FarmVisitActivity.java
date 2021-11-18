package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Recommendations;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FarmVisitActivity extends AppCompatActivity {
    private TextView tvTopHeader, txt_lat, txt_lng, txt_lcoation;
    private TableLayout mTableLayout;
    EditText remarks, address;
    AutoCompleteTextView auto_crop, auto_main_product;
    TextView txtcrop, txtmainproduct, txtsavelocation;
    ImageView iv_menu, iv_back, img_add_recommendations;
    Button btn_proceed, btn_back, btn_lcoation, btn_skip;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    SharedPrefferenceHelper sHelper;
    GpsTracker gpsTracker;
    double farmvisitLat = 0.0;
    double farvisitLong = 0.0;
    String planType = "";
    ArrayList<Recommendations> arraylist = new ArrayList<Recommendations>();
    ArrayList<Recommendations> myarraylist = new ArrayList<Recommendations>();
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> mainProductArraylist = new ArrayList<>();
    ArrayList<String> mainProductIDArraylist = new ArrayList<>();
    ArrayList<String> fertTypeArraylist = new ArrayList<>();
    ArrayList<String> fertTypeIDArraylist = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_visit);
        initView();


    }

    private void initView() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            planType = data.getString(Constants.PLAN_TYPE_FARMER);

        }
        tvTopHeader = findViewById(R.id.tv_dashboard);
        auto_crop = findViewById(R.id.auto_crop);
        btn_back = findViewById(R.id.back);
        btn_lcoation = findViewById(R.id.btn_lcoation);
        txtcrop = findViewById(R.id.txt_crop);
        txtmainproduct = findViewById(R.id.txt_main_prod);
        txtsavelocation = findViewById(R.id.txt_location);
        txt_lat = findViewById(R.id.txt_lat);
        txt_lng = findViewById(R.id.txt_lng);
        //txt_lcoation = findViewById(R.id.txt_location);
        remarks = findViewById(R.id.remarks);
        address = findViewById(R.id.address);
        mydb = new MyDatabaseHandler(FarmVisitActivity.this);
        sHelper = new SharedPrefferenceHelper(FarmVisitActivity.this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if the fields are fill add in database
//                if (isthisFarmerDataAlreadyExists()){
//                    HashMap<String, String> map = new HashMap<>();
//                    if(auto_crop.getText()!=null || !auto_crop.equals(null)){map.put(mydb.KEY_TODAY_CROPID, cropIDArraylist)}
//                    HashMap<String, String> filer = new HashMap<>();
//                    filer.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
//                    mydb.updateData(mydb.TODAY_FARMER_ACTIVITY, map, filer);
//
//                }else{
//
//                }

                Intent i = new Intent(FarmVisitActivity.this, FarmersStartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        btn_proceed = findViewById(R.id.btn_proceed);
        btn_skip = findViewById(R.id.btn_skip);

        db = new DatabaseHandler(FarmVisitActivity.this);
        auto_main_product = findViewById(R.id.auto_main_product);
        img_add_recommendations = findViewById(R.id.img_add_recommendations);
        img_add_recommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecommendations();
            }
        });

        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("FARM VISIT");
        Intent intent = getIntent();
//        if(intent.getExtras()!=null) {
//            if (intent.getExtras().getString("farmlat") != null &&
//                    !intent.getExtras().getString("farmlat").equals("") &&
//                    intent.getExtras().getString("farmlng") != null &&
//                    !intent.getExtras().getString("farmlng").equals("")
//            ) {
//                //farmvisitLat = Double.parseDouble(intent.getExtras().getString("farmlat"));
//                //farvisitLong = Double.parseDouble(intent.getExtras().getString("farmlng"));
//                txt_lat_lng.setText("Selected Lat,Log: " + farmvisitLat + " , " + farvisitLong);
//
//            }
//        }
        gpsTracker = new GpsTracker(FarmVisitActivity.this);

//        if(sHelper!=null)
//        {
//            if(sHelper.getString(Constants.CUSTOM_LNG_FARM)!=null &&
//                    !sHelper.getString(Constants.CUSTOM_LNG_FARM).equals("") &&
//                    sHelper.getString(Constants.CUSTOM_LAT_FARM)!=null &&
//                    !sHelper.getString(Constants.CUSTOM_LAT_FARM).equals("")
//            )
//            {
//                farmvisitLat = Double.parseDouble(sHelper.getString(Constants.CUSTOM_LAT_SOIL));
//                farvisitLong = Double.parseDouble(sHelper.getString(Constants.CUSTOM_LNG_SOIL));
//                txt_lat_lng.setText("Selected Lat,Log: " +  farmvisitLat + " , " + farvisitLong);
//
//            }
//
//
//        }
        //prepareRecommendationData();
        //drawRecommendationTable();
        //  getCropfromDatabase();
        // getMainProductfromDatabase();
        getFertTypeFromDatabase();
        if (isthisFarmerDataAlreadyExists()) {
            // fill the fields from database

            HashMap<String, String> map = new HashMap<>();

            map.put(mydb.KEY_TODAY_CROPID, "");
            map.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
            map.put(mydb.KEY_TODAY_REMARKS, "");
            map.put(mydb.KEY_TODAY_ADDRESS, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            Cursor cursor = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    auto_crop.setText(getCropData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                    auto_main_product.setText(getProductData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_MAIN_PRODUCT))));
                    remarks.setText(Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_REMARKS))));
                    address.setText(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_ADDRESS)));
                }
                while (cursor.moveToNext());
            }
            if (gpsTracker.canGetLocation()) {
                farmvisitLat = gpsTracker.getLatitude();
                farvisitLong = gpsTracker.getLongitude();
                txt_lat.setText("Selected Lat: " + farmvisitLat);
                txt_lng.setText("Selected Long: " + farvisitLong);

            }
        }
        if (isthisFarmerRecommendationDataAlreadyExists()) {
            //fill the fields from database

            HashMap<String, String> map = new HashMap<>();

            map.put(mydb.KEY_TODAY_CROPID, "");
            map.put(mydb.KEY_TODAY_FERTTYPE_ID, "");
            map.put(mydb.KEY_TODAY_BRAND_ID, "");
            map.put(mydb.KEY_TODAY_DOSAGE, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            Cursor cursor = mydb.getData(mydb.TODAY_FARMER_RECOMMENDATION, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Recommendations myplan = new Recommendations();
                    myplan.setCrop(getCropData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                    myplan.setDosage(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_DOSAGE)));
                    myplan.setFert(fertTypeArraylist.get(Integer.parseInt(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FERTTYPE_ID))) - 1));
                    myplan.setProduct(getProductData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_BRAND_ID))));

                    arraylist.add(myplan);

                }
                while (cursor.moveToNext());
            }
            drawRecommendationTable();
        }
        final String arraylist[] = {"Male", "female", "other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mainProductArraylist);
        auto_crop.setAdapter(arrayAdapter1);
        auto_main_product.setAdapter(arrayAdapter2);

        auto_crop.setCursorVisible(false);
        auto_main_product.setCursorVisible(false);


        SpannableStringBuilder tvCrop = setStarToLabel("Crop");
        SpannableStringBuilder tvMainProduct = setStarToLabel("Main Product");
        SpannableStringBuilder tvSvelocation = setStarToLabel("Save Location");
        txtcrop.setText(tvCrop);
        txtsavelocation.setText(tvSvelocation);
        txtmainproduct.setText(tvMainProduct);


        btn_lcoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent loc = new Intent(FarmVisitActivity.this, CustomMap.class);
                    loc.putExtra("from", "farm");
                    startActivity(loc);
                    if (gpsTracker.canGetLocation()) {
                        farmvisitLat = gpsTracker.getLatitude();
                        farvisitLong = gpsTracker.getLongitude();
                        txt_lat.setText("Selected Lat: " + farmvisitLat);
                        txt_lng.setText("Selected Long: " + farvisitLong);

                    }

                    //  GpsTracker gpsTracker = new GpsTracker(FarmVisitActivity.this);
//
//                    if (gpsTracker.canGetLocation()) {
//                        if (ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        farmvisitLat = gpsTracker.getLatitude();
//                        farvisitLong = gpsTracker.getLongitude();
//                    }
//                    txt_lat_lng.setText("Selected Lat,Lng:" + farmvisitLat + " , " + farvisitLong);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        auto_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_crop.showDropDown();
//                String selection = cropArraylist.get(position);
//                map.put(mydb.KEY_TODAY_CROPID, cropIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_crop, "crop");
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//                        map.put(mydb.KEY_TODAY_CROPID, cropIDArraylist.get(which));
//                        auto_crop.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmVisitActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_crop.showDropDown();
            }
        });
        auto_main_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_main_product.showDropDown();
//                String selection = mainProductArraylist.get(position);
//                map.put(mydb.KEY_TODAY_MAIN_PRODUCT, mainProductIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();

            }
        });

        auto_main_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_main_product, "product");
                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = mainProductArraylist.get(which);
//                        map.put(mydb.KEY_TODAY_MAIN_PRODUCT, mainProductIDArraylist.get(which));
//                        auto_main_product.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmVisitActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(mainProductArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_main_product.showDropDown();
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent soil = new Intent(FarmVisitActivity.this, SoilSamplingActivity.class);
                Bundle data = new Bundle();
                data.putString(Constants.PLAN_TYPE_FARMER, planType);
                soil.putExtras(data);
                startActivity(soil);
            }
        });
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmVisitActivity.this, FarmersStartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void selectDialouge(AutoCompleteTextView autoProduct, String from) {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
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
        if (from.equals("crop")) {
            title.setText("Select Crop");
        } else if (from.equals("product")) {
            title.setText("Select Product");
        }

        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if (from.equals("crop")) {
            prepareCropData(companyList);
        } else if (from.equals("product")) {
            prepareProductData(companyList);
        }
        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList, "salescall");

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
                if (from.equals("crop")) {

                    map.put(mydb.KEY_TODAY_CROPID, companyname.getId());
                } else if (from.equals("product")) {
                    map.put(mydb.KEY_TODAY_MAIN_PRODUCT, companyname.getId());
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
                if (search.hasFocus()) {
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

    public void selectDialougeRecommendation(AutoCompleteTextView autoProduct, String from, Recommendations plan, HashMap<String, String> mapRecommendation) {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
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
        if (from.equals("mainproduct")) {
            title.setText("Select Product");
        } else if (from.equals("autocrop")) {
            title.setText("Select Crop");
        }
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if (from.equals("mainproduct")) {
            prepareProductData(companyList);
        } else if (from.equals("autocrop")) {
            prepareCropData(companyList);
        }
        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList, "salescall");

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
                if (from.equals("mainproduct")) {
                    plan.setProduct(companyname.getPoint());
                    mapRecommendation.put(mydb.KEY_TODAY_BRAND_ID, companyname.getId());
                } else if (from.equals("autocrop")) {
                    plan.setCrop(companyname.getPoint());
                    mapRecommendation.put(mydb.KEY_TODAY_CROPID, companyname.getId());
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
                if (search.hasFocus()) {
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

    private String getProductData(String productid) {

        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_ENGRO_BRANCH_ID, productid);
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME)));

            }
            while (cursor.moveToNext());
        }
        return productName;

    }

    private void prepareProductData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID));
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
        if (
                !(Helpers.isEmptyAutoTextview(getApplicationContext(), auto_crop))
                        && !(Helpers.isEmptyAutoTextview(getApplicationContext(), auto_main_product))
                        && !String.valueOf(farmvisitLat).equals("") && !String.valueOf(farvisitLong).equals("")
        ) {
            GpsTracker gpsTracker = new GpsTracker(FarmVisitActivity.this);
            mydb = new MyDatabaseHandler(FarmVisitActivity.this);
            try {
                map.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                //map.put(mydb.KEY_TODAY_ADDRESS, txt_lcoation.getText().toString());

                if (remarks.getText().toString() != null || !remarks.equals(null)) {
                    map.put(mydb.KEY_TODAY_REMARKS, remarks.getText().toString());
                } else {
                    map.put(mydb.KEY_TODAY_REMARKS, "");
                }

                if (address.getText().toString() != null || !address.equals(null)) {
                    map.put(mydb.KEY_TODAY_ADDRESS, address.getText().toString());
                } else {
                    map.put(mydb.KEY_TODAY_ADDRESS, "");
                }

                if (gpsTracker.canGetLocation()) {
                    map.put(mydb.KEY_TODAY_LATITUTE, String.valueOf(gpsTracker.getLatitude()));
                    map.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf(gpsTracker.getLongitude()));
                } else {
                    map.put(mydb.KEY_TODAY_LATITUTE, String.valueOf("0.0"));
                    map.put(mydb.KEY_TODAY_LONGITUTE, String.valueOf("0.0"));
                }

                map.put(mydb.KEY_PLAN_TYPE, planType);

                if (isthisFarmerDataAlreadyExists()) {
                    // update record
                    HashMap<String, String> filer = new HashMap<>();
                    filer.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                    mydb.updateData(mydb.TODAY_FARMER_ACTIVITY, map, filer);
                } else {
                    // add new record
                    mydb.addData(mydb.TODAY_FARMER_ACTIVITY, map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent soil = new Intent(FarmVisitActivity.this, SoilSamplingActivity.class);
            Bundle data = new Bundle();
            data.putString(Constants.PLAN_TYPE_FARMER, planType);
            soil.putExtras(data);
            startActivity(soil);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
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

    private boolean isthisFarmerDataAlreadyExists() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_ID, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, map, filer);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            } while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    private boolean isthisFarmerRecommendationDataAlreadyExists() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMMER_ID, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_RECOMMENDATION, map, filer);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            } while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    public void getCropfromDatabase() {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
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

        for (int i = 0; i < cropArraylist.size(); i++) {
            if (cropArraylist.get(i).contains("%20"))
                cropArraylist.set(i, cropArraylist.get(i).replace("%20", " "));
        }
    }

    public void getMainProductfromDatabase() {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mainProductArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME)));
                mainProductIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID)));
            }
            while (cursor.moveToNext());
        }

        for (int i = 0; i < mainProductArraylist.size(); i++) {
            mainProductArraylist.get(i).replace("%20", " ");
            mainProductArraylist.set(i, mainProductArraylist.get(i).replace("%20", " "));
        }
    }

    public void getFertTypeFromDatabase() {
        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_FERT_NAME, "");
        map.put(db.KEY_FERT_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.FERT_TYPES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                fertTypeArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_FERT_NAME)));
                fertTypeIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_FERT_ID)));
            }
            while (cursor.moveToNext());
        }
        for (int i = 0; i < fertTypeArraylist.size(); i++) {
            fertTypeArraylist.get(i).replace("%20", " ");
            fertTypeArraylist.set(i, fertTypeArraylist.get(i).replace("%20", " "));
        }
    }

    public void addRecommendations() {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialoige_add_recommednations, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        Recommendations plan = new Recommendations();
        HashMap<String, String> mapRecommendation = new HashMap<>();

        ImageView ivClose = promptsView.findViewById(R.id.imageView);
        AutoCompleteTextView recommendation_auto_crop, recommendation_auto_main_product, recommendation_auto_fert;
        recommendation_auto_crop = promptsView.findViewById(R.id.auto_crop_recom);
        recommendation_auto_main_product = promptsView.findViewById(R.id.auto_product_recomm);
        recommendation_auto_fert = promptsView.findViewById(R.id.auto_fert);
        EditText et_acre = promptsView.findViewById(R.id.et_acre);
        EditText et_dosePerAcre = promptsView.findViewById(R.id.et_dosePerAcre);

        TextView txtCrop = promptsView.findViewById(R.id.txt_crop);
        TextView txtfert = promptsView.findViewById(R.id.txt_fert);
        TextView txtproduct = promptsView.findViewById(R.id.txt_product);
        TextView txtacres = promptsView.findViewById(R.id.txt_acre);
        TextView txtdosage = promptsView.findViewById(R.id.txt_dosage);


        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mainProductArraylist);
        final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, fertTypeArraylist);

        recommendation_auto_crop.setAdapter(arrayAdapter1);
        recommendation_auto_main_product.setAdapter(arrayAdapter2);
        recommendation_auto_fert.setAdapter(arrayAdapter3);

        SpannableStringBuilder product = setStarToLabel("Product");
        SpannableStringBuilder crop = setStarToLabel("Crop");
        SpannableStringBuilder fert = setStarToLabel("Fert App Type");
        SpannableStringBuilder acres = setStarToLabel("No. of Acres");
        SpannableStringBuilder dosage = setStarToLabel("Dosage Per Acre");

        txtCrop.setText(crop);
        txtproduct.setText(product);
        txtfert.setText(fert);
        txtacres.setText(acres);
        txtdosage.setText(dosage);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        recommendation_auto_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                recommendation_auto_crop.showDropDown();
//                String selection = cropArraylist.get(position);
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();
//                plan.setCrop(selection);
            }
        });

        recommendation_auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialougeRecommendation(recommendation_auto_crop, "autocrop", plan, mapRecommendation);
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//
//                        recommendation_auto_crop.setText(selection);
//                        plan.setCrop(selection);
//                        mapRecommendation.put(mydb.KEY_TODAY_CROPID, cropIDArraylist.get(which));
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmVisitActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                recommendation_auto_crop.showDropDown();
            }
        });
        recommendation_auto_main_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                recommendation_auto_main_product.showDropDown();
//                String selection = mainProductArraylist.get(position);
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();
//                plan.setProduct(selection);
            }
        });

        recommendation_auto_main_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialougeRecommendation(recommendation_auto_main_product, "mainproduct", plan, mapRecommendation);
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//
//                        String selection = mainProductArraylist.get(which);
//                        recommendation_auto_main_product.setText(selection);
//                        plan.setProduct(selection);
//                        mapRecommendation.put(mydb.KEY_TODAY_BRAND_ID, mainProductIDArraylist.get(which));
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmVisitActivity.this);
//                categoryAlert.setTitle("Main Product");
//
//                categoryAlert.setItems(mainProductArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                recommendation_auto_main_product.showDropDown();
            }
        });

        recommendation_auto_fert.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recommendation_auto_fert.showDropDown();
                String selection = fertTypeArraylist.get(position);
                mapRecommendation.put(mydb.KEY_TODAY_FERTTYPE_ID, fertTypeIDArraylist.get(position));
                //  Toast.makeText(getApplicationContext(), selection,
                //  Toast.LENGTH_SHORT).show();
                plan.setFert(selection);
            }
        });

        recommendation_auto_fert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                recommendation_auto_fert.showDropDown();
            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add_recommendations);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputsRecommendation(et_acre, et_dosePerAcre, recommendation_auto_crop, recommendation_auto_main_product, recommendation_auto_fert, alertDialog, plan, mapRecommendation);
            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    private void validateInputsRecommendation(EditText acre, EditText dosage, AutoCompleteTextView crop, AutoCompleteTextView mainproduct, AutoCompleteTextView fert, AlertDialog alertDialog, Recommendations plan, HashMap<String, String> mapRecommendation) {
        if (!(Helpers.isEmpty(getApplicationContext(), acre))
                && !(Helpers.isEmpty(getApplicationContext(), dosage))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(), fert))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(), mainproduct))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(), crop))
        ) {
            if (acre != null || !acre.equals(null) && dosage != null || !dosage.equals(null)) {
                int acrecal = Integer.parseInt(acre.getText().toString());
                int dosePerAcre = Integer.parseInt(dosage.getText().toString());

                int dosagecal = acrecal * dosePerAcre;
                plan.setDosage(String.valueOf(dosagecal));
                mapRecommendation.put(mydb.KEY_TODAY_DOSAGE, String.valueOf(dosagecal));
                mapRecommendation.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                mapRecommendation.put(mydb.KEY_PLAN_TYPE, planType);


            }

            arraylist.add(plan);
//                if(isthisFarmerRecommendationDataAlreadyExists()){
//                    // update record
//                    HashMap<String, String> filer = new HashMap<>();
//                    filer.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
//                    mydb.updateData(mydb.TODAY_FARMER_RECOMMENDATION, mapRecommendation, filer);
//                }else{
            // add new record
            mydb.addData(mydb.TODAY_FARMER_RECOMMENDATION, mapRecommendation);
//                }
            drawRecommendationTable();
            alertDialog.dismiss();

//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.field_required_message))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });
            AlertDialog alertDialog2 = alertDialogBuilder.create();
            alertDialog2.show();
        }
    }

    public void drawRecommendationTable() {
        int cursorIndex = 0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Crop");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(16);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Fert App Type");
        column2.setTextSize(16);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("product");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(16);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Dosage");
        column4.setTextSize(16);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 100, 0.15f));
        row.addView(column2, new TableRow.LayoutParams(0, 100, 0.37f));
        row.addView(column3, new TableRow.LayoutParams(0, 100, 0.23f));
        row.addView(column4, new TableRow.LayoutParams(0, 100, 0.25f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for (int i = 0; i < arraylist.size(); i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView crop = new TextView(this);
            crop.setText(arraylist.get(i).getCrop());
            crop.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            crop.setGravity(Gravity.CENTER);
            crop.setPadding(2, 2, 2, 2);
            TextView fert = new TextView(this);
            fert.setText(arraylist.get(i).getFert());
            fert.setGravity(Gravity.CENTER);
            fert.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            fert.setPadding(2, 2, 2, 2);

            TextView product = new TextView(this);
            product.setText(arraylist.get(i).getProduct());
            product.setGravity(Gravity.CENTER);

            product.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            product.setPadding(2, 2, 2, 2);
            TextView dosage = new TextView(this);
            dosage.setText(arraylist.get(i).getDosage());
            dosage.setGravity(Gravity.CENTER);
            dosage.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            dosage.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(crop, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f));
            row2.addView(fert, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.37f));
            row2.addView(product, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.23f));
            row2.addView(dosage, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }

    private void prepareRecommendationData() {
//       Recommendations plan = new Recommendations();
//        plan.setCrop("Wheat");
//        plan.setDosage("10");
//        plan.setFert("Trop Dressing");
//        plan.setProduct("Engro DAP");
//        arraylist.add(plan);
//
//        Recommendations plan2 = new Recommendations();
//        plan2.setCrop("Wheat");
//        plan2.setDosage("10");
//        plan2.setFert("Trop Dressing");
//        plan2.setProduct("Engro DAP");
//        arraylist.add(plan2);
//
//        Recommendations plan3 = new Recommendations();
//        plan3.setCrop("Wheat");
//        plan3.setDosage("10");
//        plan3.setFert("Trop Dressing");
//        plan3.setProduct("Engro DAP");
//        arraylist.add(plan3);
//
//        Recommendations plan4 = new Recommendations();
//        plan4.setCrop("Wheat");
//        plan4.setDosage("10");
//        plan4.setFert("Trop Dressing");
//        plan4.setProduct("Engro DAP");
//        arraylist.add(plan4);

        // notify adapter about data set changes
        // so that it will render the list with new data

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
