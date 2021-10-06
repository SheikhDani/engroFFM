package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesCallAdapter;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Commitment;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.DataModel;
import com.tallymarks.ffmapp.models.Recommendations;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.tasks.GetCompanHeldBrandBasicList;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QualityofSalesCallActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back, iv_commitment;
    ArrayList<DataModel> dataModels;
    private TableLayout mTableLayout;
    ArrayList<Commitment> arraylist = new ArrayList<Commitment>();
    ListView listView;
    private SalesCallAdapter adapter;
    Button btn_back , btn_add_market_intelligence;
    DatabaseHandler db;
    SharedPrefferenceHelper sHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_call);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        db = new DatabaseHandler(QualityofSalesCallActivity.this);
        sHelper = new SharedPrefferenceHelper(QualityofSalesCallActivity.this);
        iv_commitment = findViewById(R.id.img_commitment);
        btn_back = findViewById(R.id.back);
        btn_add_market_intelligence = findViewById(R.id.btn_market_intelligence);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(QualityofSalesCallActivity.this, MarketPricesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        btn_add_market_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMarketintelligence();
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
        iv_commitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommitment();
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

    private void openCommitment() {
        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_add_commitment, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        TextView tv_Date = promptsView.findViewById(R.id.txt_date);
        EditText et_quantity = promptsView.findViewById(R.id.et_quantity);
        TextView auto_Product = promptsView.findViewById(R.id.auto_product);
        auto_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProductDialouge(auto_Product);
            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add_commitment);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }


    private void openMarketintelligence() {
        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_market_intelligence, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        EditText et_review = promptsView.findViewById(R.id.et_Remarks);



        Button btnYes = promptsView.findViewById(R.id.btn_add);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    public void selectProductDialouge(TextView autoProduct) {
        LayoutInflater li = LayoutInflater.from(QualityofSalesCallActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QualityofSalesCallActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> companyList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);
        title.setText("Select Company");
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SaelsPoint companyname = companyList.get(position);
                alertDialog.dismiss();
                autoProduct.setText(companyname.getPoint());

                // Toast.makeText(getApplicationContext(), movie.getPoint() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareCompanyData(mAdapter, companyList);
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    private void prepareCompanyData(SalesPointAdapter mAdapter, List<SaelsPoint> movieList) {
        String productName = "", productID = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        map.put(db.KEY_ENGRO_RAND_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                productName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_RAND_NAME)));
                productID = cursor.getString(cursor.getColumnIndex(db.KEY_ENGRO_BRANCH_ID));
                companyname.setPoint(productName);
                companyname.setId(productID);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
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
