package com.tallymarks.engroffm.models.devicetokeninput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceTokenInput {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    @Expose
    private String token;


}
