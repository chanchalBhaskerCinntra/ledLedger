package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DataItemInvoices implements Serializable {

    @SerializedName("ItemName")
    public String itemName;
    @SerializedName("ItemCode")
    public String itemCode;
    @SerializedName("TotalPrice")
    public String totalPrice;
    @SerializedName("TotalQty")
    public String  totalQty;
    @SerializedName("BPList")
    public ArrayList<BpList> saleOrder;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public ArrayList<BpList> getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(ArrayList<BpList> saleOrder) {
        this.saleOrder = saleOrder;
    }
}
