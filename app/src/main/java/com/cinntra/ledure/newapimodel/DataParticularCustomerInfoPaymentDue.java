package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

public class DataParticularCustomerInfoPaymentDue {

    @SerializedName("CardName")
    public String cardName;
    @SerializedName("CardCode")
    public String cardCode;
    @SerializedName("EmailAddress")
    public String emailAddress;
    @SerializedName("ContactPerson")
    public String contactPerson;
    @SerializedName("Phone1")
    public String phone1;
    @SerializedName("GSTIN")
    public String gSTIN;
    @SerializedName("BPAddress")
    public String bPAddress;
    @SerializedName("GroupName")
    public String groupName;
    @SerializedName("CreditLimit")
    public String creditLimit;
    @SerializedName("CreditLimitDayes")
    public String creditLimitDayes;
    @SerializedName("AvgPayDays")
    public int avgPayDays;


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getgSTIN() {
        return gSTIN;
    }

    public void setgSTIN(String gSTIN) {
        this.gSTIN = gSTIN;
    }

    public String getbPAddress() {
        return bPAddress;
    }

    public void setbPAddress(String bPAddress) {
        this.bPAddress = bPAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCreditLimitDayes() {
        return creditLimitDayes;
    }

    public void setCreditLimitDayes(String creditLimitDayes) {
        this.creditLimitDayes = creditLimitDayes;
    }

    public int getAvgPayDays() {
        return avgPayDays;
    }

    public void setAvgPayDays(int avgPayDays) {
        this.avgPayDays = avgPayDays;
    }
}
