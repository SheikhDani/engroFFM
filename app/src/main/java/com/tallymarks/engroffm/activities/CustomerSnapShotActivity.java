package com.tallymarks.engroffm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.CustomerSnapShotAdapter;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.CustomerSnapShot;
import com.tallymarks.engroffm.models.CustomerSnapShotParent;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerSnapShotActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    Button btn_back;
    DatabaseHandler db;
    final ArrayList<CustomerSnapShotParent> customerStock = new ArrayList<CustomerSnapShotParent>();
    SharedPrefferenceHelper sHelper;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_snapshot);
        initView();
    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        btn_back = findViewById(R.id.back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        db = new DatabaseHandler(CustomerSnapShotActivity.this);
        sHelper = new SharedPrefferenceHelper(CustomerSnapShotActivity.this);
        tvTopHeader.setText("CUSTOMER SNAPSHOT");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerSnapShotActivity.this, FloorStockActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerSnapShotActivity.this, FloorStockActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView);
        getCustomerSnapShotLocally();

//        final ArrayList<CustomerSnapShotParent> team=getData();
        CustomerSnapShotAdapter adapter = new CustomerSnapShotAdapter(this, customerStock);
        elv.setAdapter(adapter);

        //SET ONCLICK LISTENER
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {
                CustomerSnapShot e = customerStock.get(groupPos).players.get(childPos);
                Toast.makeText(CustomerSnapShotActivity.this, "" + e.getProductname(), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(), (CharSequence) team.get(groupPos).players.get(childPos), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    private void getCustomerSnapShotLocally() {

        String categoryName = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                categoryName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY)));
                CustomerSnapShotParent floor = new CustomerSnapShotParent(categoryName);
                getFloorStockProductsLocally(categoryName, floor);
                customerStock.add(floor);

            }
            while (cursor.moveToNext());


        } else {
            Toast.makeText(CustomerSnapShotActivity.this, "\"There is No Previous SnapShot Available against this Customer", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerSnapShotActivity.this);
//            alertDialogBuilder
//                    .setMessage("There is No Previous SnapShot Available against this Customer")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    //Toast.makeText(ShopStatusActivity.this, "You are "+totalb+" Km away from the shop ", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                    );
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
        }

    }

    private void getFloorStockProductsLocally(String categoryName, CustomerSnapShotParent parentFloor) {
        String productID = "", productName = "", productQuantity = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, categoryName);
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_STOCK, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                CustomerSnapShot customerChild = new CustomerSnapShot();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID));
                productQuantity = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY));
                customerChild.setProductname(productName);
                customerChild.setProductquantity(productQuantity);
                customerChild.setProductid(productID);
                parentFloor.players.add(customerChild);

            }
            while (cursor.moveToNext());
        }
    }

    private ArrayList<CustomerSnapShotParent> getData() {


        CustomerSnapShotParent t1 = new CustomerSnapShotParent("Urea");
        CustomerSnapShot e = new CustomerSnapShot();
        e.setProductname("7 Star");
        e.setProductquantity("0");
        t1.players.add(e);

        CustomerSnapShot e2 = new CustomerSnapShot();
        e2.setProductname("Engro DAP");
        e2.setProductquantity("0");
        t1.players.add(e2);

        CustomerSnapShotParent t2 = new CustomerSnapShotParent("SSP");

        CustomerSnapShot e11 = new CustomerSnapShot();
        e11.setProductname("7 Star");
        e11.setProductquantity("0");
        t2.players.add(e11);

        CustomerSnapShot e21 = new CustomerSnapShot();
        e21.setProductname("Engro DAP");
        e21.setProductquantity("0");
        t2.players.add(e21);


        CustomerSnapShotParent t3 = new CustomerSnapShotParent("DAP");

        CustomerSnapShot e33 = new CustomerSnapShot();
        e33.setProductname("7 Star");
        e33.setProductquantity("0");
        t3.players.add(e33);

        CustomerSnapShot e34 = new CustomerSnapShot();
        e34.setProductname("Engro DAP");
        e34.setProductquantity("0");
        t3.players.add(e34);


        ArrayList<CustomerSnapShotParent> allTeams = new ArrayList<CustomerSnapShotParent>();
        allTeams.add(t1);
        allTeams.add(t2);
        allTeams.add(t3);


        return allTeams;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CustomerSnapShotActivity.this, FloorStockActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

