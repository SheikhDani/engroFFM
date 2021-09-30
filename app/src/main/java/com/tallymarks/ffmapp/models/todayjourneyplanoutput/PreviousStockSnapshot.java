
package com.tallymarks.ffmapp.models.todayjourneyplanoutput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PreviousStockSnapshot {

    @SerializedName("categoryEncodedImage")
    @Expose
    private String categoryEncodedImage;
    @SerializedName("previousStock")
    @Expose
    private List<PreviousStock> previousStock = null;
    @SerializedName("category")
    @Expose
    private String category;

    public String getCategoryEncodedImage() {
        return categoryEncodedImage;
    }

    public void setCategoryEncodedImage(String categoryEncodedImage) {
        this.categoryEncodedImage = categoryEncodedImage;
    }

    public List<PreviousStock> getPreviousStock() {
        return previousStock;
    }

    public void setPreviousStock(List<PreviousStock> previousStock) {
        this.previousStock = previousStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
