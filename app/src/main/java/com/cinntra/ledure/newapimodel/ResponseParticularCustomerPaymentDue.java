package com.cinntra.ledure.newapimodel;

import com.cinntra.ledure.model.Receivable_JE_Credit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseParticularCustomerPaymentDue {

    public String message;
    public int status;
    public ArrayList<DataparticularCustomerpaymentDue> data;
    @SerializedName("TotalSales")
    public int totalSales;
    @SerializedName("TotalReceivePayment")
    public int totalReceivePayment;
    @SerializedName("DifferenceAmount")
    public double differenceAmount;
    @SerializedName("BPData")
    public ArrayList<DataParticularCustomerInfoPaymentDue> bPData;


    @SerializedName("DataListTotal")
    public int dataListTotal;

    @SerializedName("DataList")
    @Expose
    private List<Receivable_JE_Credit> DataList = null;


    public List<Receivable_JE_Credit> getDataList() {
        return DataList;
    }

    public void setDataList(List<Receivable_JE_Credit> dataList) {
        DataList = dataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<DataparticularCustomerpaymentDue> getData() {
        return data;
    }

    public void setData(ArrayList<DataparticularCustomerpaymentDue> data) {
        this.data = data;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalReceivePayment() {
        return totalReceivePayment;
    }

    public void setTotalReceivePayment(int totalReceivePayment) {
        this.totalReceivePayment = totalReceivePayment;
    }

    public double getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(double differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public ArrayList<DataParticularCustomerInfoPaymentDue> getbPData() {
        return bPData;
    }

    public void setbPData(ArrayList<DataParticularCustomerInfoPaymentDue> bPData) {
        this.bPData = bPData;
    }

    public int getDataListTotal() {
        return dataListTotal;
    }

    public void setDataListTotal(int dataListTotal) {
        this.dataListTotal = dataListTotal;
    }
}
