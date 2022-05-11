package com.tallymarks.engroffm.models;

import java.util.ArrayList;

public class CustomerSnapShotParent {
    public String name;
    public CustomerSnapShotParent (String Name)
    {
        this.name=Name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<CustomerSnapShot> players=new ArrayList<CustomerSnapShot>();

}
