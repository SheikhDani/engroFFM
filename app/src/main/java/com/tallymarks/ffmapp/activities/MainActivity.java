package com.tallymarks.ffmapp.activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import java.util.ArrayList;
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
    TextView txt_farmer_Demo, txt_soil_logs, txt_logout;
    ImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //expandableListView = findViewById(R.id.expandableListView);
        // prepareMenuData();
        // populateExpandableList();
    }
    private void initView()

    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_Menu = findViewById(R.id.iv_drawer);
        txt_farmer_Demo = findViewById(R.id.produc_demo);
        txt_soil_logs = findViewById(R.id.soil_logs);
        headerImage = findViewById(R.id.profile_image);
        txt_logout = findViewById(R.id.logout);
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
        // test();
    //   new GetListofAllDepths(MainActivity.this).execute();
  //     new GetlistofAllFertTypes(MainActivity.this).execute();
 //       new GetListofAllCrops(MainActivity.this).execute();
//
 //      new GetOutletStatus(MainActivity.this).execute();
        new LoadCustomersTodayJourneyPlan(MainActivity.this).execute();
//        new GetAssignedSalesPoint(MainActivity.this).execute();
//        new GetlistofallGenders(MainActivity.this).execute();
//        new GetlistofAllDistrict(MainActivity.this).execute();
//        new GetListofallMarketPlayers(MainActivity.this).execute();
//        new GetListofallProductCategories(MainActivity.this).execute();
  //      new LoadCustomersAllJourneyPlan(MainActivity.this).execute();
     //  new GetAllProductBrandByCategory(MainActivity.this).execute();
//        new GetListofAllBrands(MainActivity.this).execute();
//        new GetCompanHeldBrandBasicList(MainActivity.this).execute();
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
public void test()
{


    String base64String = "data:application/zip;base64,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wL7cbmwBq5+DYM/6/ke2qnlg3eeyPuNr3ndSX/rejTG+e855HTm7flC757o89vXPaG9Y/VzPn+hu79e9ojuFRnZgrV5X9xu/4+rXP96OlzPeVD/l6eJT98V+sh5/97rP2U+4+xde8Xtvu+feyze81xMf97G7+4p/ur5T+CuPz0z53XqAuU/+YDMz94P1qV399M6fut3MzH3Goeyv/+Tr33LnPZce8PAPetRf3vUHd9/aGO7l6gf8xff3t590AlbrltvMzB5ht95qVr3nqbemGOkEQBPXnQBoIp0AaCKdAGginQBoIp2A7Xn76/rLxL26pPB/+4PB6yGdgO35keeOWfqpJYWf9/L+MhGkE7A5d//op49Y+o1X95c5evWnDV4R6QRsya88zMze/AufOKKK//jhJaXf+d6DV0Q6AVvyf3+Cmf34fe97964fdnukAH/cADOzu+5qzf53f/Mw535vyTsPU+6rf7grzQ5jmZchnYDtqKp/8i+r59uP23UPvPhCM3t0dfnyfmSAn3/Cw6rL1dea2auvrKon/7HZs595UX2GvfTi+hsuXm1m31Bdrr76LrNfftqusr937aXqZjOzf3lRXb78djP7guqa6hPfbGYXH3fFFdVfMrP/eo2ZPb268uKnB2zukIGtAJyn115+u3Puyuoed6c598B/4dxrHvKj9azn2h879+f2m8598x3uXnuEc3/hofc798pLdzj3rgvnXvjBzt1lD3fO6kHibje73bkP+djb3Ctuut+577vs3Oc87D7nbrb73Bvs1fe5+695sXMv+nh3+0f8L869zT63eGM5dgI25HfueYiZ3f2YK+wqu+/uW77U7KP+1m6wzP/yqPcye8DjX2r2vKvt0rXvNnvr8yuzb/riq80e9Ev2hy/6PrMrH/4W24/3/Tb7rGvMvvOXXmv/+Nsqs8+/x1714999YfZtl99ov3/5Yy+s+pwfMnv9B9kPvvafm910qfzgibuAgQ351H/rzNzFC59jVrmf+JRrzezep/wnMzO74dVPMLOXfvtr7bYfvcWe+8jfr9Ohur0eA/iLvv86M7vjfvfWR9TXmH7gX7z6GjOrvvYfV9dWZnab+9hfcGbmPunjv/zzn/RlZvbmD7jNHvHjH33TO64zs9uue3fpxq7xWcAAIn7mBjP70xueYfZnl+3OJ73G7HDx+dabzMxe+ZH2z77x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";
    String base64Image = base64String.split(",")[1];


    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    headerImage.setImageBitmap(decodedByte);
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
                        MainActivity.this.deleteDatabase("FFMAppDb");
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
}