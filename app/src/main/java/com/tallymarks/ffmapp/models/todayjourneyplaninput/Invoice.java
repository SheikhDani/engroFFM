
package com.tallymarks.ffmapp.models.todayjourneyplaninput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Invoice {

    @SerializedName("invoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("dispatchDate")
    @Expose
    private Long dispatchDate;
    @SerializedName("dispatchQuantity")
    @Expose
    private Integer dispatchQuantity;
    @SerializedName("availableQuantity")
    @Expose
    private Integer availableQuantity;
    @SerializedName("invoiceRate")
    @Expose
    private String invoiceRate;
    @SerializedName("stockSold")
    @Expose
    private List<StockSold> stockSold = null;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Long dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Integer getDispatchQuantity() {
        return dispatchQuantity;
    }

    public void setDispatchQuantity(Integer dispatchQuantity) {
        this.dispatchQuantity = dispatchQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getInvoiceRate() {
        return invoiceRate;
    }

    public void setInvoiceRate(String invoiceRate) {
        this.invoiceRate = invoiceRate;
    }

    public List<StockSold> getStockSold() {
        return stockSold;
    }

    public void setStockSold(List<StockSold> stockSold) {
        this.stockSold = stockSold;
    }

}
