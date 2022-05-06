
package com.tallymarks.ffmapp.models.allcsutomeroutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllJourneyPlanOutput {

    @SerializedName("customerId")
    @Expose
    private String customerId;

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    @SerializedName("locationStatus")
    @Expose
    private String locationStatus;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longtitude")
    @Expose
    private Double longtitude;
    @SerializedName("dayId")
    @Expose
    private Integer dayId;
    @SerializedName("journeyPlanId")
    @Expose
    private Integer journeyPlanId;
    @SerializedName("salePointName")
    @Expose
    private String salePointName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;


    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("previousStockSnapshot")
    @Expose
    private List<PreviousStockSnapshot> previousStockSnapshot = null;
    @SerializedName("stockSold")
    @Expose
    private List<StockSold> stockSold = null;
    @SerializedName("aggregates")


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getJourneyPlanId() {
        return journeyPlanId;
    }

    public void setJourneyPlanId(Integer journeyPlanId) {
        this.journeyPlanId = journeyPlanId;
    }

    public String getSalePointName() {
        return salePointName;
    }

    public void setSalePointName(String salePointName) {
        this.salePointName = salePointName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<PreviousStockSnapshot> getPreviousStockSnapshot() {
        return previousStockSnapshot;
    }

    public void setPreviousStockSnapshot(List<PreviousStockSnapshot> previousStockSnapshot) {
        this.previousStockSnapshot = previousStockSnapshot;
    }
    public List<StockSold> getStockSold() {
        return stockSold;
    }

    public void setStockSold(List<StockSold> stockSold) {
        this.stockSold = stockSold;
    }


}
