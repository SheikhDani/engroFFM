
package com.tallymarks.engroffm.models.snapshotforsupervisoroutput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PreviousSnapShotSupervisorOutput {

    @SerializedName("categoryEncodedImage")
    @Expose
    private String categoryEncodedImage;
    @SerializedName("encodedArtWorkExt")
    @Expose
    private String encodedArtWorkExt;
    @SerializedName("previousStock")
    @Expose
    private List<PreviousStock> previousStock = null;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("encodedArtWork")
    @Expose
    private String encodedArtWork;

    public String getCategoryEncodedImage() {
        return categoryEncodedImage;
    }

    public void setCategoryEncodedImage(String categoryEncodedImage) {
        this.categoryEncodedImage = categoryEncodedImage;
    }

    public String getEncodedArtWorkExt() {
        return encodedArtWorkExt;
    }

    public void setEncodedArtWorkExt(String encodedArtWorkExt) {
        this.encodedArtWorkExt = encodedArtWorkExt;
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

    public String getEncodedArtWork() {
        return encodedArtWork;
    }

    public void setEncodedArtWork(String encodedArtWork) {
        this.encodedArtWork = encodedArtWork;
    }

}
