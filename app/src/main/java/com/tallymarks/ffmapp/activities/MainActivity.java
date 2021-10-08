package com.tallymarks.ffmapp.activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.ExpandableListAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.MenuModel;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.models.getallcustomersplanoutput.GetAllCustomersOutput;
import com.tallymarks.ffmapp.models.listofallcrops.ListofallCropsOutput;
import com.tallymarks.ffmapp.models.outletstatusesoutput.OutletStatusOutput;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Commitment;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Invoice;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.MarketIntel;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Order;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.PreviousStock;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.PreviousStockSnapshot;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.StockSnapshot;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.StockSold__1;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.TodayCustomerPostInput;
import com.tallymarks.ffmapp.tasks.GetAllProductBrandByCategory;
import com.tallymarks.ffmapp.tasks.GetAssignedSalesPoint;
import com.tallymarks.ffmapp.tasks.GetCompanHeldBrandBasicList;
import com.tallymarks.ffmapp.tasks.GetCustomerFarmerHierarchy;
import com.tallymarks.ffmapp.tasks.GetFatmerTodayJourneyPlan;
import com.tallymarks.ffmapp.tasks.GetListofAllBrands;
import com.tallymarks.ffmapp.tasks.GetListofAllCrops;
import com.tallymarks.ffmapp.tasks.GetListofAllDepths;
import com.tallymarks.ffmapp.tasks.GetListofallMarketPlayers;
import com.tallymarks.ffmapp.tasks.GetListofallProductCategories;
import com.tallymarks.ffmapp.tasks.GetOutletStatus;
import com.tallymarks.ffmapp.tasks.GetlistofAllDistrict;
import com.tallymarks.ffmapp.tasks.GetlistofAllFertTypes;
import com.tallymarks.ffmapp.tasks.GetlistofallGenders;
import com.tallymarks.ffmapp.tasks.LoadCustomersAllJourneyPlan;
import com.tallymarks.ffmapp.tasks.LoadCustomersTodayJourneyPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    public DrawerLayout drawer;
    private TextView tvTopHeader;
    SharedPrefferenceHelper sHelper;
    NavigationView navigationView;
    ImageView iv_Menu, iv_Back;
    ImageView iv_filter;
    final static int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    GpsTracker gpsTracker;
    DatabaseHandler db;
    SharedPrefferenceHelper sharedPrefferenceHelper;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    TextView txt_farmer_Demo, txt_soil_logs, txt_logout, txt_post_data;
    ImageView headerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //expandableListView = findViewById(R.id.expandableListView);
        // prepareMenuData();
        // populateExpandableList();
    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_Menu = findViewById(R.id.iv_drawer);
        txt_farmer_Demo = findViewById(R.id.produc_demo);
        txt_soil_logs = findViewById(R.id.soil_logs);
        headerImage = findViewById(R.id.profile_image);
        txt_logout = findViewById(R.id.logout);
        txt_post_data = findViewById(R.id.txt_post);
        iv_Menu.setVisibility(View.VISIBLE);
        iv_filter = findViewById(R.id.iv_notification);
        iv_filter.setVisibility(View.VISIBLE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("DASHBOARD");
        sHelper = new SharedPrefferenceHelper(MainActivity.this);
        gpsTracker = new GpsTracker(MainActivity.this);
        db = new DatabaseHandler(MainActivity.this);
        sharedPrefferenceHelper = new SharedPrefferenceHelper(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        txt_farmer_Demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent farmer = new Intent(MainActivity.this, FarmerDemoActivity.class);
                startActivity(farmer);
            }
        });
        txt_post_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostSyncCustomer().execute();
            }
        });
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                clearDataConfirmationPopUp();

            }
        });
        txt_soil_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent soil = new Intent(MainActivity.this, SOILSamplinLogsActivity.class);
                startActivity(soil);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        iv_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        requestMultiplePermissions();
        checkStorageCompanyHeldBrand();
        checkStorageCrops();
        checkStorageFertTypes();
        checkLoadTodayCustomerJourneyPlan();
        checkProductBrandGrouopByCategory();
        checkStorageDepth();


        //new GetOutletStatus(MainActivity.this).execute();

//        new GetAssignedSalesPoint(MainActivity.this).execute();
//        new GetlistofallGenders(MainActivity.this).execute();
//        new GetlistofAllDistrict(MainActivity.this).execute();
//        new GetListofallMarketPlayers(MainActivity.this).execute();
//        new GetListofallProductCategories(MainActivity.this).execute();
        //      new LoadCustomersAllJourneyPlan(MainActivity.this).execute();

//        new GetListofAllBrands(MainActivity.this).execute();

//        new GetCustomerFarmerHierarchy(MainActivity.this).execute();
        //new GetFatmerTodayJourneyPlan(MainActivity.this).execute();
    }

    @Override
    public void onBackPressed() {

        exitPopup();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_visitcustomer:
                Intent visitCustomer = new Intent(MainActivity.this, VisitCustomerActivity.class);
                startActivity(visitCustomer);


                drawer.closeDrawers();

                return true;
            case R.id.nav_visitfarmers:

                Intent farmvisit = new Intent(MainActivity.this, VisitFarmerActivity.class);
                startActivity(farmvisit);
                drawer.closeDrawers();

                return true;
            case R.id.nav_downlaod_farmersdata:
                Intent downlaodFarmer = new Intent(MainActivity.this, DownloadFarmersDataActivity.class);
                startActivity(downlaodFarmer);
                drawer.closeDrawers();
                return true;
            case R.id.nav_customerlocation:
                Intent farmerdemo = new Intent(MainActivity.this, CustomerLocationActivity.class);
                startActivity(farmerdemo);
                drawer.closeDrawers();
                return true;

            case R.id.nav_supervisorsnapshot:
                Intent soilsampling = new Intent(MainActivity.this, SubOrdinatesActivity.class);
                startActivity(soilsampling);
                drawer.closeDrawers();
                return true;

            case R.id.nav_addnewfarmer:
                Intent salescall = new Intent(MainActivity.this, AddNewFarmerActivity.class);
                startActivity(salescall);
                drawer.closeDrawers();
                return true;

            case R.id.nav_addfarmermeeting:

                Intent farmerMeeting = new Intent(MainActivity.this, FarmerMeetingActivity.class);
                startActivity(farmerMeeting);
                drawer.closeDrawers();
                return true;


        }


        return false;
    }

    public void checkStorageCompanyHeldBrand() {

        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
            }
            while (cursor.moveToNext());
        } else {
            new GetCompanHeldBrandBasicList(MainActivity.this).execute();
        }


    }

    public void checkStorageCrops() {

        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_CROP_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            new GetListofAllCrops(MainActivity.this).execute();
        }


    }

    public void postTodayCustomerJourneyPlan() {

    }

    public void checkStorageDepth() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_SOIL_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.SOIL_DEPTHS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            new GetListofAllDepths(MainActivity.this).execute();
        }


    }

    public void checkProductBrandGrouopByCategory() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.PRODUCT_BRANDS_CATEGORY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            }
            while (cursor.moveToNext());
        } else {
            new GetAllProductBrandByCategory(MainActivity.this).execute();
        }


    }

    public void checkStorageFertTypes() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_FERT_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.FERT_TYPES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            new GetlistofAllFertTypes(MainActivity.this).execute();
        }


    }

    public void checkLoadTodayCustomerJourneyPlan() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            new LoadCustomersTodayJourneyPlan(MainActivity.this).execute();
        }


    }


    public void clearDataConfirmationPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.clear_data_alert))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.deleteDatabase("FFMAppDatabase");
                        sharedPrefferenceHelper.clearPreferenceStore();
                        Toast.makeText(MainActivity.this, "Clear Data Successfully", Toast.LENGTH_SHORT).show();
                        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logout);

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


    //    private void prepareMenuData() {
//
//        MenuModel menuModel = new MenuModel("DASHBOARD", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//        menuModel = new MenuModel("FIELD SALES", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//        menuModel = new MenuModel("DEALERS", true, true); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        List<MenuModel> childModelsList = new ArrayList<>();
//        MenuModel childModel = new MenuModel("DEALERS INFO", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("ORDERS", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("COMPLAINTS AND FEEDBACK", false, false);
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//
//            childList.put(menuModel, childModelsList);
//        }
//
//        childModelsList = new ArrayList<>();
//        menuModel = new MenuModel("FARMERS", true, true); //Menu of Python Tutorials
//        headerList.add(menuModel);
//        childModel = new MenuModel("FARMERS INFO", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("DEMO PLOTS", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("SOIL ANALYSIS", false, false);
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//            childList.put(menuModel, childModelsList);
//        }
//
//        menuModel = new MenuModel("SALES BOARD", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//        menuModel = new MenuModel("INFO & SUPPORT", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//    }
//
//    private void populateExpandableList() {
//
//        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
//        expandableListView.setAdapter(expandableListAdapter);
//
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                if (headerList.get(groupPosition).isGroup) {
//                    if (!headerList.get(groupPosition).hasChildren) {
//                        if(headerList.get(groupPosition).menuName.equals("SALES BOARD"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,SalesBoard.class);
//                            startActivity(deal);
//                        }
//                        if(headerList.get(groupPosition).menuName.equals("INFO & SUPPORT"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,SupportActivity.class);
//                            startActivity(deal);
//                        }
//                        if(headerList.get(groupPosition).menuName.equals("FIELD SALES"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,FieldSalesActivity.class);
//                            startActivity(deal);
//                        }
//
//                        onBackPressed();
//                    }
//                }
//
//                return false;
//            }
//        });
//
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                if (childList.get(headerList.get(groupPosition)) != null) {
//                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
//                    if(model.menuName.equals("DEALERS INFO"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,DealersActivity.class);
//                        startActivity(deal);
//                    }
//                    if(model.menuName.equals("FARMERS INFO"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,FarmersActivity.class);
//                        startActivity(deal);
//                    }
//                    if(model.menuName.equals("SOIL ANALYSIS"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,SoilAnalysis.class);
//                        startActivity(deal);
//                    }
//                    onBackPressed();
//
//                }
//
//                return false;
//            }
//        });
//    }
    private void requestMultiplePermissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (gpsTracker.canGetLocation()) {
                            } else {
                                enableLoc();
                            }

                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                }

                @Override
                public void onConnectionSuspended(int i) {
                    googleApiClient.connect();
                }
            }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                }
            }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    //final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            //...
                            //GetLastLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            //...
                            break;
                    }

                }
            });
        }
    }

    private void exitPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.exit_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.finishAffinity(MainActivity.this);
                        finish();
                        moveTaskToBack(true);
                        System.exit(0);

                    }
                }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private class PostSyncCustomer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        String jsonObject = "";
        private HttpHandler httpHandler;
        String customerId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            ArrayList<TodayCustomerPostInput> inputCollection = new ArrayList<>();
            Gson gson = new Gson();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Visited");
            Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN, headerParams, filter);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    TodayCustomerPostInput inputParameters = new TodayCustomerPostInput();
                    inputParameters.setCustomerCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE)));
                    inputParameters.setCustomerId(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID)));
                    inputParameters.setCustomerName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))));
                    inputParameters.setLatitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE))));
                    inputParameters.setLongtitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE))));
                    inputParameters.setJourneyPlanId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID))));
                    inputParameters.setDayId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID))));
                    inputParameters.setSalePointName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME))));
                    customerId = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                    LoadOrderforPosting(inputParameters, customerId);
                    LoadPreviousSnapShotforPosting(inputParameters, customerId);
                    loadFloorStockforPosting(inputParameters, customerId);
                    loadStartActviiyResult(inputParameters, customerId);
                    loadLocationlast(inputParameters, customerId);
                    loadFloorStockSold(inputParameters, customerId);
                    loadMarketIntel(inputParameters, customerId);
                    loadCommitments(inputParameters, customerId);
                    loadProductDicussed(inputParameters, customerId);
                    inputCollection.add(inputParameters);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams2 = new HashMap<>();
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            if (status.equals("true")) {
                                // updateOutletStatus();
                                // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
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


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Data Posted Successfully")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }


    private void LoadPreviousSnapShotforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        int position;
        String categoryname = "";
        ArrayList<PreviousStockSnapshot> previoussnapshotList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                PreviousStockSnapshot previousnapshot = new PreviousStockSnapshot();
                categoryname = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY));
                previousnapshot.setCategory("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY))));
                previoussnapshotList.add(previousnapshot);
                position = cursor2.getPosition();
                previoussnapshotList.get(position).setPreviousStock(loadPreviousStockforPosting(categoryname));
                inputParameters.setPreviousStockSnapshot(previoussnapshotList);
            }
            while (cursor2.moveToNext());
        }
    }

    private void LoadOrderforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        String orderid = "";
        int position;
        ArrayList<Order> orders = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Order order = new Order();
                orderid = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_ID));
                order.setId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_ID))));
                order.setOrderNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_NUMBER)));
                order.setOrderDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DATE))));
                order.setOrderQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY))));
                order.setBrandName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME))));
                orders.add(order);
                position = cursor2.getPosition();
                orders.get(position).setInvoices(loadInvociesforPosting(orderid));
                inputParameters.setOrders(orders);


            }
            while (cursor2.moveToNext());
        }
    }

    private ArrayList<PreviousStock> loadPreviousStockforPosting(String categoryname) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<PreviousStock> stockList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, categoryname);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_STOCK, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                PreviousStock stock = new PreviousStock();
                stock.setCompanyHeld(Boolean.parseBoolean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD))));
                stock.setId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID))));
                stock.setName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME))));
                stock.setQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY))));
                stock.setVisitDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE))));
                stockList.add(stock);
            }
            while (cursor2.moveToNext());
        }
        return stockList;
    }

    private ArrayList<Invoice> loadInvociesforPosting(String orderid) {
        HashMap<String, String> map = new HashMap<>();
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_ORDER_ID, orderid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS_INVOICES, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER)));
                invoice.setDispatchDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE))));
                invoice.setDispatchQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY))));
                invoice.setAvailableQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY))));
                invoice.setInvoiceRate(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE)));
                invoiceList.add(invoice);
            }
            while (cursor2.moveToNext());
        }
        return invoiceList;
    }

    private void loadFloorStockforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<StockSnapshot> floorstockList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                StockSnapshot stock = new StockSnapshot();
                stock.setBrandId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID))));
                Log.e("Quanity", String.valueOf(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY))));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY)) != null && !cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY)).equals("")) {
                    stock.setQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY))));
                } else {
                    stock.setQuantity(0);
                }
                floorstockList.add(stock);
                inputParameters.setStockSnapshot(floorstockList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadFloorStockSold(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<StockSold__1> stocksoldList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                StockSold__1 stock = new StockSold__1();
                stock.setQuantitySold(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD)));
                stock.setNetSellingPrice(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE)));
                stock.setOrderNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_NUMBER)));
                stock.setInvoiceNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER)));
                stock.setProductName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME))));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE)).equals("1")) {
                    stock.setSameInvoice(true);
                } else {
                    stock.setSameInvoice(false);
                }
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD)).equals("1")) {
                    stock.setOld(true);
                } else {
                    stock.setOld(false);
                }


                stocksoldList.add(stock);
                inputParameters.setStockSold(stocksoldList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadMarketIntel(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTEL_FORWARD, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTETL_COMMENT, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_MARKET_INTEL, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                MarketIntel marketIntel = new MarketIntel();
                marketIntel.setComments(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTETL_COMMENT)));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTEL_FORWARD)).equals("1")) {
                    marketIntel.setDoForward(true);
                } else {
                    marketIntel.setDoForward(false);
                }

                inputParameters.setMarketIntel(marketIntel);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadCommitments(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<Commitment> commitmentList = new ArrayList<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_CONFIRMED, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_DELIVERY_DATE, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_QUANITY, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_RAND_ID, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_COMMITMENT_RECEIVED, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Commitment commitment = new Commitment();
                commitment.setBrandId(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_RAND_ID)));
                commitment.setQuantity(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_QUANITY)));
                commitment.setDeliveryDate(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_DELIVERY_DATE)));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_CONFIRMED)).equals("1")) {
                    commitment.setConfirmed(true);
                } else {
                    commitment.setConfirmed(false);
                }
                commitmentList.add(commitment);
                inputParameters.setCommitments(commitmentList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadProductDicussed(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<Integer> dicussedList = new ArrayList<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED_ID, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                dicussedList.add(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED_ID))));
                inputParameters.setProductsDiscussed(dicussedList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadLocationlast(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DISTANCE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                inputParameters.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE)));
                inputParameters.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE)));
                inputParameters.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP))));
                inputParameters.setDistance(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DISTANCE))));
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadStartActviiyResult(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                inputParameters.setCheckInLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE)));
                inputParameters.setCheckInLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE)));
                inputParameters.setCheckInTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME))));
                inputParameters.setStatus(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));
                inputParameters.setVisitObjective("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE))));
                inputParameters.setOutletStatusId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));

            }
            while (cursor2.moveToNext());
        }

    }
}