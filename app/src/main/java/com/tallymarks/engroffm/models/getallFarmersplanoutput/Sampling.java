
package com.tallymarks.engroffm.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sampling {

    @SerializedName("previousCropId")
    @Expose
    private String previousCropId;
    @SerializedName("depthId")
    @Expose
    private String depthId;
    @SerializedName("crop1Id")
    @Expose
    private String crop1Id;
    @SerializedName("crop2Id")
    @Expose
    private String crop2Id;
    @SerializedName("plotNumber")
    @Expose
    private String plotNumber;
    @SerializedName("blockNumber")
    @Expose
    private String blockNumber;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("reference")
    @Expose
    private String reference;

    public String getPreviousCropId() {
        return previousCropId;
    }

    public void setPreviousCropId(String previousCropId) {
        this.previousCropId = previousCropId;
    }

    public String getDepthId() {
        return depthId;
    }

    public void setDepthId(String depthId) {
        this.depthId = depthId;
    }

    public String getCrop1Id() {
        return crop1Id;
    }

    public void setCrop1Id(String crop1Id) {
        this.crop1Id = crop1Id;
    }

    public String getCrop2Id() {
        return crop2Id;
    }

    public void setCrop2Id(String crop2Id) {
        this.crop2Id = crop2Id;
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getLatitude() {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
