package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseTripCheckOut implements Serializable {


    public String message;
    public String status;


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
}
