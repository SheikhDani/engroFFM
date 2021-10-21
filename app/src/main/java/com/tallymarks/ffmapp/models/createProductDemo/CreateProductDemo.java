package com.tallymarks.ffmapp.models.createProductDemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateProductDemo {
    @SerializedName("activityDate")
    @Expose
    private String activityDate;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("objective")
    @Expose
    private String objective;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("status")
    @Expose
    private String status;

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
