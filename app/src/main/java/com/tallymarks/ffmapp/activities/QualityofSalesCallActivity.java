package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesCallAdapter;
import com.tallymarks.ffmapp.models.Commitment;
import com.tallymarks.ffmapp.models.DataModel;
import com.tallymarks.ffmapp.models.Recommendations;

import java.util.ArrayList;

public class QualityofSalesCallActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    ArrayList<DataModel> dataModels;
    private TableLayout mTableLayout;
    ArrayList<Commitment> arraylist = new ArrayList<Commitment>();
    ListView listView;
    private SalesCallAdapter adapter;
    Button btn_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_call);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(QualityofSalesCallActivity.this, MarketPricesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("QUALITY OF SALES CALL");
        listView = (ListView) findViewById(R.id.listView);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QualityofSalesCallActivity.this, MarketPricesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        dataModels = new ArrayList();
        prepareRecommendationData();
        drawRecommendationTable();

        dataModels.add(new DataModel("EFERT Agritrade SSP+Zinc", false));
        dataModels.add(new DataModel("EFERT Agritrade Zoron", false));
        dataModels.add(new DataModel("Zarkhez Khas", false));
        dataModels.add(new DataModel("EFERT Agritrade Powder", true));
        dataModels.add(new DataModel("EFERT Agritrade Amonium Sulphate", true));
        dataModels.add(new DataModel("EFERT Agritrade Powder potash", true));
        dataModels.add(new DataModel("EFERT Agritrade Zingro", true));
        dataModels.add(new DataModel("Zarkhez Khas(MOP)", false));
        dataModels.add(new DataModel("EFERT Agritrade Zingro", false));
        adapter = new SalesCallAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel = dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                adapter.notifyDataSetChanged();


            }
        });
    }

    public void drawRecommendationTable() {
        int cursorIndex = 0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Product");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(16);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Quantity");
        column2.setTextSize(16);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Timeline");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(16);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Confirmed");
        column4.setTextSize(16);
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
        row.addView(column1, new TableRow.LayoutParams(0, 150, 0.25f));
        row.addView(column2, new TableRow.LayoutParams(0, 150, 0.25f));
        row.addView(column3, new TableRow.LayoutParams(0, 150, 0.25f));
        row.addView(column4, new TableRow.LayoutParams(0, 150, 0.25f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for (int i = 0; i < arraylist.size(); i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView prod = new TextView(this);
            prod.setText(arraylist.get(i).getProduct());
            prod.setTextSize(14);
            //startDate.setBackgroundResource(R.drawable.table_row);
            prod.setGravity(Gravity.CENTER);
            prod.setPadding(2, 2, 2, 2);
            TextView quantity = new TextView(this);
            quantity.setText(arraylist.get(i).getQuantity());
            quantity.setGravity(Gravity.CENTER);
            quantity.setTextSize(14);
            //name.setBackgroundResource(R.drawable.table_row);
            quantity.setPadding(2, 2, 2, 2);

            TextView timeline = new TextView(this);
            timeline.setText(arraylist.get(i).getTimeline());
            timeline.setGravity(Gravity.CENTER);

            timeline.setTextSize(14);
            //status.setBackgroundResource(R.drawable.table_row);
            timeline.setPadding(2, 2, 2, 2);
            TextView confirm = new TextView(this);
            confirm.setText(arraylist.get(i).getConfirmed());
            confirm.setGravity(Gravity.CENTER);
            confirm.setTextSize(14);
            //srNo.setBackgroundResource(R.drawable.table_row);
            confirm.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(prod, new TableRow.LayoutParams(0, 150, 0.25f));
            row2.addView(quantity, new TableRow.LayoutParams(0, 150, 0.25f));
            row2.addView(timeline, new TableRow.LayoutParams(0, 150, 0.25f));
            row2.addView(confirm, new TableRow.LayoutParams(0, 150, 0.25f));


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
        Commitment prod = new Commitment();
        prod.setProduct("Engr SSP(25)");
        prod.setQuantity("10");
        prod.setTimeline("August 4,2021");
        prod.setConfirmed("Yes");
        arraylist.add(prod);


        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
