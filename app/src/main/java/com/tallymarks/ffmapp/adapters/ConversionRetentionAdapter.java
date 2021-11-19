package com.tallymarks.ffmapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.Conversion;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConversionRetentionAdapter extends RecyclerView.Adapter<ConversionRetentionAdapter.MyViewHolder> {

    private List<Conversion> planList;
    private ItemClickListener clickListener;
    private List<Conversion> headerList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, member, actvityno, activitydate, parentactivity;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            member = (TextView) view.findViewById(R.id.textView_subcription);

            actvityno = (TextView) view.findViewById(R.id.activity_no);
            activitydate = (TextView) view.findViewById(R.id.activity_date);
            parentactivity = (TextView) view.findViewById(R.id.parent_actvity);


            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }

    }


    public ConversionRetentionAdapter(List<Conversion> moviesList) {
        this.planList = moviesList;
        this.headerList = new ArrayList<Conversion>();
        this.headerList.addAll(planList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_conversion_retention_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversion movie = planList.get(position);
        holder.title.setText(movie.getFarmername());
        if (movie.getStatus().equals("Closed")) {
            holder.member.setBackgroundColor(Color.parseColor("#159356"));
            holder.member.setText(movie.getStatus());
        } else {
            holder.member.setBackgroundColor(Color.GRAY);
            holder.member.setText(movie.getStatus());
        }
        holder.activitydate.setText("Date: "+movie.getDate());
        holder.parentactivity.setText("parent Activity: " + movie.getParentactivity());


    }


    @Override
    public int getItemCount() {
        return planList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        planList.clear();
        if (charText.length() == 0) {
            planList.addAll(headerList);
        } else {

            ArrayList<TodayPlan> filteredList = new ArrayList<>();
            for (Conversion pl : headerList) {

                if (pl.getFarmername().toLowerCase(Locale.getDefault()).contains(charText)) {
                    planList.add(pl);
                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
}
