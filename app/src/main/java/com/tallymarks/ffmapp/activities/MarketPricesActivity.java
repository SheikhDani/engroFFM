package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.MarketPrice;
import com.tallymarks.ffmapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketPricesActivity extends AppCompatActivity {
    private TextView tvTopHeader, tv_product, tv_invoice, tv_Date, tv_invoice_rate;
    private TextView tv_invoice_quantity, tv_invoice_avaialle_quantity;
    ImageView iv_menu, iv_back, iv_add, iv_del;
    private List<MarketPrice> arrayList = new ArrayList<MarketPrice>();
    private List<MarketPrice> arrayList2 = new ArrayList<MarketPrice>();
    LinearLayout linearLayoutList;
    MarketPrice marketPrice;
    Button btn_open_summary, btn_save;
    DatabaseHandler db;
    RecyclerView lv_market;
    Button btn_back;
    SharedPrefferenceHelper sHelper;
    private String previousInvoiceNumber = "";
    String quanity ,netprice;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_prices);
        initView();


    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tv_invoice = findViewById(R.id.txt_invoice_number);
        tv_Date = findViewById(R.id.txt_date);
        tv_invoice_rate = findViewById(R.id.txt_invoice_rate);
        tv_invoice_quantity = findViewById(R.id.txt_invoice_quantity);
        tv_invoice_avaialle_quantity = findViewById(R.id.txt_available_quantity);
        tv_product = findViewById(R.id.txt_product);
        btn_back = findViewById(R.id.back);
        db = new DatabaseHandler(MarketPricesActivity.this);
        sHelper = new SharedPrefferenceHelper(MarketPricesActivity.this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketPricesActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        iv_add = findViewById(R.id.img_add);
        iv_del = findViewById(R.id.img_del);
        btn_open_summary = findViewById(R.id.btn_save);
        iv_back = findViewById(R.id.iv_back);
        lv_market = findViewById(R.id.recyclerView);
        linearLayoutList = (LinearLayout) findViewById(R.id.linear_layout);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();

            }
        });
        btn_open_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSummary();
            }
        });


        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("MARKET PRICES");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketPricesActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        getInvoiceDetailLocally();
        getPreviousInvocieNumber();
       getStockSoldLocally();
    }

    private boolean isUnpostedDataExist() {
        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, map, filters);
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

    private void getStockSoldLocally() {
        arrayList2.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
        filters.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                final View addView = getLayoutInflater().inflate(R.layout.list_market_price, null, false);

                EditText quantity = (EditText) addView.findViewById(R.id.et_quanity);
                EditText netprice = (EditText) addView.findViewById(R.id.et_net_price);
                quantity.setText(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD)));
                netprice.setText(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE)));


                ImageView imgdel = (ImageView) addView.findViewById(R.id.img_del);

                imgdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeView(addView);
                    }
                });
                linearLayoutList.addView(addView);
//                MarketPrice m = new MarketPrice();
//                m.setNetprice(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE)));
//                m.setQuanity(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD)));
//                arrayList2.add(m);
//                arrayList.clear();
//                EditText quantity = (EditText) addView.findViewById(R.id.et_quanity);
//                EditText netprice = (EditText) addView.findViewById(R.id.et_net_price);
//
//
//                marketPrice = new MarketPrice();
//                marketPrice.setQuanity(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD)));
//                marketPrice.setNetprice(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE)));
//                marketPrice.setProduct(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME))));
//                marketPrice.setInvoice(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER)));
//                arrayList.add(marketPrice);
            }
            while (cursor.moveToNext());
        }
    }

    private void getPreviousInvocieNumber() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_STOCK_INVOICE_NUMBER, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN_STOCK, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                previousInvoiceNumber = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_STOCK_INVOICE_NUMBER));
            }
            while (cursor.moveToNext());
        }

    }

    private void getInvoiceDetailLocally() {
        tv_invoice.setText(sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
        tv_product.setText(sHelper.getString(Constants.TODAY_PLAN_INVOICE_PRODUCT_NAME));
        tv_Date.setText("Dated: " + sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_DATE));
        tv_invoice_rate.setText(sHelper.getString(Constants.TODAY_PLAN_INVOICE_Rate));
        tv_invoice_avaialle_quantity.setText("Available Quantity: " + sHelper.getString(Constants.TODAY_PLAN_INVOICE_AVAILABLE_QUANTITY));
        tv_invoice_quantity.setText("Invoice Quantity: " + sHelper.getString(Constants.TODAY_PLAN_INVOICE_QUANTITY));
        tv_Date.setText("Dated: " + sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_DATE));

    }

    private void getdata() {
        arrayList.clear();
        String[] quantity = new String[linearLayoutList.getChildCount()];
        String[] nsp = new String[linearLayoutList.getChildCount()];
        for (int i = 0; i < linearLayoutList.getChildCount(); i++) {
            Log.d("Child Count: ", String.valueOf(linearLayoutList.getChildCount()));
            View addView = linearLayoutList.getChildAt(i);
            EditText et_quantity = (EditText) addView.findViewById(R.id.et_quanity);
            EditText et_netprice = (EditText) addView.findViewById(R.id.et_net_price);
            quantity[i] = et_quantity.getText().toString();
            nsp[i] = et_netprice.getText().toString();
        }

        for (int i = 0; i < linearLayoutList.getChildCount(); i++) {

            marketPrice = new MarketPrice();
            marketPrice.setQuanity(quantity[i]);
            marketPrice.setNetprice(nsp[i]);
            marketPrice.setProduct(tv_product.getText().toString());
            marketPrice.setInvoice(tv_invoice.getText().toString());
            arrayList.add(marketPrice);
        }


    }

    private void addView() {

        final View addView = getLayoutInflater().inflate(R.layout.list_market_price, null, false);

        EditText quantity = (EditText) addView.findViewById(R.id.et_quanity);
        EditText netprice = (EditText) addView.findViewById(R.id.et_net_price);

        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_del);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(addView);
            }
        });
        linearLayoutList.addView(addView);
    }

    private void removeView(View v) {
        linearLayoutList.removeView(v);
    }

    public void openSummary() {
        LayoutInflater li = LayoutInflater.from(MarketPricesActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_stock_selling_summary, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MarketPricesActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final TableLayout mTableLayout = promptsView.findViewById(R.id.displayLinear);
        final ImageView img_cancel = promptsView.findViewById(R.id.imageView_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
        getdata();
        drawRecommendationTable(mTableLayout);
        Button btnYes = promptsView.findViewById(R.id.btn_procees);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isUnpostedDataExist()) {
//                    updateStockSold();
//                } else {
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
                filter.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
                filter.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER));
                filter.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
                db.deleteData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD,filter);
                    saveStockSold();
//                }
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Intent salescall = new Intent(MarketPricesActivity.this, SalesOrderMarketPriceActivity.class);
                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    private void updateStockSold() {
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, arrayList.get(i).getQuanity());
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE, arrayList.get(i).getNetprice());
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER));
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, sHelper.getString(Constants.TODAY_PLAN_INVOICE_PRODUCT_NAME));
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            if (!previousInvoiceNumber.equals("")) {
                if (previousInvoiceNumber.equals(sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER))) {
                    headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "0");
                    headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "1");
                }
            } else {
                headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "1");
                headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "0");
            }
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            filter.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            filter.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));

            if(i<arrayList2.size())
            {
                db.updateData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, headerParams, filter);
            }
            else
            {
                db.addData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, headerParams);
            }
            }
//            filter.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_UNIQUE_ID,String.valueOf(i+1));
//           db.updateData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, headerParams, filter);
        }


    private void saveStockSold() {
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, arrayList.get(i).getQuanity());
            headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE, arrayList.get(i).getNetprice());
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_ORDER_NUMBER));
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER));
            headerParams.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, sHelper.getString(Constants.TODAY_PLAN_INVOICE_PRODUCT_NAME));
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, sHelper.getString(Constants.CUSTOMER_ID));
            headerParams.put(db.KEY_TODAY_JOURNEY_TYPE, sHelper.getString(Constants.PLAN_TYPE));
            if (!previousInvoiceNumber.equals("")) {
                if (previousInvoiceNumber.equals(sHelper.getString(Constants.TODAY_PLAN_INVOICE_NUMBER))) {
                    headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "0");
                    headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "1");
                }
            } else {
                headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "1");
                headerParams.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "0");
            }
            db.addData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, headerParams);
        }
    }


    public void drawRecommendationTable(TableLayout mTableLayout) {
        mTableLayout.removeAllViews();
        int cursorIndex = 0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Invoice#");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(14);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Products");
        column2.setTextSize(14);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Quantity Sold");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(14);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("NSP");
        column4.setTextSize(14);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 100, 0.25f));
        row.addView(column2, new TableRow.LayoutParams(0, 100, 0.37f));
        row.addView(column3, new TableRow.LayoutParams(0, 100, 0.37f));
        row.addView(column4, new TableRow.LayoutParams(0, 100, 0.25f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for (int i = 0; i < arrayList.size(); i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView invoice = new TextView(this);
            invoice.setText(arrayList.get(i).getInvoice());
            invoice.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            invoice.setGravity(Gravity.CENTER);
            invoice.setPadding(2, 2, 2, 2);
            TextView product = new TextView(this);
            product.setText(arrayList.get(i).getProduct());
            product.setGravity(Gravity.CENTER);
            product.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            product.setPadding(2, 2, 2, 2);

            TextView quanitity = new TextView(this);
            quanitity.setText(arrayList.get(i).getQuanity());
            quanitity.setGravity(Gravity.CENTER);

            quanitity.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            quanitity.setPadding(2, 2, 2, 2);
            TextView nsp = new TextView(this);
            nsp.setText(arrayList.get(i).getNetprice());
            nsp.setGravity(Gravity.CENTER);
            nsp.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            nsp.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(invoice, new TableRow.LayoutParams(0, 120, 0.25f));
            row2.addView(product, new TableRow.LayoutParams(0, 120, 0.37f));
            row2.addView(quanitity, new TableRow.LayoutParams(0, 120, 0.37f));
            row2.addView(nsp, new TableRow.LayoutParams(0,120, 0.25f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(MarketPricesActivity.this, SalesOrderMarketPriceActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
