package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaleOrder implements Serializable {

    @SerializedName("InvoiceId")
    public String invoiceId;
    @SerializedName("UnitPrice")
    public String  unitPrice;
    @SerializedName("Quantity")
    public String  quantity;
    @SerializedName("DocTotal")
    public String  docTotal;

    @SerializedName("DocEntry")
    public String  DocEntry;

    @SerializedName("CreateDate")
    public String  createDate;
    @SerializedName("PaymentStatus")
    public String  paymentStatus;


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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDocTotal() {
        return docTotal;
    }

    public void setDocTotal(String docTotal) {
        this.docTotal = docTotal;
    }

    public String getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(String docEntry) {
        DocEntry = docEntry;
    }
}
