
package com.tallymarks.engroffm.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Activity {

    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mainProduct")
    @Expose
    private Integer mainProduct;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("cropAcreage")
    @Expose
    private Integer cropAcreage;
    @SerializedName("cropDeficiency")
    @Expose
    private Integer cropDeficiency;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("otherProducts")
    @Expose
    private List<OtherProduct> otherProducts = null;

    public Integer getCropAcreage() {
        return cropAcreage;
    }

    public void setCropAcreage(Integer cropAcreage) {
        this.cropAcreage = cropAcreage;
    }

    public Integer getCropDeficiency() {
        return cropDeficiency;
    }

    public void setCropDeficiency(Integer cropDeficiency) {
        this.cropDeficiency = cropDeficiency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OtherProduct> getOtherProducts() {
        return otherProducts;
    }

    public void setOtherProducts(List<OtherProduct> otherProducts) {
        this.otherProducts = otherProducts;
    }

    public Integer getPacksLiquidated() {
        return packsLiquidated;
    }

    public void setPacksLiquidated(Integer packsLiquidated) {
        this.packsLiquidated = packsLiquidated;
    }

    @SerializedName("packsLiquidated")
    @Expose
    private Integer packsLiquidated;

    public Integer getOtherPacksLiquidated() {
        return otherPacksLiquidated;
    }

    public void setOtherPacksLiquidated(Integer otherPacksLiquidated) {
        this.otherPacksLiquidated = otherPacksLiquidated;
    }

    @SerializedName("otherPacksLiquidated")
    @Expose
    private Integer otherPacksLiquidated;


    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(Integer mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
