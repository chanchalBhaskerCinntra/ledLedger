package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceivableList implements Serializable {

    @SerializedName("InvoiceId")
    String InvoiceId;
    @SerializedName("DocEntry")
    String DocEntry;

    @SerializedName("CreateDate")
    String CreateDate;
    @SerializedName("DocTotal")
    String DocTotal;
    @SerializedName("OverDueGroup")
    @Expose
    String OverDueGroup;

    public String getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(String docEntry) {
        DocEntry = docEntry;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(String docTotal) {
        DocTotal = docTotal;
    }

    public String getOverDueGroup() {
        return OverDueGroup;
    }

    public void setOverDueGroup(String overDueGroup) {
        OverDueGroup = overDueGroup;
    }
}
