package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseItemParticularCustomerInfo implements Serializable {

    public String message;
    public int status;
    public ArrayList<DataItemParticularCustomer> data;

    public ResponseItemParticularCustomerInfo() {
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

    public ArrayList<DataItemParticularCustomer> getData() {
        return data;
    }

    public void setData(ArrayList<DataItemParticularCustomer> data) {
        this.data = data;
    }
}
