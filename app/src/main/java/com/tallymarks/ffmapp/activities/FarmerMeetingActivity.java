package com.tallymarks.ffmapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.FileUtils;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.models.farmerMeeting.FarmerMeeting;
import com.tallymarks.ffmapp.utils.Helpers;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FarmerMeetingActivity extends AppCompatActivity {
    private TextView tvTopHeader, tv_date_meeting, btn_dealers;
    ImageView iv_menu,iv_back;
    EditText farmers, targetCustomers, expenses;
    EditText attendance, chiefGuest;
    Button btn_back, btn_attachment;
    DatabaseHandler db;
    DatePickerDialog datePickerDialog;
    AutoCompleteTextView auto_crop_list, auto_product_list;
    ArrayList<String> cropArraylist = new ArrayList<>();
    ArrayList<String> cropIDArraylist = new ArrayList<>();
    ArrayList<String> mainProductArraylist = new ArrayList<>();
    ArrayList<String> mainProductIDArraylist = new ArrayList<>();
    ArrayList<String> customerNameArraylist = new ArrayList<>();
    ArrayList<String> customerCodeArraylist = new ArrayList<>();
    ArrayList<String> attachments = new ArrayList<>();

    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private static final int PICK_IMAGE = 5;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_meeting);
        initView();

    }
    private void initView()
    {
        auto_crop_list = findViewById(R.id.auto_crop_list);
        auto_product_list = findViewById(R.id.auto_product_list);
        btn_attachment = findViewById(R.id.btn_attachment);
        btn_dealers = findViewById(R.id.btn_dealers);
        attendance = findViewById(R.id.attendance);
        farmers= findViewById(R.id.farmers);
        expenses = findViewById(R.id.expenses);
        targetCustomers = findViewById(R.id.targetCustomers);
        chiefGuest = findViewById(R.id.chiefGuest);
        tv_date_meeting = findViewById(R.id.tv_date_meeting);
        btn_back = findViewById(R.id.back);
        db = new DatabaseHandler(FarmerMeetingActivity.this);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        auto_crop_list.setCursorVisible(false);
        auto_product_list.setCursorVisible(false);
        tvTopHeader.setText("FARMER MEETING");
        final String arraylist[]={"Male","female","other"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylist);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cropArraylist);
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mainProductArraylist);
        auto_crop_list.setAdapter(arrayAdapter1);
        auto_product_list.setAdapter(arrayAdapter2);
        //attendance.setAdapter(arrayAdapter);
        //farmers.setAdapter(arrayAdapter);
        attendance.setCursorVisible(false);
        farmers.setCursorVisible(false);
        getCropfromDatabase();
        getMainProductfromDatabase();
        getDealersfromDatabase();
        auto_crop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_crop_list.showDropDown();
                String selection = cropArraylist.get(position);
                //map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(position));
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT).show();
            }
        });

        auto_crop_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final AlertDialog actions;
                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //goto category list with which as the category
                        String selection = cropArraylist.get(which);
                        //map.put(mydb.KEY_CROP_ID, cropIDArraylist.get(which));
                        auto_crop_list.setText(selection);

                    }
                };

                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerMeetingActivity.this);
                categoryAlert.setTitle("Crop List");

                categoryAlert.setItems(cropArraylist.toArray(new String[0]), actionListener);
                actions = categoryAlert.create();
                actions.show();

                auto_crop_list.showDropDown();
            }
        });
        auto_product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_product_list.showDropDown();
                String selection = mainProductArraylist.get(position);
                //map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(position));
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT).show();

            }
        });

        auto_product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                final AlertDialog actions;
                DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //goto category list with which as the category
                        String selection = mainProductArraylist.get(which);
                        //map.put(mydb.KEY_PRODUCT_ID, mainProductIDArraylist.get(which));
                        auto_product_list.setText(selection);

                    }
                };

                AlertDialog.Builder categoryAlert = new AlertDialog.Builder(FarmerMeetingActivity.this);
                categoryAlert.setTitle("Crop List");

                categoryAlert.setItems(mainProductArraylist.toArray(new String[0]), actionListener);
                actions = categoryAlert.create();
                actions.show();

                auto_product_list.showDropDown();
            }
        });

//        attendance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                attendance.showDropDown();
//                String selection = arraylist[position];
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT);
//
//            }
//        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerMeetingActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btn_dealers.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

            Intent intent = new Intent(FarmerMeetingActivity.this, DealersActivity.class);
            startActivity(intent);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmerMeetingActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePicture();
//
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(Intent.createChooser(intent, "select image"),
//                        PICK_IMAGE);

            }
        });
//        attendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View arg0) {
//                attendance.showDropDown();
//            }
//        });
//        farmers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                farmers.showDropDown();
//                String selection = arraylist[position];
//                Toast.makeText(getApplicationContext(), selection,
//                        Toast.LENGTH_SHORT);
//
//            }
//        });

//        farmers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View arg0) {
//                farmers.showDropDown();
//            }
//        });

        tv_date_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(FarmerMeetingActivity.this, myDateListener,mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            //Uri selectedImageUri = data.getData();
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            try {
                String base64Url = convertUriToBase64(Uri.parse(mPaths.get(0)));
                attachments.add(base64Url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }


    public void takePicture(){
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.HARD)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    protected String convertUriToBase64(Uri uri) throws Exception {
        try {
            Bitmap bitmap = null;
            String encodedString = "";
            ByteArrayOutputStream outputStream = null;
            try {
                bitmap = BitmapFactory.decodeFile(uri.getPath());
                //MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                //bitmap = new Compressor(this).compressToBitmap(new File(uri.toString())); // added line
                outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
                encodedString = "";
            } finally {
                try {
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("Base 64 String: ", encodedString);

            //new UploadImages().execute();

            return encodedString;
        } catch (Exception e) {
            throw e;
        }
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

    }

    public void getDealersfromDatabase(){
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                customerNameArraylist.add(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))));
                customerCodeArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE)));
            }
            while (cursor.moveToNext());
        }
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//
//        someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK || result.getResultCode() == 0) {
//
//
//                        }
//                    }
//                });
//    }

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

                tv_date_meeting.setText(year + "-"
                        + a + "-" + day);

            }else{
                tv_date_meeting.setText(year + "-"
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




}
