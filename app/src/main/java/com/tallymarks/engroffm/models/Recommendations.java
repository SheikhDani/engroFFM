package com.tallymarks.engroffm.models;

public class Recommendations {
    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getFert() {
        return fert;
    }

    public void setFert(String fert) {
        this.fert = fert;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    private String crop,fert,product,dosage;
}
