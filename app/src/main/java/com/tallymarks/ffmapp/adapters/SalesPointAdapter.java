package com.tallymarks.ffmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.SaelsPoint;

import java.util.List;

public class SalesPointAdapter extends RecyclerView.Adapter< SalesPointAdapter.MyViewHolder> {

    private List<SaelsPoint> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView);

        }
    }


    public  SalesPointAdapter(List<SaelsPoint> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sales_point, parent, false);

        return new  SalesPointAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( SalesPointAdapter.MyViewHolder holder, int position) {
        SaelsPoint movie = moviesList.get(position);
        holder.title.setText(movie.getPoint());


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
