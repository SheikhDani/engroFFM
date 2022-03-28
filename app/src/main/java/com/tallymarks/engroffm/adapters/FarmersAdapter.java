package com.tallymarks.engroffm.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.database.MyDatabaseHandler;
import com.tallymarks.engroffm.models.Farmes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FarmersAdapter extends RecyclerView.Adapter<FarmersAdapter.MyViewHolder> {

    private List<Farmes> moviesList;
    private List<Farmes> moviesList2;
    private List<Farmes> headerList;
    private Context mContext;
    MyDatabaseHandler mydb;
    String saelspointcode;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, status;
        ImageView image;
        Button download;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView);
            status = (TextView) view.findViewById(R.id.status);
            image = (ImageView) view.findViewById(R.id.imageView);
            download = (Button) view.findViewById(R.id.forward_img);
        }
    }


    public FarmersAdapter(List<Farmes> moviesList, Context c) {
        this.moviesList = moviesList;
        mContext = c;
        this.headerList = new ArrayList<Farmes>();
        this.headerList.addAll(moviesList);
        this.mydb = new MyDatabaseHandler(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmers_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Farmes movie = moviesList.get(position);
        getDownloadedFarmer();
        if (moviesList2 != null && moviesList2.size() > 0) {
            for (int i = 0; i < moviesList2.size(); i++) {
                if (moviesList2.get(i).getStatus().equals(movie.getStatus())) {
                    holder.download.setText("Downloaded");
                    movie.setImage(1);
                    holder.download.setClickable(false);
                    holder.download.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    break;
                }
                else
                {
                    holder.download.setText("Download");
                    holder.download.setClickable(true);
                    movie.setImage(0);
                    holder.download.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                }


            }
        }
        holder.title.setText(movie.getTitle());
        holder.status.setText(movie.getStatus());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        moviesList.clear();
        if (charText.length() == 0) {
            moviesList.addAll(headerList);
        } else {

            ArrayList<Farmes> filteredList = new ArrayList<>();
            for (Farmes pl : headerList) {

                if (pl.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    moviesList.add(pl);
                }

            }
//            headerList = filteredList;
        }

       notifyDataSetChanged();
    }

    public void getDownloadedFarmer() {
        moviesList2 = new ArrayList<Farmes>();
        moviesList2.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_DOWNLOADED_FARMER_SALES_POINT_CODE, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = mydb.getData(mydb.DOWNLOADED_FARMER_DATA, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                saelspointcode = cursor.getString(cursor.getColumnIndex(mydb.KEY_DOWNLOADED_FARMER_SALES_POINT_CODE));
                Farmes e = new Farmes();
                e.setStatus(saelspointcode);
                moviesList2.add(e);

            }
            while (cursor.moveToNext());
        }
    }
}
