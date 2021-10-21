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
import com.tallymarks.ffmapp.models.SoilSamplingLogs;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SOILSamplingLogsAdapter extends RecyclerView.Adapter<SOILSamplingLogsAdapter.MyViewHolder> {

    private List<SoilSamplingLogs> planList;
    private ItemClickListener clickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        public TextView farmerid, farmername, farmerrefernce, farmercheckin, farmercheckout;


        public MyViewHolder(View view) {
            super(view);
            farmercheckin = (TextView) view.findViewById(R.id.farmer_checkin);
            farmerid = (TextView) view.findViewById(R.id.farmer_id);
            farmername = (TextView) view.findViewById(R.id.farmer_name);
            farmercheckout = (TextView) view.findViewById(R.id.farmer_checkout);
            farmerrefernce = (TextView) view.findViewById(R.id.farmer_reference);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }

    }


    public SOILSamplingLogsAdapter(List<SoilSamplingLogs> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public SOILSamplingLogsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_soilsamplinglogs_items, parent, false);

        return new SOILSamplingLogsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SoilSamplingLogs movie = planList.get(position);
        holder.farmerid.setText("Farmer Id:" +movie.getFarmerid());
        holder.farmername.setText("Farmer Name: "+movie.getFarmername());
        holder.farmerrefernce.setText("Reference: "+movie.getReferecne());
        holder.farmercheckin.setText("Checkin : "+movie.getCheckintime());
        holder.farmercheckout.setText("Checkout : "+movie.getChecoutouttime());

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
