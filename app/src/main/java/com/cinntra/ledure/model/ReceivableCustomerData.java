package com.cinntra.ledure.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "receivable_data")
public class ReceivableCustomerData implements Serializable {

    @SerializedName("CardName")
    private String CardName;

    @NonNull
    @PrimaryKey
    @SerializedName("CardCode")
    private String CardCode;
    @SerializedName("TotalSales")
    private String TotalSales;
    @SerializedName("DifferenceAmount")
    private String DifferenceAmount;
    @SerializedName("TotalReceivePayment")
    private String TotalReceivePayment;

    @SerializedName("CreditLimit")
    private String CreditLimit;

    @SerializedName("CreditLimitDayes")
    private String CreditLimitDayes;

    @SerializedName("AvgPayDays")
    private String AvgPayDays;


    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        CreditLimit = creditLimit;
    }

    public String getCreditLimitDayes() {
        return CreditLimitDayes;
    }

    public void setCreditLimitDayes(String creditLimitDayes) {
        CreditLimitDayes = creditLimitDayes;
    }

    public String getAvgPayDays() {
        return AvgPayDays;
    }

    public void setAvgPayDays(String avgPayDays) {
        AvgPayDays = avgPayDays;
    }

    public String getDifferenceAmount() {
        return DifferenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        DifferenceAmount = differenceAmount;
    }

    public String getTotalReceivePayment() {
        return TotalReceivePayment;
    }

    public void setTotalReceivePayment(String totalReceivePayment) {
        TotalReceivePayment = totalReceivePayment;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }
}
