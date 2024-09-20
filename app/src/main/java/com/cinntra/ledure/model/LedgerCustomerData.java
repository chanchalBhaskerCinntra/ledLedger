package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LedgerCustomerData implements Serializable {

    @SerializedName("OrderId")
    private Integer orderId;

    @SerializedName("DocEntry")
    private Integer DocEntry;

    @SerializedName("Comments")
    private String Comments;
    @SerializedName("OrderAmount")
    private String orderAmount;
    @SerializedName("CreateDate")
    private String createDate;
    @SerializedName("PaymentStatus")
    @Expose
    String PaymentStatus;

    @SerializedName("OverDueDays")
    @Expose
    Integer OverDueDays;

    @SerializedName("ReceiptId")
    @Expose
    String ReceiptId;

    @SerializedName("DocNum")
    String DocNum;

    @SerializedName("DifferenceAmount")
    private String DifferenceAmount;
    @SerializedName("TotalReceivePayment")
    private String TotalReceivePayment;

    @SerializedName("IncomingPaymentInvoices")
    public ArrayList<IncomingPaymentInvoice> incomingPaymentInvoices = null;


    public ArrayList<IncomingPaymentInvoice> getIncomingPaymentInvoices() {
        return incomingPaymentInvoices;
    }

    public void setIncomingPaymentInvoices(ArrayList<IncomingPaymentInvoice> incomingPaymentInvoices) {
        this.incomingPaymentInvoices = incomingPaymentInvoices;
    }


    public String getDocNum() {
        return DocNum;
    }

    public void setDocNum(String docNum) {
        DocNum = docNum;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public Integer getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(Integer docEntry) {
        DocEntry = docEntry;
    }

    public String getReceiptId() {
        return ReceiptId;
    }

    public void setReceiptId(String receiptId) {
        ReceiptId = receiptId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public Integer getOverDueDays() {
        return OverDueDays;
    }

    public void setOverDueDays(Integer overDueDays) {
        OverDueDays = overDueDays;
    }
}
