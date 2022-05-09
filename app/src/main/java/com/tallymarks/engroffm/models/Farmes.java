package com.tallymarks.engroffm.models;

public class Farmes {
    private String title;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    private int image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public Farmes() {
    }

    public  Farmes(String title, String status, int imge) {
        this.title = title;
        this.image = image;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

}
