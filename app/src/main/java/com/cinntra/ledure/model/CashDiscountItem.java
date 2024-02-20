package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashDiscountItem implements  Serializable {
    @SerializedName("InvoiceNo")
    @Expose
    private String InvoiceNo;
    @SerializedName("OrderNo")
    @Expose
    private String OrderNo ;
    @SerializedName("CustomerName")
    @Expose
    private String CustomerName;
    @SerializedName("OrderAmount")
    @Expose
    private String OrderAmount;
    @SerializedName("InvoiceAmount")
    @Expose
    private String InvoiceAmount;
    @SerializedName("PaymentDueDate")
    @Expose
    String PaymentDueDate;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate;
    @SerializedName("DiscountAmount")
    @Expose
    String DiscountAmount;
    @SerializedName("DiscountPercentage")
    @Expose
    String DiscountPercentage;
    @SerializedName("PaymentStatus")
    @Expose
    String PaymentStatus;

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getInvoiceAmount() {
        return InvoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        InvoiceAmount = invoiceAmount;
    }

    public String getPaymentDueDate() {
        return PaymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        PaymentDueDate = paymentDueDate;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }
}
