package com.tallymarks.ffmapp.utils;

import android.database.Cursor;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Crop;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Customer;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Farmer;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Product;
import java.util.ArrayList;
import java.util.HashMap;

public class FarmerMeetingDbHelper {

    public static ArrayList<Product> loadProductsFromDB(DatabaseHandler databaseHandler){
        ArrayList<Product> productArrayList=new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(databaseHandler.KEY_ENGRO_RAND_NAME, "");
        map.put(databaseHandler.KEY_ENGRO_BRANCH_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = databaseHandler.getData(databaseHandler.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String branchName=Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_ENGRO_RAND_NAME)));
                String branchID=cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_ENGRO_BRANCH_ID));
                productArrayList.add(new Product(branchID, branchName));
               }
            while (cursor.moveToNext());
        }
        return productArrayList;
    }

    public static ArrayList<Customer> loadDealersFromDB(DatabaseHandler databaseHandler) {
        ArrayList<Customer> dealersArrayList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = databaseHandler.getData(databaseHandler.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String customerName = Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_NAME)));
                String customerCode = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_CUSTOMER_CODE));
                dealersArrayList.add(new Customer(customerCode, customerName));
            }
            while (cursor.moveToNext());
        }
        return dealersArrayList;
    }

    public static ArrayList<Crop> loadCropsFromDB(DatabaseHandler databaseHandler) {
        ArrayList<Crop> cropsArrayList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(databaseHandler.KEY_CROP_ID, "");
        map.put(databaseHandler.KEY_CROP_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = databaseHandler.getData(databaseHandler.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String cropID = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_CROP_ID));
                String cropName = Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_CROP_NAME)));
                cropsArrayList.add(new Crop(cropID, cropName));
            }
            while (cursor.moveToNext());
        }
        return cropsArrayList;
    }

    public static ArrayList<Farmer> loadFarmersFromDB(MyDatabaseHandler databaseHandler) {
        ArrayList<Farmer> farmersArrayList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(databaseHandler.KEY_TODAY_JOURNEY_FARMER_ID, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_FARMER_CODE, "");
        map.put(databaseHandler.KEY_TODAY_JOURNEY_FARMER_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = databaseHandler.getData(databaseHandler.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String farmerID = cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_FARMER_ID));
                String farmerCode = Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_FARMER_CODE)));
                String farmerName= Helpers.clean(cursor.getString(cursor.getColumnIndex(databaseHandler.KEY_TODAY_JOURNEY_FARMER_NAME)));
                farmersArrayList.add(new Farmer(farmerID, farmerCode, farmerName));
            }
            while (cursor.moveToNext());
        }
        return farmersArrayList;
    }
}
