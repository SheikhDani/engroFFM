package com.tallymarks.engroffm.models;

import java.util.ArrayList;

public class SupervisorSnapshotParent {
    public SupervisorSnapshotParent (String Name)
    {
        this.name=Name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public ArrayList<SuperVisorSnapShotChild> players=new ArrayList<SuperVisorSnapShotChild>();

}
