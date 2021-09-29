package com.tallymarks.ffmapp.models;

public class PlanPast {
    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getApprovalstatus() {
        return approvalstatus;
    }

    public void setApprovalstatus(String approvalstatus) {
        this.approvalstatus = approvalstatus;
    }

    public String getDealertime() {
        return dealertime;
    }

    public void setDealertime(String dealertime) {
        this.dealertime = dealertime;
    }

    private String plandate,approvalstatus,dealertime;
}
