package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UnderList implements Serializable {
    @SerializedName("InvoiceId")
    @Expose
    String InvoiceId;
    @SerializedName("DocEntry")
    @Expose
    String DocEntry;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate;
    @SerializedName("DocDueDate")
    @Expose
    String DocDueDate;
    @SerializedName("DocTotal")
    @Expose
    String DocTotal;
    @SerializedName("Month")
    @Expose
    String Month;
    @SerializedName("OverDueGroup")
    @Expose
    String OverDueGroup;
    @SerializedName("OverDueDays")
    @Expose
    Integer OverDueDays;


    public Integer getOverDueDays() {
        return OverDueDays;
    }

    public void setOverDueDays(Integer overDueDays) {
        OverDueDays = overDueDays;
    }

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

    public String getDocDueDate() {
        return DocDueDate;
    }

    public void setDocDueDate(String docDueDate) {
        DocDueDate = docDueDate;
    }

    public String getDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(String docTotal) {
        DocTotal = docTotal;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getOverDueGroup() {
        return OverDueGroup;
    }

    public void setOverDueGroup(String overDueGroup) {
        OverDueGroup = overDueGroup;
    }
}
