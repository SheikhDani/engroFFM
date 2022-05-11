package com.tallymarks.engroffm.models.farmerMeeting.local;

public class Crop {
    String cropID, cropName;

    public Crop(String cropID, String cropName) {
        this.cropID = cropID;
        this.cropName = cropName;
    }

    public String getCropID() {
        return cropID;
    }

    public void setCropID(String cropID) {
        this.cropID = cropID;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }
}
