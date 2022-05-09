
package com.tallymarks.engroffm.models.addfarmerinput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CroppingPattern {

    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("landHolding")
    @Expose
    private String landHolding;

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getLandHolding() {
        return landHolding;
    }

    public void setLandHolding(String landHolding) {
        this.landHolding = landHolding;
    }

}
