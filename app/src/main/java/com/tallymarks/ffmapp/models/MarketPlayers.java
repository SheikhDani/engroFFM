package com.tallymarks.ffmapp.models;

public class MarketPlayers {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCompanyheld() {
        return companyheld;
    }

    public void setCompanyheld(String companyheld) {
        this.companyheld = companyheld;
    }

    public String getEnaled() {
        return enaled;
    }

    public void setEnaled(String enaled) {
        this.enaled = enaled;
    }

    String id , code,name,desc,companyheld,enaled;
}
