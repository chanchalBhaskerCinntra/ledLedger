package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseDeliveryOrder implements Serializable {
    public String message;
    public int status;
    public ArrayList<QuotationItemDelivery> data;


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

    public ArrayList<QuotationItemDelivery> getData() {
        return data;
    }

    public void setData(ArrayList<QuotationItemDelivery> data) {
        this.data = data;
    }
}
