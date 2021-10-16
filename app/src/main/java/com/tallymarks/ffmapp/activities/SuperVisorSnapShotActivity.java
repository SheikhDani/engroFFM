package com.tallymarks.ffmapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.CustomerSnapShotAdapter;
import com.tallymarks.ffmapp.adapters.SuperVisorSnapShotAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.SuperVisorSnapShotChild;
import com.tallymarks.ffmapp.models.SupervisorSnapshotParent;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperVisorSnapShotActivity extends AppCompatActivity {
    private TextView tvTopHeader;

    ImageView iv_menu, iv_back;
    Button btnSumamry;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_snapshot);
        initView();
    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        btnSumamry = findViewById(R.id.sales_summary);

        tvTopHeader.setTextSize(12);
        tvTopHeader.setText("Stock SnapShot for "+"Riaz Ul HAQ");
        btnSumamry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n  = new Intent(SuperVisorSnapShotActivity.this,StockSellingSummaryActivity.class);

                startActivity(n);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SuperVisorSnapShotActivity.this, SubordinatListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ExpandableListView elv=(ExpandableListView) findViewById(R.id.expandableListView);

        final ArrayList<SupervisorSnapshotParent> team=getData();
       SuperVisorSnapShotAdapter adapter=new  SuperVisorSnapShotAdapter(this, team);
        elv.setAdapter(adapter);

        //SET ONCLICK LISTENER
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {
              SuperVisorSnapShotChild e = team.get(groupPos).players.get(childPos);
               // Toast.makeText(SuperVisorSnapShotActivity.this, ""+e.getQuantity(), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(), (CharSequence) team.get(groupPos).players.get(childPos), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    private ArrayList<SupervisorSnapshotParent> getData() {


        SupervisorSnapshotParent t1 = new SupervisorSnapshotParent("Brand Name: Urea");
        SuperVisorSnapShotChild e = new  SuperVisorSnapShotChild();
        e.setName("Engro Urea");
        e.setQuantity("250");
        e.setVisitdate("June 19, 2021");
        t1.players.add(e);

        SuperVisorSnapShotChild  e2 = new  SuperVisorSnapShotChild ();
        e2.setName("Sona Urea");
        e2.setVisitdate("220");
        e2.setVisitdate("June 12 , 2022");
        t1.players.add(e2);

        SupervisorSnapshotParent t2 = new SupervisorSnapshotParent("Brand Name: SSP");
        SuperVisorSnapShotChild e11 = new  SuperVisorSnapShotChild();
        e11.setName("Engro Urea");
        e11.setQuantity("250");
        e11.setVisitdate("June 19, 2021");
        t2.players.add(e11);

        SuperVisorSnapShotChild  e22 = new  SuperVisorSnapShotChild ();
        e22.setName("Sona Urea");
        e22.setVisitdate("220");
        e22.setVisitdate("June 12 , 2022");
        t2.players.add(e2);


        SupervisorSnapshotParent t3 = new SupervisorSnapshotParent("Brand Name:  DAP");
        SuperVisorSnapShotChild e33 = new  SuperVisorSnapShotChild();
        e33.setName("Engro Urea");
        e33.setQuantity("250");
        e33.setVisitdate("June 19, 2021");
        t3.players.add(e33);

        SuperVisorSnapShotChild  e34 = new  SuperVisorSnapShotChild ();
        e34.setName("Sona Urea");
        e34.setVisitdate("220");
        e34.setVisitdate("June 12 , 2022");
        t3.players.add(e34);

        ArrayList<SupervisorSnapshotParent> allTeams = new ArrayList<SupervisorSnapshotParent>();
        allTeams.add(t1);
        allTeams.add(t2);
        allTeams.add(t3);


        return allTeams;
    }

}
