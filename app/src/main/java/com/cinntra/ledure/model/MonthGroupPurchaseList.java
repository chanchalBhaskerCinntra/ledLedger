package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonthGroupPurchaseList implements Serializable {

    @SerializedName("Month")
    @Expose
    private String Month;
    @SerializedName("DocTotal")
    @Expose
    private String DocTotal;

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(String docTotal) {
        DocTotal = docTotal;
    }
}
