
package com.tallymarks.engroffm.models.productsbrandbycategory;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("divisionCode")
    @Expose
    private String divisionCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("companyHeld")
    @Expose
    private Boolean companyHeld;

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCompanyHeld() {
        return companyHeld;
    }

    public void setCompanyHeld(Boolean companyHeld) {
        this.companyHeld = companyHeld;
    }

}
