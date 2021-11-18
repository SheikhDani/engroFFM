package com.tallymarks.ffmapp.activities;

import static com.tallymarks.ffmapp.utils.Helpers.getDatefromMilis;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesOrderMarketPriceAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.SalesOrderChild;
import com.tallymarks.ffmapp.models.SalesOrderHeader;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesOrderMarketPriceActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    ExpandableListView expandableListView;
    SalesOrderMarketPriceAdapter expandableListAdapter;
    List<SalesOrderHeader> expandableListTitle;
    List<SalesOrderChild> expandableListGroup;
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;
    private List<SalesOrderHeader> headerList;
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;
    private String orderNumber, productname, orderDate;
    Button btn_procced, btn_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_marketprice);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("Market Prices");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        db = new DatabaseHandler(SalesOrderMarketPriceActivity.this);
        btn_back = findViewById(R.id.back);
        btn_procced = findViewById(R.id.btn_proceed);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SalesOrderMarketPriceActivity.this, FloorStockActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(SalesOrderMarketPriceActivity.this, QualityofSalesCallActivity.class);
                startActivity(n);
            }
        });
        sHelper = new SharedPrefferenceHelper(SalesOrderMarketPriceActivity.this);
        headerList = new ArrayList<SalesOrderHeader>();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SalesOrderMarketPriceActivity.this, FloorStockActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        // LoadSalesOrder();
        LoadSalesOrderDetailLocally();
        expandableListAdapter = new SalesOrderMarketPriceAdapter(headerList, SalesOrderMarketPriceActivity.this);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
                orderNumber = headerList.get(groupPosition).getOrderNo();
                productname = headerList.get(groupPosition).getOrderProduct();
                orderDate = headerList.get(groupPosition).getOrderDate();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                SalesOrderHeader header = headerList.get(groupPosition);
                String invoicenumber = header.getItemList().get(childPosition).getInvoiceNumber();
                String invociequanity = header.getItemList().get(childPosition).getInvoiceQuantity();
                String invocierate = header.getItemList().get(childPosition).getInvoiceRate();
                String availableQuantity = header.getItemList().get(childPosition).getInvoiceRate();
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_NUMBER, invoicenumber);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_Rate, invocierate);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_QUANTITY, invociequanity);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER, orderNumber);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_ORDER_DATE, orderDate);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_PRODUCT_NAME, productname);
                sHelper.setString(Constants.TODAY_PLAN_INVOICE_AVAILABLE_QUANTITY, availableQuantity);
                Intent market = new Intent(SalesOrderMarketPriceActivity.this, MarketPricesActivity.class);
                startActivity(market);
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Toast.makeText(JourneyPlanActivity.this, "Next", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void LoadSalesOrderDetailLocally() {
        String orderNumber = "", orderid = "", orderDate = "", product = "", orderquantity = "";
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                product = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME)));
                orderDate = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DATE));
                orderquantity = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY));
                orderNumber = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_NUMBER));
                orderid = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_ID));
                if(sHelper.getString(Constants.PLAN_TYPE).equals("today"))
                {
                    orderDate = getDatefromMilis(orderDate);
                }
                else
                {
                    orderDate = Helpers.utcToAnyDateFormat(orderDate,"yyyy-MM-dd","MMM d, yyyy");
                }
                SalesOrderHeader category = createCategory(orderNumber, orderDate, product, orderquantity);
                LoadSalesOrderInvoicesLocally(orderid);
                category.setItemList(expandableListGroup);
                headerList.add(category);

            }
            while (cursor.moveToNext());
        } else {
            Toast.makeText(SalesOrderMarketPriceActivity.this, "There are no Sales Order Against this Customer", Toast.LENGTH_SHORT).show();

//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SalesOrderMarketPriceActivity.this);
//            alertDialogBuilder
//                    .setMessage("There are no Sales Order Against this Customer")
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

    private void LoadSalesOrderInvoicesLocally(String orderid) {
        String invoiceNumber = "", invoiceDate = "", invoicequantity = "", invoicerate = "", availalequantity = "";
        HashMap<String, String> map = new HashMap<>();
        expandableListGroup = new ArrayList<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_ID, orderid);
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS_INVOICES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                invoiceNumber = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER));
                invoiceDate = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE));
                invoicerate = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE));
                availalequantity = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY));
                invoicequantity = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY));
                SalesOrderChild grouop1 = new SalesOrderChild();
                grouop1.setInvocieDate(Helpers.utcToAnyDateFormat(invoiceDate,"yyyy-MM-dd","MMM d, yyyy"));
                grouop1.setInvoiceNumber(invoiceNumber);
                grouop1.setInvoiceQuantity(invoicequantity);
                grouop1.setInvoiceRate(invoicerate);
                grouop1.setInvoiceAvailableQuantity(availalequantity);
                expandableListGroup.add(grouop1);
                //loadFloorStock(parentFloor);

            }
            while (cursor.moveToNext());
        }
    }

    private void LoadSalesOrder() {

        SalesOrderHeader cat1 = createCategory("0000023123", "May 4, 2020", "EFERT AGRITRADE ZINGRD", "280");
        SalesOrderHeader cat2 = createCategory("0000023123", "May 4, 2020", "EFERT AGRITRADE ZINGRD", "280");
        SalesOrderHeader cat3 = createCategory("0000023123", "May 4, 2020", "EFERT AGRITRADE ZINGRD", "280");
        SalesOrderHeader cat4 = createCategory("0000023123", "May 4, 2020", "EFERT AGRITRADE ZINGRD", "280");
        LoadSalesOrderDetail();
        cat1.setItemList(expandableListGroup);
        cat2.setItemList(expandableListGroup);
        cat3.setItemList(expandableListGroup);
        cat4.setItemList(expandableListGroup);
        headerList.add(cat1);
        headerList.add(cat2);
        headerList.add(cat3);
        headerList.add(cat4);


    }

    private void LoadSalesOrderDetail() {
        expandableListGroup = new ArrayList<>();
        SalesOrderChild grouop1 = new SalesOrderChild();
        grouop1.setInvocieDate("May 5, 2021");
        grouop1.setInvoiceNumber("123142342");
        grouop1.setInvoiceQuantity("345");
        expandableListGroup.add(grouop1);

        SalesOrderChild grouop2 = new SalesOrderChild();
        grouop2.setInvocieDate("May 5, 2021");
        grouop2.setInvoiceNumber("123142342");
        grouop2.setInvoiceQuantity("345");
        expandableListGroup.add(grouop2);
        SalesOrderChild grouop3 = new SalesOrderChild();
        grouop3.setInvocieDate("May 5, 2021");
        grouop3.setInvoiceNumber("123142342");
        grouop3.setInvoiceQuantity("345");
        expandableListGroup.add(grouop3);
        SalesOrderChild grouop4 = new SalesOrderChild();
        grouop4.setInvocieDate("May 5, 2021");
        grouop4.setInvoiceNumber("123142342");
        grouop4.setInvoiceQuantity("345");
        expandableListGroup.add(grouop4);
    }

    private SalesOrderHeader createCategory(String no, String date, String product, String quantity) {
        return new SalesOrderHeader(no, date, product, quantity);
    }
}
