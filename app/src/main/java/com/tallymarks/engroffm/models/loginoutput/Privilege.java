
package com.tallymarks.engroffm.models.loginoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Privilege {

    @SerializedName("module")
    @Expose
    private String module;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("menuGroup")
    @Expose
    private String menuGroup;
    @SerializedName("url")
    @Expose
    private String url;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
