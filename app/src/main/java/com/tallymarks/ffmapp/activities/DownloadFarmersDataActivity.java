package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.tasks.LoadAssignedFarmerFromSalesPoint;
import com.tallymarks.ffmapp.tasks.LoadFarmersAllJourneyPlan;
import com.tallymarks.ffmapp.utils.MyDividerItemDecoration;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadFarmersDataActivity extends AppCompatActivity {
    private List<Farmes> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FarmersAdapter mAdapter;
    private TextView tvTopHeader;
    private EditText et_Search;
    ImageView iv_menu,iv_back;
    MyDatabaseHandler mydb;
    static {
        System.loadLibrary("native-lib");
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famers);
        initView();

    }
    private void initView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        et_Search = findViewById(R.id.et_Search);
        tvTopHeader.setText("DOWNLOAD  FARMERS DATA");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);



        recyclerView.setHasFixedSize(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DownloadFarmersDataActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        et_Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(et_Search.hasFocus()) {
                    mAdapter.filter(cs.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });

        prepareMovieData();

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Farmes movie = movieList.get(position);
                if(movie.getImage()==0) {
                    //Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                    new LoadAssignedFarmerFromSalesPoint(DownloadFarmersDataActivity.this, movieList.get(position).getStatus(),mAdapter).execute();
                    //mAdapter.notifyItemChanged(position);



                }
                else
                {
                    Toast.makeText(DownloadFarmersDataActivity.this, "Farmer Already Downloaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }
    private void prepareMovieData() {
        movieList.clear();
        mydb = new MyDatabaseHandler(DownloadFarmersDataActivity.this);

        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME, "");
        map.put(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE, "");

        HashMap<String, String> filters = new HashMap<>();
        ArrayList<String> salesPointName = new ArrayList<>();

        Cursor cursor = mydb.getData(mydb.ALL_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                //fertTypeArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_FERT_NAME)));
                //fertTypeIDArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_FERT_ID)));

                if (cursor.getString(cursor.getColumnIndex(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME)).contains("%20")){
                    Farmes movie = new Farmes(cursor.getString(cursor.getColumnIndex(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME)).replace("%20", " "), cursor.getString(cursor.getColumnIndex(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE)),0 );
                    movieList.add(movie);

                }else{

                Farmes movie = new Farmes(cursor.getString(cursor.getColumnIndex(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME)), cursor.getString(cursor.getColumnIndex(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE)),0 );
                movieList.add(movie);

                }
            }
            while (cursor.moveToNext());
        }

//        Farmes movie = new Farmes("illahi Bukhsh", "Z0001",0 );
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001",0 );
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001",0 );
//        movieList.add(movie);
//
//        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
//        movieList.add(movie);
//
//
//         movie = new Farmes("illahi Bukhsh", "Z0001",0);
//        movieList.add(movie);



        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter = new FarmersAdapter(movieList,DownloadFarmersDataActivity.this);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(DownloadFarmersDataActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
