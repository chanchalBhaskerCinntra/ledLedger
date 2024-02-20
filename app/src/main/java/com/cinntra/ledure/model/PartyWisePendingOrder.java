package com.cinntra.ledure.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.cinntra.roomdb.TypeConvertorOrderWisePendingOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


@Entity(tableName = "pending_order_table")
public class PartyWisePendingOrder implements Serializable {


    @NonNull
    @PrimaryKey
    @SerializedName("CardCode")
    @Expose
    public String cardCode;
    @Expose
    @SerializedName("CardName")
    public String cardName;

    @SerializedName("PendingAmount")
    @Expose
    public double pendingAmount;

    @TypeConverters(TypeConvertorOrderWisePendingOrder.class)
    @SerializedName("Orderwise")
    @Expose
    public ArrayList<OrderWisePendingOrderData> orderwise;


    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public ArrayList<OrderWisePendingOrderData> getOrderwise() {
        return orderwise;
    }

    public void setOrderwise(ArrayList<OrderWisePendingOrderData> orderwise) {
        this.orderwise = orderwise;
    }
}
