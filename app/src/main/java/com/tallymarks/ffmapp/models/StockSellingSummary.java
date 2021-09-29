package com.tallymarks.ffmapp.models;

public class StockSellingSummary {
    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getQuanitysold() {
        return quanitysold;
    }

    public void setQuanitysold(String quanitysold) {
        this.quanitysold = quanitysold;
    }

    public String getInvocieno() {
        return invocieno;
    }

    public void setInvocieno(String invocieno) {
        this.invocieno = invocieno;
    }

    public String getProdutcs() {
        return produtcs;
    }

    public void setProdutcs(String produtcs) {
        this.produtcs = produtcs;
    }

    public String getNsp() {
        return nsp;
    }

    public void setNsp(String nsp) {
        this.nsp = nsp;
    }

    public String getVisitdate() {
        return visitdate;
    }

    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    private String orderno, quanitysold,invocieno,produtcs,nsp,visitdate;
}
