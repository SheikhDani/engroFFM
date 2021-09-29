package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.SoilSamplingCrops;
import com.tallymarks.ffmapp.models.StockSellingSummary;

import java.util.ArrayList;

public class StockSellingSummaryActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    Button btn_next;
    ImageView iv_menu, iv_back;
    private TableLayout mTableLayout;
    ArrayList<StockSellingSummary> arraylist = new ArrayList<StockSellingSummary>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_selling_summary);
        initView();
    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);

        tvTopHeader.setText("STOCK SELLING SUMMARY");
        prepareRecommendationData();
        drawRecommendationTable();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StockSellingSummaryActivity.this, SuperVisorSnapShotActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    public void drawRecommendationTable()
    {
        int cursorIndex=0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Order#");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(12);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Invoice#");
        column2.setTextSize(12);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Products");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(12);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Quantity Sold");
        column4.setTextSize(12);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column5 = new TextView(this);
        column5.setText("NSP");
        column5.setTextSize(12);
        column5.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column5.setPadding(2, 2, 2, 2);
        column5.setBackgroundColor(getResources().getColor(R.color.green));
        column5.setTextColor(getResources().getColor(R.color.colorPrimary));
        column5.setTypeface(Typeface.DEFAULT_BOLD);

        TextView column6 = new TextView(this);
        column6.setText("Visit Date");
        column6.setTextSize(12);
        column6.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column6.setPadding(2, 2, 2, 2);
        column6.setBackgroundColor(getResources().getColor(R.color.green));
        column6.setTextColor(getResources().getColor(R.color.colorPrimary));
        column6.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column2, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column3, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column4, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column5, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column6, new TableRow.LayoutParams(0, 150, 0.80f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for(int i=0;i<arraylist.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView orderno = new TextView(this);
            orderno.setText(arraylist.get(i).getOrderno());
            orderno.setTextSize(12);
            //startDate.setBackgroundResource(R.drawable.table_row);
            orderno.setGravity(Gravity.CENTER);
            orderno.setPadding(2, 2, 2, 2);
            TextView invocie = new TextView(this);
            invocie.setText(arraylist.get(i).getInvocieno());
            invocie.setGravity(Gravity.CENTER);
            invocie.setTextSize(12);
            //name.setBackgroundResource(R.drawable.table_row);
            invocie.setPadding(2, 2, 2, 2);

            TextView  products = new TextView(this);
            products.setText(arraylist.get(i).getProdutcs());
            products.setGravity(Gravity.CENTER);

            products.setTextSize(12);
            //status.setBackgroundResource(R.drawable.table_row);
            products.setPadding(2, 2, 2, 2);

            TextView quanitysold= new TextView(this);
            quanitysold.setText(arraylist.get(i).getQuanitysold());
            quanitysold.setGravity(Gravity.CENTER);
            quanitysold.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            quanitysold.setPadding(2, 2, 2, 2);

            TextView nsp= new TextView(this);
            nsp.setText(arraylist.get(i).getNsp());
            nsp.setGravity(Gravity.CENTER);
            nsp.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            nsp.setPadding(2, 2, 2, 2);

            TextView visitdate= new TextView(this);
            visitdate.setText(arraylist.get(i).getVisitdate());
            visitdate.setGravity(Gravity.CENTER);
            visitdate.setTextSize(12);
            //srNo.setBackgroundResource(R.drawable.table_row);
            visitdate.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(orderno, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(invocie, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(products, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(quanitysold, new TableRow.LayoutParams(0, 150, 0.80f));
            row2.addView(nsp, new TableRow.LayoutParams(0, 150, 0.40f));
            row2.addView(visitdate, new TableRow.LayoutParams(0, 150, 0.80f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }
    private void prepareRecommendationData() {
       StockSellingSummary prod = new  StockSellingSummary();
        prod .setInvocieno("2312312312");
        prod .setNsp("dansih");
        prod .setProdutcs("Wheat");
        prod .setVisitdate("16 June ,2021");
        prod.setQuanitysold("250");
        prod.setOrderno("23123123");
        arraylist.add(prod);




        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
