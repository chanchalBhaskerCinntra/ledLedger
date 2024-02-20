package com.cinntra.ledure.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "item_list_in_sales")
public class DataItemInSalesCard implements Serializable {

    @SerializedName("ItemName")
    public String itemName;
    @NonNull
    @PrimaryKey
    @SerializedName("ItemCode")
    public String itemCode;
    @SerializedName("UnitPrice")
    public String  unitPrice;
    @SerializedName("TotalPrice")
    public String totalPrice;
    @SerializedName("TotalQty")
    public String totalQty;
    @SerializedName("NoOfInvoice")
    public int noOfInvoice;



    public DataItemInSalesCard() {
    }

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

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
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

    public int getNoOfInvoice() {
        return noOfInvoice;
    }

    public void setNoOfInvoice(int noOfInvoice) {
        this.noOfInvoice = noOfInvoice;
    }
}
