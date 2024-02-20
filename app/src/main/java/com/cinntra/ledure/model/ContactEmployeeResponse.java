package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactEmployeeResponse implements Serializable {
    @SerializedName("ContactEmployees")
    ArrayList<ContactEmployees> value;

    public ArrayList<ContactEmployees> getValue() {
        return value;
    }

    public void setValue(ArrayList<ContactEmployees> value) {
        this.value = value;
    }
}
