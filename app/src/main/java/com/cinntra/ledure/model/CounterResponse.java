package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CounterResponse implements Serializable {
    @SerializedName("TotalSales")
     @Expose
    String TotalSales;
    @SerializedName("TotalReceivePayment")
    @Expose
    String TotalReceivePayment;
    @SerializedName("DifferenceAmount")
    @Expose
    String DifferenceAmount;
    @SerializedName("data")
    ArrayList<Count> value;

    public ArrayList<Count> getValue() {
        return value;
    }

    public void setValue(ArrayList<Count> value) {
        this.value = value;
    }


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
}
