package com.tallymarks.ffmapp.models;

public class Activity {

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    String activityId;

    public Activity (String branchID) {

        this.activityId = branchID;
    }
}
