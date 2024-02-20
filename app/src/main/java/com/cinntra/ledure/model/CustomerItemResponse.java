package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CustomerItemResponse implements Serializable
     {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<SoldItemResponse> CustomerLedgerRes;

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

    public List<SoldItemResponse> getCustomerLedgerRes() {
        return CustomerLedgerRes;
    }

    public void setCustomerLedgerRes(List<SoldItemResponse> customerLedgerRes) {
        CustomerLedgerRes = customerLedgerRes;
    }
}
