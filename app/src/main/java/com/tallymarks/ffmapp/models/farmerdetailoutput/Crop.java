
package com.tallymarks.ffmapp.models.farmerdetailoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Crop {

    @SerializedName("cropId")
    @Expose
    private String cropId;
    @SerializedName("cropName")
    @Expose
    private String cropName;

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

}
