package com.cinntra.ledure.model;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateLead implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("numOfEmployee")
    @Expose
    private Integer numOfEmployee;
    @SerializedName("turnover")
    @Expose
    private String turnover;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("contactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("productInterest")
    @Expose
    private String productInterest;
    @SerializedName("assignedTo")
    @Expose
    private String assignedTo;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("employeeId")
    @Expose
    private Integer employeeId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("leadType")
    @Expose
    private String leadType;
    @SerializedName("Attach")
    @Expose
    private String Attach;
    @SerializedName("Caption")
    @Expose
    private String Caption;
    @SerializedName("projectAmount")
    @Expose
    private String projectAmount;
    @SerializedName("customerType")
    @Expose
    private String customerType;
    @SerializedName("stages")
    @Expose
    private String stages;

    @SerializedName("U_LAT")
    @Expose
    private String U_LAT;
 @SerializedName("U_LONG")
    @Expose
    private String U_LONG;


    public final static Creator<CreateLead> CREATOR = new Creator<CreateLead>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CreateLead createFromParcel(android.os.Parcel in) {
            return new CreateLead(in);
        }

        public CreateLead[] newArray(int size) {
            return (new CreateLead[size]);
        }

    }
            ;

    protected CreateLead(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.companyName = ((String) in.readValue((String.class.getClassLoader())));
        this.numOfEmployee = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.turnover = ((String) in.readValue((String.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.contactPerson = ((String) in.readValue((String.class.getClassLoader())));
        this.designation = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.productInterest = ((String) in.readValue((String.class.getClassLoader())));
        this.assignedTo = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.leadType = ((String) in.readValue((String.class.getClassLoader())));
        this.Attach = ((String) in.readValue((String.class.getClassLoader())));
        this.Caption = ((String) in.readValue((String.class.getClassLoader())));
        this.projectAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.customerType = ((String) in.readValue((String.class.getClassLoader())));
        this.stages = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public CreateLead() {
    }

    /**
     *
     * @param date
     * @param numOfEmployee
     * @param productInterest
     * @param companyName
     * @param contactPerson
     * @param employeeId
     * @param source
     * @param message
     * @param assignedTo
     * @param phoneNumber
     * @param location
     * @param designation
     * @param turnover
     * @param email
     * @param timestamp
     */
    public CreateLead(Integer id, String date, String location, String companyName, Integer numOfEmployee, String turnover, String source, String contactPerson, String designation, String phoneNumber, String message, String email, String productInterest, String assignedTo, String timestamp, Integer employeeId, String status, String leadType) {
        super();
        this.date = date;
        this.id = id;
        this.location = location;
        this.companyName = companyName;
        this.numOfEmployee = numOfEmployee;
        this.turnover = turnover;
        this.source = source;
        this.contactPerson = contactPerson;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.email = email;
        this.productInterest = productInterest;
        this.assignedTo = assignedTo;
        this.timestamp = timestamp;
        this.employeeId = employeeId;
        this.status = status;
        this.leadType = leadType;
    }


    public String getU_LAT() {
        return U_LAT;
    }

    public void setU_LAT(String u_LAT) {
        U_LAT = u_LAT;
    }

    public String getU_LONG() {
        return U_LONG;
    }

    public void setU_LONG(String u_LONG) {
        U_LONG = u_LONG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getNumOfEmployee() {
        return numOfEmployee;
    }

    public void setNumOfEmployee(Integer numOfEmployee) {
        this.numOfEmployee = numOfEmployee;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductInterest() {
        return productInterest;
    }

    public void setProductInterest(String productInterest) {
        this.productInterest = productInterest;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getAttach() {
        return Attach;
    }

    public void setAttach(String attach) {
        Attach = attach;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(String projectAmount) {
        this.projectAmount = projectAmount;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getStages() {
        return stages;
    }

    public void setStages(String stages) {
        this.stages = stages;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(date);
        dest.writeValue(location);
        dest.writeValue(companyName);
        dest.writeValue(numOfEmployee);
        dest.writeValue(turnover);
        dest.writeValue(source);
        dest.writeValue(contactPerson);
        dest.writeValue(designation);
        dest.writeValue(phoneNumber);
        dest.writeValue(message);
        dest.writeValue(email);
        dest.writeValue(productInterest);
        dest.writeValue(assignedTo);
        dest.writeValue(timestamp);
        dest.writeValue(employeeId);
        dest.writeValue(status);
        dest.writeValue(leadType);
        dest.writeValue(Attach);
        dest.writeValue(Caption);
        dest.writeValue(projectAmount);
        dest.writeValue(customerType);
        dest.writeValue(stages);
    }

    public int describeContents() {
        return 0;
    }

}
