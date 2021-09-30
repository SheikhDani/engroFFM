
package com.tallymarks.ffmapp.models.todayjourneyplanoutput;

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

}
