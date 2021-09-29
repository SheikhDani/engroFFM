package com.tallymarks.ffmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Farmes;

import java.util.List;

public class FarmersAdapter extends RecyclerView.Adapter<FarmersAdapter.MyViewHolder> {

    private List<Farmes> moviesList;

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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmers_list_item, parent, false);

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
}
