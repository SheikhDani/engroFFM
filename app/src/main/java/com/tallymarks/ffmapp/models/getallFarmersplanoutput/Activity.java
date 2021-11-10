
package com.tallymarks.ffmapp.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Activity {


    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cropAcreage")
    @Expose
    private Integer cropAcreage;
    @SerializedName("cropDeficiency")
    @Expose
    private Integer cropDeficiency;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("id")
    @Expose
    private Integer id;

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

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("mainProduct")
    @Expose
    private Integer mainProduct;
    @SerializedName("otherProducts")
    @Expose
    private List<OtherProduct> otherProducts = null;
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
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
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



    public Integer getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(Integer mainProduct) {
        this.mainProduct = mainProduct;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
