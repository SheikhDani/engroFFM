package com.tallymarks.ffmapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.createProductDemo.CreateProductDemo;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.FarmerCheckIn;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FarmerDemoActivity extends AppCompatActivity {
    private TextView tvTopHeader, et_date, pending;
    ImageView iv_menu,iv_back;
    Button btn_back, btn_save;
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    SharedPrefferenceHelper sHelper;
    EditText et_address;
    DatePickerDialog datePickerDialog;
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> mainProductArraylist = new ArrayList<>();
    ArrayList<String> mainProductIDArraylist = new ArrayList<>();
    AutoCompleteTextView auto_crop,auto_prod,auto_objective;
    HashMap<String, String> map;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_demo);
        initView();

    }
    private void initView()
    {
        auto_crop= findViewById(R.id.auto_crop);
        auto_prod= findViewById(R.id.auto_prod);
        btn_back = findViewById(R.id.back);
        map = new HashMap<>();
        pending = findViewById(R.id.txt_pending);
        btn_save = findViewById(R.id.button2);
        auto_objective = findViewById(R.id.auto_ojective);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        et_date = findViewById(R.id.et_date);
        et_address = findViewById(R.id.et_address);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        sHelper = new SharedPrefferenceHelper(FarmerDemoActivity.this);
        db = new DatabaseHandler(FarmerDemoActivity.this);
        mydb = new MyDatabaseHandler(FarmerDemoActivity.this);
        getCropfromDatabase();
        getMainProductfromDatabase();
        tvTopHeader.setText("FARMER DEMO");
        final String arraylist[]={"Zabardast Urea vs Urea","Zarkhez vs DAP","Zarkhez vs MOP","Zarkhez vs NP","Zarkhez vs SOP"};
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
                auto_crop.showDropDown();
                String selection = cropArraylist.get(position);
                map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(position));
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT).show();
            }
        });

        auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final AlertDialog actions;
                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //goto category list with which as the category
                        String selection = cropArraylist.get(which);
                        map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(which));
                        auto_crop.setText(selection);

                    }
                };

                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerDemoActivity.this);
                categoryAlert.setTitle("Crop List");

                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
                actions = categoryAlert.create();
                actions.show();

                auto_crop.showDropDown();
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
                auto_prod.showDropDown();
                String selection = mainProductArraylist.get(position);
                map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(position));
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT).show();

            }
        });

        auto_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final AlertDialog actions;
                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //goto category list with which as the category
                        String selection = mainProductArraylist.get(which);
                        map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(which));
                        auto_prod.setText(selection);

                    }
                };

                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerDemoActivity.this);
                categoryAlert.setTitle("Crop List");

                categoryAlert.setItems(mainProductArraylist.toArray(new String[0]), actionListener);
                actions = categoryAlert.create();
                actions.show();

                auto_prod.showDropDown();
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


                // date
                if (et_date != null || !et_date.equals(null)){
                    map.put(mydb.KEY_ACTIVITY_DATE, et_date.getText().toString());
                }else{
                    map.put(mydb.KEY_ACTIVITY_DATE, "");
                }
                // address
                if (et_address != null || !et_address.equals(null)){
                    map.put(mydb.KEY_ADDRESS, et_address.getText().toString());
                }else{
                    map.put(mydb.KEY_ADDRESS, "");
                }
                // status
                map.put(mydb.KEY_STATUS, pending.getText().toString());

                // adding in table
                mydb.addData(mydb.FARMER_DEMO, map);

            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(FarmerDemoActivity.this, myDateListener,mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

    }

    public void getCropfromDatabase(){
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
    public void getMainProductfromDatabase(){
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

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

//            String m = "";
//            if(month+1 >0 && month+1<10){
//               String myMonth = String.valueOf(month);
//                m = "0" + myMonth;
//            }
//            try {
//                tvFieldVerificationDate.setText(year + "-"
//                        + Integer.parseInt(m) + "-" + day);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            if ((month+1) <10){
                String a =  String.format("%02d", month+1);

                et_date.setText(year + "-"
                        + a + "-" + day);

            }else{
                et_date.setText(year + "-"
                        + (month + 1) + "-" + day);
            }
            String myday = "";
            for (int i =1; i<=9;i++){
                if (i == day){
                    myday = "0" + day;
                }else{
                    try{
                        myday = "" + day;
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
            String d = year + "" + (month+1) + "" + myday;
            //String d = "20201105";
        }
    };

    private class PostFarmerDemo extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;
        String jsonObject = "";
        private HttpHandler httpHandler;
        String farmerId = "";
        String visitStatus;
        PostFarmerDemo(String status)
        {
            this.visitStatus = status;
        }

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
            HashMap<String, String> map = new HashMap<>();
            map.put(mydb.KEY_ACTIVITY_DATE, "");
            map.put(mydb.KEY_ADDRESS, "");
            map.put(mydb.KEY_CROP_ID, "");
            map.put(mydb.KEY_OBJECTIVE, "");
            map.put(mydb.KEY_PRODUCT_ID, "");
            map.put(mydb.KEY_STATUS, "");

            HashMap<String, String> filters = new HashMap<>();
            Cursor cursor2 = mydb.getData(mydb.FARMER_DEMO, map, filters);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {

                    CreateProductDemo createProductDemo = new CreateProductDemo();
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_ACTIVITY_DATE)));
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_ADDRESS)));
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_CROP_ID)));
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_OBJECTIVE)));
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_PRODUCT_ID)));
                    createProductDemo.setActivityDate(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_STATUS)));


                    farmerDemoCollection.add(createProductDemo);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams2 = new HashMap<>();
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
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

                            Helpers.displayMessage(FarmerDemoActivity.this, true, status);
                            System.out.println(status + " ---- " + message);
                            System.out.println(output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");

            mydb.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerDemoActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Farmer Demo posted")
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
