package com.tallymarks.ffmapp.models;

public class Subordinates {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getSubordianteid() {
        return subordianteid;
    }

    public void setSubordianteid(String subordianteid) {
        this.subordianteid = subordianteid;
    }

    public String getHasjourneyplan() {
        return hasjourneyplan;
    }

    public void setHasjourneyplan(String hasjourneyplan) {
        this.hasjourneyplan = hasjourneyplan;
    }

    public String getNoofjourneyplan() {
        return noofjourneyplan;
    }

    public void setNoofjourneyplan(String noofjourneyplan) {
        this.noofjourneyplan = noofjourneyplan;
    }

    private String subordianteid;
    private String hasjourneyplan;
    private String noofjourneyplan;
}
