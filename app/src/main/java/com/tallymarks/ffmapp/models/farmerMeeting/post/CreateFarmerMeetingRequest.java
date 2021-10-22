package com.tallymarks.ffmapp.models.farmerMeeting.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateFarmerMeetingRequest {

    @SerializedName("activityDate")
    @Expose
    private String activityDate;
    @SerializedName("attachements")
    @Expose
    private List<String> attachements = null;
    @SerializedName("chiefGuest")
    @Expose
    private String chiefGuest;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("dealers")
    @Expose
    private List<Dealer> dealers = null;
    @SerializedName("employeesAttended")
    @Expose
    private String employeesAttended;
    @SerializedName("expenses")
    @Expose
    private String expenses;
    @SerializedName("hostFarmerId")
    @Expose
    private Integer hostFarmerId;
    @SerializedName("meetingAddress")
    @Expose
    private String meetingAddress;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("totalCustomers")
    @Expose
    private Integer totalCustomers;
    @SerializedName("totalFarmers")
    @Expose
    private Integer totalFarmers;

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public List<String> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<String> attachements) {
        this.attachements = attachements;
    }

    public String getChiefGuest() {
        return chiefGuest;
    }

    public void setChiefGuest(String chiefGuest) {
        this.chiefGuest = chiefGuest;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public List<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    public String getEmployeesAttended() {
        return employeesAttended;
    }

    public void setEmployeesAttended(String employeesAttended) {
        this.employeesAttended = employeesAttended;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public Integer getHostFarmerId() {
        return hostFarmerId;
    }

    public void setHostFarmerId(Integer hostFarmerId) {
        this.hostFarmerId = hostFarmerId;
    }

    public String getMeetingAddress() {
        return meetingAddress;
    }

    public void setMeetingAddress(String meetingAddress) {
        this.meetingAddress = meetingAddress;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Integer totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Integer getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(Integer totalFarmers) {
        this.totalFarmers = totalFarmers;
    }
}
