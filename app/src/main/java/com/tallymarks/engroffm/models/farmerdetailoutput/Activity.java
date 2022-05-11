
package com.tallymarks.engroffm.models.farmerdetailoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Activity {

    @SerializedName("activityId")
    @Expose
    private String activityId;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

}
