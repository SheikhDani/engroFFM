
package com.tallymarks.ffmapp.models.postcustomerlocation;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostCustomerLocationInput {

    @SerializedName("customerCustomerName")
    @Expose
    private String customerCustomerName;
    @SerializedName("customerId")
    @Expose
    private Long customerId;

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    @SerializedName("locationStatus")
    @Expose
    private String locationStatus;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public Double getOldLatitude() {
        return oldLatitude;
    }

    public void setOldLatitude(Double oldLatitude) {
        this.oldLatitude = oldLatitude;
    }

    @SerializedName("oldLatitude")
    @Expose
    private Double oldLatitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Double getOldLongitude() {
        return oldLongitude;
    }

    public void setOldLongitude(Double oldLongitude) {
        this.oldLongitude = oldLongitude;
    }

    @SerializedName("oldLongitude")
    @Expose
    private Double oldLongitude;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("status")
    @Expose
    private String status;

    public int getLastvisitcount() {
        return lastvisitcount;
    }

    public void setLastvisitcount(int lastvisitcount) {
        this.lastvisitcount = lastvisitcount;
    }

    @SerializedName("lastVisitCount")
    @Expose
    private int lastvisitcount;

    public String getDifferenceInDistance() {
        return differenceInDistance;
    }

    public void setDifferenceInDistance(String differenceInDistance) {
        this.differenceInDistance = differenceInDistance;
    }

    @SerializedName("differenceInDistance")
    @Expose
    private String differenceInDistance;

    public String getCustomerCustomerName() {
        return customerCustomerName;
    }

    public void setCustomerCustomerName(String customerCustomerName) {
        this.customerCustomerName = customerCustomerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
