package com.tallymarks.ffmapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.CommitmentHistory;
import com.tallymarks.ffmapp.models.DealInfo;

import java.util.List;

public class CommitmentHistoryAdapter extends RecyclerView.Adapter<CommitmentHistoryAdapter.MyViewHolder> {

    private List<CommitmentHistory> planList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, product, price , weight;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            product = (TextView) view.findViewById(R.id.tv_product);
            price = (TextView) view.findViewById(R.id.tv_price);
            weight = (TextView) view.findViewById(R.id.tv_weight);
            img  = (ImageView) view.findViewById(R.id.imageView);

        }
    }


    public CommitmentHistoryAdapter(List<CommitmentHistory> moviesList) {
        this.planList = moviesList;
    }

    @Override
    public CommitmentHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_commitment_item, parent, false);

        return new CommitmentHistoryAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(CommitmentHistoryAdapter.MyViewHolder holder, int position) {
       CommitmentHistory movie = planList.get(position);
        holder.title.setText(movie.getName());
        holder.price.setText(movie.getPrice());
        holder.weight.setText(movie.getWeight());
        holder.product.setText(movie.getProduct());


    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
