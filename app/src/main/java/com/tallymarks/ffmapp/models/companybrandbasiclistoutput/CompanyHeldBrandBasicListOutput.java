
package com.tallymarks.ffmapp.models.companybrandbasiclistoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CompanyHeldBrandBasicListOutput {

    @SerializedName("divisionCode")
    @Expose
    private String divisionCode;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("fertAppType")
    @Expose
    private Integer fertAppType;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getFertAppType() {
        return fertAppType;
    }

    public void setFertAppType(Integer fertAppType) {
        this.fertAppType = fertAppType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
