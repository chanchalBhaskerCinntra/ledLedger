package com.cinntra.ledure.newapimodel;

import java.util.ArrayList;

public class ResponseReceivableGraph {

    public String message;
    public int status;
    public ArrayList<DataReceivableGraph> data;


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

    public ArrayList<DataReceivableGraph> getData() {
        return data;
    }

    public void setData(ArrayList<DataReceivableGraph> data) {
        this.data = data;
    }
}
