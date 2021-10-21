
package com.tallymarks.ffmapp.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recommendation {

    @SerializedName("cropId")
    @Expose
    private String cropId;
    @SerializedName("fertAppTypeId")
    @Expose
    private String fertAppTypeId;
    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("dosage")
    @Expose
    private Double dosage;

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getFertAppTypeId() {
        return fertAppTypeId;
    }

    public void setFertAppTypeId(String fertAppTypeId) {
        this.fertAppTypeId = fertAppTypeId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

}
