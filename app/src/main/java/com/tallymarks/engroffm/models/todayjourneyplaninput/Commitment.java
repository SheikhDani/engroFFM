
package com.tallymarks.engroffm.models.todayjourneyplaninput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Commitment {

    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("deliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

}
