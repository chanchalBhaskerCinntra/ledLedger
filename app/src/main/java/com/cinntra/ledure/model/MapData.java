package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapData implements Serializable {

    @SerializedName("Lat")
    String Lat;
    @SerializedName("Long")
    String Long;
    @SerializedName("Emp_Id")
    String Emp_Id;
    @SerializedName("Emp_Name")
    String Emp_Name;
    @SerializedName("UpdateDate")
    String UpdateDate;
        @SerializedName("UpdateTime")
    String UpdateTime;
    @SerializedName("Address")
    String Address;
    @SerializedName("type")
    String type;
    @SerializedName("shape")
    String shape;
    @SerializedName("remark")
    String remark;

    @SerializedName("ResourceId")
    String ResourceId;
    @SerializedName("SourceType")
    String SourceType;
    @SerializedName("ContactPerson")
    String ContactPerson;

    @SerializedName("ExpenseCost")
    String ExpenseCost;

    @SerializedName("ExpenseDistance")
    String ExpenseDistance;

    @SerializedName("ExpenseType")
    String ExpenseType;
    @SerializedName("ExpenseAttach")
    String ExpenseAttach;
    @SerializedName("ExpenseRemark")
    String ExpenseRemark;

    @SerializedName("Attach")
    String Attach;


    public MapData(String lat, String aLong, String emp_Id, String emp_Name,
                   String updateDate, String updateTime

    ) {
        Lat = lat;
        Long = aLong;
        Emp_Id = emp_Id;
        Emp_Name = emp_Name;
        UpdateDate = updateDate;
        UpdateTime = updateTime;
    }

    public MapData() {
    }

    public String getAttach() {
        return Attach;
    }

    public void setAttach(String attach) {
        Attach = attach;
    }

    public String getExpenseCost() {
        return ExpenseCost;
    }

    public void setExpenseCost(String expenseCost) {
        ExpenseCost = expenseCost;
    }

    public String getExpenseDistance() {
        return ExpenseDistance;
    }

    public void setExpenseDistance(String expenseDistance) {
        ExpenseDistance = expenseDistance;
    }

    public String getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(String expenseType) {
        ExpenseType = expenseType;
    }

    public String getExpenseAttach() {
        return ExpenseAttach;
    }

    public void setExpenseAttach(String expenseAttach) {
        ExpenseAttach = expenseAttach;
    }

    public String getExpenseRemark() {
        return ExpenseRemark;
    }

    public void setExpenseRemark(String expenseRemark) {
        ExpenseRemark = expenseRemark;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getEmp_Id() {
        return Emp_Id;
    }

    public void setEmp_Id(String emp_Id) {
        Emp_Id = emp_Id;
    }

    public String getEmp_Name() {
        return Emp_Name;
    }

    public void setEmp_Name(String emp_Name) {
        Emp_Name = emp_Name;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
    }

    public String getSourceType() {
        return SourceType;
    }

    public void setSourceType(String sourceType) {
        SourceType = sourceType;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }
}
