
package com.tallymarks.engroffm.models.assignedsalespoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AssignedSalesPointOutput {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("salesPointCode")
    @Expose
    private String salesPointCode;
    @SerializedName("hierarchyId")
    @Expose
    private Object hierarchyId;
    @SerializedName("salesPointName")
    @Expose
    private String salesPointName;
    @SerializedName("territoryCode")
    @Expose
    private Object territoryCode;
    @SerializedName("tehsilCode")
    @Expose
    private Object tehsilCode;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getSalesPointCode() {
        return salesPointCode;
    }

    public void setSalesPointCode(String salesPointCode) {
        this.salesPointCode = salesPointCode;
    }

    public Object getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Object hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getSalesPointName() {
        return salesPointName;
    }

    public void setSalesPointName(String salesPointName) {
        this.salesPointName = salesPointName;
    }

    public Object getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(Object territoryCode) {
        this.territoryCode = territoryCode;
    }

    public Object getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(Object tehsilCode) {
        this.tehsilCode = tehsilCode;
    }

}
