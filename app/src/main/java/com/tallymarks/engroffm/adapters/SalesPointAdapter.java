package com.tallymarks.engroffm.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.models.SaelsPoint;
import com.tallymarks.engroffm.models.TodayPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SalesPointAdapter extends RecyclerView.Adapter<SalesPointAdapter.MyViewHolder> {

    private List<SaelsPoint> moviesList;
    private List<SaelsPoint> headerList;
    String activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView);

        }
    }


    public SalesPointAdapter(List<SaelsPoint> movieList, String activity) {
        this.moviesList = movieList;
        this.headerList = new ArrayList<SaelsPoint>();
        this.headerList.addAll(moviesList);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sales_point, parent, false);

        return new SalesPointAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SalesPointAdapter.MyViewHolder holder, int position) {
        SaelsPoint movie = moviesList.get(position);
        if (activity.equals("farmer")) {
            holder.title.setText(movie.getId()+"-"+movie.getPoint());
        }
        else if (activity.equals("changelocation")) {
            holder.title.setText(movie.getCode()+"-"+movie.getPoint() +"-"+movie.getSalespoint());
        }else {
            holder.title.setText(movie.getPoint());
        }


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

            ArrayList<TodayPlan> filteredList = new ArrayList<>();
            for (SaelsPoint pl : headerList) {
                if(activity.equals("changelocation")) {
                    if (pl.getPoint().toLowerCase(Locale.getDefault()).contains(charText) ||pl.getSalespoint().toLowerCase(Locale.getDefault()).contains(charText)||pl.getCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        moviesList.add(pl);
                    }

                }
                else
                {
                    if (pl.getPoint().toLowerCase(Locale.getDefault()).contains(charText)) {
                        moviesList.add(pl);
                    }

                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
}
