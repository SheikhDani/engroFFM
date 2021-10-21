package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.User;

import java.util.HashMap;
import java.util.Map;

public class MapActivity  extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    final static int REQUEST_LOCATION = 199;
    private ClusterManager<User> mClusterManager;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleApiClient googleApiClient;
     DatabaseHandler db;
    MyDatabaseHandler mydb;
    private SupportMapFragment mapFragment;
    SharedPrefferenceHelper sHelper;
    private GpsTracker gpsTracker;
    Button btnProceed;
    String from ;

    private GoogleMap mMap;
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GpsTracker(MapActivity.this);

        mMap = googleMap;
        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
            mClusterManager = new ClusterManager<>(this, googleMap);
            googleMap.setOnCameraIdleListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);
            googleMap.setOnInfoWindowClickListener(mClusterManager);
//            currentlat = String.valueOf(gpsTracker.getLatitude());
//            currentlng = String.valueOf(gpsTracker.getLongitude());
//            // mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
//            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
//
//            LatLng location = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng));
//            mMap.addMarker(new MarkerOptions().position(location).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));

        } else {
            enableLoc();
            // DialougeManager.gpsNotEnabledPopup(ProjectDetailActivity.this);
        }

        if(from.equals("customer")) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
            map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
            map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE_MAP));
            Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE)).equals("null") || cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE)).equals("null")) {
                        LatLng location = new LatLng(0, 0);
                        mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME)))));
                    } else if (cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE)).equals("null") && cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE)).equals("null")) {
                        LatLng location = new LatLng(0, 0);
                        mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME)))));
                    } else {
                        mClusterManager.addItem(new User(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE))), Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))), ""));
                        //  LatLng location = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LANGITUDE))));
                        // mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_NAME)))));
                    }
                }
                while (cursor.moveToNext());
            }
        }
        else {

            HashMap<String, String> mapFarmer = new HashMap<>();
            mapFarmer.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, "");
            mapFarmer.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, "");
            mapFarmer.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, "");
            mapFarmer.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, "");
            HashMap<String, String> farmerfilters = new HashMap<>();
            farmerfilters.put(mydb.KEY_PLAN_TYPE, sHelper.getString(Constants.PLAN_TYPE_MAP_FARMER));
            Cursor cursorfarmer = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, mapFarmer, farmerfilters);
            if (cursorfarmer.getCount() > 0) {
                cursorfarmer.moveToFirst();
                do {
                    if (cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("null") || cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("null")) {
                        LatLng location = new LatLng(0, 0);
                        mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME)))));
                    } else if (cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("null") && cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("null")) {
                        LatLng location = new LatLng(0, 0);
                        mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME)))));
                    } else {
                        if (cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("NA") && cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("NA")) {
                            mClusterManager.addItem(new User(0.0, 0.0, Helpers.clean(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))), ""));

                        } else {
                            mClusterManager.addItem(new User(Double.parseDouble(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE))), Double.parseDouble(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE))), Helpers.clean(cursorfarmer.getString(cursorfarmer.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))), ""));

                        }
                        //  LatLng location = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LANGITUDE))));
                        // mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_NAME)))));
                    }
                }
                while (cursorfarmer.moveToNext());
            }
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();

    }

    private void initView()
    {
        sHelper = new SharedPrefferenceHelper(MapActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        db = new DatabaseHandler(MapActivity.this);
        mydb = new MyDatabaseHandler(MapActivity.this);
        btnProceed = findViewById(R.id.btnProceed);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("MAP Activity");
        Intent intent = getIntent();
       from = intent.getExtras().getString("from");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equals("customer")) {
                    Intent i = new Intent(MapActivity.this, VisitCustomerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else
                {
                    Intent i = new Intent(MapActivity.this, VisitFarmerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equals("customer")) {
                    Intent i = new Intent(MapActivity.this, VisitCustomerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else
                {
                    Intent i = new Intent(MapActivity.this, VisitFarmerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            Toast.makeText(MapActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
//            try {
//                Thread.sleep(8000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            finish();
            startActivity(getIntent());
        }
    }
    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MapActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        MapActivity.this,
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

}
