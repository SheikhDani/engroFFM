
package com.tallymarks.ffmapp.models.forgetpasswordinput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ForgetPasswordInput {

    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
