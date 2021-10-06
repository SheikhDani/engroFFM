package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.SalesOrderMarketPriceActivity;
import com.tallymarks.ffmapp.models.SalesOrderChild;
import com.tallymarks.ffmapp.models.SalesOrderHeader;

import java.util.ArrayList;
import java.util.List;

public class SalesOrderMarketPriceAdapter  extends BaseExpandableListAdapter {
    private List<SalesOrderHeader> headerList;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context context;
    public int storepos = 0;


    ArrayList<SalesOrderHeader> arraylist;
    public SalesOrderMarketPriceAdapter(List<SalesOrderHeader> headerList, Context ctx) {
        this.context = ctx;
        this.itemLayoutId = R.layout.lv_sales_order_child;
        this.groupLayoutId = R.layout.lv_sales_order_grouop;
        this.headerList = headerList;
        this.arraylist = new ArrayList<SalesOrderHeader>();
        this.arraylist.addAll(headerList);

    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        int size = headerList.get(listPosition).getItemList().size();
        // System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return headerList.get(groupPosition).getItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return headerList.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return headerList.get(groupPosition).getItemList().get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View v = convertView;
        final SalesOrderMarketPriceAdapter.ViewHolder holder;
        if (v == null) {
            holder = new SalesOrderMarketPriceAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.lv_sales_order_grouop, parent, false);
            holder.order_no = v.findViewById(R.id.sales_order_no);
            holder.order_product = v.findViewById(R.id.sales_order_product);
            holder.order_date = v.findViewById(R.id.sales_order_date);
            holder.order_quanitiy = v.findViewById(R.id.sales_order_quanity);
            holder.img = v.findViewById(R.id.img);


            v.setTag(holder);

        }
        else
        {
            holder = (SalesOrderMarketPriceAdapter.ViewHolder) v.getTag();
        }
        final SalesOrderHeader header = headerList.get(listPosition);
        storepos = listPosition;
        holder.order_no.setText("Sales Order #"+ header.getOrderNo());
        holder.order_quanitiy.setText("Order Quantity: "+ header.getOrderQuantity());
        holder.order_date.setText("Dated: " +header.getOrderDate());
        holder.order_product.setText("Product: " +header.getOrderProduct());
        if (isExpanded) {
           holder.img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        } else {
            holder.img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }
        return v;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        final SalesOrderMarketPriceAdapter.ViewHolder holder;
        if (v == null) {
            holder = new SalesOrderMarketPriceAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.lv_sales_order_child, parent, false);
            holder.invoice_no = v.findViewById(R.id.invoice_no);
            holder.invocie_quanity = v.findViewById(R.id.invoice_quanity);
            holder.invoice_Date = v.findViewById(R.id.invocie_date);
            v.setTag(holder);

        }
        else
        {
            holder = (SalesOrderMarketPriceAdapter.ViewHolder) v.getTag();
        }

        SalesOrderChild child = headerList.get(listPosition).getItemList().get(expandedListPosition);
        holder.invoice_no.setText("Invoice # "+child.getInvoiceNumber());
        holder.invoice_Date.setText("Dated : "+child.getInvocieDate());
        holder.invocie_quanity.setText("Invoice Quantity : "+child.getInvoiceQuantity());
        return v;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
    public class ViewHolder {

        TextView order_no ;
        TextView order_date ;
        TextView order_product;
        TextView order_quanitiy;
        TextView invoice_no ;
        TextView invocie_quanity;
        TextView invoice_Date;
        ImageView img;
    }
}
