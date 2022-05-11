package com.tallymarks.engroffm.models.farmerMeeting.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateFarmerMeetingResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("description")
    @Expose
    private String description;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
