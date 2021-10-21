
package com.tallymarks.ffmapp.models.getallFarmersplanoutput;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class FarmerCheckIn {

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
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("visitObjective")
    @Expose
    private String visitObjective;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("outletStatusId")
    @Expose
    private Integer outletStatusId;
    @SerializedName("checkInTimeStamp")
    @Expose
    private Long checkInTimeStamp;
    @SerializedName("checkInLatitude")
    @Expose
    private String checkInLatitude;
    @SerializedName("checkInLongitude")
    @Expose
    private String checkInLongitude;
    @SerializedName("activity")
    @Expose
    private Activity activity;
    @SerializedName("recommendations")
    @Expose
    private List<Recommendation> recommendations = null;
    @SerializedName("checkOutTimeStamp")
    @Expose
    private Long checkOutTimeStamp;
    @SerializedName("checkOutLatitude")
    @Expose
    private String checkOutLatitude;
    @SerializedName("checkOutLongitude")
    @Expose
    private String checkOutLongitude;
    @SerializedName("sampling")
    @Expose
    private List<Sampling> sampling = null;

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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getVisitObjective() {
        return visitObjective;
    }

    public void setVisitObjective(String visitObjective) {
        this.visitObjective = visitObjective;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOutletStatusId() {
        return outletStatusId;
    }

    public void setOutletStatusId(Integer outletStatusId) {
        this.outletStatusId = outletStatusId;
    }

    public Long getCheckInTimeStamp() {
        return checkInTimeStamp;
    }

    public void setCheckInTimeStamp(Long checkInTimeStamp) {
        this.checkInTimeStamp = checkInTimeStamp;
    }

    public String getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(String checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }

    public String getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(String checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public Long getCheckOutTimeStamp() {
        return checkOutTimeStamp;
    }

    public void setCheckOutTimeStamp(Long checkOutTimeStamp) {
        this.checkOutTimeStamp = checkOutTimeStamp;
    }

    public String getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(String checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }

    public String getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(String checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public List<Sampling> getSampling() {
        return sampling;
    }

    public void setSampling(List<Sampling> sampling) {
        this.sampling = sampling;
    }

}
