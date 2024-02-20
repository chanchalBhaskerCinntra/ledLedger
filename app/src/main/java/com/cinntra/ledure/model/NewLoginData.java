package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

public class NewLoginData{

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("Address")
	private String Address;

	@SerializedName("SalesEmployeeCode")
	private String salesEmployeeCode;

	@SerializedName("Email")
	private String email;

	@SerializedName("role")
	private String role;

	@SerializedName("lastLoginOn")
	private String lastLoginOn;

	@SerializedName("SalesEmployeeName")
	private String salesEmployeeName;

	@SerializedName("userName")
	private String userName;

	@SerializedName("Mobile")
	private String mobile;

	@SerializedName("branch")
	private String branch;

	@SerializedName("logedIn")
	private String logedIn;

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("companyID")
	private String companyID;

	@SerializedName("password")
	private String password;

	@SerializedName("Active")
	private String active;

	@SerializedName("middleName")
	private String middleName;

	@SerializedName("id")
	private int id;

	@SerializedName("position")
	private String position;

	@SerializedName("EmployeeID")
	private String employeeID;

	@SerializedName("passwordUpdatedOn")
	private String passwordUpdatedOn;

	@SerializedName("reportingTo")
	private String reportingTo;

	@SerializedName("timestamp")
	private String timestamp;
	@SerializedName("CheckInStatus")
	private String CheckInStatus;

	@SerializedName("Zone")
	private String Zone;


	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getZone() {
		return Zone;
	}

	public void setZone(String zone) {
		Zone = zone;
	}

	public String getCheckInStatus() {
		return CheckInStatus;
	}

	public void setCheckInStatus(String checkInStatus) {
		CheckInStatus = checkInStatus;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSalesEmployeeCode() {
		return salesEmployeeCode;
	}

	public void setSalesEmployeeCode(String salesEmployeeCode) {
		this.salesEmployeeCode = salesEmployeeCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLastLoginOn() {
		return lastLoginOn;
	}

	public void setLastLoginOn(String lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}

	public String getSalesEmployeeName() {
		return salesEmployeeName;
	}

	public void setSalesEmployeeName(String salesEmployeeName) {
		this.salesEmployeeName = salesEmployeeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getLogedIn() {
		return logedIn;
	}

	public void setLogedIn(String logedIn) {
		this.logedIn = logedIn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getPasswordUpdatedOn() {
		return passwordUpdatedOn;
	}

	public void setPasswordUpdatedOn(String passwordUpdatedOn) {
		this.passwordUpdatedOn = passwordUpdatedOn;
	}

	public String getReportingTo() {
		return reportingTo;
	}

	public void setReportingTo(String reportingTo) {
		this.reportingTo = reportingTo;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}