package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CashDiscountResponse implements Serializable {
    @SerializedName("data")
    ArrayList<CashDiscountItem> value;

    public ArrayList<CashDiscountItem> getValue() {
        return value;
    }

    public void setValue(ArrayList<CashDiscountItem> value) {
        this.value = value;
    }
}
