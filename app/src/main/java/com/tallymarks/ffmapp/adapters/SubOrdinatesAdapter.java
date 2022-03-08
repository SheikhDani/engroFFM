package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.SubordinatListActivity;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Subordinates;
import com.tallymarks.ffmapp.utils.Constants;

import java.util.ArrayList;

public class SubOrdinatesAdapter extends ArrayAdapter {

    private ArrayList<Subordinates> dataSet;
    Context mContext;
    SharedPrefferenceHelper sHelper;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView imgForward;

    }

    public SubOrdinatesAdapter(ArrayList data, Context context) {
        super(context, R.layout.list_suboridnates_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Subordinates getItem(int position) {
        return  dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        SubOrdinatesAdapter.ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new SubOrdinatesAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_suboridnates_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.imgForward = convertView.findViewById(R.id.img_forward);


            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (SubOrdinatesAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        final Subordinates item = dataSet.get(position);


        viewHolder.txtName.setText(item.getName());
        viewHolder.imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sHelper.setString(Constants.SUBORDINATE_NAME,item.getName());
                sHelper.setString(Constants.SUBORDINATE_ID,item.getSubordianteid());
                Intent next = new Intent(mContext, SubordinatListActivity.class);
                next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(next);
            }
        });


        return result;
    }
}
