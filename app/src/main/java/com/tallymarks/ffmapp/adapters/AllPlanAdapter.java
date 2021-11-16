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
import com.tallymarks.ffmapp.models.PlanPast;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllPlanAdapter extends RecyclerView.Adapter<AllPlanAdapter.MyViewHolder> {

    private List<TodayPlan> planList;

    private ItemClickListener clickListener;
    private List<TodayPlan> headerList;
    String from;


    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        public TextView title, member, customecode, salespoint, time,distance;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            member = (TextView) view.findViewById(R.id.textView_subcription);
            image = (ImageView) view.findViewById(R.id.imageView);
            customecode = (TextView) view.findViewById(R.id.customer_code);
            salespoint = (TextView) view.findViewById(R.id.sale_point);
            time = (TextView) view.findViewById(R.id.time);
            distance = (TextView) view.findViewById(R.id.distance);

            if(from.equals("farmers")) {
                member.setVisibility(View.VISIBLE);
                distance.setVisibility(View.GONE);
                time.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

            }
            else if(from.equals("subordinate"))
            {
                member.setVisibility(View.GONE);
                distance.setVisibility(View.GONE);
                time.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
            else
            {
                member.setVisibility(View.VISIBLE);
                distance.setVisibility(View.VISIBLE);
            }

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }

    }


    public AllPlanAdapter(List<TodayPlan> moviesList,String from) {
        this.planList = moviesList;
        this.from = from;
        this.headerList = new ArrayList<TodayPlan>();
        this.headerList.addAll(planList);
    }

    @Override
    public AllPlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_todayplan_items, parent, false);

        return new AllPlanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodayPlan movie = planList.get(position);
        holder.title.setText(movie.getTitle());
        if(!from.equals("subordinate")) {
            if (movie.getMemebrship().equals("Visited")) {
                holder.member.setBackgroundColor(Color.parseColor("#159356"));
                holder.member.setText(movie.getMemebrship());
            } else if(movie.getMemebrship().equals("Not Available")) {
                holder.member.setBackgroundColor(Color.RED);
                holder.member.setText(movie.getMemebrship());
            }
            else
            {
                holder.member.setBackgroundColor(Color.GRAY);
                holder.member.setText(movie.getMemebrship());
            }
        }
        holder.salespoint.setText("Sales Point: "+movie.getSalespoint());
        if(from.equals("farmers")) {
            holder.time.setText(movie.getLocation());
            holder.customecode.setText(movie.getMobilenumber());
            if (movie.getMemebrship().equals("Visited")) {
                holder.member.setBackgroundColor(Color.parseColor("#159356"));
                holder.member.setText(movie.getMemebrship());
            } else {
                holder.member.setBackgroundColor(Color.GRAY);
                holder.member.setText(movie.getMemebrship());
            }
        }
        else if(from.equals("subordinate"))
        {
            holder.customecode.setText(movie.getCustomercode());
            holder.time.setText(movie.getLocation());
        }
        else
        {
            holder.customecode.setText("Customer Code: "+movie.getCustomercode());
            holder.time.setText(movie.getTime());
            holder.distance.setText(movie.getDistance());
        }
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
            for (TodayPlan pl :headerList) {

                if (pl.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    planList.add(pl);
                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
}
