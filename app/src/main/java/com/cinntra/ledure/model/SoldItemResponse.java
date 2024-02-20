package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SoldItemResponse implements Serializable {

    @SerializedName("ItemName")
    @Expose
    private String ItemName;
    @SerializedName("ItemCode")
    @Expose
    private String ItemCode;

    @SerializedName("UnitPirce")
    @Expose
    private String UnitPirce;
    @SerializedName("LastSoldDate")
    @Expose
    private String LastSoldDate;
    @SerializedName("TotalQty")
    @Expose
    private String TotalQty;
    private String NetTotal;
    private String TotalPriceWithoutHeadDiscount;
    private String TotalPrice;
    private String UnitPrice;
    private String NoOfInvoice;

    public String getNetTotal() {
        return NetTotal;
    }

    public void setNetTotal(String netTotal) {
        NetTotal = netTotal;
    }

    public String getTotalPriceWithoutHeadDiscount() {
        return TotalPriceWithoutHeadDiscount;
    }

    public void setTotalPriceWithoutHeadDiscount(String totalPriceWithoutHeadDiscount) {
        TotalPriceWithoutHeadDiscount = totalPriceWithoutHeadDiscount;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getNoOfInvoice() {
        return NoOfInvoice;
    }

    public void setNoOfInvoice(String noOfInvoice) {
        NoOfInvoice = noOfInvoice;
    }

    @SerializedName("ItemOrderList")
    @Expose
    private ArrayList<SoldItem> ItemOrderList = null;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getUnitPirce() {
        return UnitPirce;
    }

    public void setUnitPirce(String unitPirce) {
        UnitPirce = unitPirce;
    }

    public String getLastSoldDate() {
        return LastSoldDate;
    }

    public void setLastSoldDate(String lastSoldDate) {
        LastSoldDate = lastSoldDate;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public ArrayList<SoldItem> getItemOrderList() {
        return ItemOrderList;
    }

    public void setItemOrderList(ArrayList<SoldItem> itemOrderList) {
        ItemOrderList = itemOrderList;
    }
}
