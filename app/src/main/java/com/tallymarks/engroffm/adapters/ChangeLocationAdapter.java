package com.tallymarks.engroffm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.activities.ChangeCoordinatesMapActivity;
import com.tallymarks.engroffm.models.ChangeLocation;
import com.tallymarks.engroffm.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeLocationAdapter extends RecyclerView.Adapter<ChangeLocationAdapter.MyViewHolder> {

    private List<ChangeLocation> planList;
    private List<ChangeLocation> headerList;

    private ItemClickListener clickListener;
    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, member, customecode, lat, lng, reason, createdate, comment, distance;
        Button location;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            member = (TextView) view.findViewById(R.id.textView_subcription);
            image = (ImageView) view.findViewById(R.id.imageView);
            customecode = (TextView) view.findViewById(R.id.customer_code);
            lat = (TextView) view.findViewById(R.id.latitude);
            lng = (TextView) view.findViewById(R.id.longitude);
            location = (Button) view.findViewById(R.id.iv_location);
            reason = (TextView) view.findViewById(R.id.reason);
            createdate = (TextView) view.findViewById(R.id.creation_date);
            comment = (TextView) view.findViewById(R.id.supervisorcomment);
            distance = (TextView) view.findViewById(R.id.distance);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }

    }


    public ChangeLocationAdapter(List<ChangeLocation> moviesList,Context c) {
        this.planList = moviesList;
        this.headerList = new ArrayList<ChangeLocation>();
        this.headerList.addAll(planList);
        this.mContext = c;

    }

    @Override
    public ChangeLocationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_changelocation_item, parent, false);

        return new ChangeLocationAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ChangeLocationAdapter.MyViewHolder holder, int position) {
        ChangeLocation movie = planList.get(position);
        holder.title.setText(movie.getName());

        if (movie.getStatus().equals("Completed")) {
           // holder.comment.setVisibility(View.GONE);
            holder.member.setBackgroundColor(Color.parseColor("#159356"));
            holder.member.setText(movie.getStatus());
        } else if (movie.getStatus().equals("Rejected")) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText("Supervisor Comment: " +movie.getComment());
        }
        {
           // holder.comment.setVisibility(View.GONE);
            holder.member.setBackgroundColor(Color.GRAY);
            holder.member.setText(movie.getStatus());
        }

        holder.distance.setText("Distance between locations: " +movie.getDistancedif());

        holder.customecode.setText(movie.getCode());
        holder.lat.setText(movie.getLatitude());
        holder.lng.setText(movie.getLongitude());
        holder.reason.setText(movie.getReason());
        holder.createdate.setText("Changed Date: " + movie.getDate());
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movie.getLatitude()!=null && movie.getLongitude()!=null
                        && !movie.getLatitude().equals("0.0") && !movie.getLongitude().equals("0.0") ) {
                    // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(mContext, ChangeCoordinatesMapActivity.class);
                    i.putExtra("dealerlat",movie.getOldlatitude());
                    i.putExtra("dealerlng",movie.getOldlongitude());
                    i.putExtra("dealerlatnew", movie.getLatitude());
                    i.putExtra("dealerlngnew", movie.getLongitude());
                    i.putExtra("from", "customerlocation");
                    mContext.startActivity(i);
                }
                else
                {
                    Toast.makeText(mContext, "No Location Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

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

            ArrayList<ChangeLocation> filteredList = new ArrayList<>();
            for (ChangeLocation pl : headerList) {

                if (pl.getName().toLowerCase(Locale.getDefault()).contains(charText) ||pl.getCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    planList.add(pl);
                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
}
