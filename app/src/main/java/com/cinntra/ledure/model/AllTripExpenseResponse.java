package com.cinntra.ledure.model;

import java.util.ArrayList;

public class AllTripExpenseResponse {
    public String message;
    public String status;
    public ArrayList<DataAllTripExpense> data;;


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

    public ArrayList<DataAllTripExpense> getData() {
        return data;
    }

    public void setData(ArrayList<DataAllTripExpense> data) {
        this.data = data;
    }
}
