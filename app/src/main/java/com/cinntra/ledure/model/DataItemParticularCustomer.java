package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DataItemParticularCustomer implements Serializable {

    @SerializedName("ItemName")
    public String itemName;
    @SerializedName("ItemCode")
    public String itemCode;
    @SerializedName("CardName")
    public String cardName;
    @SerializedName("CardCode")
    public String cardCode;
    @SerializedName("EmailAddress")
    public String emailAddress;
    @SerializedName("Phone1")
    public String phone1;
    @SerializedName("GSTIN")
    public String gSTIN;
    @SerializedName("BPAddress")
    public String bPAddress;
    @SerializedName("GroupName")
    public String groupName;
    @SerializedName("CreditLimit")
    public String creditLimit;
    @SerializedName("CreditLimitDayes")
    public String creditLimitDayes;
    @SerializedName("TotalPrice")
    public String totalPrice;
    @SerializedName("TotalQty")
    public String totalQty;
    @SerializedName("SaleOrder")
    public ArrayList<SaleOrder> saleOrder;

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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getgSTIN() {
        return gSTIN;
    }

    public void setgSTIN(String gSTIN) {
        this.gSTIN = gSTIN;
    }

    public String getbPAddress() {
        return bPAddress;
    }

    public void setbPAddress(String bPAddress) {
        this.bPAddress = bPAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCreditLimitDayes() {
        return creditLimitDayes;
    }

    public void setCreditLimitDayes(String creditLimitDayes) {
        this.creditLimitDayes = creditLimitDayes;
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

    public ArrayList<SaleOrder> getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(ArrayList<SaleOrder> saleOrder) {
        this.saleOrder = saleOrder;
    }
}
