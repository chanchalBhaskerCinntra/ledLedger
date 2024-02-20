package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseItemFilterDashboard {

    public String message;
    public int status;
    public ArrayList<DataItemFilterDashBoard> data;
    @SerializedName("TotalSales")
    public double totalSales;
    @SerializedName("TotalCreditNote")
    public int totalCreditNote;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<DataItemFilterDashBoard> getData() {
        return data;
    }

    public void setData(ArrayList<DataItemFilterDashBoard> data) {
        this.data = data;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalCreditNote() {
        return totalCreditNote;
    }

    public void setTotalCreditNote(int totalCreditNote) {
        this.totalCreditNote = totalCreditNote;
    }
}
