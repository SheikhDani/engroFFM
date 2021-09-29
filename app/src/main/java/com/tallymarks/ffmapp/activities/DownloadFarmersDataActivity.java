package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
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
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.utils.MyDividerItemDecoration;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class DownloadFarmersDataActivity extends AppCompatActivity {
    private List<Farmes> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FarmersAdapter mAdapter;
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
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
        tvTopHeader.setText("DOWNLOAD  FARMERS DATA");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);

        mAdapter = new FarmersAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DownloadFarmersDataActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

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
                Farmes movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }
    private void prepareMovieData() {
       Farmes movie = new Farmes("illahi Bukhsh", "Z0001",0 );
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001",0 );
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001",0 );
        movieList.add(movie);

        movie = new Farmes("illahi Bukhsh", "Z0001", 0);
        movieList.add(movie);


         movie = new Farmes("illahi Bukhsh", "Z0001",0);
        movieList.add(movie);



        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }
}
