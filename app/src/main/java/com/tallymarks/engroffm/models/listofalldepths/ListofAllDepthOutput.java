
package com.tallymarks.engroffm.models.listofalldepths;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ListofAllDepthOutput {

    @SerializedName("longDescription")
    @Expose
    private String longDescription;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("depthFrom")
    @Expose
    private Double depthFrom;
    @SerializedName("depthTo")
    @Expose
    private Double depthTo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getDepthFrom() {
        return depthFrom;
    }

    public void setDepthFrom(Double depthFrom) {
        this.depthFrom = depthFrom;
    }

    public Double getDepthTo() {
        return depthTo;
    }

    public void setDepthTo(Double depthTo) {
        this.depthTo = depthTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
