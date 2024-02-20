package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomerLedgerResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<CustomerLedgerRes> CustomerLedgerRes = null;

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

    public List<CustomerLedgerRes> getCustomerLedgerRes() {
        return CustomerLedgerRes;
    }

    public void setCustomerLedgerRes(List<CustomerLedgerRes> customerLedgerRes) {
        CustomerLedgerRes = customerLedgerRes;
    }
}
