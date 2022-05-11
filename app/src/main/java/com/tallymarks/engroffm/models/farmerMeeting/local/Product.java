package com.tallymarks.engroffm.models.farmerMeeting.local;

public class Product {
    String branchName, branchID;

    public Product(String branchID, String branchName) {
        this.branchName = branchName;
        this.branchID = branchID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
}
