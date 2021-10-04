package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.FloorStockActivity;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.FloorStockChild;
import com.tallymarks.ffmapp.models.FloorStockParent;

import java.util.ArrayList;
import java.util.List;

public class FloorStockAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<FloorStockParent> team;
    private LayoutInflater inflater;
    ExpandableListView elv;

    public FloorStockAdapter(Context c, ArrayList<FloorStockParent> team, ExpandableListView elv) {
        this.c = c;
        this.team = team;
        this.elv = elv;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_child_items_floor_stock, null);
        }

        FloorStockChild t = (FloorStockChild) getChild(groupPos, childPos);
        ImageView image = (ImageView) convertView.findViewById(R.id.productimage);
        GridView grid = (GridView) convertView.findViewById(R.id.gridview);
        GridAdapter gridadapter;

        if (t.getImageurl() != null && !t.getImageurl().equals("")) {
            image.setImageResource(R.drawable.ic_launcher_foreground);
            image.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);
            gridadapter = new GridAdapter(c, team.get(groupPos).getPlayers());
            grid.setAdapter(gridadapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                }
            });
        } else {
            image.setVisibility(View.GONE);
            grid.setVisibility(View.GONE);
            //gridadapter.notifyDataSetChanged();
        }


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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_group_items_floor_stock, null);
        }

        //GET GROUP/TEAM ITEM
        FloorStockParent t = (FloorStockParent) getGroup(groupPosition);

        //SET GROUP NAME
        TextView nameTv = (TextView) convertView.findViewById(R.id.listTitle);

        String name = t.name;
        nameTv.setText(name);
        elv.expandGroup(groupPosition);

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
