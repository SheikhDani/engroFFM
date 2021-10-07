package com.tallymarks.ffmapp.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.DataModel;

import java.util.ArrayList;

public class SalesCallAdapter extends BaseAdapter {

    private ArrayList<DataModel> dataSet;
    Context mContext;
    private LayoutInflater layoutInflater;
    private DataModel dataModel;


    public SalesCallAdapter(ArrayList data, Context context) {

        this.dataSet = data;
        this.mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }
    @Override
    public long getItemId(int position) {
        return dataSet.get(position).getItemid();
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {


        View result = convertView;

        if (result == null) {
            result = layoutInflater.inflate(R.layout.row_item_sales_call, parent, false);
        }

        TextView txtName = result.findViewById(R.id.txtName);
        CheckBox checkBox =  result.findViewById(R.id.checkBox);
        dataModel = dataSet.get(position);
        txtName.setText(dataModel.getName());
        checkBox.setChecked(dataModel.isChecked());
        checkBox.setTag(position);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  int currentPos = (int) v.getTag();
                boolean isChecked = false;
                if (dataSet.get(position).isChecked() == false) {
                    isChecked = true;
                }
               // Log.d("response ", currentPos + " " + isChecked);
                dataSet.get(position).setChecked(isChecked);
                notifyDataSetChanged();
            }
        });


        return result;
    }
}
