package com.tallymarks.ffmapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    /*Database Varaiables*/
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "FFMAppDb";
    private Context mContext;

    //List of all tables
    public static final String LOGIN = "LOGIN";
    public static final String OUTLET_STATUSES = "OUTLET_STATUS";
    public static final String CROPS_LIST = "CROPS_LIST";
    public static final String FERT_TYPES = "FERT_TYPES";
    public static final String SOIL_DEPTHS = "SOIL_DEPTHS";
    public static final String GENDER = "GENDER";
    public static final String DISTRTICTS = "DESTRICTS";



    //Login Table Fields
    public static final String KEY_ID = "_id";
    public static final String KEY_COMPANY_NAME = "companyName";
    public static final String KEY_USER_DESIGNATION = "userDesignation";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    //Outlet Statuses Table Fields
    public static final String KEY_OUTLET_ID = "outletID";
    public static final String KEY_OUTLET_STATUS = "outletName";

    //GENder Table Fields
    public static final String KEY_GENDER_ID = "genderID";
    public static final String KEY_GENDER_NAME = "genderName";

    //Destricts Table Fields
    public static final String KEY_DISTRICTS_ID = "dictrictID";
    public static final String KEY_DISCTRICTS_NAME = "districtName";
    public static final String KEY_DISCTRICTS_CODE = "dictrictName";

    //CROPS LIST Table Fields
    public static final String KEY_CROP_ID = "cropID";
    public static final String KEY_CROP_LONG_DESCRIPTION = "croplongDes";
    public static final String KEY_CROP_SHORT_DESCRIPTION = "cropshortDesc";
    public static final String KEY_CROP_NAME = "cropName";

    //FERT TYPES Table Fields
    public static final String KEY_FERT_ID = "fertID";
    public static final String KEY_FERT_NAME = "fertName";
    public static final String KEY_FERT_DESCRIPTION = "fertDesc";

    //SOIL DEPTHS Table Fields
    public static final String KEY_SOIL_ID = "soilID";
    public static final String KEY_SOIL_NAME = "soilName";
    public static final String KEY_SOIL_SHORT_DESCRIPTION = "soilshortDesc";
    public static final String KEY_SOIL_LONG_DESCRIPTION = "soillongDesc";
    public static final String KEY_SOIL_UNIT = "soilUnit";
    public static final String KEY_SOIL_DEPTH_TO = "soildepthTO";
    public static final String KEY_SOIL_DEPTH_FROM = "soildepthFROM";






    private static DatabaseHandler sInstance;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL Queries to create tables
        String TABLE_LOGIN = "CREATE TABLE " + LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_COMPANY_NAME + " TEXT,"
                + KEY_USER_DESIGNATION + " TEXT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_USER_EMAIL+ " TEXT,"
                + KEY_IS_LOGGED_IN + " TEXT " + ")";

        String TABLE_OUTLET_STATUSES = "CREATE TABLE " + OUTLET_STATUSES + "("
                + KEY_OUTLET_ID + " TEXT,"
                + KEY_OUTLET_STATUS + " TEXT" + ")";

        String TABLE_CROP_LIST = "CREATE TABLE " + CROPS_LIST+ "("
                + KEY_CROP_ID+ " TEXT,"
                + KEY_CROP_SHORT_DESCRIPTION+ " TEXT,"
                + KEY_CROP_NAME+ " TEXT,"
                + KEY_CROP_LONG_DESCRIPTION + " TEXT" + ")";

        String TABLE_FERT_TYPES = "CREATE TABLE " + FERT_TYPES+ "("
                + KEY_FERT_ID+ " TEXT,"
                + KEY_FERT_DESCRIPTION+ " TEXT,"
                + KEY_FERT_NAME + " TEXT" + ")";

        String TABLE_GENDER = "CREATE TABLE " + GENDER+ "("
                + KEY_GENDER_ID+ " TEXT,"
                + KEY_GENDER_NAME + " TEXT" + ")";

        String TABLE_DISTRICT = "CREATE TABLE " + DISTRTICTS+ "("
                + KEY_DISTRICTS_ID+ " TEXT,"
                + KEY_DISCTRICTS_NAME+ " TEXT,"
                + KEY_DISCTRICTS_CODE + " TEXT" + ")";



        String TABLE_SOIL_DEPTHS= "CREATE TABLE " + SOIL_DEPTHS+ "("
                + KEY_SOIL_ID+ " TEXT,"
                + KEY_SOIL_DEPTH_FROM+ " TEXT,"
                + KEY_SOIL_SHORT_DESCRIPTION+ " TEXT,"
                + KEY_SOIL_DEPTH_TO+ " TEXT,"
                + KEY_SOIL_NAME+ " TEXT,"
                + KEY_SOIL_UNIT+ " TEXT,"
                + KEY_SOIL_LONG_DESCRIPTION + " TEXT" + ")";



        db.execSQL(TABLE_LOGIN);
        db.execSQL(TABLE_OUTLET_STATUSES);
        db.execSQL(TABLE_CROP_LIST);
        db.execSQL(TABLE_FERT_TYPES);
        db.execSQL(TABLE_SOIL_DEPTHS);
        db.execSQL(TABLE_GENDER);
        db.execSQL(TABLE_DISTRICT);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + OUTLET_STATUSES);
        db.execSQL("DROP TABLE IF EXISTS " + CROPS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + FERT_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + SOIL_DEPTHS);
        db.execSQL("DROP TABLE IF EXISTS " + GENDER);
        db.execSQL("DROP TABLE IF EXISTS " + DISTRTICTS);
        onCreate(db);

    }

    //Storing Data inside Database Table
    public void addData(String tablename, HashMap<String, String> keyMap) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            for (Map.Entry entry : keyMap.entrySet()) {
                String value = entry.getValue() == null ? null : entry.getValue().toString();
                if (!entry.getKey().toString().contains("Ar")) {
                    value = clean(value);
                }

                try {
                    if (!entry.getKey().toString().contains("Ar")) {
                        value = URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("%3A", ":");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                values.put(entry.getKey().toString(), value);
            }
            db.insert(tablename, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addData2(String tablename, HashMap<String, String> keyMap) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            for (Map.Entry entry : keyMap.entrySet()) {
                String value = entry.getValue() == null ? null : entry.getValue().toString();
                values.put(entry.getKey().toString(), value);
            }
            db.insert(tablename, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateData(String tablename, HashMap<String, String> hashMap, HashMap<String, String> filters) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            String[] filterArray = null;
            String filterKeys = null;
            String filterValues = null;
            ContentValues values = new ContentValues();
            for (Map.Entry entry : hashMap.entrySet()) {
                String value = entry.getValue() == null ? null : entry.getValue().toString();
                value = clean(value);
                try {
                    value = URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("%3A", ":");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                values.put(entry.getKey().toString(), value);
            }
            if (!filters.isEmpty()) {
                filterKeys = filterBuilder(filters, false);
                filterValues = paramsBuilder(filters, true);
                filterArray = paramsBuilder(filters, true).split(",");
            }
            int records = db.update(tablename, values, filterKeys, filterArray);
            Log.e("Records updated", "" + records);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Cursor getData(String tablename, HashMap<String, String> params, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String parameters = paramsBuilder(params, false);
        String[] paramArray = paramsBuilder(params, false).split(",");
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",", -1);
        }
        Cursor cursor = db.query(tablename, paramArray, filterKeys, filterArray, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return cursor;
    }

    public boolean checkData(String tablename, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
        Cursor cursor = db.rawQuery("Select * from " + tablename + " where " + filterKeys, filterArray);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return exists;
    }

    public void deleteData(String tablename, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
        db.delete(tablename, filterKeys, filterArray);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    public static String paramsBuilder(HashMap<String, String> hashMap, boolean isValue) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!isValue) {
                list.add(entry.getKey().toString());
            } else {
                list.add(value);
            }
        }
        return TextUtils.join(",", list);
    }

    public static String filterBuilder(HashMap<String, String> hashMap, boolean isValue) {
        ArrayList<String> list = new ArrayList<>();
        boolean isOr = false;
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!isValue) {
                list.add(entry.getKey().toString() + "=?");
            } else {
                list.add(value);
            }
        }
        return TextUtils.join((isOr ? "or" : " and "), list);
    }

    public static String clean(String data) {
        if (data == null) return "";
        data = data.replaceAll("([^A-Za-z0-9&: \\-\\.,_\\?\\*]*)", "");
        data = data.replaceAll("([ ]+)", " ");
        return data;
    }

    public void truncateTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

}
