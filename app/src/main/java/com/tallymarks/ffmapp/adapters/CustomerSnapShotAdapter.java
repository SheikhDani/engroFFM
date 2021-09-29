package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerSnapShotAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<CustomerSnapShotParent> team;
    private LayoutInflater inflater;

    public CustomerSnapShotAdapter(Context c, ArrayList<CustomerSnapShotParent> team)
    {
        this.c=c;
        this.team=team;
        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).players.get(childPos);
    }

    //GET PLAYER ID
    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET PLAYER ROW
    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.list_child_item_customer_snapshot, null);
        }

        //GET CHILD/PLAYER NAME
        CustomerSnapShot t=(CustomerSnapShot)getChild(groupPos, childPos);

        //SET CHILD NAME
        TextView nameTv=(TextView) convertView.findViewById(R.id.productname);
        TextView quantity=(TextView) convertView.findViewById(R.id.product_quantity);

        nameTv.setText(t.getProductname());
        quantity.setText(t.getProductquantity());

        //GET TEAM NAME
        String teamName= getGroup(groupPos).toString();

        //ASSIGN IMAGES TO PLAYERS ACCORDING TO THEIR NAMES AN TEAMS



        return convertView;
    }

    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return team.get(groupPosw).players.size();
    }

    //GET TEAM
    @Override
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos);
    }

    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return team.size();
    }

    //GET TEAM ID
    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET TEAM ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if(convertView == null)
        {
            convertView=inflater.inflate(R.layout.list_grouop_items_customersnapshot, null);
        }

        //GET GROUP/TEAM ITEM
        CustomerSnapShotParent t=(CustomerSnapShotParent) getGroup(groupPosition);

        //SET GROUP NAME
        TextView nameTv=(TextView) convertView.findViewById(R.id.listTitle);
        ImageView img = convertView.findViewById(R.id.img);

        String name=t.name;
        nameTv.setText(name);
        if (isExpanded) {
            img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        } else {
           img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }

        //ASSIGN TEAM IMAGES ACCORDING TO TEAM NAME



        //SET TEAM ROW BACKGROUND COLOR


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }
}
