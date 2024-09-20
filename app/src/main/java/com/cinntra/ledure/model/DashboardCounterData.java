package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DashboardCounterData implements Serializable {

    @SerializedName("TotalSales")
    public String totalSales;
    @SerializedName("TotalReceivePayment")
    public String totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public String differenceAmount;
    @SerializedName("TotalCreditNote")
    public String totalCreditNote;
    @SerializedName("TotalPendingSales")
    String TotalPendingSales;

    @SerializedName("TotalSalesWithoutCreditNote")
    String TotalSalesWithoutCreditNote;

    public String getTotalSalesWithoutCreditNote() {
        return TotalSalesWithoutCreditNote;
    }

    public void setTotalSalesWithoutCreditNote(String totalSalesWithoutCreditNote) {
        TotalSalesWithoutCreditNote = totalSalesWithoutCreditNote;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    public String getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(String totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public String getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public String getTotalCreditNote() {
        return totalCreditNote;
    }

    public void setTotalCreditNote(String totalCreditNote) {
        this.totalCreditNote = totalCreditNote;
    }

    public String getTotalPendingSales() {
        return TotalPendingSales;
    }

    public void setTotalPendingSales(String totalPendingSales) {
        TotalPendingSales = totalPendingSales;
    }
}
