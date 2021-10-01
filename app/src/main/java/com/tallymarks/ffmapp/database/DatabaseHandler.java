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
    public static final String ENGRO_BRANCH = "ENGRO_BRANCH";
    public static final String PRODUCT_BRANDS = "PRODUCT_BRANDS";
    public static final String PRODUCT_BRANDS_CATEGORY = "PRODUCT_BRANDS_CATEGORY";
    public static final String TODAY_JOURNEY_PLAN = "TODAY_JOURNEY_PLAN";
    public static final String TODAY_JOURNEY_PLAN_ORDERS = "TODAY_JOURNEY_PLAN_ORDERS";
    public static final String TODAY_JOURNEY_PLAN_PREVIOUS_STOCK = "TODAY_JOURNEY_PLAN_PREVIOUS_STOCK";
    public static final String TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT = "TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT";
    public static final String TODAY_JOURNEY_PLAN_ORDERS_INVOICES = "TODAY_JOURNEY_PLAN_ORDERS_INVOICES";



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

    //Product Brand Category Table Fields
    public static final String KEY_PRODUCT_BRAND_CATEOGRY_NAME = "productbrandCateogry";


    //Destricts Table Fields
    public static final String KEY_DISTRICTS_ID = "dictrictID";
    public static final String KEY_DISCTRICTS_NAME = "districtName";
    public static final String KEY_DISCTRICTS_CODE = "dictrictName";

    //Product Product Brand Table Fields
    public static final String KEY_PRODUCT_BRAND_ID = "brandID";
    public static final String KEY_PRODUCT_BRAND_DIVISION_CODE = "branddivisionCode";
    public static final String KEY_PRODUCT_BRAND_COMPANY_HELD = "brandcompanyheld";
    public static final String KEY_PRODUCT_BRAND_NAME = "brandName";

    //Engro Branch Table Fields
    public static final String KEY_ENGRO_BRANCH_ID = "engrbranchID";
    public static final String KEY_ENGRO_DIVISION_CODE = "engrodivisionCode";
    public static final String KEY_ENGRO_RAND_NAME = "engrobrandName";
    public static final String KEY_ENGRO_FERT_TYPE = "engrofertType";

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


    //Today Journey Plan Table Fields
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_ID = "todayCustomerID";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_CODE = "todayCustomerCode";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_NAME = "todayCustomerName";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE = "todayCustomerLatitude";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE = "todayCustomerLongitude";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID = "todayCustomerDayID";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID = "todayCustoemrJourneyID";
    public static final String KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME = "todayCustoemrSalesPointName";

    //Today Journey Plan Orders Table Fields
    public static final String KEY_TODAY_JOURNEY_ORDER_ID = "todayOrderID";
    public static final String KEY_TODAY_JOURNEY_ORDER_NUMBER = "todayOrderNumber";
    public static final String KEY_TODAY_JOURNEY_ORDER_DATE = "todayOrderDate";
    public static final String KEY_TODAY_JOURNEY_ORDER_BRAND_NAME = "todayOrderBrandName";
    public static final String KEY_TODAY_JOURNEY_ORDER_QUANTITY = "todayOrderQuantity";

    //Today Journey Plan Previous Stock Table Fields
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID = "todayOrderPrevoisStockID";
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME = "todayOrderPreviousStockname";
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY = "todayOrderPreviousStockQuanity";
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE = "todayOrderpreviousStockDate";
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD = "todayOrderpreviousStockCompany";

    //Today Journey Plan Previous SNapShot Table Fields
    public static final String KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY = "todayOrderPrevoisSNapShotCategory";

    //Today Journey Plan Orders Invoice Table Fields
    public static final String KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER = "todayOrderInvocieNumber";
    public static final String KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE= "todayOrderDispatchDate";
    public static final String KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY = "todayOrderDispatchQuantity";
    public static final String KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY = "todayOrderAvailableQuantity";
    public static final String KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE = "todayOrderInvoiceRate";







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

        String TABLE_PRODUCT_BRAND_CATEGORY = "CREATE TABLE " + PRODUCT_BRANDS_CATEGORY + "("
                + KEY_PRODUCT_BRAND_CATEOGRY_NAME + " TEXT" + ")";

        String TABLE_CROP_LIST = "CREATE TABLE " + CROPS_LIST+ "("
                + KEY_CROP_ID+ " TEXT,"
                + KEY_CROP_SHORT_DESCRIPTION+ " TEXT,"
                + KEY_CROP_NAME+ " TEXT,"
                + KEY_CROP_LONG_DESCRIPTION + " TEXT" + ")";

        String TABLE_ENGRO_BRANCH = "CREATE TABLE " + ENGRO_BRANCH+ "("
                + KEY_ENGRO_BRANCH_ID+ " TEXT,"
                + KEY_ENGRO_RAND_NAME+ " TEXT,"
                + KEY_ENGRO_DIVISION_CODE+ " TEXT,"
                + KEY_ENGRO_FERT_TYPE + " TEXT" + ")";

        String TABLE_PRODUCT_BRAND = "CREATE TABLE " + PRODUCT_BRANDS+ "("
                + KEY_PRODUCT_BRAND_ID+ " TEXT,"
                + KEY_PRODUCT_BRAND_NAME+ " TEXT,"
                + KEY_PRODUCT_BRAND_CATEOGRY_NAME+ " TEXT,"
                + KEY_PRODUCT_BRAND_DIVISION_CODE+ " TEXT,"
                + KEY_PRODUCT_BRAND_COMPANY_HELD + " TEXT" + ")";


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

        String TABLE_TODAY_JOURNEY_PLAN= "CREATE TABLE " + TODAY_JOURNEY_PLAN+ "("
                + KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_CODE+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_NAME+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME + " TEXT" + ")";

        String TABLE_TODAY_JOURNEY_PLAN_ORDERS= "CREATE TABLE " + TODAY_JOURNEY_PLAN_ORDERS+ "("
                + KEY_TODAY_JOURNEY_ORDER_DATE+ " TEXT,"
                + KEY_TODAY_JOURNEY_CUSTOMER_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_BRAND_NAME+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_NUMBER+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_QUANTITY + " TEXT" + ")";

        String TABLE_TODAY_JOURNEY_PLAN_PREVIOUS_STOCK= "CREATE TABLE " + TODAY_JOURNEY_PLAN_PREVIOUS_STOCK+ "("
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY+ " TEXT" + ")";


        String TABLE_TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT= "CREATE TABLE " + TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT+ "("
                + KEY_TODAY_JOURNEY_CUSTOMER_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY+ " TEXT" + ")";


        String TABLE_TODAY_JOURNEY_PLAN_ORDERS_INVOICES= "CREATE TABLE " + TODAY_JOURNEY_PLAN_ORDERS_INVOICES+ "("
                + KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_ID+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY+ " TEXT,"
                + KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY + " TEXT" + ")";




        db.execSQL(TABLE_LOGIN);
        db.execSQL(TABLE_OUTLET_STATUSES);
        db.execSQL(TABLE_CROP_LIST);
        db.execSQL(TABLE_FERT_TYPES);
        db.execSQL(TABLE_SOIL_DEPTHS);
        db.execSQL(TABLE_GENDER);
        db.execSQL(TABLE_DISTRICT);
        db.execSQL(TABLE_ENGRO_BRANCH);
        db.execSQL(TABLE_PRODUCT_BRAND);
        db.execSQL(TABLE_PRODUCT_BRAND_CATEGORY);
        db.execSQL(TABLE_TODAY_JOURNEY_PLAN);
        db.execSQL(TABLE_TODAY_JOURNEY_PLAN_ORDERS);
        db.execSQL(TABLE_TODAY_JOURNEY_PLAN_ORDERS_INVOICES);
        db.execSQL(TABLE_TODAY_JOURNEY_PLAN_PREVIOUS_STOCK);
        db.execSQL(TABLE_TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT);





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
        db.execSQL("DROP TABLE IF EXISTS " + ENGRO_BRANCH);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_BRANDS);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_BRANDS_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN_ORDERS_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN_PREVIOUS_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT);
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
