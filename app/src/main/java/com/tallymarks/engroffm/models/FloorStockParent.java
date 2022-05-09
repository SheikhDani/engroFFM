package com.tallymarks.engroffm.models;

import java.util.ArrayList;

public class FloorStockParent {
    public String name;
    public FloorStockParent (String Name)
    {
        this.name=Name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<FloorStockChild> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<FloorStockChild> players) {
        this.players = players;
    }

    public ArrayList<FloorStockChild> players=new ArrayList<FloorStockChild>();

}
