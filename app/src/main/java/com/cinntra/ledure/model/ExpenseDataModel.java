package com.cinntra.ledure.model;


import com.cinntra.ledure.newapimodel.AttachDocument;
import com.cinntra.ledure.newapimodel.EmployeeId;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ExpenseDataModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("trip_name")
    @Expose
    private String tripName;
    @SerializedName("type_of_expense")
    @Expose
    private String typeOfExpense;
    @SerializedName("expense_from")
    @Expose
    private String expenseFrom;
    @SerializedName("expense_to")
    @Expose
    private String expenseTo;
    @SerializedName("totalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("createdBy")
    @Expose
    private String createdBy = "";
    @SerializedName("updateDate")
    @Expose
    private String updateDate;
    @SerializedName("updateTime")
    @Expose
    private String updateTime;
    @SerializedName("updatedBy")
    @Expose
    private List<EmployeeValue> updatedBy = null;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("employeeId")
    @Expose
    private String employeeId = "";

    @SerializedName("Attach")
    @Expose
    private String attach = "";

    @SerializedName("startLat")
    @Expose
    private String startLat = "";


    @SerializedName("startLong")
    @Expose
    private String startLong = "";

    @SerializedName("endLat")
    @Expose
    private String endLat = "";

    @SerializedName("endLong")
    @Expose
    private String endLong = "";

    @SerializedName("travelDistance")
    @Expose
    private String travelDistance = "";


    private final static long serialVersionUID = -4928717263569093848L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ExpenseDataModel() {
    }

    /**
     *
     * @param updateDate
     * @param updatedBy
     * @param typeOfExpense
     * @param expenseFrom
     * @param updateTime
     * @param employeeId
     * @param totalAmount
     * @param createTime
     * @param createdBy
     * @param expenseTo
     * @param tripName
     * @param id
     * @param attach
     * @param remarks
     * @param createDate
     */
    public ExpenseDataModel(Integer id, String tripName, String typeOfExpense, String expenseFrom, String expenseTo, Integer totalAmount,
                            String createDate, String createTime, String createdBy,
                            String updateDate, String updateTime, List<EmployeeValue> updatedBy,

                            String remarks, String employeeId, String attach) {
        super();
        this.id = id;
        this.tripName = tripName;
        this.typeOfExpense = typeOfExpense;
        this.expenseFrom = expenseFrom;
        this.expenseTo = expenseTo;
        this.totalAmount = totalAmount;
        this.createDate = createDate;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.updateDate = updateDate;
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
        this.remarks = remarks;
        this.employeeId = employeeId;
        this.attach = attach;
    }


    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public String getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(String travelDistance) {
        this.travelDistance = travelDistance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTypeOfExpense() {
        return typeOfExpense;
    }

    public void setTypeOfExpense(String typeOfExpense) {
        this.typeOfExpense = typeOfExpense;
    }

    public String getExpenseFrom() {
        return expenseFrom;
    }

    public void setExpenseFrom(String expenseFrom) {
        this.expenseFrom = expenseFrom;
    }

    public String getExpenseTo() {
        return expenseTo;
    }

    public void setExpenseTo(String expenseTo) {
        this.expenseTo = expenseTo;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<EmployeeValue> getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(List<EmployeeValue> updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

}