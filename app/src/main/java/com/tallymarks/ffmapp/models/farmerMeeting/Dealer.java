
package com.tallymarks.ffmapp.models.farmerMeeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dealer {

    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;

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

}
