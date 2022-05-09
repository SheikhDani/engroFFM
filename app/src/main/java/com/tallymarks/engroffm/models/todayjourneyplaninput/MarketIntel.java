
package com.tallymarks.engroffm.models.todayjourneyplaninput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MarketIntel {

    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("doForward")
    @Expose
    private Boolean doForward;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getDoForward() {
        return doForward;
    }

    public void setDoForward(Boolean doForward) {
        this.doForward = doForward;
    }

}
