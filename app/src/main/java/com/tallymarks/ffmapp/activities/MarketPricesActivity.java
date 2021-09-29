package com.tallymarks.ffmapp.activities;

import android.content.Intent;
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
import com.tallymarks.ffmapp.models.MarketPrice;

import java.util.ArrayList;
import java.util.List;

public class MarketPricesActivity extends AppCompatActivity {
    private TextView tvTopHeader,tv_product,tv_invoice;
    ImageView iv_menu, iv_back, iv_add, iv_del;
    private List<MarketPrice> arrayList = new ArrayList<MarketPrice>();
    LinearLayout linearLayoutList;
    MarketPrice marketPrice;
    Button btn_open_summary,btn_save;
    RecyclerView lv_market;
    Button btn_back;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_prices);
        initView();


    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tv_invoice = findViewById(R.id.txt_invoice_number);
        tv_product = findViewById(R.id.txt_product);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketPricesActivity.this,SalesOrderMarketPriceActivity.class);
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
                Intent i = new Intent(MarketPricesActivity.this,SalesOrderMarketPriceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getdata()
    {
        arrayList.clear();
        String[] quantity = new String[linearLayoutList.getChildCount()];
        String[] nsp = new String[linearLayoutList.getChildCount()];
        for (int i =0; i< linearLayoutList.getChildCount();i++ ) {
            Log.d("Child Count: ", String.valueOf(linearLayoutList.getChildCount()));
            View addView = linearLayoutList.getChildAt(i);
            EditText et_quantity = (EditText) addView.findViewById(R.id.et_quanity);
            EditText et_netprice = (EditText) addView.findViewById(R.id.et_net_price);
            quantity[i] = et_quantity.getText().toString();
            nsp[i] = et_netprice.getText().toString();
        }

        for (int i =0; i< linearLayoutList.getChildCount();i++)
        {

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
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
        getdata();
        drawRecommendationTable(mTableLayout);
        Button btnYes = promptsView.findViewById(R.id.btn_procees);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent salescall = new Intent(MarketPricesActivity.this,QualityofSalesCallActivity.class);
                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }
    public void drawRecommendationTable(TableLayout mTableLayout)
    {
        mTableLayout.removeAllViews();
        int cursorIndex=0;
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

        for(int i=0;i<arrayList.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView invoice = new TextView(this);
            invoice.setText(arrayList.get(i).getInvoice());
            invoice.setTextSize(14);
            //startDate.setBackgroundResource(R.drawable.table_row);
            invoice.setGravity(Gravity.CENTER);
            invoice.setPadding(2, 2, 2, 2);
            TextView product = new TextView(this);
            product.setText(arrayList.get(i).getProduct());
            product.setGravity(Gravity.CENTER);
            product.setTextSize(14);
            //name.setBackgroundResource(R.drawable.table_row);
            product.setPadding(2, 2, 2, 2);

            TextView quanitity = new TextView(this);
            quanitity.setText(arrayList.get(i).getQuanity());
            quanitity.setGravity(Gravity.CENTER);

            quanitity.setTextSize(14);
            //status.setBackgroundResource(R.drawable.table_row);
            quanitity .setPadding(2, 2, 2, 2);
            TextView nsp= new TextView(this);
            nsp.setText(arrayList.get(i).getNetprice());
            nsp.setGravity(Gravity.CENTER);
            nsp.setTextSize(14);
            //srNo.setBackgroundResource(R.drawable.table_row);
            nsp.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(invoice, new TableRow.LayoutParams(0, 100, 0.25f));
            row2.addView(product, new TableRow.LayoutParams(0, 100, 0.37f));
            row2.addView(quanitity, new TableRow.LayoutParams(0, 100, 0.37f));
            row2.addView(nsp, new TableRow.LayoutParams(0, 100, 0.25f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }

}
