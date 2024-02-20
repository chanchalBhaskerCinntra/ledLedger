package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReceivableResponse implements  Serializable {
    @SerializedName("TotalSales")
    @Expose
    private String TotalSales;
    @SerializedName("TotalReceivePayment")
    @Expose
    private String TotalReceivePayment ;
    @SerializedName("DifferenceAmount")
    @Expose
    private String DifferenceAmount;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    List<ReceivableCustomerData> data=null;

    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }

    public String getTotalReceivePayment() {
        return TotalReceivePayment;
    }

    public void setTotalReceivePayment(String totalReceivePayment) {
        TotalReceivePayment = totalReceivePayment;
    }

    public String getDifferenceAmount() {
        return DifferenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        DifferenceAmount = differenceAmount;
    }

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

    public List<ReceivableCustomerData> getData() {
        return data;
    }

    public void setData(List<ReceivableCustomerData> data) {
        this.data = data;
    }
}
