package com.cinntra.ledure.model;

import java.util.ArrayList;

public class WareHouseResponse {
    public String message;
    public int status;
    public ArrayList<WareHouseData> data;

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

    public ArrayList<WareHouseData> getData() {
        return data;
    }

    public void setData(ArrayList<WareHouseData> data) {
        this.data = data;
    }

}
