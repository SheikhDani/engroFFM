package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.CustomerSnapShotAdapter;
import com.tallymarks.ffmapp.adapters.FloorStockAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.FloorStockParent;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloorStockActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    List<FloorStockChild> arraylist = new ArrayList<>();
    ImageView iv_menu, iv_back;
    Button btn_snap, btn_proceed,btn_back;
    DatabaseHandler db;
    final ArrayList<FloorStockParent> floorStock= new ArrayList<FloorStockParent>();
    private int count = 0;


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
        db = new DatabaseHandler(FloorStockActivity.this);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FloorStockActivity.this,StartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("FLOOR STOCK");
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView);
        getFloorStockCategoryDataLocally();
        //getData();
        FloorStockAdapter adapter = new FloorStockAdapter(this, floorStock, elv);
        elv.setAdapter(adapter);
        btn_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent snap = new Intent(FloorStockActivity.this, CustomerSnapShotActivity.class);
                startActivity(snap);
            }
        });
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent proceed = new Intent(FloorStockActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(proceed);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FloorStockActivity.this,StartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    private void getFloorStockCategoryDataLocally()
    {

            String  categoryName="";
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME, "");

            //map.put(db.KEY_IS_VALID_USER, "");
            HashMap<String, String> filters = new HashMap<>();
            Cursor cursor = db.getData(db.PRODUCT_BRANDS_CATEGORY, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    count = 0;
                    categoryName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME)));
                    FloorStockParent floor = new FloorStockParent(categoryName);
                    getFloorStockProductsLocally(categoryName,floor);
                    floorStock.add(floor);

                }
                while (cursor.moveToNext());


            }

    }
    private void getFloorStockProductsLocally(String categoryName,FloorStockParent parentFloor)
    {
        String  productID="" , productName="" ;
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
                productID= cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_BRAND_ID));
                floorChild.setProductname(productName);
                floorChild.setProductID(productID);
                if(count==1) {
                    floorChild.setImageurl("Test");
                }
                parentFloor.players.add(floorChild);

            }
            while (cursor.moveToNext());
        }
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

}
