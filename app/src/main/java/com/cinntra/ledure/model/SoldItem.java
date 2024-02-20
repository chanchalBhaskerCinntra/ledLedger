package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SoldItem implements Serializable {

    @SerializedName("OrderId")
    @Expose
    private String InvoiceID;
    @SerializedName("UnitPirce")
    @Expose
    private String UnitPirce;

    @SerializedName("SoldDate")
    @Expose
    private String SoldDate;
    @SerializedName("TotalQty")
    @Expose
    private String TotalQty;


    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getUnitPirce() {
        return UnitPirce;
    }

    public void setUnitPirce(String unitPirce) {
        UnitPirce = unitPirce;
    }

    public String getSoldDate() {
        return SoldDate;
    }

    public void setSoldDate(String soldDate) {
        SoldDate = soldDate;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }
}
