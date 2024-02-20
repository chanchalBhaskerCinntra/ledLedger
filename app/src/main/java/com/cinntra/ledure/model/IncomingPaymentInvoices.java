package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IncomingPaymentInvoices implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("IncomingPaymentsId")
    String IncomingPaymentsId;
    @SerializedName("InvoiceDocEntry")
    String InvoiceDocEntry;
    @SerializedName("SumApplied")
    String SumApplied;
    @SerializedName("AppliedFC")
    String AppliedFC;
    @SerializedName("AppliedSys")
    String AppliedSys;
    @SerializedName("DiscountPercent")
    String DiscountPercent;
    @SerializedName("TotalDiscount")
    String TotalDiscount;
    @SerializedName("TotalDiscountFC")
    String TotalDiscountFC;
    @SerializedName("TotalDiscountSC")
    String TotalDiscountSC;
    @SerializedName("DocDate")
    String DocDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncomingPaymentsId() {
        return IncomingPaymentsId;
    }

    public void setIncomingPaymentsId(String incomingPaymentsId) {
        IncomingPaymentsId = incomingPaymentsId;
    }

    public String getInvoiceDocEntry() {
        return InvoiceDocEntry;
    }

    public void setInvoiceDocEntry(String invoiceDocEntry) {
        InvoiceDocEntry = invoiceDocEntry;
    }

    public String getSumApplied() {
        return SumApplied;
    }

    public void setSumApplied(String sumApplied) {
        SumApplied = sumApplied;
    }

    public String getAppliedFC() {
        return AppliedFC;
    }

    public void setAppliedFC(String appliedFC) {
        AppliedFC = appliedFC;
    }

    public String getAppliedSys() {
        return AppliedSys;
    }

    public void setAppliedSys(String appliedSys) {
        AppliedSys = appliedSys;
    }

    public String getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        TotalDiscount = totalDiscount;
    }

    public String getTotalDiscountFC() {
        return TotalDiscountFC;
    }

    public void setTotalDiscountFC(String totalDiscountFC) {
        TotalDiscountFC = totalDiscountFC;
    }

    public String getTotalDiscountSC() {
        return TotalDiscountSC;
    }

    public void setTotalDiscountSC(String totalDiscountSC) {
        TotalDiscountSC = totalDiscountSC;
    }

    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
    }
}
