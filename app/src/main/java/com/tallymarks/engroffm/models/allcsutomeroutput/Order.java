
package com.tallymarks.engroffm.models.allcsutomeroutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("brandName")
    @Expose
    private Object brandName;
    @SerializedName("orderQuantity")
    @Expose
    private Integer orderQuantity;
    @SerializedName("invoices")
    @Expose
    private List<Invoice> invoices = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Object getBrandName() {
        return brandName;
    }

    public void setBrandName(Object brandName) {
        this.brandName = brandName;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

}
