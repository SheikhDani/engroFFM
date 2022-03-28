
package com.tallymarks.engroffm.models.addfarmerinput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServingDealer {

    @SerializedName("marketPlayer")
    @Expose
    private MarketPlayer marketPlayer;
    @SerializedName("customerCode")
    @Expose
    private Object customerCode;
    @SerializedName("customerName")
    @Expose
    private String customerName;

    public MarketPlayer getMarketPlayer() {
        return marketPlayer;
    }

    public void setMarketPlayer(MarketPlayer marketPlayer) {
        this.marketPlayer = marketPlayer;
    }

    public Object getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Object customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
