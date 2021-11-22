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

public class MyDatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    /*Database Varaiables*/
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "FFMAppDb_Zohaib_v5";
    private Context mContext;

    //List of all tables
    public static final String TODAY_FARMER_JOURNEY_PLAN = "TODAY_FARMER_JOURNEY_PLAN";
    public static final String TODAY_FARMER_ACTIVITY = "TODAY_FARMER_ACTIVITY";
    public static final String DOWNLOADED_FARMER_DATA = "DOWNLOADED_FARMER_DATA";
    public static final String TODAY_FARMER_RECOMMENDATION = "TODAY_FARMER_RECOMMENDATION";
    public static final String TODAY_FARMER_OTHERPACKS = "TODAY_FARMER_OTHERPACKS";
    public static final String TODAY_FARMER_OTHER_PRODUCTS = "TODAY_FARMER_OTHER_PRODUCTS";
    public static final String TODAY_FARMER_SAMPLING = "TODAY_FARMER_SAMPLING";
    public static final String TODAY_FARMER_CHECKIN = "TODAY_FARMER_CHECKIN";
    public static final String ALL_FARMER_JOURNEY_PLAN = "ALL_FARMER_JOURNEY_PLAN";
    public static final String FARMER_DEMO = "FARMER_DEMO";
    public static final String FARMER_MEETING = "FARMER_MEETING";
    public static final String DEALERS = "DEALERS";
    public static final String ATTACHMETNS = "ATTACHMENTS";
    public static final String TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY = "TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY";
    public static final String TODAY_JOURNEY_PLAN_POST_DATA = "TODAY_JOURNEY_PLAN_POST_DATA";
    public static final String ENGRO_DEALAERS_LIST = "ENGRO_DEALERS_LIST";

    // Post data fields
    //Today Journey Plan Post Data TableFields

    public static final String KEY_TODAY_JOURNEY_FARMER_DISTANCE= "todayCustomerDistance";
    public static final String KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE= "todayCustomerCheckoutLatitude";
    public static final String KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE= "todayCustomerCheckoutLongitude";
    public static final String KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP =  "todayCustomerCheckoutTime";

    // All farmer fields
    public static final String KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE = "farmer_SALES_POINT_CODE";
    public static final String KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME = "farmer_SALES_POINT_NAME";

    // Engro Branch Table Field
    public static final String KEY_ENGRO_PRODUCT_ID= "engrpRODUCTid";
    public static final String KEY_ENGRO_PRODUCT_NAME = "engroPRODUCTNAME";
    public static final String KEY_ENGRO_PRODUCT_CHECKED = "engrocHEKecekd";

    // Downloaded farmer fields
    public static final String KEY_DOWNLOADED_FARMER_SALES_POINT_CODE= "DOWNLOADED_FARMER_SALES_POINT_CODE";

    // farmer Start activity fields
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME = "farmer_startactivity_time";
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS = "farmer_startactivity_objective_status";
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS = "farmer_startactivity_status";
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE = "farmer_startactivity_objective";
    public static final String KEY_TODAY_FARMER_FARMER_ID = "farmerId";
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE = "farmer_startactivity_latitude";
    public static final String KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE = "farmer_startactivity_longitude";

    // farmer demo fields
    public static final String KEY_ACTIVITY_DATE = "activity_date";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CROP_ID = "cropID";
    public static final String KEY_OBJECTIVE = "objective";
    public static final String KEY_PRODUCT_ID = "productID";
    public static final String KEY_STATUS = "status";

    // farmer meeting fields
    public static final String KEY_ACTIVITY_DATE_MEETING = "activityDate";
    public static final String KEY_CHIEF_GUEST = "chiefGuest";
    public static final String KEY_CROP_ID_MEETING = "cropID";
    public static final String KEY_EMPLOYEE_ATTENDADNCE = "employeesAttended";
    public static final String KEY_EXPENSES = "expenses";
    public static final String KEY_HOST_FARMER_ID = "hostFarmerId";
    public static final String KEY_MEETING_ADDRESS = "meetingAddress";
    public static final String KEY_PRODUCT_ID_MEETING = "productId";
    public static final String KEY_REMARKS = "remarks";
    public static final String KEY_TOTAL_CUSTOMERS = "totalCustomers";
    public static final String KEY_TOTAL_FARMERS = "totalFarmers";

    // farmer dealers
    public static final String KEY_HOST_FARMER_ID_DEALERS = "hostFarmerId";
    public static final String KEY_CUSTOMER_CODE = "customerCode";
    public static final String KEY_CUSTOEMR_NAME = "customerName";

    // farmer attachments
    public static final String KEY_HOST_FARMER_ID_ATTACHMENTS = "hostFarmerId";
    public static final String KEY_ATTACHMETS = "attachments";


    // Farmer Today activity
    public static final String KEY_TODAY_FARMER_ID = "farmerId";
    public static final String KEY_TODAY_CROPID = "cropId";
    public static final String KEY_TODAY_SERVINGDEALERID = "servingdealerId";
    public static final String KEY_TODAY_ADDRESS = "address";
    public static final String KEY_TODAY_PACKS_LIQUIATED = "croppackliquiadted";
    public static final String KEY_TODAY_CROP_DEF = "cropdef";
    public static final String KEY_TODAY_CROP_ACE = "cropacre";
    public static final String KEY_TODAY_OTHER_PRODUCT_LIQUIDATED= "otherprodcutliquiadetdid";
    public static final String KEY_TODAY_MAIN_PRODUCT = "mainProduct";
    public static final String KEY_TODAY_REMARKS = "remarks";
    public static final String KEY_TODAY_LATITUTE = "latitude";
    public static final String KEY_TODAY_LONGITUTE = "longitude";


    // Farmer Today Journey Plan Table Fields
    public static final String KEY_TODAY_JOURNEY_FARMER_ID = "farmerId";
    public static final String KEY_TODAY_JOURNEY_FARMER_CODE = "farmerCode";
    public static final String KEY_TODAY_JOURNEY_FARMER_USERTYPE = "farmeruserType";
    public static final String KEY_TODAY_JOURNEY_FARMER_AREA_CULTIVATION = "areacultivation";
    public static final String KEY_TODAY_JOURNEY_FARMER_ACRAEGE = "farmeracerage";
    public static final String KEY_TODAY_JOURNEY_FARMER_NAME = "farmerName";
    public static final String KEY_TODAY_JOURNEY_FARMER_LATITUDE = "latitude";
    public static final String KEY_TODAY_JOURNEY_FARMER_LONGITUDE = "longtitude";
    public static final String KEY_TODAY_JOURNEY_FARMER_DAY_ID = "dayId";
    public static final String KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID = "journeyPlanId";
    public static final String KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME = "salesPoint";
    public static final String KEY_TODAY_FARMER_MOBILE_NO = "mobileNo";
    public static final String KEY_TODAY_JOURNEY_IS_EDITED= "isVisitedTodayCustomerJourneyPlanIsEdited";
    public static final String KEY_TODAY_JOURNEY_IS_VISITED= "isVisitedTodayCustomerJourneyPlan";
    public static final String KEY_TODAY_JOURNEY_IS_POSTED= "isVisitedTodayCustomerJourneyPlanIsPosted";
    public static final String KEY_PLAN_TYPE= "planType";


    // Farmer Today Recommendation fields
    public static final String KEY_TODAY_FARMMER_ID = "farmerId";
    public static final String KEY_TODAY_FERTTYPE_ID = "fertAppTypeId";
    public static final String KEY_TODAY_BRAND_ID = "brandId";
    public static final String KEY_TODAY_DOSAGE = "dosage";

    //Farmer Today  Other Product fields
    public static final String KEY_TODAY_OTHER_PACKS_ID= "farmerTodayOtherProductID";
    public static final String KEY_TODAY_OTHER_PACKS_LIQUIDATED= "farmerTodayOtherPacksLiquidated";

    // Farmer Today Other Product fields
    public static final String KEY_TODAY_FARMER_OTHER_PRODUCT_ID = "farmerOtherProductId";

    // Farmer Today Sampling fields
    public static final String KEY_TODAY_FARMMMER_ID = "farmerId";
    public static final String KEY_TODAY_PREVIOUSCROP_ID = "previousCropId";
    public static final String KEY_TODAY_DEPTH_ID = "depthId";
    public static final String KEY_TODAY_CROP1_ID = "crop1Id";
    public static final String KEY_TODAY_CROP2_ID = "crop2Id";
    public static final String KEY_TODAY_PLOT_NUMBER = "plotNumber";
    public static final String KEY_TODAY_BLOCK_NUMBER = "blockNumber";
    public static final String KEY_TODAY_REFRENCE = "reference";

    // Farmer Today check in
    public static final String KEY_TODAY_DISTANCE = "distance";
    public static final String KEY_TODAY_VISIT_OBJECTIVE = "visitObjective";
    public static final String KEY_TODAY_STATUS = "status";
    public static final String KEY_TODAY_OUTLET_STATUS_ID = "outletStatusId";
    public static final String KEY_TODAY_CHECK_IN_TIMESTAMP = "checkInTimeStamp";
    public static final String KEY_TODAY_CHECKIN_LATITUTE = "checkInLatitude";
    public static final String KEY_TODAY_CHECKIN_LONGITUTE = "checkInLongitude";
    public static final String KEY_TODAY_CEHCKOUT_TIMESTMAP = "checkOutTimeStamp";
    public static final String KEY_TODAY_CHECKOUT_LATITUTE = "checkOutLatitude";
    public static final String KEY_TODAY_CHECKOUT_LONGITUTE = "checkOutLongitude";



    //SQL Queries to create tables

    // farmer attachments
    String TABLE_ATTACHMENTS= "CREATE TABLE " + ATTACHMETNS+ "("
            + KEY_HOST_FARMER_ID_ATTACHMENTS+ " TEXT,"
            + KEY_ATTACHMETS+ " TEXT" + ")";

    // farmer dealers
    String TABLE_DEALERS= "CREATE TABLE " + DEALERS+ "("
            + KEY_HOST_FARMER_ID_DEALERS+ " TEXT,"
            + KEY_CUSTOMER_CODE+ " TEXT,"
            + KEY_CUSTOEMR_NAME+ " TEXT" + ")";


    String TABLE_ALL_FARMER_JOURNEY_PLAN= "CREATE TABLE " + ALL_FARMER_JOURNEY_PLAN+ "("
            + KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE+ " TEXT,"
            + KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME+ " TEXT" + ")";

    String TABLE_DOWNLAODED_FARMER_DATA= "CREATE TABLE " + DOWNLOADED_FARMER_DATA+ "("
            + KEY_DOWNLOADED_FARMER_SALES_POINT_CODE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_JOURNEY_PLAN= "CREATE TABLE " + TODAY_FARMER_JOURNEY_PLAN+ "("
            + KEY_TODAY_JOURNEY_FARMER_DAY_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_CODE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_USERTYPE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_ACRAEGE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_AREA_CULTIVATION+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_LATITUDE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_LONGITUDE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_NAME+ " TEXT,"
            + KEY_TODAY_FARMER_MOBILE_NO+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME + " TEXT,"
            + KEY_TODAY_JOURNEY_IS_VISITED + " TEXT,"
            + KEY_TODAY_JOURNEY_IS_EDITED + " TEXT,"
            + KEY_TODAY_JOURNEY_IS_POSTED + " TEXT,"
            + KEY_PLAN_TYPE + " TEXT" + ")";


    String TABLE_FARMER_DEMO= "CREATE TABLE " + FARMER_DEMO+ "("
            + KEY_ACTIVITY_DATE+ " TEXT,"
            + KEY_ADDRESS+ " TEXT,"
            + KEY_CROP_ID+ " TEXT,"
            + KEY_OBJECTIVE+ " TEXT,"
            + KEY_PRODUCT_ID+ " TEXT,"
            + KEY_STATUS+ " TEXT" + ")";

    String TABLE_FARMER_MEETING = "CREATE TABLE " + FARMER_MEETING + "("
            + KEY_ACTIVITY_DATE_MEETING+ " TEXT,"
            + KEY_CHIEF_GUEST+ " TEXT,"
            + KEY_CROP_ID_MEETING+ " TEXT,"
            + KEY_EMPLOYEE_ATTENDADNCE+ " TEXT,"
            + KEY_EXPENSES+ " TEXT,"
            + KEY_HOST_FARMER_ID+ " TEXT,"
            + KEY_MEETING_ADDRESS+ " TEXT,"
            + KEY_PRODUCT_ID_MEETING+ " TEXT,"
            + KEY_REMARKS+ " TEXT,"
            + KEY_TOTAL_CUSTOMERS+ " TEXT,"
            + KEY_TOTAL_FARMERS+ " TEXT"+ ")";

    String TABLE_TODAY_FARMER_ACTIVITY= "CREATE TABLE " + TODAY_FARMER_ACTIVITY+ "("
            + KEY_TODAY_FARMER_ID+ " TEXT,"
            + KEY_TODAY_CROPID+ " TEXT,"
            + KEY_TODAY_SERVINGDEALERID+ " TEXT,"
            + KEY_TODAY_CROP_ACE+ " TEXT,"
            + KEY_TODAY_CROP_DEF+ " TEXT,"
            + KEY_TODAY_OTHER_PRODUCT_LIQUIDATED+ " TEXT,"
            + KEY_TODAY_PACKS_LIQUIATED+ " TEXT,"
            + KEY_TODAY_ADDRESS+ " TEXT,"
            + KEY_TODAY_MAIN_PRODUCT+ " TEXT,"
            + KEY_TODAY_REMARKS+ " TEXT,"
            + KEY_TODAY_LATITUTE+ " TEXT,"
            + KEY_TODAY_LONGITUTE + " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_RECOMMENDATION= "CREATE TABLE " + TODAY_FARMER_RECOMMENDATION+ "("
            + KEY_TODAY_FARMMER_ID+ " TEXT,"
            + KEY_TODAY_CROPID+ " TEXT,"
            + KEY_TODAY_FERTTYPE_ID+ " TEXT,"
            + KEY_TODAY_BRAND_ID+ " TEXT,"
            + KEY_TODAY_DOSAGE + " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_OTHERPACKS= "CREATE TABLE " + TODAY_FARMER_OTHERPACKS+ "("
            + KEY_TODAY_FARMMER_ID+ " TEXT,"
            + KEY_TODAY_OTHER_PACKS_ID +" TEXT,"
            + KEY_TODAY_OTHER_PACKS_LIQUIDATED+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_OTHER_PRODUCTS= "CREATE TABLE " + TODAY_FARMER_OTHER_PRODUCTS+ "("
            + KEY_TODAY_FARMMER_ID+ " TEXT,"
            + KEY_TODAY_FARMER_OTHER_PRODUCT_ID+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_ENGRO_PRODUCTS= "CREATE TABLE " + ENGRO_DEALAERS_LIST+ "("
            + KEY_TODAY_FARMMER_ID+ " TEXT,"
            + KEY_ENGRO_PRODUCT_CHECKED+ " TEXT,"
            + KEY_ENGRO_PRODUCT_NAME+ " TEXT,"
            + KEY_ENGRO_PRODUCT_ID+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_SAMPLING= "CREATE TABLE " + TODAY_FARMER_SAMPLING+ "("
            + KEY_TODAY_FARMMMER_ID+ " TEXT,"
            + KEY_TODAY_PREVIOUSCROP_ID+ " TEXT,"
            + KEY_TODAY_DEPTH_ID+ " TEXT,"
            + KEY_TODAY_CROP1_ID+ " TEXT,"
            + KEY_TODAY_CROP2_ID+ " TEXT,"
            + KEY_TODAY_PLOT_NUMBER+ " TEXT,"
            + KEY_TODAY_BLOCK_NUMBER + " TEXT,"
            + KEY_TODAY_LATITUTE+ " TEXT,"
            + KEY_TODAY_LONGITUTE+ " TEXT,"
            + KEY_TODAY_REFRENCE + " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_START_ACTIVITY= "CREATE TABLE " + TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY+ "("
            + KEY_TODAY_FARMER_FARMER_ID+ " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME+ " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE_STATUS+ " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS+ " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE+ " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE + " TEXT,"
            + KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";

    String TABLE_TODAY_FARMER_CHECKIN= "CREATE TABLE " + TODAY_FARMER_CHECKIN+ "("
            + KEY_TODAY_JOURNEY_FARMER_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_DAY_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_NAME+ " TEXT,"
            + KEY_TODAY_LATITUTE+ " TEXT,"
            + KEY_TODAY_LONGITUTE + " TEXT,"
            + KEY_TODAY_FARMER_MOBILE_NO+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME+ " TEXT,"
            + KEY_TODAY_DISTANCE + " TEXT,"
            + KEY_TODAY_VISIT_OBJECTIVE+ " TEXT,"
            + KEY_TODAY_STATUS+ " TEXT,"
            + KEY_TODAY_OUTLET_STATUS_ID+ " TEXT,"
            + KEY_TODAY_CHECK_IN_TIMESTAMP + " TEXT,"
            + KEY_TODAY_CHECKIN_LATITUTE+ " TEXT,"
            + KEY_TODAY_CHECKIN_LONGITUTE+ " TEXT,"
            + KEY_TODAY_CEHCKOUT_TIMESTMAP + " TEXT,"
            + KEY_TODAY_CHECKOUT_LATITUTE+ " TEXT,"
            + KEY_TODAY_CHECKOUT_LONGITUTE+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT"+ ")";

    String TABLE_TODAY_JOURNEY_POST_DATA= "CREATE TABLE " + TODAY_JOURNEY_PLAN_POST_DATA+ "("
            + KEY_TODAY_JOURNEY_FARMER_DISTANCE+ " TEXT,"
            + KEY_TODAY_FARMER_ID+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE+ " TEXT,"
            + KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP+ " TEXT,"
            + KEY_PLAN_TYPE+ " TEXT" + ")";



    public MyDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TODAY_FARMER_JOURNEY_PLAN);
        db.execSQL(TABLE_TODAY_FARMER_ACTIVITY);
        db.execSQL(TABLE_TODAY_FARMER_RECOMMENDATION);
        db.execSQL(TABLE_TODAY_FARMER_SAMPLING);
        db.execSQL(TABLE_TODAY_FARMER_CHECKIN);
        db.execSQL(TABLE_TODAY_FARMER_START_ACTIVITY);
        db.execSQL(TABLE_ALL_FARMER_JOURNEY_PLAN);
        db.execSQL(TABLE_TODAY_JOURNEY_POST_DATA);
        db.execSQL(TABLE_FARMER_DEMO);
        db.execSQL(TABLE_FARMER_MEETING);
        db.execSQL(TABLE_DEALERS);
        db.execSQL(TABLE_ATTACHMENTS);
        db.execSQL(TABLE_DOWNLAODED_FARMER_DATA);
        db.execSQL(TABLE_TODAY_FARMER_OTHER_PRODUCTS);
        db.execSQL(TABLE_TODAY_ENGRO_PRODUCTS);
        db.execSQL(TABLE_TODAY_FARMER_OTHERPACKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_JOURNEY_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_RECOMMENDATION);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_SAMPLING);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_CHECKIN);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + ALL_FARMER_JOURNEY_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TODAY_JOURNEY_PLAN_POST_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + FARMER_DEMO);
        db.execSQL("DROP TABLE IF EXISTS " + FARMER_MEETING);
        db.execSQL("DROP TABLE IF EXISTS " + DEALERS);
        db.execSQL("DROP TABLE IF EXISTS " + ATTACHMETNS);
        db.execSQL("DROP TABLE IF EXISTS " + DOWNLOADED_FARMER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " +TODAY_FARMER_OTHER_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " +ENGRO_DEALAERS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " +TODAY_FARMER_OTHERPACKS);



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


}
