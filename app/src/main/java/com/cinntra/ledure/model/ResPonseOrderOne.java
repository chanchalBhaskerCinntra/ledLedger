package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResPonseOrderOne implements Serializable {

    public String message;
    public String status;
    public ArrayList<DataOrderOne> data;

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

    public ArrayList<DataOrderOne> getData() {
        return data;
    }

    public void setData(ArrayList<DataOrderOne> data) {
        this.data = data;
    }
}
