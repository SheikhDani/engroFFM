
package com.tallymarks.engroffm.models.addconversioninput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddConversioninput {

    @SerializedName("acreageForRetention")
    @Expose
    private Integer acreageForRetention;
    @SerializedName("acreageRetentionPercentage")
    @Expose
    private float acreageRetentionPercentage;
    @SerializedName("activityDate")
    @Expose
    private String activityDate;
    @SerializedName("activityId")
    @Expose
    private String activityId;
    @SerializedName("actualAcreage")
    @Expose
    private Integer actualAcreage;
    @SerializedName("actualDosagePerAcre")
    @Expose
    private Integer actualDosagePerAcre;
    @SerializedName("actualProductQty")
    @Expose
    private Integer actualProductQty;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cropAcreage")
    @Expose
    private Double cropAcreage;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("cropName")
    @Expose
    private String cropName;
    @SerializedName("farmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("parentActivity")
    @Expose
    private String parentActivity;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("reasonForDeviation")
    @Expose
    private String reasonForDeviation;
    @SerializedName("recommendedAcreage")
    @Expose
    private Integer recommendedAcreage;
    @SerializedName("recommendedDosage")
    @Expose
    private Integer recommendedDosage;
    @SerializedName("recommendedProductQty")
    @Expose
    private Integer recommendedProductQty;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("targetAcreageRetention")
    @Expose
    private Integer targetAcreageRetention;
    @SerializedName("totalFarmSize")
    @Expose
    private Double totalFarmSize;
    @SerializedName("zarkhezUser")
    @Expose
    private String zarkhezUser;

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    @SerializedName("activityCode")
    @Expose
    private String activityCode;

    public Integer getAcreageForRetention() {
        return acreageForRetention;
    }

    public void setAcreageForRetention(Integer acreageForRetention) {
        this.acreageForRetention = acreageForRetention;
    }

    public float getAcreageRetentionPercentage() {
        return acreageRetentionPercentage;
    }

    public void setAcreageRetentionPercentage(float acreageRetentionPercentage) {
        this.acreageRetentionPercentage = acreageRetentionPercentage;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Integer getActualAcreage() {
        return actualAcreage;
    }

    public void setActualAcreage(Integer actualAcreage) {
        this.actualAcreage = actualAcreage;
    }

    public Integer getActualDosagePerAcre() {
        return actualDosagePerAcre;
    }

    public void setActualDosagePerAcre(Integer actualDosagePerAcre) {
        this.actualDosagePerAcre = actualDosagePerAcre;
    }

    public Integer getActualProductQty() {
        return actualProductQty;
    }

    public void setActualProductQty(Integer actualProductQty) {
        this.actualProductQty = actualProductQty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCropAcreage() {
        return cropAcreage;
    }

    public void setCropAcreage(Double cropAcreage) {
        this.cropAcreage = cropAcreage;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
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

    public String getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(String parentActivity) {
        this.parentActivity = parentActivity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReasonForDeviation() {
        return reasonForDeviation;
    }

    public void setReasonForDeviation(String reasonForDeviation) {
        this.reasonForDeviation = reasonForDeviation;
    }

    public Integer getRecommendedAcreage() {
        return recommendedAcreage;
    }

    public void setRecommendedAcreage(Integer recommendedAcreage) {
        this.recommendedAcreage = recommendedAcreage;
    }

    public Integer getRecommendedDosage() {
        return recommendedDosage;
    }

    public void setRecommendedDosage(Integer recommendedDosage) {
        this.recommendedDosage = recommendedDosage;
    }

    public Integer getRecommendedProductQty() {
        return recommendedProductQty;
    }

    public void setRecommendedProductQty(Integer recommendedProductQty) {
        this.recommendedProductQty = recommendedProductQty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTargetAcreageRetention() {
        return targetAcreageRetention;
    }

    public void setTargetAcreageRetention(Integer targetAcreageRetention) {
        this.targetAcreageRetention = targetAcreageRetention;
    }

    public Double getTotalFarmSize() {
        return totalFarmSize;
    }

    public void setTotalFarmSize(Double totalFarmSize) {
        this.totalFarmSize = totalFarmSize;
    }

    public String getZarkhezUser() {
        return zarkhezUser;
    }

    public void setZarkhezUser(String zarkhezUser) {
        this.zarkhezUser = zarkhezUser;
    }

}
