package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.CustomerSnapShotAdapter;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.Farmes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerSnapShotActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    Button btn_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_snapshot);
       initView();
    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        btn_back = findViewById(R.id.back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
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
        ExpandableListView elv=(ExpandableListView) findViewById(R.id.expandableListView);

        final ArrayList<CustomerSnapShotParent> team=getData();
        CustomerSnapShotAdapter adapter=new CustomerSnapShotAdapter(this, team);
        elv.setAdapter(adapter);

        //SET ONCLICK LISTENER
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {
                CustomerSnapShot e = team.get(groupPos).players.get(childPos);
                Toast.makeText(CustomerSnapShotActivity.this, ""+e.getProductname(), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(), (CharSequence) team.get(groupPos).players.get(childPos), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
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
}

