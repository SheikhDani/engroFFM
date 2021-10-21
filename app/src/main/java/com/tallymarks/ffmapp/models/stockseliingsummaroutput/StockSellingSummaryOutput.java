
package com.tallymarks.ffmapp.models.stockseliingsummaroutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StockSellingSummaryOutput {

    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("invoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("quantitySold")
    @Expose
    private Integer quantitySold;
    @SerializedName("netSellingPrice")
    @Expose
    private Double netSellingPrice;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("visitDate")
    @Expose
    private Long visitDate;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getNetSellingPrice() {
        return netSellingPrice;
    }

    public void setNetSellingPrice(Double netSellingPrice) {
        this.netSellingPrice = netSellingPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Long visitDate) {
        this.visitDate = visitDate;
    }

}
