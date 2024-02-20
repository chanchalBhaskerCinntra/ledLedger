package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseItemDashboard implements Serializable {

    public String message;
    public int status;
    public ArrayList<DataItemDashBoard> data;

    public ResponseItemDashboard() {
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

    public ArrayList<DataItemDashBoard> getData() {
        return data;
    }

    public void setData(ArrayList<DataItemDashBoard> data) {
        this.data = data;
    }
}
