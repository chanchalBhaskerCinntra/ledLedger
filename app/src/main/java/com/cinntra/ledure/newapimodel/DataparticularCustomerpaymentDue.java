package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

public class DataparticularCustomerpaymentDue {

    @SerializedName("OrderId")
    public String orderId;
    @SerializedName("DocEntry")
    public String docEntry;
    @SerializedName("DocDueDate")
    public String docDueDate;
    @SerializedName("OrderAmount")
    public int orderAmount;
    @SerializedName("OverDueDays")
    public String overDueDays;
    @SerializedName("CreateDate")
    public String createDate;
    @SerializedName("PaymentStatus")
    public String paymentStatus;
    @SerializedName("TotalReceivePayment")
    public int totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public double differenceAmount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(String docEntry) {
        this.docEntry = docEntry;
    }

    public String getDocDueDate() {
        return docDueDate;
    }

    public void setDocDueDate(String docDueDate) {
        this.docDueDate = docDueDate;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOverDueDays() {
        return overDueDays;
    }

    public void setOverDueDays(String overDueDays) {
        this.overDueDays = overDueDays;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(int totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public double getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(double differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}


