package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesCallAdapter;
import com.tallymarks.ffmapp.adapters.SubOrdinatesAdapter;
import com.tallymarks.ffmapp.models.DataModel;
import com.tallymarks.ffmapp.models.Subordinates;

import java.util.ArrayList;

public class SubOrdinatesActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    ListView listView;
    private SubOrdinatesAdapter adapter;
    ArrayList<Subordinates> dataModels;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinates);
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
        tvTopHeader.setText("SUBORDINATES");
        listView = findViewById(R.id.lv_subordinates);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubOrdinatesActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        dataModels = new ArrayList();
        Subordinates s = new Subordinates();
        s.setName("Riaz Ul Haq");
        dataModels.add(s);



        adapter = new SubOrdinatesAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
    }
}
