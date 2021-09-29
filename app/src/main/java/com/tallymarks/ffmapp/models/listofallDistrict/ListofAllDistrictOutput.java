
package com.tallymarks.ffmapp.models.listofallDistrict;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListofAllDistrictOutput {

    @SerializedName("districtCode")
    @Expose
    private String districtCode;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
