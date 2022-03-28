
package com.tallymarks.engroffm.models.getallcustomersplanoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Aggregates {

    @SerializedName("avgSale")
    @Expose
    private Object avgSale;
    @SerializedName("lastCommittment")
    @Expose
    private String lastCommittment;
    @SerializedName("mtdSale")
    @Expose
    private Object mtdSale;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("pendingOrders")
    @Expose
    private Object pendingOrders;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("lastVisitDate")
    @Expose
    private String lastVisitDate;
    @SerializedName("ytdSale")
    @Expose
    private Object ytdSale;
    @SerializedName("lastFloorStock")
    @Expose
    private String lastFloorStock;

    public Object getAvgSale() {
        return avgSale;
    }

    public void setAvgSale(Object avgSale) {
        this.avgSale = avgSale;
    }

    public String getLastCommittment() {
        return lastCommittment;
    }

    public void setLastCommittment(String lastCommittment) {
        this.lastCommittment = lastCommittment;
    }

    public Object getMtdSale() {
        return mtdSale;
    }

    public void setMtdSale(Object mtdSale) {
        this.mtdSale = mtdSale;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Object getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Object pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public Object getYtdSale() {
        return ytdSale;
    }

    public void setYtdSale(Object ytdSale) {
        this.ytdSale = ytdSale;
    }

    public String getLastFloorStock() {
        return lastFloorStock;
    }

    public void setLastFloorStock(String lastFloorStock) {
        this.lastFloorStock = lastFloorStock;
    }

}
