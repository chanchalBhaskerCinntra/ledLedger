package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Receivable_JE_Credit implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("DocNum")
    private String DocNum;
    @SerializedName("DocTotal")
    private String DocTotal;
    @SerializedName("DocType")
    private String DocType;
    @SerializedName("DocDate")
    private String DocDate;


    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
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

    public String getDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(String docTotal) {
        DocTotal = docTotal;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }
}
