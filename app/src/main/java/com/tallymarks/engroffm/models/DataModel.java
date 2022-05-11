package com.tallymarks.engroffm.models;

public class DataModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String name,id;
    public boolean checked;

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int itemid;


//    public DataModel(String name, boolean checked,String id) {
//        this.name = name;
//        this.checked = checked;
//        this.id = id;
//
//    }
}
