package com.cinntra.ledure.model;

import com.cinntra.ledure.newapimodel.SubGroupItemStock;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResSubCatItems implements Serializable {

    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;
    @SerializedName("TotalSales")
    String TotalSales;
    @SerializedName("TotalCreditNote")
    String TotalCreditNote;

    @SerializedName("data")
    public ArrayList<SubGroupItemStock> subGroup;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }

    public String getTotalCreditNote() {
        return TotalCreditNote;
    }

    public void setTotalCreditNote(String totalCreditNote) {
        TotalCreditNote = totalCreditNote;
    }

    public ArrayList<SubGroupItemStock> getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(ArrayList<SubGroupItemStock> subGroup) {
        this.subGroup = subGroup;
    }
}
