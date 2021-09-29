package com.tallymarks.ffmapp.models;

public class SoilAnalysis {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemebrship() {
        return memebrship;
    }

    public void setMemebrship(String memebrship) {
        this.memebrship = memebrship;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSampleno() {
        return sampleno;
    }

    public void setSampleno(String sampleno) {
        this.sampleno = sampleno;
    }

    private String title,memebrship,date,sampleno;
}
