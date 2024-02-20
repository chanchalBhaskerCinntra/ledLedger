package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseItemInSalesCard implements Serializable {

    public String message;
    public int status;
    public ArrayList<DataItemInSalesCard> data;

    public ResponseItemInSalesCard() {
    }

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

    public ArrayList<DataItemInSalesCard> getData() {
        return data;
    }

    public void setData(ArrayList<DataItemInSalesCard> data) {
        this.data = data;
    }
}
