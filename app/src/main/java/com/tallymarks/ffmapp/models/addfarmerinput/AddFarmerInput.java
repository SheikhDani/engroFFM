
package com.tallymarks.ffmapp.models.addfarmerinput;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddFarmerInput {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("landlineNumber")
    @Expose
    private String landlineNumber;
    @SerializedName("cnicNumber")
    @Expose
    private String cnicNumber;



    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("genderId")
    @Expose
    private Integer genderId;
    @SerializedName("salesPointCode")
    @Expose
    private String salesPointCode;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("landProfiles")
    @Expose
    private List<LandProfile> landProfiles = null;
    @SerializedName("servingDealers")
    @Expose
    private List<ServingDealer> servingDealers = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLandlineNumber() {
        return landlineNumber;
    }

    public void setLandlineNumber(String landlineNumber) {
        this.landlineNumber = landlineNumber;
    }

    public String getCnicNumber() {
        return cnicNumber;
    }

    public void setCnicNumber(String cnicNumber) {
        this.cnicNumber = cnicNumber;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public String getSalesPointCode() {
        return salesPointCode;
    }

    public void setSalesPointCode(String salesPointCode) {
        this.salesPointCode = salesPointCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public List<LandProfile> getLandProfiles() {
        return landProfiles;
    }

    public void setLandProfiles(List<LandProfile> landProfiles) {
        this.landProfiles = landProfiles;
    }

    public List<ServingDealer> getServingDealers() {
        return servingDealers;
    }

    public void setServingDealers(List<ServingDealer> servingDealers) {
        this.servingDealers = servingDealers;
    }

}
