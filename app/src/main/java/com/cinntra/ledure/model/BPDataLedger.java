package com.cinntra.ledure.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BPDataLedger implements Serializable {

    @SerializedName("CardName")
    private String cardName;

    @SerializedName("CardCode")
    private String cardCode;

    @SerializedName("GSTIN")
    private String gstIn;
    @SerializedName("ContactPerson")
    private String ContactPerson;

    @SerializedName("GroupName")
    private String groupName;
    @SerializedName("CreditLimit")
    private String creditLimit;
    @SerializedName("CreditLimitDayes")
    private String creditLimitDayes;

    @SerializedName("EmailAddress")
    private String EmailAddress;

    @SerializedName("Phone1")
    private String Phone1;
    @SerializedName("BPAddress")
    private String BPAddress;



    public BPDataLedger(String cardName, String cardCode, String gstIn,
                        String groupName, String creditLimit, String creditLimitDayes
            ,String EmailAddress ,String Phone1,
    String BPAddress)
     {
        this.cardName = cardName;
        this.cardCode = cardCode;
        this.gstIn = gstIn;
        this.groupName = groupName;
        this.creditLimit = creditLimit;
        this.creditLimitDayes = creditLimitDayes;
        this.EmailAddress = EmailAddress;
        this.Phone1 = Phone1;
        this.BPAddress = BPAddress;
    }


    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getBPAddress() {
        return BPAddress;
    }

    public void setBPAddress(String BPAddress) {
        this.BPAddress = BPAddress;
    }

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

    public String getGstIn() {
        return gstIn;
    }

    public void setGstIn(String gstIn) {
        this.gstIn = gstIn;
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

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }
}
