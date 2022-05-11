
package com.tallymarks.engroffm.models.todayjourneyplaninput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PreviousStock {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("visitDate")
    @Expose
    private Long visitDate;
    @SerializedName("companyHeld")
    @Expose
    private Boolean companyHeld;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

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

    public Long getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Long visitDate) {
        this.visitDate = visitDate;
    }

    public Boolean getCompanyHeld() {
        return companyHeld;
    }

    public void setCompanyHeld(Boolean companyHeld) {
        this.companyHeld = companyHeld;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
