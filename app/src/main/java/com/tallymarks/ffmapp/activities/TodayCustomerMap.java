package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import com.tallymarks.ffmapp.utils.Helpers;
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

public class TodayCustomerMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView tvTopHeader, tvDistance,tvDuration;
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

    String dealerlat, dealerlng;
    String distanceText,durationtext;
    int distancesum ,timesum;
    int totaldistancesum ,totaltimesum;
    int shoplength = 0;



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
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GpsTracker(TodayCustomerMap.this);

        mMap = googleMap;
        if (gpsTracker.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;

            }
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.setMyLocationEnabled(true);
            //            currentlat = String.valueOf(gpsTracker.getLatitude());
            currentlng = String.valueOf(gpsTracker.getLongitude());
            currentlat = String.valueOf(gpsTracker.getLatitude());

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
                    if(!cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE)).equals("NA") && !cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE)).equals("NA")) {
                        LatLng location = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE))));
                        locationArrayList.add(location);
                        shoplength++;
                        mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))))).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            //mClusterManager.addItem(new User(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE))), Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))), ""));
                            //  LatLng location = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LATITUDE))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_LANGITUDE))));
                            // mMap.addMarker(new MarkerOptions().position(location).title(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_OUTLET_NAME)))));

                    }
                }
                while (cursor.moveToNext());
            }

            // mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
//            for (int i = 0; i < locationArrayList.size(); i++) {
            LatLng location = new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng));
         //   LatLng locationdealer = new LatLng(Double.parseDouble(dealerlat), Double.parseDouble(dealerlng));
            mMap.addMarker(new MarkerOptions().position(location).title("Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            LatLng locationdemo = new LatLng(32.48765794516779,74.52277713987364);
            LatLng destination= getNearestMarker(locationArrayList , location);
         //   mMap.addMarker(new MarkerOptions().position(destination).title("Destination Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentlat), Double.parseDouble(currentlng)), 10));



            String url = getDirectionsUrl(location, destination);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
//            }
        } else {
            enableLoc();
            // DialougeManager.gpsNotEnabledPopup(ProjectDetailActivity.this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_customer_map);
        initView();

    }
    private LatLng getNearestMarker(List<LatLng> markers,
                                    LatLng origin) {

        LatLng nearestMarker = null;

        double lowestDistance = Double.MIN_VALUE;

        if (markers != null) {

            for (LatLng marker : markers) {

                double dist =  distBetween(origin.latitude,origin.longitude, marker.latitude,marker.longitude);

                if (dist  > lowestDistance) {
                    nearestMarker = marker;
                    lowestDistance = dist;
                }
            }
        }

        return nearestMarker;
    }


    /** distance in meters **/
    private float distBetween(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return (float) (dist * meterConversion);
    }

    private void initView() {
        sHelper = new SharedPrefferenceHelper(TodayCustomerMap.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        db = new DatabaseHandler(TodayCustomerMap.this);
        btnProceed = findViewById(R.id.btnProceed);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvDistance = findViewById(R.id.txt_distance);
        tvDuration = findViewById(R.id.txt_time);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("MAP Activity");


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
            Toast.makeText(TodayCustomerMap.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
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
            googleApiClient = new GoogleApiClient.Builder(TodayCustomerMap.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        TodayCustomerMap.this,
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


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String str_key = "key=" + "AIzaSyCqbQ7pazQ2cH-gX_0R0zo39m_ikBNOvsk";
        String waypoints = "waypoints=";
        for(int i=0;i<locationArrayList.size();i++){
            LatLng point  = (LatLng) locationArrayList.get(i);

            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_key + "&" + str_origin + "&" + str_dest + "&" + sensor + "&" + mode +"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                for(int i=0; i< legs.length();i++)
                {
                    JSONObject legs1 = legs.getJSONObject(i);
                    JSONObject distance = legs1.getJSONObject("distance");
                    JSONObject duration = legs1.getJSONObject("duration");
                    distanceText = distance.getString("value");
                    durationtext = duration.getString("value");
                    distancesum = Integer.parseInt(distanceText);
                    totaldistancesum += distancesum;
                    timesum = Integer.parseInt(durationtext);
                    totaltimesum += timesum;
                }
                float totaldistance = totaldistancesum/ 1000;
               // int totalb = (int) Math.round(totaldistance);
                tvDistance.setText("Total Distance : " + totaldistance + " KM");
                int shop  = shoplength * 1800;
                totaltimesum = totaltimesum+shop;

                int sec = totaltimesum % 60;
                int min = (totaltimesum / 60)%60;
                int hours = (totaltimesum/60)/60;

                String strSec=(sec<10)?"0"+Integer.toString(sec):Integer.toString(sec);
                String strmin=(min<10)?"0"+Integer.toString(min):Integer.toString(min);
                String strHours=(hours<10)?"0"+Integer.toString(hours):Integer.toString(hours);

               // System.out.println(strHours + ":" + strmin + ":" + strSec);

                tvDuration.setText("Total Duration : " + strHours +" h" + ":" + strmin + " m" +":" + strSec+" s");
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
            if (lineOptions != null) {
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
