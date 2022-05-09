
package com.tallymarks.engroffm.models.changecustomerlocation;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChnageCustomerLocationinput {

    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
