package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IncomingPaymentInvoice implements Serializable {
    @SerializedName("InvoiceId")
    public int invoiceId;
    @SerializedName("ReceiptId")
    public int receiptId;
    @SerializedName("PaymentAmt")
    public String paymentAmt;
    @SerializedName("CreateDate")
    public String createDate;

    @SerializedName("DocEntry")
    public String docEntry;

    public IncomingPaymentInvoice() {
    }

    public String getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(String docEntry) {
        this.docEntry = docEntry;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
