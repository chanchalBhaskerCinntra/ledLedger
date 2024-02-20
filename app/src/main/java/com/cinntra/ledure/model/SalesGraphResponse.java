package com.cinntra.ledure.model;

import java.util.ArrayList;

public class SalesGraphResponse {
    public String message;
    public int status;
    public ArrayList<GraphModel> data;

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

    public ArrayList<GraphModel> getData() {
        return data;
    }

    public void setData(ArrayList<GraphModel> data) {
        this.data = data;
    }

}
