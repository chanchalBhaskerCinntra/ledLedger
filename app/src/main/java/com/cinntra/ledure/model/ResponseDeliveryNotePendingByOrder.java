package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseDeliveryNotePendingByOrder implements Serializable {
    public String message;
    public int status;
    public ArrayList<DataDeliveryNotePendingByOrder> data;

    public ResponseDeliveryNotePendingByOrder() {
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

    public ArrayList<DataDeliveryNotePendingByOrder> getData() {
        return data;
    }

    public void setData(ArrayList<DataDeliveryNotePendingByOrder> data) {
        this.data = data;
    }
}
