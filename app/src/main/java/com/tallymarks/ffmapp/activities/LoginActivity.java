package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.devicetokeninput.DeviceTokenInput;
import com.tallymarks.ffmapp.models.forgetpasswordinput.ForgetPasswordInput;
import com.tallymarks.ffmapp.models.loginoutput.LoginOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.FingerprintDialog;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Base64;
import java.util.HashMap;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    FingerprintManagerCompat managerCompat;
    ImageView sensorimg;
    Button btn_sumit;
    EditText et_username, et_password;
    TextView txtForget;
    DatabaseHandler db;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    MyHelper myHelper;
    final static int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    GpsTracker gpsTracker;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();


    }

    private void initView() {

        sensorimg = findViewById(R.id.icon);
        gpsTracker = new GpsTracker(LoginActivity.this);
        btn_sumit = findViewById(R.id.btn_submit);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        txtForget = findViewById(R.id.txt_forget);
        db = new DatabaseHandler(LoginActivity.this);
        sHelper = new SharedPrefferenceHelper(LoginActivity.this);
        extraHelper = new ExtraHelper(LoginActivity.this);
        myHelper = new MyHelper(LoginActivity.this);
        requestMultiplePermissions();
        btn_sumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent main  = new Intent(LoginActivity.this,MainActivity.class);
                //startActivity(main);
                Constants.LOGIN_USERNAME = et_username.getText().toString();
                Constants.LOGIN_PASSWORD = et_password.getText().toString();
                if (Helpers.isNetworkAvailable(LoginActivity.this)) {
                    gpsTracker = new GpsTracker(LoginActivity.this);
                    if (gpsTracker.canGetLocation()) {
                        if (Constants.LOGIN_USERNAME != null && Constants.LOGIN_PASSWORD != null
                                && !Constants.LOGIN_USERNAME.equals("") && !Constants.LOGIN_PASSWORD.equals("")
                        ) {

                            new LoginData().execute();
                        } else {
                            DialougeManager.invalidCredentialsPopup(LoginActivity.this, "", getResources().getString(R.string.usercredential));

                        }
                    } else {
                        DialougeManager.gpsNotEnabledPopup(LoginActivity.this);
                    }
                } else {
                    Helpers.noConnectivityPopUp(LoginActivity.this);
                }
            }
        });
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgetPasswordDialouge();
            }
        });
        sensorimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerCompat = FingerprintManagerCompat.from(LoginActivity.this);
                if (managerCompat.isHardwareDetected() && managerCompat.hasEnrolledFingerprints()) {
                    showFingerPrintDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Fingerprint not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (checkForExistingUser()) {

            Intent i = new Intent(LoginActivity.this, MainActivity.class);

            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        setAppInfo();


    }

    private void showFingerPrintDialog() {

        FingerprintDialog fragment = new FingerprintDialog();
        fragment.setContext(this);
        fragment.show(getSupportFragmentManager(), "");


    }

    private void openForgetPasswordDialouge() {

        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_forget_password, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText et_email = promptsView.findViewById(R.id.et_email);

        ImageView ivClsoe = promptsView.findViewById(R.id.ivClose);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ForgetPassword(et_email.getText().toString(), alertDialog).execute();
                // alertDialog.dismiss();
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    private boolean checkForExistingUser() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_LOGGED_IN, "");
        map.put(db.KEY_USER_EMAIL, "");
        map.put(db.KEY_USER_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.LOGIN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndex(db.KEY_IS_LOGGED_IN)).toString().equals("1")) {
                    return true;
                } else {
                    return false;
                }

            }
            while (cursor.moveToNext());
        } else {
            return false;
        }
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

    private class ForgetPassword extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String email = "";
        AlertDialog alertDialog;

        ForgetPassword(String email, AlertDialog alertDialog) {
            this.email = email;
            this.alertDialog = alertDialog;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            //System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            Gson gson = new Gson();
            ForgetPasswordInput inputParameters = new ForgetPasswordInput();
            inputParameters.setEmail(email);
            httpHandler = new HttpHandler(LoginActivity.this);
            HashMap<String, String> headerParams2 = new HashMap<>();
//            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
//                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
//            }
//            else
//            {
//                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
//            }
            // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String jsonObject = new Gson().toJson(inputParameters, ForgetPasswordInput.class);
            Log.e("postoutput", String.valueOf(jsonObject));
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.POST_FORGET_PASSWORD, headerParams2, bodyParams, jsonObject);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("description"));
                        if (status.equals("true")) {
                            // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                            Helpers.displayMessage(LoginActivity.this, true, message);
                        } else if (status.equals("false")) {
                            Helpers.displayMessage(LoginActivity.this, true, message);
                        }
                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(LoginActivity.this, true, e.getMessage());
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
                                    Helpers.displayMessage(LoginActivity.this, true, errorMessage);
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
            alertDialog.dismiss();
//            if (status.equals("true")) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerLocationActivity.this);
//                alertDialogBuilder.setTitle(R.string.alert)
//                        .setMessage(message)
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                //new PostSyncOutlet().execute();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }

        }
    }

    private class LoginData extends AsyncTask<String, Void, String> {

        private HttpHandler httpHandler;
        ProgressDialog pDialog;
        String message = "";
        String status = "";
        String error = "";
        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... Url) {
            String response = "";
            String basicAuth = "";
            String auth = Constants.LOGIN_USERNAME + ":" + Constants.LOGIN_PASSWORD;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                basicAuth = "Basic " + new String(Base64.getEncoder().encode(auth.getBytes()));
            }
            String loginUrl = Constants.FFM_LOGIN + "?username=" + Constants.LOGIN_USERNAME + "&password=" + Constants.LOGIN_PASSWORD + "&grant_type=" + Constants.LOGIN_GRANT_TYPE;
            try {
                httpHandler = new HttpHandler(LoginActivity.this);
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(Constants.AUTHORIZATION, Constants.BASIC);
                HashMap<String, String> bodyParams = new HashMap<>();
                response = httpHandler.httpPost(loginUrl, headerParams, bodyParams, null);
                Log.e("lOGIN Url", loginUrl);
                Log.e("Response", response);
                LoginOutput logincode = new Gson().fromJson(response, LoginOutput.class);
                if (logincode != null) {
                    for (int i = 0; i < logincode.getAuthorities().size(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_ROLE_NAME, "" + logincode.getAuthorities().get(i) == null || logincode.getAuthorities().get(i).equals("") ? getString(R.string.not_applicable) : logincode.getAuthorities().get(i));
                        db.addData(db.ROLES, map);
                        extraHelper.setString(Constants.ROLE, logincode.getAuthorities().get(i));
//                        logincode.getAuthorities().get(i);

                    }
                    sHelper.setString(Constants.ACCESS_TOKEN, logincode.getAccessToken());
                    sHelper.setString(Constants.REFERSH_TOKEN, logincode.getRefreshToken());
                    sHelper.setString(Constants.TOKEN_TYPE, logincode.getTokenType());
                    extraHelper.setString(Constants.ACCESS_TOKEN, logincode.getAccessToken());
                    extraHelper.setString(Constants.REFERSH_TOKEN, logincode.getRefreshToken());
                    extraHelper.setString(Constants.TOKEN_TYPE, logincode.getTokenType());
                    extraHelper.setString(Constants.USER_NAME, logincode.getUsername());
                    extraHelper.setString(Constants.NAME, logincode.getName());
                    myHelper.setString(Constants.REFERSH_TOKEN, logincode.getRefreshToken());

                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_COMPANY_NAME, "" + logincode.getCompanyName() == null || logincode.getCompanyName().equals("") ? getString(R.string.not_applicable) : logincode.getCompanyName());
                    map.put(db.KEY_USER_NAME, "" + logincode.getUsername() == null || logincode.getUsername().equals("") ? getString(R.string.not_applicable) : logincode.getUsername());
                    map.put(db.KEY_NAME, logincode.getName() == null || logincode.getName().equals("") ? getString(R.string.not_applicable) : logincode.getName());
                    map.put(db.KEY_USER_DESIGNATION, logincode.getDesignation() == null || logincode.getDesignation().equals("") ? getString(R.string.not_applicable) : logincode.getDesignation());
                    map.put(db.KEY_USER_EMAIL, logincode.getEmail() == null || logincode.getEmail().equals("") ? getString(R.string.not_applicable) : logincode.getEmail());
                    map.put(db.KEY_IS_LOGGED_IN, "1");
                    db.addData(db.LOGIN, map);

                    sHelper.setString(Constants.CUSTOMER_ALL_PLAN_NOT_FOUND, "2");
                    sHelper.setString(Constants.FARMER_TODAY_PLAN_NOT_FOUND, "2");
                    sHelper.setString(Constants.CUSTOMER_TODAY_PLAN_NOT_FOUND, "2");
//                    Helpers.displayMessage(LoginActivity.this, true, "Successfully Login");
//                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
//                    // main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(main);
//                    finish();
                   getDeviceToken(pDialog);

                } else {
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //Helpers.displayMessage(LoginActivity.this, true, getResources().getString(R.string.invalid_credentials_message));


                //  return response.toString();
            } catch (Exception exception) {
                pDialog.dismiss();
                if (response.equals("")) {
                    Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                    //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                    //pDialog.dismiss();
                } else {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                        errorMessage = json.getString("error_description");
                        error = json.getString("error");


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // pDialog.dismiss();
            View view = LoginActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (error != null && !error.equals("")) {

                DialougeManager.invalidCredentialsPopup(LoginActivity.this, "", errorMessage);

            }


//            parseErrorResponse(result);
        }


    }

    @Override
    public void onBackPressed() {
        exitPopup();
    }

    private void exitPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.exit_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.finishAffinity(LoginActivity.this);
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

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(LoginActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
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
                                        LoginActivity.this,
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

    public void getDeviceToken(ProgressDialog progressDialog) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        new SendDeviceToekntoServer(token, progressDialog).execute();
                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setAppInfo() {
        TextView appinfo = (TextView) findViewById(R.id.tv_appinfo);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("App Ver:");
        sb.append(pInfo.versionName);
        sb.append("\t \t");
        sb.append("Build:");
        sb.append(Constants.ENVIRONMENT);
        appinfo.setTextSize(13);
        appinfo.setTypeface(null, Typeface.ITALIC);
        appinfo.setText(sb.toString());
    }

    private class SendDeviceToekntoServer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String token = "";


        SendDeviceToekntoServer(String token, ProgressDialog progressDialog) {
            this.token = token;
            this.pDialog = progressDialog;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(LoginActivity.this);
//            pDialog.setMessage(getResources().getString(R.string.loading));
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            //System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            Gson gson = new Gson();
            DeviceTokenInput inputParameters = new DeviceTokenInput();
            inputParameters.setToken(token);
            httpHandler = new HttpHandler(LoginActivity.this);
            HashMap<String, String> headerParams2 = new HashMap<>();
            if (sHelper.getString(Constants.ACCESS_TOKEN) != null && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
//            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
//                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
//            }
//            else
//            {
//                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
//            }
            // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String jsonObject = new Gson().toJson(inputParameters, DeviceTokenInput.class);
            Log.e("postoutput", String.valueOf(jsonObject));
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.FFM_POST_DEVICE_TOKEN, headerParams2, bodyParams, jsonObject);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("description"));
                        if (status.equals("true")) {
                            FirebaseMessaging.getInstance().subscribeToTopic("ffm")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String msg = "ffm";
                                            if (!task.isSuccessful()) {
                                                msg = "failed";
                                            } else {
                                                pDialog.dismiss();
                                                Helpers.displayMessage(LoginActivity.this, true, "Successfully Login");
                                                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                                // main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(main);
                                                finish();
                                            }
                                            //Log.d(TAG, msg);
                                            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                            //Helpers.displayMessage(LoginActivity.this, true, message);
                        } else if (status.equals("false")) {
                            Helpers.displayMessage(LoginActivity.this, true, message);
                        }
                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(LoginActivity.this, true, e.getMessage());
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
                                    Helpers.displayMessage(LoginActivity.this, true, errorMessage);
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
            //  pDialog.dismiss();


        }
    }

}
