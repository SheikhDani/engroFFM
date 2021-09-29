
package com.tallymarks.ffmapp.models.listofallrands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListofAllBrandsOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sapCode")
    @Expose
    private String sapCode;
    @SerializedName("marketPlayer")
    @Expose
    private String marketPlayer;
    @SerializedName("productDivision")
    @Expose
    private String productDivision;
    @SerializedName("productCategory")
    @Expose
    private String productCategory;
    @SerializedName("productGroup")
    @Expose
    private String productGroup;
    @SerializedName("productPackage")
    @Expose
    private String productPackage;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("productCategoryImage")
    @Expose
    private Object productCategoryImage;
    @SerializedName("productCategoryImageType")
    @Expose
    private Object productCategoryImageType;
    @SerializedName("fertAppType")
    @Expose
    private String fertAppType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public String getMarketPlayer() {
        return marketPlayer;
    }

    public void setMarketPlayer(String marketPlayer) {
        this.marketPlayer = marketPlayer;
    }

    public String getProductDivision() {
        return productDivision;
    }

    public void setProductDivision(String productDivision) {
        this.productDivision = productDivision;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getProductPackage() {
        return productPackage;
    }

    public void setProductPackage(String productPackage) {
        this.productPackage = productPackage;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Object getProductCategoryImage() {
        return productCategoryImage;
    }

    public void setProductCategoryImage(Object productCategoryImage) {
        this.productCategoryImage = productCategoryImage;
    }

    public Object getProductCategoryImageType() {
        return productCategoryImageType;
    }

    public void setProductCategoryImageType(Object productCategoryImageType) {
        this.productCategoryImageType = productCategoryImageType;
    }

    public String getFertAppType() {
        return fertAppType;
    }

    public void setFertAppType(String fertAppType) {
        this.fertAppType = fertAppType;
    }

}
