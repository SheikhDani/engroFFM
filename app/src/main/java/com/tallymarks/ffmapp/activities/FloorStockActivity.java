package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.FloorStockAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.FloorStockParent;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloorStockActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    List<FloorStockChild> arraylist = new ArrayList<>();
    ImageView iv_menu, iv_back;
    Button btn_snap, btn_proceed, btn_back;
    DatabaseHandler db;
    SharedPrefferenceHelper sHelper;
    final ArrayList<FloorStockParent> floorStock = new ArrayList<FloorStockParent>();
    final ArrayList<FloorStockChild> floorStockChild = new ArrayList<FloorStockChild>();
    private int count = 0;
    ScrollView scroll;
    static {
        System.loadLibrary("native-lib");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_stock);
        initView();


    }


    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        btn_snap = findViewById(R.id.btn_prev_snap);
        btn_proceed = findViewById(R.id.btn_proceed);
        iv_back = findViewById(R.id.iv_back);
        scroll = findViewById(R.id.scroll);
        db = new DatabaseHandler(FloorStockActivity.this);
        sHelper = new SharedPrefferenceHelper(FloorStockActivity.this);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FloorStockActivity.this, StartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
//        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        scroll.setFocusable(true);
//        scroll.setFocusableInTouchMode(true);
//        scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.requestFocusFromTouch();
//                return false;
//            }
//        });
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("FLOOR STOCK");
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView);
        getFloorStockCategoryDataLocally();
        //getData();
        FloorStockAdapter adapter = new FloorStockAdapter(this, floorStock, elv);
        elv.setAdapter(adapter);
        Constants.FLOOR_STOCK_RELOAD_ADAPTER = "1";


        if(sHelper.getString(Constants.SCROLL_POSITION)!=null) {
            if (sHelper.getString(Constants.SCROLL_POSITION) == "0") {

            } else {
                String pos = sHelper.getString(Constants.SCROLL_POSITION);
                elv.setSelection(Integer.parseInt(pos));
            }
        }
        btn_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnpostedDataExist()) {
                    updateFloorStock();
                } else {
                    addFloorStock();
                }
                Intent snap = new Intent(FloorStockActivity.this, CustomerSnapShotActivity.class);
                startActivity(snap);
            }
        });
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnpostedDataExist()) {
                    updateFloorStock();
                } else {
                    addFloorStock();
                }
                Intent proceed = new Intent(FloorStockActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(proceed);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FloorStockActivity.this, StartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void getFloorStockCategoryDataLocally() {
//ArrayList<String> categoryNameArray = new ArrayList<>();
        String categoryName = "";
        String categoryImage = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME, "");
        map.put(db.KEY_PRODUCT_BRAND_CATEOGRY_IMAGE_BASE64, "");

        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();

        Cursor cursor = db.getData(db.PRODUCT_BRANDS_CATEGORY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                count = 0;
                categoryName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME)));
                categoryImage= cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_CATEOGRY_IMAGE_BASE64));
                //categoryNameArray.add(categoryName);
                FloorStockParent floor = new FloorStockParent(categoryName);
                getFloorStockProductsLocally(categoryName, floor,categoryImage);
                floorStock.add(floor);

            }
            while (cursor.moveToNext());

        }
//        for(int i=0 ;i<categoryNameArray.size();i++)
//        {
//            getFloorStockProductsLocally(categoryNameArray.get(i), floorStock.get(i));
//
//        }


    }

    private void getFloorStockProductsLocally(String categoryName, FloorStockParent parentFloor,String categoryImage) {
        String productID = "", productName = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_PRODUCT_BRAND_NAME, "");
        map.put(db.KEY_PRODUCT_BRAND_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME, categoryName);
        Cursor cursor = db.getData(db.PRODUCT_BRANDS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                count++;
                FloorStockChild floorChild = new FloorStockChild();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_ID));
                floorChild.setProductname(productName);
                floorChild.setProductID(productID);
                if (count == 1) {
                    floorChild.setImageurl(categoryImage);
                }
                parentFloor.players.add(floorChild);
                floorStockChild.add(floorChild);
                //loadFloorStock(parentFloor);

            }
            while (cursor.moveToNext());
        }
    }

    public void loadFloorStock(FloorStockParent parentFloor) {
        String brandquantity;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                FloorStockChild child = new FloorStockChild();
                brandquantity = cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS));
                child.setProductinput(brandquantity);
                parentFloor.players.add(child);

            }
            while (cursor.moveToNext());
        }
    }

    public void addFloorStock() {
        for (int i = 0; i < floorStockChild.size(); i++) {

            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID, floorStockChild.get(i).getProductID());
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDNAME, floorStockChild.get(i).getProductname());
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, floorStockChild.get(i).getProductinput());
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            db.addData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, headerParams);
        }
        //sHelper.setString(Constants.SURVEY_ID,String.valueOf(totalscore));
    }

    public void updateFloorStock() {
        for (int i = 0; i < floorStockChild.size(); i++) {
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, floorStockChild.get(i).getProductinput());
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID, floorStockChild.get(i).getProductID());
            filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            db.updateData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, headerParams, filter);
        }

        //sHelper.setString(Constants.SURVEY_ID,String.valueOf(totalscore));

    }

    private boolean isUnpostedDataExist() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, map, filters);
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

    private void getData() {


        FloorStockParent t1 = new FloorStockParent("Urea");
        FloorStockChild e = new FloorStockChild();
        e.setProductname("Zinc Urea");
        e.setImageurl("");
        t1.players.add(e);
        arraylist.add(e);

        FloorStockChild e2 = new FloorStockChild();
        e2.setProductname("tara SSP");
        e2.setImageurl("");
        t1.players.add(e2);
        arraylist.add(e);

        FloorStockParent t2 = new FloorStockParent("SSP");

        FloorStockChild e11 = new FloorStockChild();
        e11.setProductname("Engro SSP(50)");
        e11.setImageurl("hello");
        t2.players.add(e11);
        arraylist.add(e11);

        FloorStockChild e21 = new FloorStockChild();
        e21.setProductname("Engro SSP(25)");
        e21.setImageurl("");
        t2.players.add(e21);
        arraylist.add(e21);

        FloorStockChild e31 = new FloorStockChild();
        e31.setProductname("Tara SSP");
        e31.setImageurl("");
        t2.players.add(e31);
        arraylist.add(e31);

        FloorStockChild e41 = new FloorStockChild();
        e41.setProductname("SSP+Zinc");
        e41.setImageurl("");
        t2.players.add(e41);
        arraylist.add(e41);
        FloorStockChild e51 = new FloorStockChild();
        e51.setProductname("FFFRT Amitrid");
        e51.setImageurl("");
        t2.players.add(e51);
        arraylist.add(e51);

        FloorStockParent t3 = new FloorStockParent("Urea 2");
        FloorStockChild e61 = new FloorStockChild();
        e61.setProductname("Zinc Urea");
        e61.setImageurl("hello");
        t3.players.add(e61);
        arraylist.add(e61);

        FloorStockChild e62 = new FloorStockChild();
        e62.setProductname("tara SSP");
        e62.setImageurl("");
        t3.players.add(e62);
        arraylist.add(e62);

        floorStock.add(t1);
        floorStock.add(t2);
        floorStock.add(t3);

//        ArrayList<FloorStockParent> allTeams = new ArrayList<FloorStockParent>();
//
//        allTeams.add(t1);
//        allTeams.add(t2);
//        allTeams.add(t3);


//        return allTeams;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(FloorStockActivity.this, StartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
