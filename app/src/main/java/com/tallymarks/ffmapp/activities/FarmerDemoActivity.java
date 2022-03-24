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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.createProductDemo.CreateProductDemo;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FarmerDemoActivity extends AppCompatActivity {
    private TextView tvTopHeader, et_date, pending;
    ImageView iv_menu, iv_back;
    Button btn_back, btn_save;
    DatabaseHandler db;
    String targetDate = "";
    Calendar myCalendar;
    MyDatabaseHandler mydb;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    EditText et_address;
    DatePickerDialog datePickerDialog;
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> mainProductArraylist = new ArrayList<>();
    ArrayList<String> mainProductIDArraylist = new ArrayList<>();
    AutoCompleteTextView auto_crop, auto_prod, auto_objective;
    HashMap<String, String> map;
    int ProductId;
    int cropId;
    TextView tvCrop , tvProduct , tvDate , tvAddress, tvObjective;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_demo);
        initView();

    }
    private void prepareProductData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        map.put(db.KEY_ENGRO_BRANCH_ID, "");
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

    }
    public void selectDialouge(AutoCompleteTextView autoProduct,String from) {
        LayoutInflater li = LayoutInflater.from(FarmerDemoActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerDemoActivity.this);
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
        if(from.equals("crop")) {
            title.setText("Select Crop");
        }
        else if(from.equals("product"))
        {
            title.setText("Select Product");
        }

        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if(from.equals("crop")) {
            prepareCropData(companyList);
        }
        else if(from.equals("product"))
        {
            prepareProductData(companyList);
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
                if(from.equals("crop")) {

                    map.put(mydb.KEY_CROP_ID, companyname.getId());
                }
                else if(from.equals("product"))
                {
                    map.put(mydb.KEY_PRODUCT_ID, companyname.getId());
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

    private void prepareCropData(List<SaelsPoint> movieList) {
        movieList.clear();
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID));
                companyname.setPoint(productName);
                companyname.setId(productID);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }

    }

    private void initView() {
        auto_crop = findViewById(R.id.auto_crop);
        auto_prod = findViewById(R.id.auto_prod);
        btn_back = findViewById(R.id.back);
        map = new HashMap<>();
        myCalendar = Calendar.getInstance();
        pending = findViewById(R.id.txt_pending);
        btn_save = findViewById(R.id.button2);
        auto_objective = findViewById(R.id.auto_ojective);
        tvAddress = findViewById(R.id.txt_address);
        tvObjective = findViewById(R.id.txt_objective);
        tvDate = findViewById(R.id.txt_date);
        tvProduct = findViewById(R.id.txt_prod);
        tvCrop = findViewById(R.id.txt_crop);

        tvTopHeader = findViewById(R.id.tv_dashboard);
        et_date = findViewById(R.id.et_date);
        et_address = findViewById(R.id.et_address);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        sHelper = new SharedPrefferenceHelper(FarmerDemoActivity.this);
        extraHelper = new ExtraHelper(FarmerDemoActivity.this);
        db = new DatabaseHandler(FarmerDemoActivity.this);
        mydb = new MyDatabaseHandler(FarmerDemoActivity.this);

        SpannableStringBuilder activityDate = setStarToLabel("Activity Date");
        SpannableStringBuilder crop = setStarToLabel("Crop");
        SpannableStringBuilder product = setStarToLabel("Product");
        SpannableStringBuilder address = setStarToLabel("Address");
        SpannableStringBuilder objective = setStarToLabel("Objective of Demo");
        tvCrop.setText(crop);
        tvDate.setText(activityDate);
        tvProduct.setText(product);
        tvAddress.setText(address);
        tvObjective.setText(objective);
        //getCropfromDatabase();
        //getMainProductfromDatabase();
        tvTopHeader.setText("FARMER DEMO");
        final String arraylist[] = {"Zabardast Urea vs Urea", "Zarkhez vs DAP", "Zarkhez vs MOP", "Zarkhez vs NP", "Zarkhez vs SOP"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);

        //auto_crop.setAdapter(arrayAdapter);
        //auto_prod.setAdapter(arrayAdapter);
        auto_objective.setAdapter(arrayAdapter);
        auto_objective.setCursorVisible(false);
        auto_crop.setCursorVisible(false);
        auto_prod.setCursorVisible(false);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mainProductArraylist);
        auto_crop.setAdapter(arrayAdapter1);
        auto_prod.setAdapter(arrayAdapter2);
        auto_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_crop.showDropDown();
//                String selection = cropArraylist.get(position);
//                map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();
            }
        });

        auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_crop,"crop");
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = cropArraylist.get(which);
//                        map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(which));
//                        auto_crop.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerDemoActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_crop.showDropDown();
            }
        });
        auto_objective.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_objective.showDropDown();
                String selection = arraylist[position];
                map.put(mydb.KEY_OBJECTIVE, arraylist[position]);
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);

            }
        });

        auto_objective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_objective.showDropDown();
            }
        });
        auto_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                auto_prod.showDropDown();
//                String selection = mainProductArraylist.get(position);
//                map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(position));
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT).show();

            }
        });

        auto_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                selectDialouge(auto_prod,"product");
//                final AlertDialog actions;
//                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //goto category list with which as the category
//                        String selection = mainProductArraylist.get(which);
//                        map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(which));
//                        auto_prod.setText(selection);
//
//                    }
//                };
//
//                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerDemoActivity.this);
//                categoryAlert.setTitle("Crop List");
//
//                categoryAlert.setItems(mainProductArraylist.toArray(new String[0]), actionListener);
//                actions = categoryAlert.create();
//                actions.show();
//
//                auto_prod.showDropDown();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerDemoActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerDemoActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saving data


//                // date
//                if (et_date != null || !et_date.equals(null)) {
//                    map.put(mydb.KEY_ACTIVITY_DATE, Helpers.utcToAnyDateFormat(et_date.getText().toString(),"MMM d yyyy","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//                } else {
//                    map.put(mydb.KEY_ACTIVITY_DATE, "");
//                }
//                // address
//                if (et_address != null || !et_address.equals(null)) {
//                    map.put(mydb.KEY_ADDRESS, et_address.getText().toString());
//                } else {
//                    map.put(mydb.KEY_ADDRESS, "");
//                }
//                // status
//                map.put(mydb.KEY_STATUS, pending.getText().toString());
//
//                // adding in table
//                mydb.addData(mydb.FARMER_DEMO, map);
              validateInputs();

            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FarmerDemoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
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
            targetDate = month + " " + dayString + " " + year;
            et_date.setText(targetDate);
            // Log.e("targetdate", String.valueOf(target_date));


        }


    };

    public void getCropfromDatabase() {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cropArraylist.add(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME))));


                cropIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID)));
            }
            while (cursor.moveToNext());
        }

//        for (int i=0;i<cropArraylist.size();i++){
//            if (cropArraylist.get(i).contains("%20"))
//                cropArraylist.set(i, cropArraylist.get(i).replace("%20" , " "));
//        }
    }
    private void validateInputs() {
        if (    auto_prod!=null
                && et_address!=null
                && auto_objective!=null
                && auto_crop!=null
                && !(Helpers.isEmptyTextview(getApplicationContext(),  et_date))
                && !(Helpers.isEmpty(getApplicationContext(), et_address))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_crop))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_objective))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_prod))

        ) {
            if (Helpers.isNetworkAvailable(FarmerDemoActivity.this)) {
                new PostFarmerDemo().execute();
            } else {
                Helpers.noConnectivityPopUp(FarmerDemoActivity.this);
            }

        } else {
            Helpers.alertWarning(FarmerDemoActivity.this,getResources().getString(R.string.field_required_message),"Warning",null,null);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerDemoActivity.this);
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
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
        }
    }

    public void getMainProductfromDatabase() {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                mainProductArraylist.add(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME))));
                mainProductIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID)));
            }
            while (cursor.moveToNext());
        }

//        for (int i=0;i<mainProductArraylist.size();i++){
//            mainProductArraylist.get(i).replace("%20" , " ");
//            mainProductArraylist.set(i, mainProductArraylist.get(i).replace("%20" , " "));
//        }
    }

    public void ProductfromName(String name) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_RAND_NAME, "");
        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_ENGRO_RAND_NAME,name);
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
               ProductId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID)));

            }
            while (cursor.moveToNext());
        }

//        for (int i=0;i<mainProductArraylist.size();i++){
//            mainProductArraylist.get(i).replace("%20" , " ");
//            mainProductArraylist.set(i, mainProductArraylist.get(i).replace("%20" , " "));
//        }
    }
    public void getCropfromName(String name) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_CROP_NAME, name);
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cropId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID)));



            }
            while (cursor.moveToNext());
        }

//        for (int i=0;i<cropArraylist.size();i++){
//            if (cropArraylist.get(i).contains("%20"))
//                cropArraylist.set(i, cropArraylist.get(i).replace("%20" , " "));
//        }
    }

    private class PostFarmerDemo extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;

        private HttpHandler httpHandler;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FarmerDemoActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            mydb = new MyDatabaseHandler(FarmerDemoActivity.this);
            System.out.println("Post Outlet URl" + Constants.FFM_POST_FARMER_DEMO);
            ArrayList<CreateProductDemo> farmerDemoCollection = new ArrayList<>();
            Gson gson = new Gson();
//            HashMap<String, String> map = new HashMap<>();
//            map.put(mydb.KEY_ACTIVITY_DATE, "");
//            map.put(mydb.KEY_ADDRESS, "");
//            map.put(mydb.KEY_CROP_ID, "");
//            map.put(mydb.KEY_OBJECTIVE, "");
//            map.put(mydb.KEY_PRODUCT_ID, "");
//            map.put(mydb.KEY_STATUS, "");
//
//            HashMap<String, String> filters = new HashMap<>();
//            Cursor cursor2 = mydb.getData(mydb.FARMER_DEMO, map, filters);
//            if (cursor2.getCount() > 0) {
//                cursor2.moveToFirst();
//                do {

//                    CreateProductDemo createProductDemo = new CreateProductDemo();
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_ACTIVITY_DATE)));
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_ADDRESS)));
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_CROP_ID)));
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_OBJECTIVE)));
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_PRODUCT_ID)));
//                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_STATUS)));
//                    farmerDemoCollection.add(createProductDemo);

//                }
//                while (cursor2.moveToNext());
            CreateProductDemo createProductDemo = new CreateProductDemo();
            String date = et_date.getText().toString();
            createProductDemo.setActivityDate(Helpers.utcToAnyDateFormat(date,"MMM d yyyy","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            ProductfromName(auto_prod.getText().toString());
            getCropfromName(auto_crop.getText().toString());
           createProductDemo.setProductId(ProductId);
           createProductDemo.setCropId(cropId);
           createProductDemo.setAddress(et_address.getText().toString());
            createProductDemo.setObjective(auto_objective.getText().toString());
            createProductDemo.setStatus(pending.getText().toString());
            farmerDemoCollection.add(createProductDemo);

                 httpHandler = new HttpHandler(FarmerDemoActivity.this);
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
                String output = gson.toJson(farmerDemoCollection);
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.FFM_POST_FARMER_DEMO, headerParams2, bodyParams, output);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerDemoActivity.this);
                alertDialogBuilder.setTitle("Success")
                        .setMessage("Farmer Demo posted")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_done_all_24)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                et_address.setText("");
                                auto_crop.setText("");
                                auto_objective.setText("");
                                auto_prod.setText("");
                                et_date.setText("");
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(FarmerDemoActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
