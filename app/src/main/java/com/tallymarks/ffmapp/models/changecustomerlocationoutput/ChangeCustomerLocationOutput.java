package com.tallymarks.ffmapp.models.changecustomerlocationoutput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeCustomerLocationOutput {
    @SerializedName("customerId")
    @Expose
    private Long customerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("latitude")
    @Expose
    private double latitude;

    public int getLastVisitCount() {
        return lastVisitCount;
    }

    public void setLastVisitCount(int lastVisitCount) {
        this.lastVisitCount = lastVisitCount;
    }

    @SerializedName("lastVisitCount")
    @Expose
    private int lastVisitCount;

    public double getOldLatitude() {
        return oldLatitude;
    }

    public void setOldLatitude(double oldLatitude) {
        this.oldLatitude = oldLatitude;
    }

    @SerializedName("oldLatitude")
    @Expose
    private double oldLatitude;

    public double getOldLongitude() {
        return oldLongitude;
    }

    public void setOldLongitude(double oldLongitude) {
        this.oldLongitude = oldLongitude;
    }

    @SerializedName("oldLongitude")
    @Expose
    private double oldLongitude;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("customerCustomerName")
    @Expose
    private String customerName;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @SerializedName("creationDate")
    @Expose
    private String creationDate;
}
