
package com.tallymarks.ffmapp.models.productsbrandbycategory;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductBrandByCategoryOutput {
    @SerializedName("encodedArtWorkExt")
    @Expose
    private String encodedArtWorkExt;
    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("encodedArtWork")
    @Expose
    private String encodedArtWork;

    public String getEncodedArtWorkExt() {
        return encodedArtWorkExt;
    }

    public void setEncodedArtWorkExt(String encodedArtWorkExt) {
        this.encodedArtWorkExt = encodedArtWorkExt;
    }
    public String getEncodedArtWork() {
        return encodedArtWork;
    }

    public void setEncodedArtWork(String encodedArtWork) {
        this.encodedArtWork = encodedArtWork;
    }
    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
