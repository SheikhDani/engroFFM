
package com.tallymarks.ffmapp.models.allcsutomeroutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StockSold {

    @SerializedName("quantitySold")
    @Expose
    private String quantitySold;
    @SerializedName("netSellingPrice")
    @Expose
    private String netSellingPrice;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("invoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("sameInvoice")
    @Expose
    private Boolean sameInvoice;
    @SerializedName("old")
    @Expose
    private Boolean old;

    public String getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(String quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getNetSellingPrice() {
        return netSellingPrice;
    }

    public void setNetSellingPrice(String netSellingPrice) {
        this.netSellingPrice = netSellingPrice;
    }

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getSameInvoice() {
        return sameInvoice;
    }

    public void setSameInvoice(Boolean sameInvoice) {
        this.sameInvoice = sameInvoice;
    }

    public Boolean getOld() {
        return old;
    }

    public void setOld(Boolean old) {
        this.old = old;
    }

}
