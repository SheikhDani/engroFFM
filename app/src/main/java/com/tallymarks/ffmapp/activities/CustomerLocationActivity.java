package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.Gson;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.changecustomerlocation.ChnageCustomerLocationinput;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.TodayCustomerPostInput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    final static int REQUEST_LOCATION = 199;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleApiClient googleApiClient;
    private SupportMapFragment mapFragment;
    SharedPrefferenceHelper sHelper;
    private GpsTracker gpsTracker;
    private String currentlng = "";
    private String currentlat = "";
    TextView txt_lat, txt_lng;
    ImageView iv_location;
    Button btn_submit;
    EditText et_customercode;

    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_location);
        initView();

    }

    private void initView() {

        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        txt_lat = findViewById(R.id.txt_latitude);
        btn_submit = findViewById(R.id.btn_submit);
        et_customercode = findViewById(R.id.et_custoemrcode);
        iv_location = findViewById(R.id.iv_location);
        txt_lng = findViewById(R.id.txt_longitude);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("CUSTOMER'S LOCATION");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerLocationActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_customercode.getText().toString().equals("") || et_customercode == null) {
                    Toast.makeText(CustomerLocationActivity.this, "Please Enter Customer Code", Toast.LENGTH_SHORT).show();

                } else if (currentlat == null || currentlat.equals("") || currentlng == null || currentlng.equals("")) {
                    Toast.makeText(CustomerLocationActivity.this, "Please Check your Location is Enabled Correctly", Toast.LENGTH_SHORT).show();
                } else {
                    new ChangeCustomerLocation(et_customercode.getText().toString()).execute();
                }
            }
        });
        gpsTracker = new GpsTracker(CustomerLocationActivity.this);

        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;

            }
            currentlng = String.valueOf(gpsTracker.getLongitude());
            currentlat = String.valueOf(gpsTracker.getLatitude());
            txt_lat.setText("latitude: " + currentlat);
            txt_lng.setText("longitude: " + currentlng);
        }
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_lat.setText("latitude: " + currentlat);
                txt_lng.setText("longitude: " + currentlng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));
            }
        });
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
        gpsTracker = new GpsTracker(CustomerLocationActivity.this);

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
            // mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));

            LatLng location = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng));
            mMap.addMarker(new MarkerOptions().position(location).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));
        } else {
            enableLoc();
            // DialougeManager.gpsNotEnabledPopup(ProjectDetailActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            Toast.makeText(CustomerLocationActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
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
            googleApiClient = new GoogleApiClient.Builder(CustomerLocationActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        CustomerLocationActivity.this,
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

    private class ChangeCustomerLocation extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String customercode = "";

        ChangeCustomerLocation(String customercode) {
            this.customercode = customercode;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CustomerLocationActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            Gson gson = new Gson();
            ChnageCustomerLocationinput inputParameters = new ChnageCustomerLocationinput();
            inputParameters.setLocationCode(customercode);
            inputParameters.setLatitude(currentlat);
            inputParameters.setLongitude(currentlng);

            httpHandler = new HttpHandler();
            HashMap<String, String> headerParams2 = new HashMap<>();
            headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String jsonObject = new Gson().toJson(inputParameters, ChnageCustomerLocationinput .class);
            Log.e("postoutput", String.valueOf(jsonObject));
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.POST_CUSTOMER_CHANGE_LOACTION, headerParams2, bodyParams, jsonObject);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("description"));
                        if (status.equals("true")) {

                            // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                            // Helpers.displayMessage(MainActivity.this, true, message);
                        }
                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(CustomerLocationActivity.this, true, e.getMessage());
                            pDialog.dismiss();
                            //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                            //pDialog.dismiss();
                        } else {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response);
                                errorMessage = json.getString("message");
                                String status = json.getString("success");
                                if (status.equals("false")) {
                                    Helpers.displayMessage(CustomerLocationActivity.this, true, errorMessage);
                                    pDialog.dismiss();
                                }
                            } catch (JSONException exception) {
                                exception.printStackTrace();

                            }
                            //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        pDialog.dismiss();
        if (status.equals("true")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerLocationActivity.this);
            alertDialogBuilder.setTitle(R.string.alert)
                    .setMessage(message)
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
}
