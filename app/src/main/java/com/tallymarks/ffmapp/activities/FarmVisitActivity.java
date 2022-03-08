package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesCallAdapter;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.DataModel;
import com.tallymarks.ffmapp.models.OtherProduct;
import com.tallymarks.ffmapp.models.Recommendations;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Activity;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.FarmerCheckIn;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Recommendation;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Sampling;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FarmVisitActivity extends AppCompatActivity {
    private TextView tvTopHeader, txt_lat, txt_lng, txt_lcoation;
    private TableLayout mTableLayout,mTableLayoutotherproducts;
    EditText remarks, address, etpacks, etcropacre, etcropdef, etotherpacksliq;
    AutoCompleteTextView auto_crop, auto_main_product, auto_other_product, auto_Serving_dealer;
    TextView txtcrop, txtmainproduct, txtsavelocation;
    TextView txtpacks, txtcropAcre, txtcropDef, txtotherProduct, txtservingdealers, txtotherpacksliq;
    ImageView iv_menu, iv_back, img_add_recommendations, img_add_other_packs;
    Button btn_proceed, btn_back, btn_lcoation, btn_skip;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    Double checkoutlat = null, checkoutlng = null;
    String journeyType;
    GpsTracker gpsTracker;
    double farmvisitLat = 0.0;
    String checkinlat;
    String checkinlong;
    double farvisitLong = 0.0;
    String  rolename = "";
    String planType = "";
    ArrayList<Recommendations> arraylist = new ArrayList<Recommendations>();
    ArrayList<OtherProduct> arraylistotherproduct = new ArrayList<OtherProduct>();
    ArrayList<Recommendations> myarraylist = new ArrayList<Recommendations>();
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> mainProductArraylist = new ArrayList<>();
    ArrayList<String> mainProductIDArraylist = new ArrayList<>();
    ArrayList<String> servingDealersArraylist = new ArrayList<>();
    ArrayList<String> servingDealersIDArraylist = new ArrayList<>();
    ArrayList<String> fertTypeArraylist = new ArrayList<>();
    ArrayList<String> fertTypeIDArraylist = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();
    String otherproductid;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_visit);
        initView();


    }

    private void initView() {
        Bundle data = getIntent().getExtras();
        sHelper = new SharedPrefferenceHelper(FarmVisitActivity.this);
        extraHelper = new ExtraHelper(FarmVisitActivity.this);
//        if (data != null) {
//            planType = data.getString(Constants.PLAN_TYPE_FARMER);
//
//        }
        planType = sHelper.getString(Constants.PLAN_TYPE_FARMER);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        auto_crop = findViewById(R.id.auto_crop);
        auto_other_product = findViewById(R.id.auto_other_product);
        auto_Serving_dealer = findViewById(R.id.auto_serving_dealer);
        btn_back = findViewById(R.id.back);
        btn_lcoation = findViewById(R.id.btn_lcoation);
        txtcrop = findViewById(R.id.txt_crop);
        txtmainproduct = findViewById(R.id.txt_main_prod);
        txtsavelocation = findViewById(R.id.txt_location);

        txtpacks = findViewById(R.id.txt_pack);
        txtcropAcre = findViewById(R.id.txt_crop_acre);
        txtcropDef = findViewById(R.id.txt_crop_def);
        txtotherProduct = findViewById(R.id.txt_other_product);
        txtotherpacksliq = findViewById(R.id.txt_other_liquidated);
        txtservingdealers = findViewById(R.id.txt_serving_dealer);
        txt_lat = findViewById(R.id.txt_lat);
        txt_lng = findViewById(R.id.txt_lng);
        //txt_lcoation = findViewById(R.id.txt_location);
        remarks = findViewById(R.id.remarks);
        address = findViewById(R.id.address);
        etpacks = findViewById(R.id.etPack);
        etcropacre = findViewById(R.id.etCropAcre);
        etcropdef = findViewById(R.id.etCropdef);
        mydb = new MyDatabaseHandler(FarmVisitActivity.this);

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
        mTableLayoutotherproducts = findViewById(R.id.displayLinearptherpacks);
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
        img_add_other_packs = findViewById(R.id.img_add_other_packs);
        img_add_other_packs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOtherPacks();
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
        loadCheckInLocation();
        loadRoles();
        getFertTypeFromDatabase();
        getServingDealersfromDatabase();


        if (isthisFarmerDataAlreadyExists()) {
            // fill the fields from database

            HashMap<String, String> map = new HashMap<>();

            map.put(mydb.KEY_TODAY_CROPID, "");
            map.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
            map.put(mydb.KEY_TODAY_SERVINGDEALERID, "");
            map.put(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED, "");
            map.put(mydb.KEY_TODAY_PACKS_LIQUIATED, "");
            map.put(mydb.KEY_TODAY_CROP_ACE, "");
            map.put(mydb.KEY_TODAY_CROP_DEF, "");
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
                    auto_Serving_dealer.setText(getDealerData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_SERVINGDEALERID))));
                    //etotherpacksliq.setText(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED)));
                    etcropdef.setText(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROP_DEF)));
                    etcropacre.setText(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_CROP_ACE)));
                    etpacks.setText(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_PACKS_LIQUIATED)));
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

        if (isthisFarmerOtherProductDataAlreadyExists()) {
            //fill the fields from database
            HashMap<String, String> map = new HashMap<>();
            map.put(mydb.KEY_TODAY_OTHER_PACKS_ID, "");
            map.put(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            Cursor cursor = mydb.getData(mydb.TODAY_FARMER_OTHERPACKS, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    OtherProduct myplan = new OtherProduct();
                    myplan.setOtherproducts(getProductData(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_ID))));
                    myplan.setOtherpacksliqudiated(cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED)));
                    arraylistotherproduct.add(myplan);

                }
                while (cursor.moveToNext());
            }
            drawOtherProductsTable();
        }
        final String arraylist[] = {"Male", "female", "other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mainProductArraylist);
        final ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, servingDealersArraylist);
        auto_crop.setAdapter(arrayAdapter1);
        auto_main_product.setAdapter(arrayAdapter2);
        auto_Serving_dealer.setAdapter(arrayAdapter3);

        auto_crop.setCursorVisible(false);
        auto_main_product.setCursorVisible(false);
        auto_Serving_dealer.setCursorVisible(false);


        SpannableStringBuilder tvCrop = setStarToLabel("Crop");
        SpannableStringBuilder tvMainProduct = setStarToLabel("Main Product");
        SpannableStringBuilder tvSvelocation = setStarToLabel("Save Location");
        SpannableStringBuilder tvPacks = setStarToLabel("Packs Liquidated");
        SpannableStringBuilder tvCropAcre = setStarToLabel("Crop Acreage");
        SpannableStringBuilder tvCropDef = setStarToLabel("Crop Deficiency");
        SpannableStringBuilder tvOtherProduct = setStarToLabel("Other Product");
        SpannableStringBuilder tvOtherliquidated = setStarToLabel("Other Packs Liquidated");
        SpannableStringBuilder tvServignDealers = setStarToLabel("Serving Dealers");
        txtcrop.setText(tvCrop);
        txtsavelocation.setText(tvSvelocation);
        txtmainproduct.setText(tvMainProduct);
        txtpacks.setText(tvPacks);
        txtcropAcre.setText(tvCropAcre);
        txtcropDef.setText(tvCropDef);
        txtotherProduct.setText(tvOtherProduct);
        txtotherpacksliq.setText(tvOtherliquidated);
        txtservingdealers.setText(tvServignDealers);


        btn_lcoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent loc = new Intent(FarmVisitActivity.this, CustomMap.class);
                    loc.putExtra("from", "farm");
                    startActivity(loc);
                    gpsTracker = new GpsTracker(FarmVisitActivity.this);
                    if  (gpsTracker.canGetLocation()) {
                        if (ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        farmvisitLat = gpsTracker.getLatitude();
                        farvisitLong = gpsTracker.getLongitude();
                    }
                    txt_lat.setText("Selected Lat: " +  farmvisitLat);
                    txt_lng.setText("Selected Long: " +  farvisitLong);
//                    if (gpsTracker.canGetLocation()) {
//                        farmvisitLat = gpsTracker.getLatitude();
//                        farvisitLong = gpsTracker.getLongitude();
//                        txt_lat.setText("Selected Lat: " + farmvisitLat);
//                        txt_lng.setText("Selected Long: " + farvisitLong);
//
//                    }

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
               // final AlertDialog actions;
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
//        auto_Serving_dealer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_Serving_dealer.showDropDown();
//                String selection = servingDealersArraylist.get(position);
//                map.put(mydb.KEY_TODAY_SERVINGDEALERID, servingDealersIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });
        auto_Serving_dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_Serving_dealer, "dealer");
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = servingDealersArraylist.get(which);
//                        map.put(mydb.KEY_TODAY_SERVINGDEALERID, servingDealersIDArraylist.get(which));
//                        auto_Serving_dealer.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmVisitActivity.this);
//                categoryAlert.setTitle("Dealer List");
//
//                categoryAlert.setItems(servingDealersArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//                auto_Serving_dealer.showDropDown();
            }
        });
        auto_other_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openotherproduct();

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

    private void addOtherProducts(ArrayList<DataModel> dataModels) {
        for (int i = 0; i < dataModels.size(); i++) {

            if (dataModels.get(i).isChecked()) {
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(mydb.KEY_TODAY_FARMER_OTHER_PRODUCT_ID, dataModels.get(i).getId());
                headerParams.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                headerParams.put(mydb.KEY_PLAN_TYPE, planType);
                mydb.addData(mydb.TODAY_FARMER_OTHER_PRODUCTS, headerParams);
            }


            HashMap<String, String> headerParams2 = new HashMap<>();
            if (dataModels.get(i).isChecked()) {
                headerParams2.put(mydb.KEY_ENGRO_PRODUCT_CHECKED, "true");
            } else {
                headerParams2.put(mydb.KEY_ENGRO_PRODUCT_CHECKED, "false");
            }

            headerParams2.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            headerParams2.put(mydb.KEY_PLAN_TYPE, planType);
            HashMap<String, String> filter2 = new HashMap<>();
            filter2.put(mydb.KEY_ENGRO_PRODUCT_ID, dataModels.get(i).getId());
            mydb.updateData(mydb.ENGRO_DEALAERS_LIST, headerParams2, filter2);

        }
    }

    private void updateOtherProducts(ArrayList<DataModel> dataModels) {
        for (int i = 0; i < dataModels.size(); i++) {
            if (dataModels.get(i).isChecked()) {
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(mydb.KEY_TODAY_FARMER_OTHER_PRODUCT_ID, dataModels.get(i).getId());
                HashMap<String, String> filter = new HashMap<>();
                filter.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                filter.put(mydb.KEY_PLAN_TYPE, planType);
                mydb.updateData(mydb.TODAY_FARMER_OTHER_PRODUCTS, headerParams, filter);
            }


            HashMap<String, String> headerParams2 = new HashMap<>();
            if (dataModels.get(i).isChecked()) {
                headerParams2.put(mydb.KEY_ENGRO_PRODUCT_CHECKED, "true");
            } else {
                headerParams2.put(mydb.KEY_ENGRO_PRODUCT_CHECKED, "false");
            }

            headerParams2.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
            headerParams2.put(mydb.KEY_PLAN_TYPE, planType);
            HashMap<String, String> filter2 = new HashMap<>();
            filter2.put(mydb.KEY_ENGRO_PRODUCT_ID, dataModels.get(i).getId());
            mydb.updateData(mydb.ENGRO_DEALAERS_LIST, headerParams2, filter2);


        }
    }

    private void openotherproduct() {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_other_product, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final ArrayList<DataModel> dataModels = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);
        final Button otherProduct = promptsView.findViewById(R.id.btn_add_other_product);
        final ImageView ivClsoe = promptsView.findViewById(R.id.iv_Close);
        final ListView listView = promptsView.findViewById(R.id.listView);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        otherProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataModels.size() > 0 && dataModels != null) {
                    if (isthisFarmeOtherProductsAlreadyExists()) {
                        updateOtherProducts(dataModels);
                    } else {
                        addOtherProducts(dataModels);
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(FarmVisitActivity.this, "Please Select at least one Product", Toast.LENGTH_SHORT).show();
                }

            }
        });
        title.setText("Select Product");
        prepareBrandData(dataModels);
        final SalesCallAdapter adapter = new SalesCallAdapter(dataModels, getApplicationContext(), "farmvisit");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel = dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();


            }
        });
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    private boolean isFarmerCheckedSaved() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_PLAN_TYPE, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.ENGRO_DEALAERS_LIST, map, filer);
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

    private void prepareBrandData(ArrayList<DataModel> dataModels) {
        String productName = "", productID = "", productChecked = "";
        HashMap<String, String> map = new HashMap<>();
        dataModels.clear();

        map.put(mydb.KEY_ENGRO_PRODUCT_ID, "");
        map.put(mydb.KEY_ENGRO_PRODUCT_NAME, "");
        map.put(mydb.KEY_ENGRO_PRODUCT_CHECKED, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        if (isFarmerCheckedSaved()) {
            filters.put(mydb.KEY_PLAN_TYPE, planType);
            filters.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        }

        Cursor cursor = mydb.getData(mydb.ENGRO_DEALAERS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                DataModel model = new DataModel();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(mydb.KEY_ENGRO_PRODUCT_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(mydb.KEY_ENGRO_PRODUCT_ID));
                productChecked = cursor.getString(cursor.getColumnIndex(mydb.KEY_ENGRO_PRODUCT_CHECKED));
                model.setName(productName);
                model.setId(productID);
                if (isFarmerCheckedSaved()) {
                    if (productChecked.equals("false")) {
                        model.setChecked(false);
                    } else {
                        model.setChecked(true);
                    }
                } else {
                    model.setChecked(false);
                }
                dataModels.add(model);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data

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
        else if(from.equals("dealer"))
        {
            title.setText("Select Dealer");
        }

        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if (from.equals("crop")) {
            prepareCropData(companyList);
        } else if (from.equals("product")) {
            prepareProductData(companyList);
        }
        else if (from.equals("dealer")) {
            prepareDealerData(companyList);
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
                else if(from.equals("dealer"))
                {
                    map.put(mydb.KEY_TODAY_SERVINGDEALERID, companyname.getId());
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

    public void selectDialougeOtherProducts(AutoCompleteTextView autoProduct, OtherProduct plan, HashMap<String, String> mapRecommendation) {
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

        title.setText("Select Product");
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);

        prepareProductData(companyList);

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
                plan.setOtherproducts(companyname.getPoint());
                mapRecommendation.put(mydb.KEY_TODAY_OTHER_PACKS_ID, companyname.getId());


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
    private String getDealerData(String id)
    {

        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");


        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, id);
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

               productName = Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));

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

    private void prepareDealerData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                String customerName = Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));
                String customerCode = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                companyname.setPoint(customerName);
                companyname.setId(customerCode);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }





        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }



    private void validateInputs() {
        if (!(Helpers.isEmptyAutoTextview(getApplicationContext(), auto_crop))
                        && !(Helpers.isEmptyAutoTextview(getApplicationContext(), auto_Serving_dealer)) &&
                        !(Helpers.isEmpty(getApplicationContext(), etcropacre)) &&
                        !(Helpers.isEmpty(getApplicationContext(), etcropdef)) &&
                        !(Helpers.isEmpty(getApplicationContext(), etpacks))
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
                if (etpacks.getText().toString() != null || !etpacks.equals(null)) {
                    map.put(mydb.KEY_TODAY_PACKS_LIQUIATED, etpacks.getText().toString());
                } else {
                    map.put(mydb.KEY_TODAY_PACKS_LIQUIATED, "");
                }
//                if (etotherpacksliq.getText().toString() != null || !etotherpacksliq.equals(null)) {
//                    map.put(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED, etotherpacksliq.getText().toString());
//                } else {
//                    map.put(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED, "");
//                }
                if (etcropacre.getText().toString() != null || !etcropacre.equals(null)) {
                    map.put(mydb.KEY_TODAY_CROP_ACE, etcropacre.getText().toString());
                } else {
                    map.put(mydb.KEY_TODAY_CROP_ACE, "");
                }
                if (etcropdef.getText().toString() != null || !etcropdef.equals(null)) {
                    map.put(mydb.KEY_TODAY_CROP_DEF, etcropdef.getText().toString());
                } else {
                    map.put(mydb.KEY_TODAY_CROP_DEF, "");
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
         if (Helpers.isNetworkAvailable(FarmVisitActivity.this))
         {
             addCheckOut();
             updateOutletStatus("Visited","1");
             new PostSyncFarmer().execute();
         }
         else {
             addCheckOut();
             updateOutletStatus("Visited","0");

             Toast.makeText(getApplicationContext(), "Farmer Saved", Toast.LENGTH_SHORT).show();
             //sHelper.clearPreferenceStore();

             Intent farmvisit = new Intent(FarmVisitActivity.this, VisitFarmerActivity.class);
             startActivity(farmvisit);
         }
//
//            Intent soil = new Intent(FarmVisitActivity.this, SoilSamplingActivity.class);
//            Bundle data = new Bundle();
//            data.putString(Constants.PLAN_TYPE_FARMER, planType);
//            soil.putExtras(data);
//            startActivity(soil);
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

    private boolean isthisFarmeOtherProductsAlreadyExists() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_OTHER_PRODUCT_ID, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        filer.put(mydb.KEY_PLAN_TYPE, planType);
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_OTHER_PRODUCTS, map, filer);
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

    public void getServingDealersfromDatabase() {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                servingDealersArraylist.add(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))));
                servingDealersIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID)));
            }
            while (cursor.moveToNext());
        }
        for (int i = 0; i < servingDealersArraylist.size(); i++) {
            servingDealersArraylist.get(i).replace("%20", " ");
            servingDealersArraylist.set(i, servingDealersArraylist.get(i).replace("%20", " "));
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
    private boolean isthisFarmerOtherProductDataAlreadyExists() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMMER_ID, "");
        HashMap<String, String> filer = new HashMap<>();
        filer.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_OTHERPACKS, map, filer);
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

    public void addOtherPacks() {
        LayoutInflater li = LayoutInflater.from(FarmVisitActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_add_other_packs, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        OtherProduct plan = new OtherProduct();
        HashMap<String, String> mapOtherProduct = new HashMap<>();

        ImageView ivClose = promptsView.findViewById(R.id.imageView);
        AutoCompleteTextView auto_other_product = promptsView.findViewById(R.id.auto_other_product);
        ;
        EditText et_packs_liquidated = promptsView.findViewById(R.id.et_packs_liquidated);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        auto_other_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialougeOtherProducts(auto_other_product, plan, mapOtherProduct);


            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add_packs);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packsLiquidated  = et_packs_liquidated.getText().toString();
                mapOtherProduct.put(mydb.KEY_TODAY_FARMMER_ID, sHelper.getString(Constants.S_FARMER_ID));
                mapOtherProduct.put(mydb.KEY_PLAN_TYPE, planType);
                mapOtherProduct.put(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED, packsLiquidated);
                plan.setOtherpacksliqudiated(packsLiquidated);
                arraylistotherproduct.add(plan);
                mydb.addData(mydb.TODAY_FARMER_OTHERPACKS, mapOtherProduct);
                //drawRecommendationTable();
                drawOtherProductsTable();
                alertDialog.dismiss();

            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();

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
                // validateInputsRecommendation(et_acre, et_dosePerAcre, recommendation_auto_crop, recommendation_auto_main_product, recommendation_auto_fert, alertDialog, plan, mapRecommendation);
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
    public void drawOtherProductsTable()
    {
        mTableLayoutotherproducts.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Other Product");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(16);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Other Packs Liquidated");
        column2.setTextSize(16);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));



//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 100, 0.50f));
        row.addView(column2, new TableRow.LayoutParams(0, 100, 0.50f));



        mTableLayoutotherproducts.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayoutotherproducts.addView(vline);

        for (int i = 0; i < arraylistotherproduct.size(); i++) {


            TableRow row2 = new TableRow(this);

            TextView otherproduct = new TextView(this);
            otherproduct.setText(arraylistotherproduct.get(i).getOtherproducts());
            otherproduct.setTextSize(16);
            //startDate.setBackgroundResource(R.drawable.table_row);
            otherproduct.setGravity(Gravity.CENTER);
            otherproduct.setPadding(2, 2, 2, 2);
            TextView packsliquidated = new TextView(this);
            packsliquidated.setText(arraylistotherproduct.get(i).getOtherpacksliqudiated());
            packsliquidated.setGravity(Gravity.CENTER);
            packsliquidated.setTextSize(16);
            //name.setBackgroundResource(R.drawable.table_row);
            packsliquidated.setPadding(2, 2, 2, 2);



//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(otherproduct, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.50f));
            row2.addView(packsliquidated, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.50f));



            mTableLayoutotherproducts.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayoutotherproducts.addView(vline);
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

    private void updateOutletStatus(String visited,String interentstatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, visited);
        params.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED_INTERNET_AVAILALE, interentstatus);
        params.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        filter.put(mydb.KEY_PLAN_TYPE, sHelper.getString(Constants.PLAN_TYPE_FARMER));
        mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN, params, filter);
        mydb.close();
    }

    private void addCheckOut() {
        mydb = new MyDatabaseHandler(FarmVisitActivity.this);

        gpsTracker = new GpsTracker(FarmVisitActivity.this);
        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FarmVisitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            checkoutlat = gpsTracker.getLatitude();
            checkoutlng = gpsTracker.getLongitude();
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(FarmVisitActivity.this, FarmersStartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private class PostSyncFarmer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;
        String jsonObject = "";
        private HttpHandler httpHandler;
        String farmerId = "";
        String visitStatus;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FarmVisitActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            String farmerIdToupdate = "";
            mydb = new MyDatabaseHandler(FarmVisitActivity.this);

            System.out.println("Post Outlet URl" + Constants.FFM_POST_FARMER_TODAY_JOURNEY_PLAN);
            ArrayList<FarmerCheckIn> inputCollection = new ArrayList<>();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<>();
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, "");
            map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CODE, "");
            map.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
            Cursor cursor2 = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {

                    FarmerCheckIn farmerCheckIn = new FarmerCheckIn();
                    farmerCheckIn.setFarmerId(Integer.valueOf(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID))));
                    farmerIdToupdate = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID)); // to update status of record
                    farmerId = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID));
                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID)).equals("NA")) {
                        farmerCheckIn.setDayId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID))));
                    } else {
                        farmerCheckIn.setDayId(0);
                    }

                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID)).equals("NA")) {
                        farmerCheckIn.setJourneyPlanId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID))));
                    } else {
                        farmerCheckIn.setJourneyPlanId(null);
                    }

                    farmerCheckIn.setFarmerName(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))));

                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("NA") && !cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("null")) {
                        farmerCheckIn.setLatitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE))));
                    } else {
                        farmerCheckIn.setLatitude(0.0);
                    }
                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("NA") && !cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("null")) {
                        farmerCheckIn.setLongtitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE))));
                    } else {
                        farmerCheckIn.setLongtitude(0.0);
                    }

                    farmerCheckIn.setMobileNo(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_FARMER_MOBILE_NO)));

                    farmerCheckIn.setSalesPoint(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME))));
                    //farmerCheckIn.setDistance(0);

                    if (Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_IS_VISITED))).equals("Visited")) {


                        myloadStartActviiyResult(farmerCheckIn, farmerId);
                        loadLocationlastFarmer(farmerCheckIn, farmerId);
                        if(rolename.equals("Field Force Team") || rolename.equals("FieldAssistant"))
                        {
                            loadActivityRoleWise(farmerCheckIn, farmerId);
                        }
                        else
                        {
                            loadActivity(farmerCheckIn, farmerId);
                        }
                        //loadActivity(farmerCheckIn, farmerId);
                        if(rolename.equals("Field Force Team") ||rolename.equals("FieldAssistant") ) {
                            loadSampling(farmerCheckIn, farmerId);
                        }
                        loadRecommendations(farmerCheckIn, farmerId);

                    }

                    //farmerCheckIn.setCheckOutLatitude("");
                    //farmerCheckIn.setCheckOutLongitude("");
                    farmerId = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID));
                    inputCollection.add(farmerCheckIn);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler(FarmVisitActivity.this);
                HashMap<String, String> headerParams2 = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
                // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.FFM_POST_FARMER_TODAY_JOURNEY_PLAN, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            //discription = String.valueOf(jsonObj.getString("description"));

                            // Helpers.displayMessage(MainActivity.this, true, status);
                            System.out.println(status + " ---- " + message);
                            System.out.println(output);

                            //Toast.makeText(getApplicationContext(), status +" "+ message, Toast.LENGTH_SHORT).show();
                            if (status.equals("true")) {
                                //updateOutletStatus();
                                myUpdateOutletStatus();
                                // Helpers.displayMessage(MainActivity.this, true, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");

            mydb.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmVisitActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Data Posted Successfully")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent farmvisit = new Intent(FarmVisitActivity.this, VisitFarmerActivity.class);
                                startActivity(farmvisit);
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }
    private void myUpdateOutletStatus() {
        HashMap<String, String> params = new HashMap<>();
        params.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "1");
        HashMap<String, String> filter = new HashMap<>();
        //filter.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "0");
        filter.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN, params, filter);
    }
    private void loadActivity(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivity = new HashMap<>();
        mapForActivity.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForActivity.put(mydb.KEY_TODAY_CROPID, "");
        mapForActivity.put(mydb.KEY_TODAY_ADDRESS, "");
        mapForActivity.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
        mapForActivity.put(mydb.KEY_TODAY_REMARKS, "");
        mapForActivity.put(mydb.KEY_TODAY_CROP_DEF, "");
        mapForActivity.put(mydb.KEY_TODAY_CROP_ACE, "");
        mapForActivity.put(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED, "");
        mapForActivity.put(mydb.KEY_TODAY_PACKS_LIQUIATED, "");
        mapForActivity.put(mydb.KEY_TODAY_SERVINGDEALERID, "");
        mapForActivity.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForActivity.put(mydb.KEY_TODAY_LONGITUTE, "");

        HashMap<String, String> filtersforActivity = new HashMap<>();
        filtersforActivity.put(mydb.KEY_TODAY_FARMMMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, mapForActivity, filtersforActivity);
        Activity activity = new Activity();
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                // do for activity

                activity.setCropId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                activity.setAddress(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_ADDRESS)));
                activity.setMainProduct(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_MAIN_PRODUCT))));
                activity.setRemarks(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_REMARKS)));
                activity.setLatitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LATITUTE))));
                activity.setLongitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LONGITUTE))));
                activity.setCropDeficiency(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROP_DEF))));
                activity.setCropAcreage(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROP_ACE))));
                activity.setCustomerId(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_SERVINGDEALERID)));
                activity.setPacksLiquidated(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_PACKS_LIQUIATED))));
                // activity.setOtherPacksLiquidated(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED))));
                activity.setOtherProducts(loadotherProducts(thisfarmerId));

                thisfarmerCheckIn.setActivity(activity);
            }
            while (cursorActivity.moveToNext());
        }

        //return activity;
    }
    private ArrayList<com.tallymarks.ffmapp.models.getallFarmersplanoutput.OtherProduct> loadotherProducts(String farmerid) {
        ArrayList<com.tallymarks.ffmapp.models.getallFarmersplanoutput.OtherProduct> productsList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_TODAY_OTHER_PACKS_ID, "");
        map.put(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMMER_ID, farmerid);
        Cursor cursor2 = mydb.getData(mydb.TODAY_FARMER_OTHERPACKS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                com.tallymarks.ffmapp.models.getallFarmersplanoutput.OtherProduct prod = new com.tallymarks.ffmapp.models.getallFarmersplanoutput.OtherProduct();
                prod.setProductId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_ID))));
                prod.setOtherPacksLiquidated(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED))));
                productsList.add(prod);

            }
            while (cursor2.moveToNext());
        }
        return productsList;
    }
    private void loadRecommendations(FarmerCheckIn thisFarmerCheckIn, String thisFarmerId) {
        // Mapping for recommendation
        //mydb = new MyDatabaseHandler(MainActivity.this);
        HashMap<String, String> mapForRecommendation = new HashMap<>();
        mapForRecommendation.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_CROPID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_FERTTYPE_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_BRAND_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_DOSAGE, "");

        HashMap<String, String> filtersforRecommendation = new HashMap<>();
        filtersforRecommendation.put(mydb.KEY_TODAY_FARMMMER_ID, thisFarmerId);

        ArrayList<Recommendation> recommendationList = new ArrayList<>();

        // for recommendations
        Cursor cursorRecommendations = mydb.getData(mydb.TODAY_FARMER_RECOMMENDATION, mapForRecommendation, filtersforRecommendation);
        if (cursorRecommendations.getCount() > 0) {
            cursorRecommendations.moveToFirst();
            do {
                // do for recommendation
                Recommendation recommendation = new Recommendation();
                recommendation.setCropId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_CROPID)));
                recommendation.setFertAppTypeId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_FERTTYPE_ID)));
                recommendation.setBrandId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_BRAND_ID)));
                recommendation.setDosage(Integer.parseInt(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_DOSAGE))));
                recommendationList.add(recommendation);
            }
            while (cursorRecommendations.moveToNext());
        }

        thisFarmerCheckIn.setRecommendations(recommendationList);
        //return recommendationList;
    }
    private void loadSampling(FarmerCheckIn thisFarmerCheckIn, String thisFarmerId) {
        // Mapping for sampling
        HashMap<String, String> mapForSampleing = new HashMap<>();
        mapForSampleing.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_DEPTH_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_CROP1_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_CROP2_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_PLOT_NUMBER, "");
        mapForSampleing.put(mydb.KEY_TODAY_BLOCK_NUMBER, "");
        mapForSampleing.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForSampleing.put(mydb.KEY_TODAY_LONGITUTE, "");
        mapForSampleing.put(mydb.KEY_TODAY_REFRENCE, "");
        HashMap<String, String> filtersforSampling = new HashMap<>();
        filtersforSampling.put(mydb.KEY_TODAY_FARMMMER_ID, thisFarmerId);
        Sampling sampling;
        ArrayList<Sampling> samplingList = new ArrayList<>();
        Cursor cursorSampling = mydb.getData(mydb.TODAY_FARMER_SAMPLING, mapForSampleing, filtersforSampling);
        if (cursorSampling.getCount() > 0) {
            cursorSampling.moveToFirst();
            do {
                // do for sampling
                sampling = new Sampling();
                sampling.setPreviousCropId(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_PREVIOUSCROP_ID)));
                sampling.setDepthId(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_DEPTH_ID)));
                sampling.setCrop1Id(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_CROP1_ID)));
                sampling.setCrop2Id(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_CROP2_ID)));
                sampling.setPlotNumber(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_PLOT_NUMBER)));
                sampling.setBlockNumber(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_BLOCK_NUMBER)));
                sampling.setLatitude(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_LATITUTE)));
                sampling.setLongitude(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_LONGITUTE)));
                sampling.setReference(Helpers.clean(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_REFRENCE))));
                samplingList.add(sampling);
            }
            while (cursorSampling.moveToNext());
        }

        thisFarmerCheckIn.setSampling(samplingList);
        //return samplingList;
    }
    private void loadActivityRoleWise(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivity = new HashMap<>();
        mapForActivity.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForActivity.put(mydb.KEY_TODAY_CROPID, "");
        mapForActivity.put(mydb.KEY_TODAY_ADDRESS, "");
        mapForActivity.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
        mapForActivity.put(mydb.KEY_TODAY_REMARKS, "");

        mapForActivity.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForActivity.put(mydb.KEY_TODAY_LONGITUTE, "");

        HashMap<String, String> filtersforActivity = new HashMap<>();
        filtersforActivity.put(mydb.KEY_TODAY_FARMMMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, mapForActivity, filtersforActivity);
        Activity activity = new Activity();
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                // do for activity

                activity.setCropId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                activity.setAddress(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_ADDRESS)));
                activity.setMainProduct(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_MAIN_PRODUCT))));
                activity.setRemarks(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_REMARKS)));
                activity.setLatitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LATITUTE))));
                activity.setLongitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LONGITUTE))));
                thisfarmerCheckIn.setActivity(activity);
            }
            while (cursorActivity.moveToNext());
        }

        //return activity;
    }
    private void loadLocationlastFarmer(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMER_ID, thisfarmerId);
        Cursor cursor2 = mydb.getData(mydb.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                thisfarmerCheckIn.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE)));
                thisfarmerCheckIn.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE)));
                thisfarmerCheckIn.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP))));
                thisfarmerCheckIn.setDistance(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE))));
            }
            while (cursor2.moveToNext());
        }

    }
    private void myloadStartActviiyResult(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivityResult = new HashMap<>();
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, "");

        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, "");
        mapForActivityResult.put(mydb.KEY_PLAN_TYPE, "");

        HashMap<String, String> filtersforActivityResult = new HashMap<>();
        filtersforActivityResult.put(mydb.KEY_TODAY_FARMER_FARMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, mapForActivityResult, filtersforActivityResult);
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                journeyType = cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_PLAN_TYPE));
                thisfarmerCheckIn.setVisitObjective(Helpers.clean(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE))));
                thisfarmerCheckIn.setStatus(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setOutletStatusId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setCheckInTimeStamp(Long.parseLong(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME))));
                thisfarmerCheckIn.setCheckInLatitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE)));
                thisfarmerCheckIn.setCheckInLongitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE)));

            }
            while (cursorActivity.moveToNext());
        }
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
}
