
package com.tallymarks.engroffm.models.supervisorsnapshotoutput;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SuperVisorSnapShotOutput {

    @SerializedName("subordinateUserId")
    @Expose
    private Integer subordinateUserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hasJourneyPlans")
    @Expose
    private Boolean hasJourneyPlans;
    @SerializedName("numberOfJourneyPlans")
    @Expose
    private Integer numberOfJourneyPlans;

    public Integer getSubordinateUserId() {
        return subordinateUserId;
    }

    public void setSubordinateUserId(Integer subordinateUserId) {
        this.subordinateUserId = subordinateUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasJourneyPlans() {
        return hasJourneyPlans;
    }

    public void setHasJourneyPlans(Boolean hasJourneyPlans) {
        this.hasJourneyPlans = hasJourneyPlans;
    }

    public Integer getNumberOfJourneyPlans() {
        return numberOfJourneyPlans;
    }

    public void setNumberOfJourneyPlans(Integer numberOfJourneyPlans) {
        this.numberOfJourneyPlans = numberOfJourneyPlans;
    }

}
