package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseJournalEntryBpWise implements Serializable {
    @SerializedName("message")
    @Expose()
    public String message;

    @SerializedName("status")
    @Expose()
    public String status;

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

    public ArrayList<JournalEntryBpWiseHeaderData> getData() {
        return data;
    }

    public void setData(ArrayList<JournalEntryBpWiseHeaderData> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose()
    public ArrayList<JournalEntryBpWiseHeaderData> data;

}
