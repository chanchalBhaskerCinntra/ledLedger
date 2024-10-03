package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomerLedgerRes implements Serializable
{

    @SerializedName("CardCode")
    @Expose
    private String CardCode;
    @SerializedName("CardName")
    @Expose
    private String CardName;
    @SerializedName("LastSalesDate")
    @Expose
    private String LastSalesDate;

    @SerializedName("LastRecipetDate")
    @Expose
    private String LastRecipetDate;
    @SerializedName("InvoiceCount")
    @Expose
    private String InvoiceCount;



    @SerializedName("AvgInvoiceAmount")
    @Expose
    private String AvgInvoiceAmount;

    @SerializedName("AvgPayDays")
    @Expose
    private String AvgPayDays;

    @SerializedName("PendingAmount")
    @Expose
    private String PendingAmount;
    @SerializedName("TotalSales")
    @Expose
    private String TotalSales;
    @SerializedName("TotalReceipt")
    @Expose
    private String TotalReceipt;
    @SerializedName("TotalReceivable")
    @Expose
    private String TotalReceivable;


    @SerializedName("TotalPayable")
    @Expose
    private String TotalPayable;


    @SerializedName("TotalJECreditNotepay")
    @Expose
    private String TotalJECreditNotepay;

    @SerializedName("CreditLimit")
    @Expose
    private String CreditLimit;

    @SerializedName("CreditLimitLeft")
    @Expose
    private String CreditLimitLeft;



    @SerializedName("TotalPurchasesReceipt")
    @Expose
    private String TotalPurchasesReceipt;
    @SerializedName("PurchaseCreditNote")
    @Expose
    private String PurchaseCreditNote;
    @SerializedName("TotalPurchases")
    @Expose
    private String TotalPurchases;
    @SerializedName("LinkedBusinessPartner")
    @Expose
    private String LinkedBusinessPartner;

    @SerializedName("TotalCreditNote")
    @Expose
    private String TotalCreditNote;

    @SerializedName("TotalJECreditNote")
    @Expose
    String TotalJECreditNote;

    @SerializedName("Advance")
    @Expose
    String Advance;
    @SerializedName("SalesList")
    @Expose
    private List<SalesList> SalesList = null;
    @SerializedName("ReceiptList")
    @Expose
    private List<ReceiptList> ReceiptList = null;
    @SerializedName("ReceivableList")
    @Expose
    private List<ReceivableList> ReceivableList = null;


    @SerializedName("MonthGroupSalesList")
    @Expose
    private List<MonthGroupSalesList> MonthGroupSalesList = null;

    @SerializedName("MonthGroupPurchaseList")
    @Expose
    private List<MonthGroupPurchaseList> MonthGroupPurchaseList = null;

    @SerializedName("MonthGroupReceivableList")
    @Expose
    private List<MonthGroupSalesList> MonthGroupReceivableList = null;
    @SerializedName("MonthGroupReceiptList")
    @Expose
    private List<MonthGroupSalesList> MonthGroupReceiptList = null;

    @SerializedName("UnderList")
    @Expose
    private List<UnderList> UnderList = null;

    @SerializedName("OverList")
    @Expose
    private List<UnderList> OverList = null;


    @SerializedName("OverListpay")
    @Expose
    private List<UnderList> OverListpay = null;


    public String getAdvance() {
        return Advance;
    }

    public void setAdvance(String advance) {
        Advance = advance;
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public CustomerLedgerRes() {
    }


    public String getTotalPayable() {
        return TotalPayable;
    }

    public void setTotalPayable(String totalPayable) {
        TotalPayable = totalPayable;
    }

    public String getTotalCreditNote() {
        return TotalCreditNote;
    }

    public void setTotalCreditNote(String totalCreditNote) {
        TotalCreditNote = totalCreditNote;
    }

    public List<com.cinntra.ledure.model.MonthGroupPurchaseList> getMonthGroupPurchaseList() {
        return MonthGroupPurchaseList;
    }

    public void setMonthGroupPurchaseList(List<com.cinntra.ledure.model.MonthGroupPurchaseList> monthGroupPurchaseList) {
        MonthGroupPurchaseList = monthGroupPurchaseList;
    }


    public String getTotalJECreditNotepay() {
        return TotalJECreditNotepay;
    }

    public void setTotalJECreditNotepay(String totalJECreditNotepay) {
        TotalJECreditNotepay = totalJECreditNotepay;
    }

    public String getCreditLimitLeft() {
        return CreditLimitLeft;
    }

    public void setCreditLimitLeft(String creditLimitLeft) {
        CreditLimitLeft = creditLimitLeft;
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getLastSalesDate() {
        return LastSalesDate;
    }

    public void setLastSalesDate(String lastSalesDate) {
        LastSalesDate = lastSalesDate;
    }

    public String getLastRecipetDate() {
        return LastRecipetDate;
    }

    public void setLastRecipetDate(String lastRecipetDate) {
        LastRecipetDate = lastRecipetDate;
    }

    public String getInvoiceCount() {
        return InvoiceCount;
    }

    public void setInvoiceCount(String invoiceCount) {
        InvoiceCount = invoiceCount;
    }

    public String getAvgInvoiceAmount() {
        return AvgInvoiceAmount;
    }

    public void setAvgInvoiceAmount(String avgInvoiceAmount) {
        AvgInvoiceAmount = avgInvoiceAmount;
    }

    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }

    public String getTotalReceipt() {
        return TotalReceipt;
    }

    public void setTotalReceipt(String totalReceipt) {
        TotalReceipt = totalReceipt;
    }

    public String getTotalReceivable() {
        return TotalReceivable;
    }

    public void setTotalReceivable(String totalReceivable) {
        TotalReceivable = totalReceivable;
    }

    public List<com.cinntra.ledure.model.SalesList> getSalesList() {
        return SalesList;
    }

    public void setSalesList(List<com.cinntra.ledure.model.SalesList> salesList) {
        SalesList = salesList;
    }

    public List<com.cinntra.ledure.model.ReceiptList> getReceiptList() {
        return ReceiptList;
    }

    public void setReceiptList(List<com.cinntra.ledure.model.ReceiptList> receiptList) {
        ReceiptList = receiptList;
    }

    public List<com.cinntra.ledure.model.ReceivableList> getReceivableList() {
        return ReceivableList;
    }

    public void setReceivableList(List<com.cinntra.ledure.model.ReceivableList> receivableList) {
        ReceivableList = receivableList;
    }

    public List<com.cinntra.ledure.model.MonthGroupSalesList> getMonthGroupSalesList() {
        return MonthGroupSalesList;
    }

    public void setMonthGroupSalesList(List<com.cinntra.ledure.model.MonthGroupSalesList> monthGroupSalesList) {
        MonthGroupSalesList = monthGroupSalesList;
    }

    public List<MonthGroupSalesList> getMonthGroupReceivableList() {
        return MonthGroupReceivableList;
    }

    public void setMonthGroupReceivableList(List<com.cinntra.ledure.model.MonthGroupSalesList> monthGroupReceivableList) {
        MonthGroupReceivableList = monthGroupReceivableList;
    }

    public List<com.cinntra.ledure.model.MonthGroupSalesList> getMonthGroupReceiptList() {
        return MonthGroupReceiptList;
    }

    public void setMonthGroupReceiptList(List<com.cinntra.ledure.model.MonthGroupSalesList> monthGroupReceiptList) {
        MonthGroupReceiptList = monthGroupReceiptList;
    }

    public List<com.cinntra.ledure.model.UnderList> getUnderList() {
        return UnderList;
    }

    public void setUnderList(List<com.cinntra.ledure.model.UnderList> underList) {
        UnderList = underList;
    }

    public List<com.cinntra.ledure.model.UnderList> getOverList() {
        return OverList;
    }

    public void setOverList(List<com.cinntra.ledure.model.UnderList> overList) {
        OverList = overList;
    }


    public List<com.cinntra.ledure.model.UnderList> getOverListpay() {
        return OverListpay;
    }

    public void setOverListpay(List<com.cinntra.ledure.model.UnderList> overListpay) {
        OverListpay = overListpay;
    }

    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        CreditLimit = creditLimit;
    }


    public String getAvgPayDays() {
        return AvgPayDays;
    }

    public void setAvgPayDays(String avgPayDays) {
        AvgPayDays = avgPayDays;
    }

    public String getPendingAmount() {
        return PendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        PendingAmount = pendingAmount;
    }

    public String getTotalPurchasesReceipt() {
        return TotalPurchasesReceipt;
    }

    public void setTotalPurchasesReceipt(String totalPurchasesReceipt) {
        TotalPurchasesReceipt = totalPurchasesReceipt;
    }

    public String getPurchaseCreditNote() {
        return PurchaseCreditNote;
    }

    public void setPurchaseCreditNote(String purchaseCreditNote) {
        PurchaseCreditNote = purchaseCreditNote;
    }

    public String getTotalPurchases() {
        return TotalPurchases;
    }

    public void setTotalPurchases(String totalPurchases) {
        TotalPurchases = totalPurchases;
    }

    public String getLinkedBusinessPartner() {
        return LinkedBusinessPartner;
    }

    public void setLinkedBusinessPartner(String linkedBusinessPartner) {
        LinkedBusinessPartner = linkedBusinessPartner;
    }

    public String getTotalJECreditNote() {
        return TotalJECreditNote;
    }

    public void setTotalJECreditNote(String totalJECreditNote) {
        TotalJECreditNote = totalJECreditNote;
    }
}
