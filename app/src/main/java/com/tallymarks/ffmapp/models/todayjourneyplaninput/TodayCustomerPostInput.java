
package com.tallymarks.ffmapp.models.todayjourneyplaninput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TodayCustomerPostInput {

    @SerializedName("customerId")
    @Expose
    private String customerId;
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
    private Object category;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("previousStockSnapshot")
    @Expose
    private List<PreviousStockSnapshot> previousStockSnapshot = null;
    @SerializedName("aggregates")
    @Expose
    private Aggregates aggregates;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("visitObjective")
    @Expose
    private String visitObjective;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("checkInTimeStamp")
    @Expose
    private Long checkInTimeStamp;
    @SerializedName("outletStatusId")
    @Expose
    private Integer outletStatusId;
    @SerializedName("checkInLatitude")
    @Expose
    private String checkInLatitude;
    @SerializedName("checkInLongitude")
    @Expose
    private String checkInLongitude;
    @SerializedName("stockSnapshot")
    @Expose
    private List<StockSnapshot> stockSnapshot = null;
    @SerializedName("stockSold")
    @Expose
    private List<StockSold__1> stockSold = null;
    @SerializedName("checkOutTimeStamp")
    @Expose
    private Long checkOutTimeStamp;
    @SerializedName("checkOutLatitude")
    @Expose
    private String checkOutLatitude;
    @SerializedName("checkOutLongitude")
    @Expose
    private String checkOutLongitude;
    @SerializedName("marketIntel")
    @Expose
    private MarketIntel marketIntel;
    @SerializedName("commitments")
    @Expose
    private List<Commitment> commitments = null;
    @SerializedName("productsDiscussed")
    @Expose
    private List<Integer> productsDiscussed = null;

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

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
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

    public Aggregates getAggregates() {
        return aggregates;
    }

    public void setAggregates(Aggregates aggregates) {
        this.aggregates = aggregates;
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

    public Long getCheckInTimeStamp() {
        return checkInTimeStamp;
    }

    public void setCheckInTimeStamp(Long checkInTimeStamp) {
        this.checkInTimeStamp = checkInTimeStamp;
    }

    public Integer getOutletStatusId() {
        return outletStatusId;
    }

    public void setOutletStatusId(Integer outletStatusId) {
        this.outletStatusId = outletStatusId;
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

    public List<StockSnapshot> getStockSnapshot() {
        return stockSnapshot;
    }

    public void setStockSnapshot(List<StockSnapshot> stockSnapshot) {
        this.stockSnapshot = stockSnapshot;
    }

    public List<StockSold__1> getStockSold() {
        return stockSold;
    }

    public void setStockSold(List<StockSold__1> stockSold) {
        this.stockSold = stockSold;
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

    public MarketIntel getMarketIntel() {
        return marketIntel;
    }

    public void setMarketIntel(MarketIntel marketIntel) {
        this.marketIntel = marketIntel;
    }

    public List<Commitment> getCommitments() {
        return commitments;
    }

    public void setCommitments(List<Commitment> commitments) {
        this.commitments = commitments;
    }

    public List<Integer> getProductsDiscussed() {
        return productsDiscussed;
    }

    public void setProductsDiscussed(List<Integer> productsDiscussed) {
        this.productsDiscussed = productsDiscussed;
    }

}
