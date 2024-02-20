package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardCounterResponse implements Serializable {

    public String message;
    public int status;
    public ArrayList<DashboardCounterData> data;

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

    public ArrayList<DashboardCounterData> getData() {
        return data;
    }

    public void setData(ArrayList<DashboardCounterData> data) {
        this.data = data;
    }
}
