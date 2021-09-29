package com.tallymarks.ffmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.PlanFuture;
import com.tallymarks.ffmapp.models.PlanPast;

import java.util.List;

public class PastPlanAdapter extends RecyclerView.Adapter<PastPlanAdapter.MyViewHolder> {

    private List<PlanPast> planList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, delearduration, approvalstatus;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textView_date);
            approvalstatus = (TextView) view.findViewById(R.id.textView_approval);
            delearduration = (TextView) view.findViewById(R.id.plan_duration);

        }
    }


    public PastPlanAdapter(List<PlanPast> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public PastPlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_pastplan_items, parent, false);

        return new PastPlanAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PastPlanAdapter.MyViewHolder holder, int position) {
        PlanPast movie = planList.get(position);
        holder.date.setText(movie.getPlandate());
        holder.delearduration.setText(movie.getDealertime());
        holder.approvalstatus.setText(movie.getApprovalstatus());


    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}


