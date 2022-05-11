
package com.tallymarks.engroffm.models.soilsamplingoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SoilSamplingLogsOutput {

    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("farmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("checkInTime")
    @Expose
    private String checkInTime;
    @SerializedName("checkOutTime")
    @Expose
    private String checkOutTime;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

}
