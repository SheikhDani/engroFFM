package com.tallymarks.ffmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.PlanFuture;
import com.tallymarks.ffmapp.models.TodayPlan;

import java.util.List;

public class FuturePlanAdapter extends RecyclerView.Adapter<FuturePlanAdapter.MyViewHolder> {

    private List<PlanFuture> planList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, delearduration, approvalstatus;
        Button btn_request_devitation;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textView_date);
            approvalstatus = (TextView) view.findViewById(R.id.textView_approval);
            delearduration = (TextView) view.findViewById(R.id.plan_duration);
            btn_request_devitation = view.findViewById(R.id.request_devitation);
        }
    }


    public FuturePlanAdapter(List<PlanFuture> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public FuturePlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_futureplan_items, parent, false);

        return new FuturePlanAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(FuturePlanAdapter.MyViewHolder holder, int position) {
        PlanFuture movie = planList.get(position);
        holder.date.setText(movie.getPlandate());
        holder.delearduration.setText(movie.getDealertime());
        holder.approvalstatus.setText(movie.getApprovalstatus());


    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
