package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiptResponse implements Serializable {
    @SerializedName("status")
    String status;
    @SerializedName("data")
    ArrayList<ReceiptHead> value;

    public ArrayList<ReceiptHead> getValue() {
        return value;
    }

    public void setValue(ArrayList<ReceiptHead> value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
