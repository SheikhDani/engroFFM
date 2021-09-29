package com.tallymarks.ffmapp.models;

public class TodayPlan {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemebrship() {
        return memebrship;
    }

    public void setMemebrship(String memebrship) {
        this.memebrship = memebrship;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomercode() {
        return customercode;
    }

    public void setCustomercode(String customercode) {
        this.customercode = customercode;
    }

    public String getSalespoint() {
        return salespoint;
    }

    public void setSalespoint(String salespoint) {
        this.salespoint = salespoint;
    }

    private String title;
    private String memebrship;
    private String time;
    private String customercode;
    private String salespoint;

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String mobilenumber;
    private String location;
}
