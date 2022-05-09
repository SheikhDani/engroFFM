
package com.tallymarks.engroffm.models.getnotifications;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdDate")
    @Expose
    private Long createdDate;
    @SerializedName("c2a")
    @Expose
    private String c2a;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getC2a() {
        return c2a;
    }

    public void setC2a(String c2a) {
        this.c2a = c2a;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
