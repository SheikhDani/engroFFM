package com.tallymarks.engroffm.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.models.SoilAnalysis;

import java.util.List;

public class SoilAnalysisAdapter extends RecyclerView.Adapter<SoilAnalysisAdapter.MyViewHolder> {

    private List<SoilAnalysis> planList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, member, date,sampleno;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            member = (TextView) view.findViewById(R.id.textView_subcription);
            image = (ImageView) view.findViewById(R.id.imageView);
            date = (TextView) view.findViewById(R.id.date);
            sampleno = (TextView) view.findViewById(R.id.textView_sample_no);

        }
    }


    public SoilAnalysisAdapter(List<SoilAnalysis> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public SoilAnalysisAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_solyanalysis_items, parent, false);

        return new SoilAnalysisAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SoilAnalysisAdapter.MyViewHolder holder, int position) {
        SoilAnalysis movie = planList.get(position);
        holder.title.setText(movie.getTitle());
        holder.member.setText(movie.getMemebrship());
        holder.sampleno.setText("Sample No:"+movie.getSampleno());
        holder.date.setText(movie.getDate());


    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
