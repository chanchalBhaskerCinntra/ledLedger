package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DepartMent implements Serializable {

    @SerializedName("Name")
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
