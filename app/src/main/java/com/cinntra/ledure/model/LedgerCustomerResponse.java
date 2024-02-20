package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LedgerCustomerResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("TotalSales")
    @Expose
    private String TotalSales;
    @SerializedName("TotalReceivePayment")
    @Expose
    String TotalReceivePayment;
    @SerializedName("DifferenceAmount")
    @Expose
    String DifferenceAmount;

    @SerializedName("TotalCreditNote")
    @Expose
    double TotalCreditNote;
    @SerializedName("DataListTotal")
    @Expose
    String DataListTotal;


    @SerializedName("data")
    @Expose
    private List<LedgerCustomerData> data = null;



    @SerializedName("BPData")
    @Expose
    private List<BPDataLedger> bPData = null;

    @SerializedName("DataList")
    @Expose
    private List<Receivable_JE_Credit> DataList = null;




    private final static long serialVersionUID = -637633141134967407L;

    /**
     * No args constructor for use in serialization
     *
     */
    public LedgerCustomerResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public LedgerCustomerResponse(String message, String status, List<LedgerCustomerData> data,List<BPDataLedger> bpData) {
        super();
        this.message = message;
        this.status = status;
        this.data = data;
        this.bPData=bpData;
    }


    public double getTotalCreditNote() {
        return TotalCreditNote;
    }

    public void setTotalCreditNote(double totalCreditNote) {
        TotalCreditNote = totalCreditNote;
    }

    public List<BPDataLedger> getbPData() {
        return bPData;
    }

    public void setbPData(List<BPDataLedger> bPData) {
        this.bPData = bPData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LedgerCustomerData> getData() {
        return data;
    }

    public void setData(List<LedgerCustomerData> data) {
        this.data = data;
    }


    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }

    public String getTotalReceivePayment() {
        return TotalReceivePayment;
    }

    public void setTotalReceivePayment(String totalReceivePayment) {
        TotalReceivePayment = totalReceivePayment;
    }

    public String getDifferenceAmount() {
        return DifferenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        DifferenceAmount = differenceAmount;
    }

    public List<Receivable_JE_Credit> getDataList() {
        return DataList;
    }

    public void setDataList(List<Receivable_JE_Credit> dataList) {
        DataList = dataList;
    }

    public String getDataListTotal() {
        return DataListTotal;
    }

    public void setDataListTotal(String dataListTotal) {
        DataListTotal = dataListTotal;
    }
}
