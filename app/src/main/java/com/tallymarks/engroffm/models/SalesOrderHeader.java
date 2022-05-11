package com.tallymarks.engroffm.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderHeader implements Serializable {
    String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(String orderProduct) {
        this.orderProduct = orderProduct;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    String orderDate;
    String orderProduct;
    String orderQuantity;


    private List<SalesOrderChild> itemList = new ArrayList<SalesOrderChild>();

    public SalesOrderHeader(String orderNo, String orderDate, String orderProduct, String orderQuantity) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.orderProduct = orderProduct;
        this.orderQuantity = orderQuantity;

    }

    public List<SalesOrderChild> getItemList() {
        return itemList;
    }

    public void setItemList(List<SalesOrderChild> itemList) {
        this.itemList = itemList;
    }

}
