package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePaymentDueDashboardCustomerList {
    public String message;
    public int status;
    public ArrayList<DataPaymentDueDashboardCustomerList> data;
    @SerializedName("TotalSales")
    public int totalSales;
    @SerializedName("TotalReceivePayment")
    public int totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public int differenceAmount;


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

    public ArrayList<DataPaymentDueDashboardCustomerList> getData() {
        return data;
    }

    public void setData(ArrayList<DataPaymentDueDashboardCustomerList> data) {
        this.data = data;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(int totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public int getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(int differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}
