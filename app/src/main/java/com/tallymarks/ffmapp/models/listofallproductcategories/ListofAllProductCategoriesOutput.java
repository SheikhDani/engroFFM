
package com.tallymarks.ffmapp.models.listofallproductcategories;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ListofAllProductCategoriesOutput {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("enabled")
    @Expose
    private Object enabled;
    @SerializedName("encodedImage")
    @Expose
    private Object encodedImage;
    @SerializedName("encodedImageType")
    @Expose
    private Object encodedImageType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Object getEnabled() {
        return enabled;
    }

    public void setEnabled(Object enabled) {
        this.enabled = enabled;
    }

    public Object getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(Object encodedImage) {
        this.encodedImage = encodedImage;
    }

    public Object getEncodedImageType() {
        return encodedImageType;
    }

    public void setEncodedImageType(Object encodedImageType) {
        this.encodedImageType = encodedImageType;
    }

}
