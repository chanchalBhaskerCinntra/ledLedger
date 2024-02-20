package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiptHead implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("DocNum")
    String DocNum;
    @SerializedName("DocType")
    String DocType;
    @SerializedName("DocDate")
    String DocDate;
    @SerializedName("CardCode")
    String CardCode;
    @SerializedName("CardName")
    String CardName;
    @SerializedName("Address")
    String Address;
    @SerializedName("DocCurrency")
    String DocCurrency;

    @SerializedName("DueDate")
    String DueDate;

    @SerializedName("DocEntry")
    String DocEntry;



    @SerializedName("TransferDate")
    String transferDate;



    @SerializedName("TransferSum")
    String transferSum;


    @SerializedName("BPLName")
    String BPLName;

    @SerializedName("Comments")
    String Comments;
    @SerializedName("IncomingPaymentInvoices")
    @Expose
    List<IncomingPaymentInvoices> IncomingPaymentInvoices=null;


    public String getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(String docEntry) {
        DocEntry = docEntry;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocNum() {
        return DocNum;
    }

    public void setDocNum(String docNum) {
        DocNum = docNum;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDocCurrency() {
        return DocCurrency;
    }

    public void setDocCurrency(String docCurrency) {
        DocCurrency = docCurrency;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getBPLName() {
        return BPLName;
    }

    public void setBPLName(String BPLName) {
        this.BPLName = BPLName;
    }

    public List<com.cinntra.ledure.model.IncomingPaymentInvoices> getIncomingPaymentInvoices() {
        return IncomingPaymentInvoices;
    }

    public void setIncomingPaymentInvoices(List<com.cinntra.ledure.model.IncomingPaymentInvoices> incomingPaymentInvoices) {
        IncomingPaymentInvoices = incomingPaymentInvoices;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getTransferSum() {
        return transferSum;
    }

    public void setTransferSum(String transferSum) {
        this.transferSum = transferSum;
    }
}
