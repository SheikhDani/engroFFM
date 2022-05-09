package com.tallymarks.engroffm.activities;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.SalesCallAdapter;
import com.tallymarks.engroffm.adapters.SalesPointAdapter;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.ExtraHelper;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.Commitment;
import com.tallymarks.engroffm.models.DataModel;
import com.tallymarks.engroffm.models.SaelsPoint;
import com.tallymarks.engroffm.models.todayjourneyplaninput.MarketIntel;
import com.tallymarks.engroffm.models.todayjourneyplaninput.StockSnapshot;
import com.tallymarks.engroffm.models.todayjourneyplaninput.StockSold__1;
import com.tallymarks.engroffm.models.todayjourneyplaninput.TodayCustomerPostInput;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.DialougeManager;
import com.tallymarks.engroffm.utils.GpsTracker;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.HttpHandler;
import com.tallymarks.engroffm.utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QualityofSalesCallActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back, iv_commitment;
    ArrayList<DataModel> dataModels;
    private TableLayout mTableLayout;
    ArrayList<Commitment> arraylist = new ArrayList<Commitment>();
    ListView listView;
    private SalesCallAdapter adapter;
    Button btn_back, btn_add_market_intelligence;
    DatabaseHandler db;
    SharedPrefferenceHelper sHelper;
    String targetDate = "";
    Calendar myCalendar;
    Button btnCheckout;
    TextView tv_Date;
    String checkedCommitement = "No";
    GpsTracker gps;
    double checkoutlat;
    double checkoutlng;
    String productID;
    String forward = "0";
    String checkinlat = "";
    String checkinlng = "";
    ExtraHelper extraHelper;
    String journeyType;

    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_call);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        db = new DatabaseHandler(QualityofSalesCallActivity.this);
        sHelper = new SharedPrefferenceHelper(QualityofSalesCallActivity.this);
        extraHelper = new ExtraHelper(QualityofSalesCallActivity.this);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerSavedConfirmationPopUp();
            }
        });
        iv_commitment = findViewById(R.id.img_commitment);
        btn_back = findViewById(R.id.back);
        myCalendar = Calendar.getInstance();
        btn_add_market_intelligence = findViewById(R.id.btn_market_intelligence);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle extras = getIntent().getExtras();
//                if (extras != null) {
//                    if(extras.containsKey("startfrom"))
//                    {
//                        if (extras.getString("startfrom").equals("start")) {
//                            Intent i = new Intent(QualityofSalesCallActivity.this, StartActivity.class);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                        }
//                    }
//                    // and get whatever type user account id is
//                }
//                else {
                Intent i = new Intent(QualityofSalesCallActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

//                }


            }
        });
        btn_add_market_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openMarketintelligence();
            }
        });
        loadCurrentLocation();
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("QUALITY OF SALES CALL");
        listView = (ListView) findViewById(R.id.listView);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
//                if (extras != null) {
//                    if(extras.containsKey("startfrom"))
//                    {
//                        if (extras.getString("startfrom").equals("start")) {
//                            Intent i = new Intent(QualityofSalesCallActivity.this, StartActivity.class);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                        }
//                    }
//                    // and get whatever type user account id is
//                }
//                else {
                Intent i = new Intent(QualityofSalesCallActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

//                }
            }
        });
        iv_commitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommitment();
            }
        });

        dataModels = new ArrayList();
        prepareBrandData(dataModels);


//        dataModels.add(new DataModel("EFERT Agritrade SSP+Zinc", false));
//        dataModels.add(new DataModel("EFERT Agritrade Zoron", false));
//        dataModels.add(new DataModel("Zarkhez Khas", false));
//        dataModels.add(new DataModel("EFERT Agritrade Powder", true));
//        dataModels.add(new DataModel("EFERT Agritrade Amonium Sulphate", true));
//        dataModels.add(new DataModel("EFERT Agritrade Powder potash", true));
//        dataModels.add(new DataModel("EFERT Agritrade Zingro", true));
//        dataModels.add(new DataModel("Zarkhez Khas(MOP)", false));
//        dataModels.add(new DataModel("EFERT Agritrade Zingro", false));

        adapter = new SalesCallAdapter(dataModels, getApplicationContext(), "quality");


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel = dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();


            }
        });
    }

    private void addProductDiscussed() {
        for (int i = 0; i < dataModels.size(); i++) {
            if (dataModels.get(i).isChecked()) {
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED_ID, dataModels.get(i).getId());
                headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
                headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
                db.addData(db.TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED, headerParams);
            }
        }
    }

    private void addMarketIntel(String review, String forward) {
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTEL_FORWARD, forward);
        headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTETL_COMMENT, review);
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        db.addData(db.TODAY_JOURNEY_PLAN_MARKET_INTEL, headerParams);
    }

    private void addCheckoutLocation(int distance) {
        long time = System.currentTimeMillis();
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE, String.valueOf(checkoutlat));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE, String.valueOf(checkoutlng));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP, String.valueOf(time));
        headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DISTANCE, String.valueOf(distance));
        db.addData(db.TODAY_JOURNEY_PLAN_POST_DATA, headerParams);
    }

    private void addCommitment() {
        for (int i = 0; i < arraylist.size(); i++) {

            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_CONFIRMED, arraylist.get(i).getConfirmed());
            headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_DELIVERY_DATE, arraylist.get(i).getTimeline());
            headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_QUANITY, arraylist.get(i).getQuantity());
            headerParams.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_RAND_ID, arraylist.get(i).getProductid());
            db.addData(db.TODAY_JOURNEY_PLAN_COMMITMENT_RECEIVED, headerParams);

        }
    }

    public static float getMeterFromLatLong(float lat1, float lng1, float lat2, float lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }

    public void customerSavedConfirmationPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder
                .setTitle("Warning")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage(QualityofSalesCallActivity.this.getString(R.string.outlet_confirmation))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Helpers.isNetworkAvailable(QualityofSalesCallActivity.this)) {
                            int totalb = 0;
                            gps = new GpsTracker(QualityofSalesCallActivity.this);
                            if (gps.canGetLocation()) {
                                if (ActivityCompat.checkSelfPermission(QualityofSalesCallActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(QualityofSalesCallActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                checkoutlat = gps.getLatitude();
                                checkoutlng = gps.getLongitude();
                                if (checkoutlat > 0 && checkoutlng > 0) {
                                    float distance = getMeterFromLatLong(Float.parseFloat(String.valueOf(checkoutlat)), Float.parseFloat(String.valueOf(checkoutlng)), Float.parseFloat(checkinlat), Float.parseFloat(checkinlng));
                                    float totaldistance = distance / 1000;
                                    totalb = (int) Math.round(totaldistance);
                                }
                                totalb = 0;
                                addProductDiscussed();
                                addCommitment();
                                addCheckoutLocation(totalb);
                                updateOutletStatus("Visited", "1");
                               // Toast.makeText(QualityofSalesCallActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                //addMarketIntel();
                                dialog.cancel();
                                if (isUnpostedDataExist()) {
                                    if (isDataSaved()) {
                                        new PostSyncCustomer().execute();
                                    }
                                }
                            }
                        }


                        else {
                            int totalb = 0;
                            gps = new GpsTracker(QualityofSalesCallActivity.this);
                            if (gps.canGetLocation()) {
                                if (ActivityCompat.checkSelfPermission(QualityofSalesCallActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(QualityofSalesCallActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                checkoutlat = gps.getLatitude();
                                checkoutlng = gps.getLongitude();
                                if (checkoutlat > 0 && checkoutlng > 0) {
                                    float distance = getMeterFromLatLong(Float.parseFloat(String.valueOf(checkoutlat)), Float.parseFloat(String.valueOf(checkoutlng)), Float.parseFloat(checkinlat), Float.parseFloat(checkinlng));
                                    float totaldistance = distance / 1000;
                                    totalb = (int) Math.round(totaldistance);
                                }
                                totalb = 0;
                                addProductDiscussed();
                                addCommitment();
                                addCheckoutLocation(totalb);
                                updateOutletStatus("Visited","0");
                                Toast.makeText(QualityofSalesCallActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                //addMarketIntel();
                                dialog.cancel();
                                Intent shift = new Intent(QualityofSalesCallActivity.this, VisitCustomerActivity.class);
                                shift.putExtra("from", sHelper.getString(Constants.PLAN_TYPE));
                                startActivity(shift);

                            } else {
                                DialougeManager.gpsNotEnabledPopup(QualityofSalesCallActivity.this);
                            }
                        }

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

    private void updateOutletStatus(String status,String internetstatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put(db.KEY_TODAY_JOURNEY_IS_VISITED, status);
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED_INTERNET_AVAILALE, internetstatus);
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        db.updateData(db.TODAY_JOURNEY_PLAN, params, filter);
    }
    private void updateOutletStatus() {
        HashMap<String, String> params = new HashMap<>();
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "1");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        db.updateData(db.TODAY_JOURNEY_PLAN, params, filter);
    }
    private boolean isDataSaved() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Visited");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
                //   statuCustomer = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED));;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;

    }
    private boolean isUnpostedDataExist() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
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

    private void openCommitment() {
        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_add_commitment, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        tv_Date = promptsView.findViewById(R.id.txt_date);
        EditText et_quantity = promptsView.findViewById(R.id.et_quantity);
        TextView tv_quantity = promptsView.findViewById(R.id.txt_quantity);
        TextView tv_product = promptsView.findViewById(R.id.txt_product);
        TextView tv_timeline = promptsView.findViewById(R.id.txt_timeline);
        CheckBox ck_confirmed = promptsView.findViewById(R.id.checkBox_confirmed);
        TextView auto_Product = promptsView.findViewById(R.id.auto_product);
        ImageView ivClose = promptsView.findViewById(R.id.imageView);

        SpannableStringBuilder product = setStarToLabel("Product");
        SpannableStringBuilder quantity = setStarToLabel("Quantity");
        SpannableStringBuilder timeline = setStarToLabel("Timeline");
        tv_quantity.setText(quantity);
        tv_product.setText(product);
        tv_timeline.setText(timeline);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        auto_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProductDialouge(auto_Product);

            }
        });
        tv_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(QualityofSalesCallActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();

            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add_commitment);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ck_confirmed.isChecked()) {
                    checkedCommitement = "Yes";
                } else {
                    checkedCommitement = "No";
                }

                validateInputs(et_quantity, auto_Product, tv_Date, alertDialog);


            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // view.setMinDate(System.currentTimeMillis() - 1000);
            myCalendar.set(Calendar.YEAR, year);
            //myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                    "August", "September", "October", "November",
                    "December"};
            String month = monthName[myCalendar.get(Calendar.MONTH)];
            System.out.println("Month name:" + month);
            String monthString = String.valueOf(month + 1);
            String dayString = String.valueOf(dayOfMonth);
            if (monthString.length() == 1) {
                monthString = "0" + monthString;
            }
            if (dayString.length() == 1) {
                dayString = "0" + dayString;
            }
            targetDate = month + " " + dayString + " " + year;
            tv_Date.setText(targetDate);
            // Log.e("targetdate", String.valueOf(target_date));


        }


    };

    private void validateInputs(EditText quanity, TextView product, TextView date, AlertDialog alertDialog) {
        if (quanity != null
                && product != null
                && date != null
                && !(Helpers.isEmptyTextview(getApplicationContext(), date))
                && !(Helpers.isEmptyTextview(getApplicationContext(), product))
                && !(Helpers.isEmpty(getApplicationContext(), quanity))
        ) {
            prepareRecommendationData(product.getText().toString(), quanity.getText().toString(), date.getText().toString(), checkedCommitement);
            drawRecommendationTable();
            alertDialog.dismiss();

        } else {
            Helpers.alertWarning(QualityofSalesCallActivity.this,getResources().getString(R.string.field_required_message),"Warning",null,null);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
//            alertDialogBuilder
//                    .setMessage(getResources().getString(R.string.field_required_message))
//                    .setCancelable(false)
//                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//
//
//                        }
//                    });
//            AlertDialog alertDialog2 = alertDialogBuilder.create();
//            alertDialog2.show();
        }
    }

    private void openMarketintelligence() {

        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_market_intelligence, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();


        EditText et_review = promptsView.findViewById(R.id.et_Remarks);
        CheckBox ck_forward = promptsView.findViewById(R.id.checkBox_forward);
        ImageView ivClsoe = promptsView.findViewById(R.id.ivClose);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (ck_forward.isChecked()) {
            forward = "1";
        } else {
            forward = "0";
        }


        Button btnYes = promptsView.findViewById(R.id.btn_add);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarketIntel(et_review.getText().toString(), forward);
                alertDialog.dismiss();
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    public void loadCurrentLocation() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                checkinlat = cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE));
                checkinlng = cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE));

            }
            while (cursor.moveToNext());
        }
    }

    public void selectProductDialouge(TextView autoProduct) {
        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
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
        title.setText("Select Company");
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        prepareCompanyData(companyList);
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
                productID = companyname.getId();

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

    private void prepareCompanyData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        map.put(db.KEY_ENGRO_RAND_NAME, "");
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

    private void prepareBrandData(ArrayList<DataModel> dataModels) {
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        map.put(db.KEY_ENGRO_RAND_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                DataModel model = new DataModel();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID));
                model.setName(productName);
                model.setId(productID);
                model.setChecked(false);
                dataModels.add(model);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    public void drawRecommendationTable() {
        int cursorIndex = 0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Product");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(12);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Quantity");
        column2.setTextSize(12);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Timeline");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(12);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Confirmed");
        column4.setTextSize(12);
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
        row.addView(column1, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
        row.addView(column2, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
        row.addView(column3, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
        row.addView(column4, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for (int i = 0; i < arraylist.size(); i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView prod = new TextView(this);
            prod.setText(arraylist.get(i).getProduct());
            prod.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            prod.setGravity(Gravity.CENTER);
            prod.setPadding(2, 2, 2, 2);
            TextView quantity = new TextView(this);
            quantity.setText(arraylist.get(i).getQuantity());
            quantity.setGravity(Gravity.CENTER);
            quantity.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            quantity.setPadding(2, 2, 2, 2);

            TextView timeline = new TextView(this);

            timeline.setText(Helpers.utcToAnyDateFormat(arraylist.get(i).getTimeline(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMM d yyyy"));
            timeline.setGravity(Gravity.CENTER);

            timeline.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            timeline.setPadding(2, 2, 2, 2);
            TextView confirm = new TextView(this);
            confirm.setText(arraylist.get(i).getConfirmed());
            confirm.setGravity(Gravity.CENTER);
            confirm.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            confirm.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(prod, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
            row2.addView(quantity, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
            row2.addView(timeline, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));
            row2.addView(confirm, new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }

    private void prepareRecommendationData(String product, String quantity, String time, String confirm) {
        Commitment prod = new Commitment();
        prod.setProduct(product);
        prod.setQuantity(quantity);
        prod.setProductid(productID);
        prod.setTimeline(Helpers.utcToAnyDateFormat(time, "MMM d yyyy", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        prod.setConfirmed(confirm);
        arraylist.add(prod);


        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    @Override
    public void onBackPressed() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            if(extras.containsKey("startfrom"))
//            {
//                if (extras.getString("startfrom").equals("start")) {
//                    Intent i = new Intent(QualityofSalesCallActivity.this, StartActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                }
//            }
//            // and get whatever type user account id is
//        }
//        else {
        Intent i = new Intent(QualityofSalesCallActivity.this, SalesOrderMarketPriceActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//
//        }
//        Intent i = new Intent(QualityofSalesCallActivity.this, SalesOrderMarketPriceActivity.class);
//        startActivity(i);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private class PostSyncCustomer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String errorMessage = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String customerId = "";
        String visitStatus;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QualityofSalesCallActivity.this);
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
            headerParams.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
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

                    if (!cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID)).equals("NA")) {
                        inputParameters.setJourneyPlanId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID))));
                    } else {
                        inputParameters.setJourneyPlanId(null);
                    }
                    if (!cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID)).equals("NA")) {
                        inputParameters.setDayId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID))));
                    } else {
                        inputParameters.setDayId(null);
                    }
                    inputParameters.setSalePointName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME))));
                    customerId = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                    if (Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED))).equals("Visited")) {
                        loadStartActviiyResult(inputParameters, customerId);
                        //LoadOrderforPosting(inputParameters, customerId);
                        // LoadPreviousSnapShotforPosting(inputParameters, customerId);
                        loadFloorStockforPosting(inputParameters, customerId);
                        loadLocationlast(inputParameters, customerId);
                        loadFloorStockSold(inputParameters, customerId);
                        loadMarketIntel(inputParameters, customerId);
                        loadCommitments(inputParameters, customerId);
                        loadProductDicussed(inputParameters, customerId);
                    }
                    inputCollection.add(inputParameters);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler(QualityofSalesCallActivity.this);
                HashMap<String, String> headerParams2 = new HashMap<>();
                if (sHelper.getString(Constants.ACCESS_TOKEN) != null && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                } else {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
                //headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                Log.e("postoutput", String.valueOf(output));
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            if (status.equals("true")) {
                                updateOutletStatus();
                                // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                                // Helpers.displayMessage(MainActivity.this, true, message);
                            }
                        } catch (JSONException e) {
                            if (response.equals("")) {
                                Helpers.displayMessage(QualityofSalesCallActivity.this, true, e.getMessage());
                                //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                                //pDialog.dismiss();
                            } else {
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response);
                                    errorMessage = json.getString("message");
                                    String status = json.getString("success");
                                    if (status.equals("false")) {
                                        Helpers.displayMessage(QualityofSalesCallActivity.this, true, errorMessage);
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

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
                alertDialogBuilder.setTitle("Success")
                        .setMessage("Data Posted Successfully")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_done_all_24)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent shift = new Intent(QualityofSalesCallActivity.this, VisitCustomerActivity.class);
                                shift.putExtra("from", sHelper.getString(Constants.PLAN_TYPE));
                                startActivity(shift);
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
//                                String currentDateandTime = sdf.format(new Date());
//                                sHelper.setString(Constants.LAST_POSTED, currentDateandTime);
                               // lastPosted.setText("Last Posted on " + sHelper.getString(Constants.LAST_POSTED));
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }
    private void loadCommitments(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<com.tallymarks.engroffm.models.todayjourneyplaninput.Commitment> commitmentList = new ArrayList<>();
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
                com.tallymarks.engroffm.models.todayjourneyplaninput.Commitment commitment = new com.tallymarks.engroffm.models.todayjourneyplaninput.Commitment();
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
    private void loadStartActviiyResult(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_TYPE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                journeyType = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_TYPE));
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

}
