package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LeadFilter implements Serializable {
    @SerializedName("assignedTo")
    @Expose
    private String assignedTo;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("leadType")
    @Expose
    private String leadType;
    @SerializedName("PageNo")
    @Expose
    private int PageNo;
    @SerializedName("maxItem")
    @Expose
    private int maxItem;


    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int pageNo) {
        PageNo = pageNo;
    }

    public int getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(int maxItem) {
        this.maxItem = maxItem;
    }
}
