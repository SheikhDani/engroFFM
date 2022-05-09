package com.tallymarks.engroffm.adapters;

import android.content.Context;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.database.MyDatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.DataModel;
import com.tallymarks.engroffm.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class SalesCallAdapter extends BaseAdapter {

    private ArrayList<DataModel> dataSet;
    Context mContext;
    private LayoutInflater layoutInflater;
    private DataModel dataModel;
    private ArrayList<DataModel> dataSet2;
    String from;
    MyDatabaseHandler mydb;
    String otherproductid;
    SharedPrefferenceHelper sHelper;

    public SalesCallAdapter(ArrayList data, Context context, String from) {

        this.dataSet = data;
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.from = from;

        this.mydb = new MyDatabaseHandler(mContext);

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
        if (from.equals("farmvisit")) {
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
        } else {
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
        }
//        txtName.setText(dataModel.getName());
//        checkBox.setChecked(dataModel.isChecked());
//        checkBox.setTag(position);
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  int currentPos = (int) v.getTag();
//                boolean isChecked = false;
//                if (dataSet.get(position).isChecked() == false) {
//                    isChecked = true;
//                }
//               // Log.d("response ", currentPos + " " + isChecked);
//                dataSet.get(position).setChecked(isChecked);
//                notifyDataSetChanged();
//            }
//        });


        return result;
    }
    public void getSavedDealers() {
        dataSet2 = new ArrayList<DataModel>();
        dataSet2.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_FARMER_OTHER_PRODUCT_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_TODAY_FARMER_ID, sHelper.getString(Constants.S_FARMER_ID));
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_OTHER_PRODUCTS , map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                otherproductid = cursor.getString(cursor.getColumnIndex(mydb.KEY_TODAY_FARMER_OTHER_PRODUCT_ID));
                DataModel e = new DataModel();
                e.setId(otherproductid);
                dataSet2.add(e);

            }
            while (cursor.moveToNext());
        }
    }
}
