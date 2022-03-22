package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DirectionsJSONParser;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChangeCoordinatesMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView tvTopHeader,tvDistance;
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
    private GoogleMap mMap;
    Button btnProceed;
    private ArrayList<LatLng> locationArrayList;
    String dealerlat,dealerlng,from,dealetlatnew,delaerlngnew,dealername;
    String distanceText ;

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
        gpsTracker = new GpsTracker(ChangeCoordinatesMapActivity.this);

        mMap = googleMap;
        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;

            }
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //            currentlat = String.valueOf(gpsTracker.getLatitude());
            if(from.equals("approvallocation")) {
                currentlng = delaerlngnew;
                currentlat =  dealetlatnew;
            }
            else if(from.equals("customerlocation")) {
                currentlng = delaerlngnew;
                currentlat =  dealetlatnew;
            }
            else
            {
                currentlng = String.valueOf(gpsTracker.getLongitude());
                currentlat = String.valueOf(gpsTracker.getLatitude());
            }

            // mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
//            for (int i = 0; i < locationArrayList.size(); i++) {
                LatLng location = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng));
            mMap.addMarker(new MarkerOptions().position(location).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));
            if(!dealerlat.equals("NA") && !dealerlng.equals("NA")) {
                LatLng locationdealer = new LatLng(Double.parseDouble(dealerlat), Double.parseDouble(dealerlng));
                mMap.addMarker(new MarkerOptions().position(locationdealer).title("Previous Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                float distance = getMeterFromLatLong(Float.parseFloat(String.valueOf(currentlat)), Float.parseFloat(String.valueOf(currentlng)), Float.parseFloat(dealerlat), Float.parseFloat(dealerlng));
                float totaldistance = distance / 1000;
                int totalb = (int) Math.round(totaldistance);
                //tvDistance.setText("Distance " + totalb +" KM ");
                // googleMap.addPolyline(new PolylineOptions()
                //  .clickable(true)
                //.add(location,locationdealer));
                // Getting URL to the Google Directions API
                //  LatLng locationdemo = new LatLng(32.48711012882795, 74.52280822655976);
                String url = getDirectionsUrl(location, locationdealer);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
//            }
        } else {
            enableLoc();
            // DialougeManager.gpsNotEnabledPopup(ProjectDetailActivity.this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location_map);
        initView();

    }

    private void initView() {
        sHelper = new SharedPrefferenceHelper(ChangeCoordinatesMapActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        db = new DatabaseHandler(ChangeCoordinatesMapActivity.this);
        btnProceed = findViewById(R.id.btnProceed);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvDistance = findViewById(R.id.txt_distance);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("MAP Activity");
        Intent intent = getIntent();
        from= intent.getExtras().getString("from");
        dealerlat = intent.getExtras().getString("dealerlat");
        dealerlng = intent.getExtras().getString("dealerlng");

        if(from.equals("approvallocation"))
        {
            dealetlatnew = intent.getExtras().getString("dealerlatnew");
            delaerlngnew = intent.getExtras().getString("dealerlngnew");
            //delaername = intent.getExtras().getString("dealerlngnew");
        }
        else if(from.equals("customerlocation"))
        {
            dealetlatnew = intent.getExtras().getString("dealerlatnew");
            delaerlngnew = intent.getExtras().getString("dealerlngnew");
            //delaername = intent.getExtras().getString("dealerlngnew");
        }

        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            Toast.makeText(ChangeCoordinatesMapActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
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
            googleApiClient = new GoogleApiClient.Builder(ChangeCoordinatesMapActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        ChangeCoordinatesMapActivity.this,
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
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String str_key = "key=" + "AIzaSyCqbQ7pazQ2cH-gX_0R0zo39m_ikBNOvsk";

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_key + "&" +str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONObject routes1 = routes.getJSONObject(0);

                JSONArray legs = routes1.getJSONArray("legs");

                JSONObject legs1 = legs.getJSONObject(0);

                JSONObject distance = legs1.getJSONObject("distance");

                JSONObject duration = legs1.getJSONObject("duration");

                distanceText = distance.getString("text");
                tvDistance.setText("Distance between current and last location : "+distanceText);
                sHelper.setString(Constants.DISTANCETEXT,distanceText);

               // durationText = duration.getString("text");
                ParserTask parserTask = new ParserTask();


                parserTask.execute(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

