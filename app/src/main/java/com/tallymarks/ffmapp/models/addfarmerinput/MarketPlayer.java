
package com.tallymarks.ffmapp.models.addfarmerinput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MarketPlayer {

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
    @SerializedName("companyHeld")
    @Expose
    private Boolean companyHeld;
    @SerializedName("enabled")
    @Expose
    private Object enabled;

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

    public Boolean getCompanyHeld() {
        return companyHeld;
    }

    public void setCompanyHeld(Boolean companyHeld) {
        this.companyHeld = companyHeld;
    }

    public Object getEnabled() {
        return enabled;
    }

    public void setEnabled(Object enabled) {
        this.enabled = enabled;
    }

}
