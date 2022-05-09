package com.tallymarks.engroffm.models.getFarmerTodayJourneyPlan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerTodayJourneyPlan {

    @SerializedName("farmerId")
    @Expose
    private Integer farmerId;
    @SerializedName("dayId")
    @Expose
    private Object dayId;
    @SerializedName("journeyPlanId")
    @Expose
    private Integer journeyPlanId;
    @SerializedName("farmerCode")
    @Expose
    private Object farmerCode;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longtitude")
    @Expose
    private Object longtitude;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("salesPoint")
    @Expose
    private String salesPoint;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Double getAreaCultivation() {
        return areaCultivation;
    }

    public void setAreaCultivation(Double areaCultivation) {
        this.areaCultivation = areaCultivation;
    }

    public Double getAcreage() {
        return acreage;
    }

    public void setAcreage(Double acreage) {
        this.acreage = acreage;
    }

    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("areaCultivation")
    @Expose
    private Double areaCultivation;
    @SerializedName("acreage")
    @Expose
    private Double acreage;

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public Object getDayId() {
        return dayId;
    }

    public void setDayId(Object dayId) {
        this.dayId = dayId;
    }

    public Integer getJourneyPlanId() {
        return journeyPlanId;
    }

    public void setJourneyPlanId(Integer journeyPlanId) {
        this.journeyPlanId = journeyPlanId;
    }

    public Object getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(Object farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Object longtitude) {
        this.longtitude = longtitude;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSalesPoint() {
        return salesPoint;
    }

    public void setSalesPoint(String salesPoint) {
        this.salesPoint = salesPoint;
    }
}
