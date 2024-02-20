package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LedgerResponse implements Serializable {

    @SerializedName("TotalSales")
     @Expose
    String TotalSales;
    @SerializedName("TotalReceivePayment")
    @Expose
    String TotalReceivePayment;

    @SerializedName("Closing")
    @Expose
    String Closing;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<LedgerItem>  data=null;


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

    public String getClosing() {
        return Closing;
    }

    public void setClosing(String closing) {
        Closing = closing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LedgerItem> getData() {
        return data;
    }

    public void setData(List<LedgerItem> data) {
        this.data = data;
    }
}
