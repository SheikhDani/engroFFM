package com.tallymarks.ffmapp.models.farmerMeeting.local;

public class Farmer {

    String farmerID, farmerCode, farmerName;
    String farmersalespoint;

    public String getFarmersalespoint() {
        return farmersalespoint;
    }

    public void setFarmersalespoint(String farmersalespoint) {
        this.farmersalespoint = farmersalespoint;
    }

    public String getFarmerUserType() {
        return farmerUserType;
    }

    public void setFarmerUserType(String farmerUserType) {
        this.farmerUserType = farmerUserType;
    }

    public String getFarmerAcearage() {
        return farmerAcearage;
    }

    public void setFarmerAcearage(String farmerAcearage) {
        this.farmerAcearage = farmerAcearage;
    }

    public String getFarmerAreaCultivation() {
        return farmerAreaCultivation;
    }

    public void setFarmerAreaCultivation(String farmerAreaCultivation) {
        this.farmerAreaCultivation = farmerAreaCultivation;
    }

    String farmerUserType;
    String farmerAcearage;
    String farmerAreaCultivation;


    public Farmer(String farmerID, String farmerCode, String farmerName,String farmersalespoint,String farmerUserType,String farmerAcerage,String farmerAreaCultivation) {
        this.farmerID = farmerID;
        this.farmerCode = farmerCode;
        this.farmerName = farmerName;
        this.farmersalespoint = farmersalespoint;
        this.farmerAcearage = farmerAcerage;
        this.farmerUserType = farmerUserType;
        this.farmerAreaCultivation = farmerAreaCultivation;
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
