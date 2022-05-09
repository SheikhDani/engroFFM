
package com.tallymarks.engroffm.models.addfarmerinput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LandProfile {

    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("salesPointId")
    @Expose
    private Object salesPointId;
    @SerializedName("salesPointName")
    @Expose
    private String salesPointName;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("ownership")
    @Expose
    private String ownership;
    @SerializedName("waterSource")
    @Expose
    private String waterSource;
    @SerializedName("croppingPatterns")
    @Expose
    private List<CroppingPattern> croppingPatterns = null;
    @SerializedName("salesPointCode")
    @Expose
    private String salesPointCode;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Object getSalesPointId() {
        return salesPointId;
    }

    public void setSalesPointId(Object salesPointId) {
        this.salesPointId = salesPointId;
    }

    public String getSalesPointName() {
        return salesPointName;
    }

    public void setSalesPointName(String salesPointName) {
        this.salesPointName = salesPointName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public List<CroppingPattern> getCroppingPatterns() {
        return croppingPatterns;
    }

    public void setCroppingPatterns(List<CroppingPattern> croppingPatterns) {
        this.croppingPatterns = croppingPatterns;
    }

    public String getSalesPointCode() {
        return salesPointCode;
    }

    public void setSalesPointCode(String salesPointCode) {
        this.salesPointCode = salesPointCode;
    }

}
