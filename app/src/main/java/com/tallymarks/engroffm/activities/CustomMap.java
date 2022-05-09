package com.tallymarks.engroffm.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.GpsTracker;
import com.tallymarks.engroffm.utils.User;

public class CustomMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    final static int REQUEST_LOCATION = 199;
    private ClusterManager<User> mClusterManager;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleApiClient googleApiClient;
    DatabaseHandler db;
    private SupportMapFragment mapFragment;
    SharedPrefferenceHelper sHelper;
    private GpsTracker gpsTracker;
    private String currentlng = "";
    private String currentlat = "";
    private String shoplng = "";
    private String shoplat = "";
    private String shopoldlng = "";
    private String shopoldlat = "";
    private String shopname = "";
    private GoogleMap mMap;
    Button btnProceed;
    String from;

    static {
        System.loadLibrary("native-lib");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GpsTracker(CustomMap.this);

        mMap = googleMap;
        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;

            }
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //            currentlat = String.valueOf(gpsTracker.getLatitude());
            currentlng = String.valueOf(gpsTracker.getLongitude());
            currentlat = String.valueOf(gpsTracker.getLatitude());
            if (from.equals("soil")) {
                sHelper.setString(Constants.CUSTOM_LAT_SOIL, currentlat);
                sHelper.setString(Constants.CUSTOM_LNG_SOIL, currentlng);
            } else if (from.equals("farm")) {
                sHelper.setString(Constants.CUSTOM_LAT_FARM, currentlat);
                sHelper.setString(Constants.CUSTOM_LNG_FARM, currentlng);
            }

            // mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
            if (from.equals("changecordinates")) {
                LatLng location = new LatLng(Double.parseDouble(shoplat), Double.parseDouble(shoplng));
                mMap.addMarker(new MarkerOptions().position(location).title(shopname)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(shoplat), Double.parseDouble(shoplng)), 10));
            } else {
                LatLng location = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng));
                mMap.addMarker(new MarkerOptions().position(location).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));

            }
        } else {
            enableLoc();
            // DialougeManager.gpsNotEnabledPopup(ProjectDetailActivity.this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();

    }

    private void initView() {
        sHelper = new SharedPrefferenceHelper(CustomMap.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        db = new DatabaseHandler(CustomMap.this);
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
        shoplat = intent.getExtras().getString("shoplat");
        shopoldlat = intent.getExtras().getString("shoplat");
        shopname = intent.getExtras().getString("shopname");
        shoplng = intent.getExtras().getString("shoplng");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent i = new Intent(StockSellingSummaryActivity.this, SuperVisorSnapShotActivity.class);
//                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                if (from.equals("soil")) {
//                    Intent i = new Intent(CustomMap.this, SoilSamplingActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                } else if (from.equals("farm")) {
//                    Intent i = new Intent(CustomMap.this, FarmVisitActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                }

            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("soil")) {
                    sHelper.setString(Constants.CUSTOM_LAT_SOIL, currentlat);
                    sHelper.setString(Constants.CUSTOM_LNG_SOIL, currentlng);
                    onBackPressed();
////                    Intent i = new Intent(CustomMap.this, SoilSamplingActivity.class);
////                    i.putExtra("soillat", currentlat);
////                    i.putExtra("soillng", currentlng);
//                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (from.equals("farm")) {
                    sHelper.setString(Constants.CUSTOM_LAT_FARM, currentlat);
                    sHelper.setString(Constants.CUSTOM_LNG_FARM, currentlng);
//                    Intent i = new Intent(CustomMap.this, FarmVisitActivity.class);
//                    i.putExtra("farmlat", currentlat);
//                    i.putExtra("farmlng", currentlng);
//                    startActivity(i);
                    onBackPressed();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (from.equals("changecordinates")) {
                    onBackPressed();
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
            Toast.makeText(CustomMap.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
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
            googleApiClient = new GoogleApiClient.Builder(CustomMap.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        CustomMap.this,
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (from.equals("soil")) {
//            Intent i = new Intent(CustomMap.this, SoilSamplingActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        } else if (from.equals("farm")) {
//            Intent i = new Intent(CustomMap.this, FarmVisitActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }
    }


}
