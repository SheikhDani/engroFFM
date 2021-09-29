package com.tallymarks.ffmapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.DealInfo;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.TextItemViewHolder;

import java.util.List;


public class DealersAdapter extends RecyclerView.Adapter<DealersAdapter.MyViewHolder> {

    private List<DealInfo> planList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, member, status;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            member = (TextView) view.findViewById(R.id.textView_subcription);
            image = (ImageView) view.findViewById(R.id.imageView);
            status = (TextView) view.findViewById(R.id.update_status);

        }
    }


    public DealersAdapter(List<DealInfo> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public DealersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_list_item, parent, false);

        return new DealersAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(DealersAdapter.MyViewHolder holder, int position) {
        DealInfo movie = planList.get(position);
        holder.title.setText(movie.getTitle());
        holder.member.setText(movie.getMemebrship());
        holder.status.setText(movie.getStatus());


    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
