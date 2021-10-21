package com.tallymarks.ffmapp.adapters;


import android.icu.text.UnicodeSetSpanner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.TodayPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FarmersAdapter extends RecyclerView.Adapter<FarmersAdapter.MyViewHolder> {

    private List<Farmes> moviesList;
    private List<Farmes> headerList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, status;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView);
            status = (TextView) view.findViewById(R.id.status);
            image = (ImageView) view.findViewById(R.id.imageView);
        }
    }


    public FarmersAdapter(List<Farmes> moviesList) {
        this.moviesList = moviesList;
        this.headerList = new ArrayList<Farmes>();
        this.headerList.addAll(moviesList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmers_list_item, parent, false);

        Button button = itemView.findViewById(R.id.forward_img);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "Clicked position: ", Toast.LENGTH_SHORT).show();
            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Farmes movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.status.setText(movie.getStatus());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        moviesList.clear();
        if (charText.length() == 0) {
            moviesList.addAll(headerList);
        } else {

            ArrayList<Farmes> filteredList = new ArrayList<>();
            for (Farmes pl :headerList) {

                if (pl.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    moviesList.add(pl);
                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
}
