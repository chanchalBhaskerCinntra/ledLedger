package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiptList implements Serializable {

    @SerializedName("ReceiptId")
    String ReceiptId;
    @SerializedName("DocEntry")
    String DocEntry;

    @SerializedName("CreateDate")
    String CreateDate;
    @SerializedName("DocTotal")
    String DocTotal;

    public String getReceiptId() {
        return ReceiptId;
    }

    public void setReceiptId(String receiptId) {
        ReceiptId = receiptId;
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
}
