package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseCustomerLedger implements Serializable {

    public String message;
    public String status;
    public ArrayList<DataCustomerLedger> data;
    @SerializedName("TotalSales")
    public Double totalSales;
    @SerializedName("TotalReceivePayment")
    public Double totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public double differenceAmount;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DataCustomerLedger> getData() {
        return data;
    }

    public void setData(ArrayList<DataCustomerLedger> data) {
        this.data = data;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Double getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(Double totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public double getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(double differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}
