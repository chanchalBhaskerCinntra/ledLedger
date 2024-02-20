package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LedgerItem implements Serializable {

    @SerializedName("CardCode")
     @Expose
    String CardCode;
    @SerializedName("Type")
    @Expose
    String Type;

    @SerializedName("OrderId")
    @Expose
    String OrderId;
    @SerializedName("Amount")
    @Expose
    String Amount;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate;
    @SerializedName("Balance")
    @Expose
    String Balance;

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
