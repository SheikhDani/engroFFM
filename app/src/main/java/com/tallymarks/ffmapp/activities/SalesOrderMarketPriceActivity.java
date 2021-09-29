package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesOrderMarketPriceAdapter;
import com.tallymarks.ffmapp.models.SalesOrderChild;
import com.tallymarks.ffmapp.models.SalesOrderHeader;

import java.util.ArrayList;
import java.util.List;

public class SalesOrderMarketPriceActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    ExpandableListView expandableListView;
    SalesOrderMarketPriceAdapter expandableListAdapter;
    List<SalesOrderHeader> expandableListTitle;
    List<SalesOrderChild> expandableListGroup;
    private List<SalesOrderHeader> headerList;
    private static final String LIST_STATE = "listState";
    private Parcelable mListState  = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_marketprice);
        initView();

    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("Market Prices");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
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
        LoadSalesOrder();
        expandableListAdapter = new SalesOrderMarketPriceAdapter(headerList, SalesOrderMarketPriceActivity.this);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
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
                String invoicenumber  = header.getItemList().get(childPosition).getInvoiceNumber();
                String invociequanity = header.getItemList().get(groupPosition).getInvoiceQuantity();
                Log.e("invoice",String.valueOf(invoicenumber));
                Intent market = new Intent(SalesOrderMarketPriceActivity.this,MarketPricesActivity.class);
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
    private void LoadSalesOrder()
    {

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
    private void LoadSalesOrderDetail()
    {
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
