package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePayMentDueCounter {
    public String message;
    public int status;
    public ArrayList<DataPaymentDueCounter> data;

    @SerializedName("TotalPaybal")
    public double totalPaybal;


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

    public ArrayList<DataPaymentDueCounter> getData() {
        return data;
    }

    public void setData(ArrayList<DataPaymentDueCounter> data) {
        this.data = data;
    }

    public double getTotalPaybal() {
        return totalPaybal;
    }

    public void setTotalPaybal(double totalPaybal) {
        this.totalPaybal = totalPaybal;
    }
}
