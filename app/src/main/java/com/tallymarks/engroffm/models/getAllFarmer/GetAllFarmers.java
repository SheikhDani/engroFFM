package com.tallymarks.engroffm.models.getAllFarmer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllFarmers {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("salesPointCode")
    @Expose
    private String salesPointCode;

    @SerializedName("hierarchyId")
    @Expose
    private String hierarchyId;

    @SerializedName("salesPointName")
    @Expose
    private String salesPointName;

    @SerializedName("territoryCode")
    @Expose
    private String territoryCode;

    @SerializedName("tehsilCode")
    @Expose
    private String tehsilCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalesPointCode() {
        return salesPointCode;
    }

    public void setSalesPointCode(String salesPointCode) {
        this.salesPointCode = salesPointCode;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getSalesPointName() {
        return salesPointName;
    }

    public void setSalesPointName(String salesPointName) {
        this.salesPointName = salesPointName;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public String getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(String tehsilCode) {
        this.tehsilCode = tehsilCode;
    }

}
