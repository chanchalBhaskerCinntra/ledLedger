package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemResponse implements Serializable {
    @SerializedName("data")
    ArrayList<DocumentLines> value;

    public ArrayList<DocumentLines> getValue() {
        return value;
    }

    public void setValue(ArrayList<DocumentLines> value) {
        this.value = value;
    }
}
