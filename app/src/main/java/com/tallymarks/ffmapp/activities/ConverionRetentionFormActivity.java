package com.tallymarks.ffmapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.adapters.SubOrdinatesAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Activity;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.Subordinates;
import com.tallymarks.ffmapp.models.addconversioninput.AddConversioninput;
import com.tallymarks.ffmapp.models.createProductDemo.CreateProductDemo;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Crop;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Farmer;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Product;
import com.tallymarks.ffmapp.models.farmerdetailoutput.FarmerDetailOutput;
import com.tallymarks.ffmapp.models.supervisorsnapshotoutput.SuperVisorSnapShotOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.FarmerMeetingDbHelper;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ConverionRetentionFormActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    SharedPrefferenceHelper sHelper;
    AutoCompleteTextView auto_parent ,auto_farmer,autocrop,autoprod,autoActivity;
    private ArrayList<Farmer> farmerArrayList;
    private ArrayList<Crop> cropsArrayList = new ArrayList<>();
    ExtraHelper extraHelper;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    String from = "";
    MyDatabaseHandler mydb;
    DatabaseHandler db;
    TextView tvUserdata,tvSalesPointData;
    TextView txtParent, txtFarmer,txtUser,txtActivity,txtCrop,txtProduct,txt_rec_dosage,txt_rec_Acereage;
    TextView txtActivityDate, txtActivityNo;
    String farmerid = "";
    private String productID, cropID,activityID;
    Button btnSave,btnBack;
    EditText et_rec_dose,et_Rec_Acreage,et_remarks;
    TextView tvDate,tvActivityNo,tv_rec_pro_quantoty,tv_zerkhaih_user,tv_crop_Acer,tv_total_farm_size;
    TextView tvActualProductQuanity,tvTargeAcragaeRent,tvAcreagePercent,tvAcearageRetention;
    EditText etReasoNDevaition , etAddress,et_actual_dose_per_acre,et_actual_acre_data;;
    AutoCompleteTextView auto_stauts;
    int dose = 0;
    int acre = 0;
    int actualdose = 0;
    int targetacerage= 0;
    int acreretention = 0;
    int actualacre = 0;
    Calendar myCalendar;
    String targetDate = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_form);
        initView();

    }
    private void initView() {
        sHelper = new SharedPrefferenceHelper(ConverionRetentionFormActivity.this);
        extraHelper = new ExtraHelper(ConverionRetentionFormActivity.this);
        mydb = new MyDatabaseHandler(ConverionRetentionFormActivity.this);
        myCalendar = Calendar.getInstance();
        db = new DatabaseHandler(ConverionRetentionFormActivity.this);
        auto_parent = findViewById(R.id.auto_parent);
        tvActualProductQuanity = findViewById(R.id.txt_actual_prod_quantity_data);
        tvTargeAcragaeRent = findViewById(R.id.txt_tareget_Acre_Rent_data);
        tvAcreagePercent = findViewById(R.id.txt_acre_percent_data);
        tvAcearageRetention = findViewById(R.id.txt_acre_retetion_data);
        etReasoNDevaition = findViewById(R.id.et_reason_deviation);
        etAddress = findViewById(R.id.et_address);
        auto_stauts = findViewById(R.id.auto_status);
        auto_farmer = findViewById(R.id.auto_farmer);
        et_Rec_Acreage = findViewById(R.id.et_rec_acre);
        et_remarks = findViewById(R.id.et_remarks);
        et_actual_acre_data = findViewById(R.id.et_actual_acre_data);
        et_actual_dose_per_acre = findViewById(R.id.et_actual_dose_per_acre);
        et_rec_dose = findViewById(R.id.et_rec_dosage);
        tvDate = findViewById(R.id.txt_date_data);
        tv_total_farm_size = findViewById(R.id.txt_farm_size_data);
        tv_crop_Acer = findViewById(R.id.txt_crop_acre_data);
        tv_zerkhaih_user = findViewById(R.id.txt_zarkesh_user_data);
        tv_rec_pro_quantoty = findViewById(R.id.txt_rec_pr_quantity_data);
        autocrop = findViewById(R.id.auto_crop);
        autoprod = findViewById(R.id.auto_product);
        autoActivity = findViewById(R.id.auto_activity);
        tvSalesPointData = findViewById(R.id.txt_sales_point_data);
        tvUserdata = findViewById(R.id.txt_user_data);
        txtParent = findViewById(R.id.txt_parent);
        txtFarmer = findViewById(R.id.txt_farmer);
        txtUser = findViewById(R.id.txt_user);
        txtActivity = findViewById(R.id.txt_activity);
        txtActivityDate = findViewById(R.id.txt_date);

        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.back);
        txtCrop = findViewById(R.id.txt_crop);
        txtProduct = findViewById(R.id.txt_product);
        txt_rec_Acereage = findViewById(R.id.txt_rec_acre);
        txt_rec_dosage = findViewById(R.id.txt_rec_dosage);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("CONVERSION RETENTION");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        if(sHelper!=null)
        {
            from = sHelper.getString(Constants.FARMER_CONVERSION_RETENTION);
        }
        if(from.equals("add"))
        {
            auto_stauts.setEnabled(false);
            auto_stauts.setBackgroundResource(R.drawable.textview_grey_boader);
            et_actual_dose_per_acre.setEnabled(false);
            et_actual_dose_per_acre.setBackgroundResource(R.drawable.textview_grey_boader);
            et_actual_acre_data.setEnabled(false);
            et_actual_acre_data.setBackgroundResource(R.drawable.textview_grey_boader);
            etReasoNDevaition.setEnabled(false);
            etReasoNDevaition.setBackgroundResource(R.drawable.textview_grey_boader);

        }
        else if(from.equals("edit")) {
            if (sHelper != null) {
                auto_parent.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY));
                auto_parent.setEnabled(false);
                auto_parent.setBackgroundResource(R.drawable.textview_grey_boader);
                auto_farmer.setText(sHelper.getString(Constants.FARMER_CONVERSION_FARMER_NAME));
                auto_farmer.setEnabled(false);
                auto_farmer.setBackgroundResource(R.drawable.textview_grey_boader);
                autoActivity.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_ID));
                autoActivity.setEnabled(false);
                autoActivity.setBackgroundResource(R.drawable.textview_grey_boader);
                autocrop.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_CROP));
                autocrop.setEnabled(false);
                autocrop.setBackgroundResource(R.drawable.textview_grey_boader);
                autoprod.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_PRODUCT));
                autoprod.setEnabled(false);
                autoprod.setBackgroundResource(R.drawable.textview_grey_boader);
                tv_zerkhaih_user.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ZERKHAIZ_USER));
                tv_zerkhaih_user.setEnabled(false);
                tv_zerkhaih_user.setBackgroundResource(R.drawable.textview_grey_boader);
                tv_total_farm_size.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_TOTAL_FARM_SIZE));
                tv_total_farm_size.setEnabled(false);
                tv_total_farm_size.setBackgroundResource(R.drawable.textview_grey_boader);
                tv_crop_Acer.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_CROP_ACREAGE));
                tv_crop_Acer.setEnabled(false);
                tv_crop_Acer.setBackgroundResource(R.drawable.textview_grey_boader);
                et_rec_dose.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_DOSAGE));
                et_rec_dose.setEnabled(false);
                et_rec_dose.setBackgroundResource(R.drawable.textview_grey_boader);
                et_Rec_Acreage.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_ACERAGE));
                et_Rec_Acreage.setEnabled(false);
                et_Rec_Acreage.setBackgroundResource(R.drawable.textview_grey_boader);
                tv_rec_pro_quantoty.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDE_PRODUCT_QUANITY));
                tv_rec_pro_quantoty.setEnabled(false);
                tv_rec_pro_quantoty.setBackgroundResource(R.drawable.textview_grey_boader);
                et_remarks.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_REMAKRS));
                et_remarks.setEnabled(true);
                et_remarks.setBackgroundResource(R.drawable.eidttext_boader);
                etAddress.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ADDRESS));
                etAddress.setEnabled(true);
                etAddress.setBackgroundResource(R.drawable.eidttext_boader);
                tvDate.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_DATE));
                tvDate.setEnabled(true);
                tvDate.setBackgroundResource(R.drawable.eidttext_boader);

                et_actual_dose_per_acre.setEnabled(true);
                et_actual_dose_per_acre.setBackgroundResource(R.drawable.eidttext_boader);
                et_actual_acre_data.setEnabled(true);
                et_actual_acre_data.setBackgroundResource(R.drawable.eidttext_boader);
                etReasoNDevaition.setEnabled(true);
                etReasoNDevaition.setBackgroundResource(R.drawable.eidttext_boader);
                auto_stauts.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_STATUS));
                auto_stauts.setEnabled(true);
                auto_stauts.setBackgroundResource(R.drawable.eidttext_boader);

                tvTargeAcragaeRent.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_ACERAGE));


            }
        }
            else if(from.equals("close")) {
                if (sHelper != null) {
                    auto_parent.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY));
                    auto_parent.setEnabled(false);
                    auto_parent.setBackgroundResource(R.drawable.textview_grey_boader);
                    auto_farmer.setText(sHelper.getString(Constants.FARMER_CONVERSION_FARMER_NAME));
                    auto_farmer.setEnabled(false);
                    auto_farmer.setBackgroundResource(R.drawable.textview_grey_boader);
                    autoActivity.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_ID));
                    autoActivity.setEnabled(false);
                    autoActivity.setBackgroundResource(R.drawable.textview_grey_boader);
                    autocrop.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_CROP));
                    autocrop.setEnabled(false);
                    autocrop.setBackgroundResource(R.drawable.textview_grey_boader);
                    autoprod.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_PRODUCT));
                    autoprod.setEnabled(false);
                    autoprod.setBackgroundResource(R.drawable.textview_grey_boader);
                    tv_zerkhaih_user.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ZERKHAIZ_USER));
                    tv_zerkhaih_user.setEnabled(false);
                    tv_zerkhaih_user.setBackgroundResource(R.drawable.textview_grey_boader);
                    tv_total_farm_size.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_TOTAL_FARM_SIZE));
                    tv_total_farm_size.setEnabled(false);
                    tv_total_farm_size.setBackgroundResource(R.drawable.textview_grey_boader);
                    tv_crop_Acer.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_CROP_ACREAGE));
                    tv_crop_Acer.setEnabled(false);
                    tv_crop_Acer.setBackgroundResource(R.drawable.textview_grey_boader);
                    et_rec_dose.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_DOSAGE));
                    et_rec_dose.setEnabled(false);
                    et_rec_dose.setBackgroundResource(R.drawable.textview_grey_boader);
                    et_Rec_Acreage.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_ACERAGE));
                    et_Rec_Acreage.setEnabled(false);
                    et_Rec_Acreage.setBackgroundResource(R.drawable.textview_grey_boader);
                    tv_rec_pro_quantoty.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDE_PRODUCT_QUANITY));
                    tv_rec_pro_quantoty.setEnabled(false);
                    tv_rec_pro_quantoty.setBackgroundResource(R.drawable.textview_grey_boader);
                    et_remarks.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_REMAKRS));
                    et_remarks.setEnabled(false);
                    et_remarks.setBackgroundResource(R.drawable.textview_grey_boader);
                    etAddress.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ADDRESS));
                    etAddress.setEnabled(false);
                    etAddress.setBackgroundResource(R.drawable.textview_grey_boader);
                    tvDate.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_DATE));
                    tvDate.setEnabled(false);
                    tvDate.setBackgroundResource(R.drawable.textview_grey_boader);

                    et_actual_dose_per_acre.setEnabled(false);
                    et_actual_dose_per_acre.setBackgroundResource(R.drawable.textview_grey_boader);
                    et_actual_acre_data.setEnabled(false);
                    et_actual_acre_data.setBackgroundResource(R.drawable.textview_grey_boader);
                    etReasoNDevaition.setEnabled(false);
                    etReasoNDevaition.setBackgroundResource(R.drawable.textview_grey_boader);
                    auto_stauts.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_STATUS));
                    auto_stauts.setEnabled(false);
                    auto_stauts.setBackgroundResource(R.drawable.textview_grey_boader);
                    tvTargeAcragaeRent.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_ACERAGE));
                    et_actual_dose_per_acre.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_DOSAGE_PER_ACRE));
                    et_actual_acre_data.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_ACREAGE));
                    tvActualProductQuanity.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_PRODUCT_QUANTITY));
                    tvAcearageRetention.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACERAGE_RETENTION));
                    tvAcreagePercent.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_ACERAGE_RETENTION_PERCENT));
                    etReasoNDevaition.setText(sHelper.getString(Constants.FARMER_CONVERSION_PARENT_REASON));
                    btnSave.setVisibility(View.GONE);


                }

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConverionRetentionFormActivity.this, ConversionRetentionActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConverionRetentionFormActivity.this, ConversionRetentionActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();

            }
        });

        SpannableStringBuilder parentActivity = setStarToLabel("Parent Activity");
        SpannableStringBuilder farmer = setStarToLabel("Farmer");
        SpannableStringBuilder user = setStarToLabel("User");
        SpannableStringBuilder activity = setStarToLabel("Activity");
        SpannableStringBuilder crop = setStarToLabel("Crop");
        SpannableStringBuilder product = setStarToLabel("Product");
        SpannableStringBuilder rec_dosage = setStarToLabel("Recommended Dose");
        SpannableStringBuilder rec_Acre = setStarToLabel("Recommended Acearage");
        SpannableStringBuilder activityDate = setStarToLabel("Activity Date");

        txtParent.setText(parentActivity);
        txtFarmer.setText(farmer);
        txtUser.setText(user);
        txtActivity.setText(activity);
        txtCrop.setText(crop);
        txtProduct.setText(product);
        txt_rec_dosage.setText(rec_dosage);
        txt_rec_Acereage.setText(rec_Acre);
        txtActivityDate.setText(activityDate);


        loadFarmers(mydb);
        loadLoginData();
        auto_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(auto_farmer,"autofarmer");
            }
        });
        autocrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autocrop,"autocrop");
            }
        });
        autoprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoprod,"autoproduct");
            }
        });
        autoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoActivity,"autoactivity");
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDateandTime = sdf.format(new Date());
        tvDate.setText(currentDateandTime);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ConverionRetentionFormActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });
        final String arraylistwatersource[] = {"Farm Visit", "Soil Sampling"};
        final ArrayAdapter<String> arrayAdapterwatersource = new ArrayAdapter<>(
                this, android.R.layout.select_dialog_item, arraylistwatersource);
        auto_parent.setAdapter(arrayAdapterwatersource);
        auto_parent.setCursorVisible(false);
        auto_parent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_parent.showDropDown();
                auto_farmer.setText("");
                tvSalesPointData.setText("");
                autocrop.setText("");
                autoprod.setText("");
                autoActivity.setText("");
                tv_crop_Acer.setText("");
                tv_zerkhaih_user.setText("");
                et_Rec_Acreage.setText("");
                et_rec_dose.setText("");
                tv_rec_pro_quantoty.setText("");
                tv_total_farm_size.setText("");
                et_remarks.setText("");
                etAddress.setText("");

              //  new GetFarmerDetaiLData(farmerid,auto_parent.getText().toString()).execute();


            }
        });
        auto_stauts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_stauts.showDropDown();


                //  new GetFarmerDetaiLData(farmerid,auto_parent.getText().toString()).execute();


            }
        });
        auto_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_parent.showDropDown();
            }
        });
        final String arraylistwatersource2[] = {"Pending", "Closed"};
        final ArrayAdapter<String> arrayAdapterwatersource2 = new ArrayAdapter<>(
                this, android.R.layout.select_dialog_item, arraylistwatersource2);
        auto_stauts.setAdapter(arrayAdapterwatersource2);
        auto_stauts.setCursorVisible(false);
        auto_stauts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_stauts.showDropDown();
            }
        });
        et_rec_dose.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    tv_rec_pro_quantoty.setText(Calculate(Integer.parseInt(et_rec_dose.getText().toString()), acre));
                }
            }
        });
        et_Rec_Acreage.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    tv_rec_pro_quantoty.setText(Calculate(dose, Integer.parseInt(et_Rec_Acreage.getText().toString())));
                }
            }
        });
        et_actual_dose_per_acre.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    tvActualProductQuanity.setText(Calculate2(Integer.parseInt(et_actual_dose_per_acre.getText().toString()), actualacre));
                }
            }
        });
        et_actual_acre_data.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    tvActualProductQuanity.setText(Calculate2(actualdose, Integer.parseInt(et_actual_acre_data.getText().toString())));
                    tvAcearageRetention.setText(et_actual_acre_data.getText().toString());
                    tvAcreagePercent.setText(CalculateAcrePercent(Integer.parseInt(tvAcearageRetention.getText().toString()), Integer.parseInt(tvTargeAcragaeRent.getText().toString())));
                }
            }
        });

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
            targetDate =  dayString+ " " + month + " " + year;
           tvDate.setText(targetDate);
            // Log.e("targetdate", String.valueOf(target_date));


        }


    };
    private String Calculate(int dose, int acre)
    {
        this.dose = dose;
        this.acre = acre;
        int  result   = acre * dose;
        if(result>0) {
            return String.valueOf(result);
        }
        else
        {
            return "";
        }

    }
    private String Calculate2(int dose, int acre)
    {
        this.actualdose = dose;
        this.actualacre = acre;
        int  result   = acre * dose;
        if(result>0) {
            return String.valueOf(result);
        }
        else
        {
            return "";
        }

    }
    private String CalculateAcrePercent(int retention, int acre)
    {

        float result   = retention * 100/ acre ;
        if(result>0) {
            return String.valueOf(result);
        }
        else
        {
            return "";
        }

    }
    public class GetFarmerDetaiLData extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "", farmerid = "", parentactivity="";



        public GetFarmerDetaiLData(String farmerid ,String parentactivity) {
            this.farmerid = farmerid;
            this.parentactivity = parentactivity;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConverionRetentionFormActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);

            //expandableListGroup.clear();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            String response = "";
            String getsupervsorsnapshot = Constants.FFM_FARMER_DETAIL_DATA + "?farmerId=" + farmerid+ "&parentActivity=" + parentactivity ;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
               // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
//                Type journeycodeType = new TypeToken<ArrayList<FarmerDetailOutput>>() {
//                }.getType();
                FarmerDetailOutput journeycode = new Gson().fromJson(response,FarmerDetailOutput.class);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {

                   if(journeycode!=null) {
                       if(journeycode.getCrops()!=null && journeycode.getCrops().size()>0) {
                           for (int i = 0; i < journeycode.getCrops().size(); i++) {
                               String cropID = journeycode.getCrops().get(i).getCropId();
                               String cropName = journeycode.getCrops().get(i).getCropName();
                               cropsArrayList.add(new Crop(cropID, cropName));
                           }
                       }
                       if(journeycode.getProducts()!=null && journeycode.getProducts().size()>0) {
                           for (int j = 0; j < journeycode.getProducts().size(); j++) {
                               String productID = journeycode.getProducts().get(j).getProductId();
                               String productName = journeycode.getProducts().get(j).getProductName();
                               productArrayList.add(new Product(productID, productName));
                           }
                       }
                       if(journeycode.getActivities()!=null && journeycode.getActivities().size()>0) {
                           for (int k = 0; k < journeycode.getActivities().size(); k++) {
                               String productID = journeycode.getActivities().get(k).getActivityId();
                               activityArrayList.add(new Activity(productID));
                           }
                       }



//                        Subordinates s = new Subordinates();
//                        s.setName(journeycode.get(j).getName() == null || journeycode.get(j).getName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getName().toString());
//                        s.setSubordianteid(journeycode.get(j).getSubordinateUserId() == null || journeycode.get(j).getSubordinateUserId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getSubordinateUserId().toString());
//                        s.setNoofjourneyplan(journeycode.get(j).getNumberOfJourneyPlans() == null || journeycode.get(j).getNumberOfJourneyPlans().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getNumberOfJourneyPlans().toString());
//                        s.setHasjourneyplan(journeycode.get(j).getHasJourneyPlans() == null || journeycode.get(j).getHasJourneyPlans().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getHasJourneyPlans().toString());
//                        dataModels.add(s);


                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(ConverionRetentionFormActivity.this, true, exception.getMessage());
                    //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                    //pDialog.dismiss();
                } else {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                        errorMessage = json.getString("message");
                        String status = json.getString("success");
                        if (status.equals("false")) {
                            // Helpers.displayMessage(JourneyPlanActivity.this, true, errorMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            pDialog.dismiss();
            if(cropsArrayList!=null) {
                if (cropsArrayList.size() > 0) {
                    autocrop.setText(cropsArrayList.get(0).getCropName());
                    cropID = cropsArrayList.get(0).getCropID();
                }
                else
                {
                    autocrop.setText("");
                }

            }
            if(productArrayList!=null) {
                if (productArrayList.size() > 0) {
                    autoprod.setText(productArrayList.get(0).getBranchName());
                    productID = productArrayList.get(0).getBranchID();
                }
                else
                {
                    autoprod.setText("");
                }
            }
            if(activityArrayList!=null) {
                if (activityArrayList.size() > 0) {
                    autoActivity.setText(activityArrayList.get(0).getActivityId());
                    activityID = activityArrayList.get(0).getActivityId();
                }
                else
                {
                    autoActivity.setText("");
                }
            }

//            adapter = new SubOrdinatesAdapter(dataModels, getApplicationContext());
//            listView.setAdapter(adapter);

        }
    }
    private void loadFarmers(MyDatabaseHandler databaseHandler) {
        farmerArrayList = FarmerMeetingDbHelper.loadFarmersFromDB(databaseHandler);
       // populateFarmersSpinner();
    }
    public void selectDialouge(AutoCompleteTextView autoProduct, String from) {
        LayoutInflater li = LayoutInflater.from(ConverionRetentionFormActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConverionRetentionFormActivity.this);
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
        if(from.equals("autofarmer")) {
            title.setText("Select Farmer");
        }
        else if(from.equals("autocrop")) {
            title.setText("Select Crop");
        }
        else if(from.equals("autoproduct")) {
            title.setText("Select Product");
        }
        else if(from.equals("autoactivity")) {
            title.setText("Select Activity");
        }


        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if(from.equals("autofarmer"))
        {
            for (int i = 0; i < farmerArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(farmerArrayList.get(i).getFarmerID());
                crop.setPoint(farmerArrayList.get(i).getFarmerName());
                crop.setSalespoint(farmerArrayList.get(i).getFarmersalespoint());
                crop.setAcerage(farmerArrayList.get(i).getFarmerAcearage());
                crop.setAreacultivation(farmerArrayList.get(i).getFarmerAreaCultivation());
                crop.setUsertype(farmerArrayList.get(i).getFarmerUserType());
                companyList.add(crop);
            }

        }
        else if(from.equals("autocrop"))
        {
            for (int i = 0; i < cropsArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(cropsArrayList.get(i).getCropID());
                crop.setPoint(cropsArrayList.get(i).getCropName());
                companyList.add(crop);
            }

        }
        else if(from.equals("autoproduct"))
        {
            for (int i = 0; i < productArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(productArrayList.get(i).getBranchID());
                crop.setPoint(productArrayList.get(i).getBranchName());
                companyList.add(crop);
            }

        }
        else if(from.equals("autoactivity"))
        {
            for (int i = 0; i < activityArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
               // crop.setId(activityArrayList.get(i).getBranchID());
                crop.setPoint(activityArrayList.get(i).getActivityId());
                companyList.add(crop);
            }

        }

        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList,"salescall");

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
                if(from.equals("autofarmer"))
                {
                    farmerid= companyname.getId();

                }
                else if(from.equals("autocrop"))
                {
                    cropID= companyname.getId();

                }
                else if(from.equals("autoproduct"))
                {
                    productID= companyname.getId();

                }
                else if(from.equals("autoactivity"))
                {
                    activityID= companyname.getPoint();

                }

                if(from.equals("autofarmer")) {
                    cropsArrayList.clear();
                    productArrayList.clear();
                    activityArrayList.clear();
                    tvSalesPointData.setText(companyname.getSalespoint());
                    if(!companyname.getAcerage().equals("NA") && !companyname.getAreacultivation().equals("NA"))
                    {
                        tv_crop_Acer.setText(companyname.getAcerage());
                        tv_total_farm_size.setText(companyname.getAreacultivation());
                    }
                    else
                    {
                        tv_crop_Acer.setText("");
                        tv_total_farm_size.setText("");
                    }
                    if(!companyname.getUsertype().equals("NA")) {
                        tv_zerkhaih_user.setText(companyname.getUsertype());
                    }
                    else
                    {
                        tv_zerkhaih_user.setText("");
                    }
                    new GetFarmerDetaiLData(farmerid, auto_parent.getText().toString()).execute();
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
                if(search.hasFocus()) {
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
    public void loadLoginData() {
        String username= "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.LOGIN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                username = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_NAME)));
                while (cursor.moveToNext()) ;
            }
            while (cursor.moveToNext());
            tvUserdata.setText(username);

        }
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
    private void validateInputs() {
        if (    autoprod!=null
                &&autoActivity!=null
                && autocrop!=null
                && auto_parent!=null
                && auto_farmer!=null
                && !(Helpers.isEmptyTextview(getApplicationContext(),  tvDate))
                && !(Helpers.isEmptyTextview(getApplicationContext(),  tvUserdata))
                && !(Helpers.isEmpty(getApplicationContext(), et_rec_dose))
                && !(Helpers.isEmpty(getApplicationContext(), et_Rec_Acreage))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  autoprod))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  autocrop))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_parent))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  autoActivity))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_farmer))

        ) {
            if (Helpers.isNetworkAvailable(ConverionRetentionFormActivity.this)) {
                if(from.equals("add")) {
                    new AddConversionRetention().execute();
                }
                else if(from.equals("edit"))
                {
                    new EditConversionRetention().execute();
                }
            } else {
                Helpers.noConnectivityPopUp(ConverionRetentionFormActivity.this);
            }

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConverionRetentionFormActivity.this);
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.field_required_message))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private class AddConversionRetention extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;

        private HttpHandler httpHandler;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConverionRetentionFormActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {


            System.out.println("Post Outlet URl" + Constants.FFM_FARMER_ADD_CONVERSION_RETENTION);
            Gson gson = new Gson();
           AddConversioninput createConversion = new AddConversioninput();
           createConversion.setActivityDate(Helpers.utcToAnyDateFormat(tvDate.getText().toString(),"dd MMMM yyyy","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
           createConversion.setAcreageForRetention(0);
           createConversion.setAcreageRetentionPercentage(0);
           createConversion.setActualAcreage(0);
           createConversion.setActualDosagePerAcre(0);
           createConversion.setActualProductQty(0);
           createConversion.setTargetAcreageRetention(0);
           createConversion.setReasonForDeviation("");
           createConversion.setActivityId(activityID);
           createConversion.setAddress(etAddress.getText().toString());
           createConversion.setCropAcreage(Double.parseDouble(tv_crop_Acer.getText().toString()));
           createConversion.setCropId(Integer.parseInt(cropID));
           createConversion.setTotalFarmSize(Double.parseDouble(tv_total_farm_size.getText().toString()));
           createConversion.setCropName(autocrop.getText().toString());
           createConversion.setFarmerId(Integer.parseInt(farmerid));
           createConversion.setFarmerName(auto_farmer.getText().toString());
           createConversion.setZarkhezUser(tv_zerkhaih_user.getText().toString());
           createConversion.setParentActivity(auto_parent.getText().toString());
           createConversion.setProductId(Integer.parseInt(productID));
           createConversion.setRecommendedAcreage(Integer.parseInt(et_Rec_Acreage.getText().toString()));
           createConversion.setRecommendedDosage(Integer.parseInt(et_rec_dose.getText().toString()));
           createConversion.setRecommendedProductQty(Integer.parseInt( tv_rec_pro_quantoty.getText().toString()));
           createConversion.setRemarks(et_remarks.getText().toString());
           createConversion.setStatus(auto_stauts.getText().toString());
           createConversion.setProductName(autoprod.getText().toString());

           httpHandler = new HttpHandler();
            HashMap<String, String> headerParams2 = new HashMap<>();
            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
            else
            {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
           // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String output = gson.toJson(createConversion);
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.FFM_FARMER_ADD_CONVERSION_RETENTION, headerParams2, bodyParams, output);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("message"));
                        //discription = String.valueOf(jsonObj.getString("description"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConverionRetentionFormActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Conversion Rerention Added Successfully")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent con = new Intent(ConverionRetentionFormActivity.this,ConversionRetentionActivity.class);
                                startActivity(con);

                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }
    private class EditConversionRetention extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;


        private HttpHandler httpHandler;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConverionRetentionFormActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            String id = sHelper.getString(Constants.FARMER_CONVERSION_ID);

            String editConverion = Constants.FFM_FARMER_EDIT_CONVERSION_RETENTION + "?id=" + id;
            Gson gson = new Gson();
            AddConversioninput createConversion = new AddConversioninput();
            createConversion.setAcreageForRetention(Integer.parseInt(tvAcearageRetention.getText().toString()));
            createConversion.setAcreageRetentionPercentage(Float.parseFloat(tvAcreagePercent.getText().toString()));
            createConversion.setActualAcreage(Integer.parseInt(et_actual_acre_data.getText().toString()));
            createConversion.setActualDosagePerAcre(Integer.parseInt(et_actual_dose_per_acre.getText().toString()));
            createConversion.setActualProductQty(Integer.parseInt(tvActualProductQuanity.getText().toString()));
            createConversion.setTargetAcreageRetention(Integer.parseInt(tvTargeAcragaeRent.getText().toString()));
            createConversion.setReasonForDeviation(etReasoNDevaition.getText().toString());
            createConversion.setAddress(etAddress.getText().toString());
            createConversion.setRemarks(et_remarks.getText().toString());
            createConversion.setStatus(auto_stauts.getText().toString());
            httpHandler = new HttpHandler();
            HashMap<String, String> headerParams2 = new HashMap<>();
            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
            else
            {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
           // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            String output = gson.toJson(createConversion);
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPut(editConverion, headerParams2, bodyParams, output);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("message"));
                        //discription = String.valueOf(jsonObj.getString("description"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConverionRetentionFormActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Conversion Retention Record has been updated")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent con = new Intent(ConverionRetentionFormActivity.this,ConversionRetentionActivity.class);
                                startActivity(con);

                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }

}
