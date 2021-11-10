
package com.tallymarks.ffmapp.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OtherProduct {


    @SerializedName("productId")
    @Expose
    private Integer productId;



    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
