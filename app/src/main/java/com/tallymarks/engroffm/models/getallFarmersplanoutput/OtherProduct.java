
package com.tallymarks.engroffm.models.getallFarmersplanoutput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OtherProduct {


    @SerializedName("productId")
    @Expose
    private Integer productId;

    public Integer getOtherPacksLiquidated() {
        return otherPacksLiquidated;
    }

    public void setOtherPacksLiquidated(Integer otherPacksLiquidated) {
        this.otherPacksLiquidated = otherPacksLiquidated;
    }

    @SerializedName("otherPacksLiquidated")
    @Expose
    private Integer otherPacksLiquidated;





    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
