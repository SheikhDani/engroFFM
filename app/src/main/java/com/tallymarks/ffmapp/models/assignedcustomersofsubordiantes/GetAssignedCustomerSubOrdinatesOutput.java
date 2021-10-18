
package com.tallymarks.ffmapp.models.assignedcustomersofsubordiantes;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetAssignedCustomerSubOrdinatesOutput{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("locationCode")
    @Expose
    private Object locationCode;
    @SerializedName("territory")
    @Expose
    private String territory;
    @SerializedName("salesPoint")
    @Expose
    private String salesPoint;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("proprietorName")
    @Expose
    private Object proprietorName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("stockQty")
    @Expose
    private Object stockQty;
    @SerializedName("lastFiveYearsAvgSales")
    @Expose
    private Object lastFiveYearsAvgSales;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Object getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Object locationCode) {
        this.locationCode = locationCode;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getSalesPoint() {
        return salesPoint;
    }

    public void setSalesPoint(String salesPoint) {
        this.salesPoint = salesPoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(Object proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Object getStockQty() {
        return stockQty;
    }

    public void setStockQty(Object stockQty) {
        this.stockQty = stockQty;
    }

    public Object getLastFiveYearsAvgSales() {
        return lastFiveYearsAvgSales;
    }

    public void setLastFiveYearsAvgSales(Object lastFiveYearsAvgSales) {
        this.lastFiveYearsAvgSales = lastFiveYearsAvgSales;
    }

}
