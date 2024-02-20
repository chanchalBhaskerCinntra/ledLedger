package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataDeliveryNotePendingByOrder implements Serializable {

    @SerializedName("ItemCode")
    public String itemCode;
    @SerializedName("ItemDescription")
    public String itemDescription;
    @SerializedName("Quantity")
    public int quantity;
    @SerializedName("UnitPrice")
    public double unitPrice;
    @SerializedName("PendingAmount")
    public double pendingAmount;

    @SerializedName("DocDueDate")
    public String docDueDate;

    public DataDeliveryNotePendingByOrder() {
    }

    public String getDocDueDate() {
        return docDueDate;
    }

    public void setDocDueDate(String docDueDate) {
        this.docDueDate = docDueDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
}
