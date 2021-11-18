
package com.tallymarks.ffmapp.models.farmerdetailoutput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FarmerDetailOutput {

    @SerializedName("activities")
    @Expose
    private List<Activity> activities = null;
    @SerializedName("crops")
    @Expose
    private List<Crop> crops = null;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Crop> getCrops() {
        return crops;
    }

    public void setCrops(List<Crop> crops) {
        this.crops = crops;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
