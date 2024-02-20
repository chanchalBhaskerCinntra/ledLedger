package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;



public class SubGroupItemStock {

    @SerializedName("GroupName")
    public String groupName;
    @SerializedName("GroupCode")
    public String groupCode;
    @SerializedName("TotalPrice")
    public double totalPrice;
    @SerializedName("TotalQty")
    public int totalQty;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}
