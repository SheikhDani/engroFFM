package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Customer;
import com.tallymarks.ffmapp.models.postcustomerlocation.PostCustomerLocationInput;
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

public class LocationChangeRequestActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    private String currentlng = "";
    private String currentlat = "";
    private ArrayList<Customer> dealersArrayList;
    TextView txt_lat, txt_lng, location_status, tvReason, tvDealer, tvlastvisitcount;
    ImageView iv_location;
    Button btn_submit;
    EditText et_reason;
    private DatabaseHandler databaseHandler;
    private AutoCompleteTextView autoDealer;
    private String dealerID, dealerName, dealerlat, dealerlng, dealerlocationstatus;
    GpsTracker gps;
    String lastvisitCount = "";

    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_change_request);
        initView();

    }

    private void initView() {
        iv_location = findViewById(R.id.iv_location);
        txt_lat = findViewById(R.id.txt_latitude);
        txt_lng = findViewById(R.id.txt_longitude);
        location_status = findViewById(R.id.lcoationstatus);
        tvDealer = findViewById(R.id.txt_sel_del);
        tvlastvisitcount = findViewById(R.id.txt_lastvisitcount);
        tvReason = findViewById(R.id.txt_rea);
        location_status = findViewById(R.id.lcoationstatus);
        btn_submit = findViewById(R.id.btn_submit);
        et_reason = findViewById(R.id.et_reason);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        autoDealer = findViewById(R.id.auto_dealer);
        databaseHandler = new DatabaseHandler(LocationChangeRequestActivity.this);
        sHelper = new SharedPrefferenceHelper(LocationChangeRequestActivity.this);
        extraHelper = new ExtraHelper(LocationChangeRequestActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("Change Customer Location");

        SpannableStringBuilder objective = setStarToLabel("Select Dealer");
        tvDealer.setText(objective);
        SpannableStringBuilder reason = setStarToLabel("Enter Reason");
        tvReason.setText(reason);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LocationChangeRequestActivity.this, ChangeCustomerLocationListActivity.class);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // loadDealers(databaseHandler);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dealerID != null && !dealerID.equals("") && !currentlat.equals("")
                        && !currentlng.equals("") && !et_reason.getText().toString().equals("")) {
                    {
                        if (Helpers.isNetworkAvailable(LocationChangeRequestActivity.this)) {
                            new PostCustomerLocation(dealerID).execute();
                        } else {
                            Helpers.noConnectivityPopUp(LocationChangeRequestActivity.this);
                        }

                    }

                } else {
                    Toast.makeText(LocationChangeRequestActivity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }


        });
        autoDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoDealer);
            }
        });
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dealerID != null && !dealerID.equals("")) {


                    Intent loc = new Intent(LocationChangeRequestActivity.this, ChangeCoordinatesMapActivity.class);
                    loc.putExtra("dealerlat", dealerlat);
                    loc.putExtra("dealerlng", dealerlng);
                    loc.putExtra("from", "changelocation");
                    startActivity(loc);

                    gps = new GpsTracker(LocationChangeRequestActivity.this);
                    if (gps.canGetLocation()) {
                        if (ActivityCompat.checkSelfPermission(LocationChangeRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationChangeRequestActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        currentlat = String.valueOf(gps.getLatitude());
                        currentlng = String.valueOf(gps.getLongitude());
                    }
                    txt_lat.setText("Latitude: " + currentlat);
                    txt_lng.setText("Longitude: " + currentlng);


                } else {
                    Toast.makeText(LocationChangeRequestActivity.this, "Please Select Dealer", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    public void selectDialouge(AutoCompleteTextView autoProduct) {
        LayoutInflater li = LayoutInflater.from(LocationChangeRequestActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LocationChangeRequestActivity.this);
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

        title.setText("Select Dealer");


        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);


        prepareDealerData(companyList);


        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList, "changelocation");

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
                autoProduct.setText(companyname.getCode() + "-" + companyname.getPoint());
                new GetLastVisitCount(companyname.getId()).execute();


                dealerID = companyname.getId();
                dealerName = companyname.getPoint();
                dealerlat = companyname.getLat();
                dealerlng = companyname.getLng();
                dealerlocationstatus = companyname.getLocationstatus();
                location_status.setText(dealerlocationstatus);
                if (dealerlocationstatus.equals("Verified")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LocationChangeRequestActivity.this);
                    alertDialogBuilder.setTitle(R.string.alert)
                            .setMessage("You would require your supervisor's approval to change the dealers verified location.")
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

    private void prepareDealerData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LOCATION_STATUS, "");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(databaseHandler.KEY_TODAY_JOURNEY_TYPE, "all");
        Cursor cursor = databaseHandler.getData(databaseHandler.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                String customerName = Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));
                String customerCode = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_CODE));
                String customerlat = Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE)));
                String customerlng = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE));
                String customerID = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                String customerlocation = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_LOCATION_STATUS));
                companyname.setPoint(customerName);
                companyname.setId(customerID);
                companyname.setCode(customerCode);
                companyname.setLat(customerlat);
                companyname.setLng(customerlng);
                companyname.setLocationstatus(customerlocation);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }

    private class PostCustomerLocation extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String customercode = "";

        PostCustomerLocation(String customercode) {
            this.customercode = customercode;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LocationChangeRequestActivity.this);
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
            PostCustomerLocationInput inputParameters = new PostCustomerLocationInput();
            inputParameters.setCustomerId(Long.parseLong(customercode));
            inputParameters.setLocationStatus(dealerlocationstatus);
            inputParameters.setLatitude(Double.parseDouble(currentlat));
            if (!dealerlat.equals("NA") && !dealerlng.equals("NA")) {
                inputParameters.setOldLatitude(Double.parseDouble(dealerlat));
                inputParameters.setOldLongitude(Double.parseDouble(dealerlng));
            } else {
                inputParameters.setOldLatitude(0.0);
                inputParameters.setOldLongitude(0.0);

            }
            inputParameters.setLongitude(Double.parseDouble(currentlng));
            if (dealerlocationstatus.equals("Verified")) {
                inputParameters.setStatus("Pending");
            } else if (dealerlocationstatus.equals("Unverified")) {
                inputParameters.setStatus("Completed");
            }
            inputParameters.setReason(et_reason.getText().toString());
            inputParameters.setLastvisitcount(Integer.parseInt(lastvisitCount));

            httpHandler = new HttpHandler(LocationChangeRequestActivity.this);
            HashMap<String, String> headerParams2 = new HashMap<>();
            if (sHelper.getString(Constants.ACCESS_TOKEN) != null && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            } else {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
            // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String jsonObject = new Gson().toJson(inputParameters, PostCustomerLocationInput.class);
            Log.e("postoutput", String.valueOf(jsonObject));
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.POST_CUSTOMER_LOACTION, headerParams2, bodyParams, jsonObject);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("description"));
                        if (status.equals("true")) {

                            // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                            //Helpers.displayMessage(LocationChangeRequestActivity.this, true, message);
                        }
                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(LocationChangeRequestActivity.this, true, e.getMessage());
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
                                    Helpers.displayMessage(LocationChangeRequestActivity.this, true, errorMessage);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LocationChangeRequestActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(LocationChangeRequestActivity.this, ChangeCustomerLocationListActivity.class);

                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LocationChangeRequestActivity.this, ChangeCustomerLocationListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    private class GetLastVisitCount extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String customercode = "";

        GetLastVisitCount(String customercode) {
            this.customercode = customercode;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LocationChangeRequestActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);

            String journeyPlanUrl = Constants.FFM_LAST_VISIT_COUNT + "?customerId=" + customercode;

            httpHandler = new HttpHandler(LocationChangeRequestActivity.this);
            HashMap<String, String> headerParams2 = new HashMap<>();
            if (sHelper.getString(Constants.ACCESS_TOKEN) != null && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            } else {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
            // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            // HashMap<String, String> bodyParams = new HashMap<>();

            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpGet(journeyPlanUrl, headerParams2);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        lastvisitCount = String.valueOf(jsonObj.getString("lastVisitCount"));

                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(LocationChangeRequestActivity.this, true, e.getMessage());
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
                                    Helpers.displayMessage(LocationChangeRequestActivity.this, true, errorMessage);
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
            if (!lastvisitCount.equals("")) {
                tvlastvisitcount.setText("Last Visit Count: " + lastvisitCount);
            }


        }
    }

}
