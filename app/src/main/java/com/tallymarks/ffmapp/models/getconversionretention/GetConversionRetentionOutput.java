
package com.tallymarks.ffmapp.models.getconversionretention;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetConversionRetentionOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parentActivity")
    @Expose
    private String parentActivity;
    @SerializedName("activityId")
    @Expose
    private String activityId;
    @SerializedName("zarkhezUser")
    @Expose
    private String zarkhezUser;
    @SerializedName("totalFarmSize")
    @Expose
    private Integer totalFarmSize;
    @SerializedName("cropAcreage")
    @Expose
    private Integer cropAcreage;
    @SerializedName("recommendedDosage")
    @Expose
    private Integer recommendedDosage;
    @SerializedName("recommendedAcreage")
    @Expose
    private Integer recommendedAcreage;
    @SerializedName("recommendedProductQty")
    @Expose
    private Integer recommendedProductQty;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("activityDate")
    @Expose
    private Object activityDate;
    @SerializedName("actualDosagePerAcre")
    @Expose
    private Integer actualDosagePerAcre;
    @SerializedName("actualAcreage")
    @Expose
    private Integer actualAcreage;
    @SerializedName("actualProductQty")
    @Expose
    private Integer actualProductQty;
    @SerializedName("targetAcreageRetention")
    @Expose
    private Integer targetAcreageRetention;
    @SerializedName("acreageForRetention")
    @Expose
    private Integer acreageForRetention;
    @SerializedName("acreageRetentionPercentage")
    @Expose
    private Integer acreageRetentionPercentage;
    @SerializedName("reasonForDeviation")
    @Expose
    private String reasonForDeviation;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("cropName")
    @Expose
    private String cropName;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("farmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(String parentActivity) {
        this.parentActivity = parentActivity;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getZarkhezUser() {
        return zarkhezUser;
    }

    public void setZarkhezUser(String zarkhezUser) {
        this.zarkhezUser = zarkhezUser;
    }

    public Integer getTotalFarmSize() {
        return totalFarmSize;
    }

    public void setTotalFarmSize(Integer totalFarmSize) {
        this.totalFarmSize = totalFarmSize;
    }

    public Integer getCropAcreage() {
        return cropAcreage;
    }

    public void setCropAcreage(Integer cropAcreage) {
        this.cropAcreage = cropAcreage;
    }

    public Integer getRecommendedDosage() {
        return recommendedDosage;
    }

    public void setRecommendedDosage(Integer recommendedDosage) {
        this.recommendedDosage = recommendedDosage;
    }

    public Integer getRecommendedAcreage() {
        return recommendedAcreage;
    }

    public void setRecommendedAcreage(Integer recommendedAcreage) {
        this.recommendedAcreage = recommendedAcreage;
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

    public Object getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Object activityDate) {
        this.activityDate = activityDate;
    }

    public Integer getActualDosagePerAcre() {
        return actualDosagePerAcre;
    }

    public void setActualDosagePerAcre(Integer actualDosagePerAcre) {
        this.actualDosagePerAcre = actualDosagePerAcre;
    }

    public Integer getActualAcreage() {
        return actualAcreage;
    }

    public void setActualAcreage(Integer actualAcreage) {
        this.actualAcreage = actualAcreage;
    }

    public Integer getActualProductQty() {
        return actualProductQty;
    }

    public void setActualProductQty(Integer actualProductQty) {
        this.actualProductQty = actualProductQty;
    }

    public Integer getTargetAcreageRetention() {
        return targetAcreageRetention;
    }

    public void setTargetAcreageRetention(Integer targetAcreageRetention) {
        this.targetAcreageRetention = targetAcreageRetention;
    }

    public Integer getAcreageForRetention() {
        return acreageForRetention;
    }

    public void setAcreageForRetention(Integer acreageForRetention) {
        this.acreageForRetention = acreageForRetention;
    }

    public Integer getAcreageRetentionPercentage() {
        return acreageRetentionPercentage;
    }

    public void setAcreageRetentionPercentage(Integer acreageRetentionPercentage) {
        this.acreageRetentionPercentage = acreageRetentionPercentage;
    }

    public String getReasonForDeviation() {
        return reasonForDeviation;
    }

    public void setReasonForDeviation(String reasonForDeviation) {
        this.reasonForDeviation = reasonForDeviation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
