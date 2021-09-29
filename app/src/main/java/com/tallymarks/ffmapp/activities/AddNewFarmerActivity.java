package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewFarmerActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    ImageView img_add_serving, img_add_profile;
    LinearLayout linearLayoutList, LinearLayoutList2;
    Button btn_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_farmer);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        img_add_serving = findViewById(R.id.img_add_serving);
        img_add_profile = findViewById(R.id.img_add_profile);
        linearLayoutList = (LinearLayout) findViewById(R.id.linear_layout_dynamic_serving);
        LinearLayoutList2 = (LinearLayout) findViewById(R.id.linear_layout_dynamic_profile);
        iv_menu = findViewById(R.id.iv_drawer);
        btn_back = findViewById(R.id.back);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("ADD NEW FARMER");
        img_add_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });
        img_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView2();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewFarmerActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewFarmerActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void addView() {

        final View addView = getLayoutInflater().inflate(R.layout.list_serving, null, false);
        EditText et_dealer = (EditText) addView.findViewById(R.id.et_dealer);
        EditText auto_company = addView.findViewById(R.id.auto_select_company);
        TextView txt_company = (TextView) addView.findViewById(R.id.txt_select_company);
        TextView txt_dealer = (TextView) addView.findViewById(R.id.txt_dealer);
        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);
        auto_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalesPoint("company");
            }
        });


        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_commitment);

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

    private void addView2() {

        final View addView = getLayoutInflater().inflate(R.layout.list_profile, null, false);

        EditText et_size = (EditText) addView.findViewById(R.id.et_size);
        EditText et_landmark = (EditText) addView.findViewById(R.id.et_landmark);
        AutoCompleteTextView auto_ownership = addView.findViewById(R.id.auto_owner_ship);
        AutoCompleteTextView auto_water_source = addView.findViewById(R.id.auto_water_soruce);
        Button btn_cropping = addView.findViewById(R.id.btn_cropping_pattern);
        Button btn_sales_point = addView.findViewById(R.id.btn_sales_point);
        final LinearLayout linear_crop = addView.findViewById(R.id.linear_layout_dynamic_cropping_pattern);
        TextView txt_size = (TextView) addView.findViewById(R.id.txt_size);
        TextView txt_water_source = (TextView) addView.findViewById(R.id.txt_water_Source);
        TextView txt_ownership = (TextView) addView.findViewById(R.id.txt_ownership);
        TextView txt_sales_point = (TextView) addView.findViewById(R.id.txt_sales_point);
        TextView txt_land_makr = (TextView) addView.findViewById(R.id.txt_landmarl);

        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);


        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_delete);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView2(addView);
            }
        });
        btn_cropping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView3(linear_crop);
            }
        });
        btn_sales_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalesPoint("sales");
            }
        });
        LinearLayoutList2.addView(addView);
    }

    private void removeView2(View v) {
        LinearLayoutList2.removeView(v);
    }

    private void addView3(final LinearLayout linear) {

        final View addView = getLayoutInflater().inflate(R.layout.list_cropping, null, false);

        EditText et_land_holding = (EditText) addView.findViewById(R.id.et_land_holding);
        AutoCompleteTextView auto_crop = findViewById(R.id.auto_crop);
        TextView txt_crop = (TextView) addView.findViewById(R.id.txt_crop);
        TextView txt_land_holding = (TextView) addView.findViewById(R.id.txt_land_holding);
        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);


        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_delete);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView3(addView, linear);
            }
        });
        linear.addView(addView);
    }

    private void removeView3(View v, LinearLayout linear) {
        linear.removeView(v);
    }

    public void openSalesPoint(String salespoint) {
        LayoutInflater li = LayoutInflater.from(AddNewFarmerActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> movieList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);
        if (salespoint.equals("sales")) {
            title.setText("Sales Point");
        } else {
            title.setText("Select Company");
        }
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(movieList);
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
                SaelsPoint movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getPoint() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareMovieData(mAdapter, movieList, salespoint);
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    private void prepareMovieData(SalesPointAdapter mAdapter, List<SaelsPoint> movieList, String from) {
        SaelsPoint movie = new SaelsPoint();
        if (from.equals("sales")) {
            movie.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie);

        SaelsPoint movie2 = new SaelsPoint();
        if (from.equals("sales")) {
            movie2.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie2.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie2);

        SaelsPoint movie3 = new SaelsPoint();
        if (from.equals("sales")) {
            movie3.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie3.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie3);

        SaelsPoint movie4 = new SaelsPoint();
        if (from.equals("sales")) {
            movie4.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie4.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie4);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }
}

