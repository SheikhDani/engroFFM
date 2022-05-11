
package com.tallymarks.engroffm.models.addapprovalrequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddLocationApproval {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("supervisorComments")
    @Expose
    private String supervisorComments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupervisorComments() {
        return supervisorComments;
    }

    public void setSupervisorComments(String supervisorComments) {
        this.supervisorComments = supervisorComments;
    }

}
