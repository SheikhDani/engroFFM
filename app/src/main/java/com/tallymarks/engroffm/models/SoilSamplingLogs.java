package com.tallymarks.engroffm.models;

public class SoilSamplingLogs {
    public String getFarmerid() {
        return farmerid;
    }

    public void setFarmerid(String farmerid) {
        this.farmerid = farmerid;
    }

    public String getFarmername() {
        return farmername;
    }

    public void setFarmername(String farmername) {
        this.farmername = farmername;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getChecoutouttime() {
        return checoutouttime;
    }

    public void setChecoutouttime(String checoutouttime) {
        this.checoutouttime = checoutouttime;
    }

    public String getReferecne() {
        return referecne;
    }

    public void setReferecne(String referecne) {
        this.referecne = referecne;
    }

    String farmerid , farmername ,checkintime,checoutouttime,referecne;
}
