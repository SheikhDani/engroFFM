package com.tallymarks.ffmapp.models;

public class SoilSamplingCrops {
    private String acre;
    private String block;
    private String previouscrop;
    private String crop1;

    public String getAcre() {
        return acre;
    }

    public void setAcre(String acre) {
        this.acre = acre;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPreviouscrop() {
        return previouscrop;
    }

    public void setPreviouscrop(String previouscrop) {
        this.previouscrop = previouscrop;
    }

    public String getCrop1() {
        return crop1;
    }

    public void setCrop1(String crop1) {
        this.crop1 = crop1;
    }

    public String getCrop2() {
        return crop2;
    }

    public void setCrop2(String crop2) {
        this.crop2 = crop2;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    private String crop2;
    private String depth;
}
