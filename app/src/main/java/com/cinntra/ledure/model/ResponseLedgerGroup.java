package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseLedgerGroup implements Serializable {

    public String message;
    public int status;
    public ArrayList<DataLedgerGroup> data;
    @SerializedName("TotalSales")
    public double totalSales;
    @SerializedName("TotalReceivePayment")
    public double totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public double differenceAmount;

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

    public ArrayList<DataLedgerGroup> getData() {
        return data;
    }

    public void setData(ArrayList<DataLedgerGroup> data) {
        this.data = data;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(double totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public double getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(double differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}
