package com.tallymarks.ffmapp.models;

public class ChangeLocation {
    private String latitude;

    public  String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String longitude;

    private String oldlongitude;

    public String getOldlongitude() {
        return oldlongitude;
    }

    public void setOldlongitude(String oldlongitude) {
        this.oldlongitude = oldlongitude;
    }

    public String getOldlatitude() {
        return oldlatitude;
    }

    public void setOldlatitude(String oldlatitude) {
        this.oldlatitude = oldlatitude;
    }

    private String oldlatitude;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private String reason;
    private String status;
    private String name;

    public String getLastvisitcount() {
        return lastvisitcount;
    }

    public void setLastvisitcount(String lastvisitcount) {
        this.lastvisitcount = lastvisitcount;
    }

    private String lastvisitcount;

    private String code;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


}
