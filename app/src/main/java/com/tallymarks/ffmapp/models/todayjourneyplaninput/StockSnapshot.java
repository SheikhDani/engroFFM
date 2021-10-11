
package com.tallymarks.ffmapp.models.todayjourneyplaninput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StockSnapshot {

    @SerializedName("brandId")
    @Expose
    private Integer brandId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
