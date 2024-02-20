package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseItemInvoices implements Serializable {

    public String message;
    public int status;
    public ArrayList<DataItemInvoices> data;

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

    public ArrayList<DataItemInvoices> getData() {
        return data;
    }

    public void setData(ArrayList<DataItemInvoices> data) {
        this.data = data;
    }
}
