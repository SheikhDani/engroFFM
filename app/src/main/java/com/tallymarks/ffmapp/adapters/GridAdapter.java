package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.FloorStockParent;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<FloorStockChild> team;

    public GridAdapter(Context c,List<FloorStockChild> team ) {
        mContext = c;
        this.team = team;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return team.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
//        View  grid = new View(mContext);;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.card_items, null);

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        FloorStockChild e= team.get(position);

        textView.setText(e.getProductname());

        return convertView;
    }
}
