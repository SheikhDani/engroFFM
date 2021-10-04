package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.MarketPricesActivity;
import com.tallymarks.ffmapp.activities.QualityofSalesCallActivity;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.FloorStockParent;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<FloorStockChild> team;

    public GridAdapter(Context c, List<FloorStockChild> team) {
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
    SparseBooleanArray booleanArray = new SparseBooleanArray();
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // TODO Auto-generated method stub
//        View  grid = new View(mContext);;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.card_items, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_product = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.textView_quanity = (EditText)convertView.findViewById(R.id.txt_quanity);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FloorStockChild e = team.get(position);
//        TextView txt_product= (TextView) convertView.findViewById(R.id.textView);
//        final TextView textView_quanity = (TextView) convertView.findViewById(R.id.txt_quanity);
        viewHolder.txt_product.setText(e.getProductname());
        viewHolder.textView_quanity.setText(e.getProductinput());
        viewHolder.textView_quanity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                e.setProductinput(viewHolder.textView_quanity.getText().toString());

            }
        });
//        viewHolder.textView_quanity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                e.setProductinput(viewHolder.textView_quanity.getText().toString());
//
//                //openQuanityDialouge(viewHolder.textView_quanity,e);
//            }
//        });



        if(booleanArray.get(position)){
            convertView.setActivated(true);
        } else {
            convertView.setActivated(false);
        }

        return convertView;
    }
    static class ViewHolder
    {
        TextView txt_product ,textView_quanity ;
    }

    private void openQuanityDialouge(TextView txt,FloorStockChild e) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialouge_edittetext_input, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText etInput = promptsView.findViewById(R.id.et_input);
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        Button btnYes = promptsView.findViewById(R.id.btn_add_quanityt);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                txt.setText(etInput.getText().toString());
                e.setProductinput(etInput.getText().toString());


            }


        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

}
