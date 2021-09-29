package com.tallymarks.ffmapp.models;

import java.io.Serializable;

public class SalesOrderChild implements Serializable {
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public String getInvocieDate() {
        return invocieDate;
    }

    public void setInvocieDate(String invocieDate) {
        this.invocieDate = invocieDate;
    }

    String invoiceNumber;
    String invoiceQuantity;
    String  invocieDate;
}
