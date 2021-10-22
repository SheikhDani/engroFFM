package com.tallymarks.ffmapp.models.farmerMeeting.local;

public class Farmer {

    String farmerID, farmerCode, farmerName;

    public Farmer(String farmerID, String farmerCode, String farmerName) {
        this.farmerID = farmerID;
        this.farmerCode = farmerCode;
        this.farmerName = farmerName;
    }

    public String getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }
}
