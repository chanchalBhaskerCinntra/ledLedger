package com.cinntra.ledure.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.cinntra.roomdb.BpAddressConverter;
import com.cinntra.roomdb.ContactEmployeeConverter;
import com.cinntra.roomdb.ListConverter;
import com.cinntra.roomdb.SaleEmployeeItemConverter;
import com.cinntra.roomdb.UTypeDataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "salesData")
public class BusinessPartnerData implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("CardCode")
    @Expose
    private String cardCode;
    @SerializedName("CardName")
    @Expose
    private String cardName;
    @SerializedName("Industry")
    @Expose
    private String industry;
    @SerializedName("CardType")
    @Expose
    private String cardType;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("Phone1")
    @Expose
    private String phone1;
    @SerializedName("DiscountPercent")
    @Expose
    private String discountPercent;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("IntrestRatePercent")
    @Expose
    private String intrestRatePercent;
    @SerializedName("CommissionPercent")
    @Expose
    private String commissionPercent;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @TypeConverters(ListConverter.class)
    @SerializedName("PayTermsGrpCode")
    @Expose
    private List<PayMentTerm> payTermsGrpCode;
    @SerializedName("CreditLimit")
    @Expose
    private String creditLimit;
    @SerializedName("CreditLimitLeft")
    @Expose
    private String CreditLimitLeft;
    @SerializedName("AttachmentEntry")
    @Expose
    private String attachmentEntry;

    @TypeConverters(SaleEmployeeItemConverter.class)
    @SerializedName("SalesPersonCode")
    @Expose
    private List<SalesEmployeeItem> salesPersonCode;
    @SerializedName("ContactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("U_PARENTACC")
    @Expose
    private String uParentacc;
    @SerializedName("U_BPGRP")
    @Expose
    private String uBpgrp;
    @SerializedName("U_CONTOWNR")
    @Expose
    private String uContownr;
    @SerializedName("U_RATING")
    @Expose
    private String uRating;


    @TypeConverters(UTypeDataConverter.class)
    @SerializedName("U_TYPE")
    @Expose
    private List<UTypeData> uType = null;
    @SerializedName("U_ANLRVN")
    @Expose
    private String uAnlrvn;
    @SerializedName("U_CURBAL")
    @Expose
    private String uCurbal;
    @SerializedName("U_ACCNT")
    @Expose
    private String uAccnt;
    @SerializedName("U_INVNO")
    @Expose
    private String uInvno;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("CreateTime")
    @Expose
    private String createTime;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("UpdateTime")
    @Expose
    private String updateTime;

    @SerializedName("U_LAT")
    @Expose
    private String U_LAT;
    @SerializedName("U_LONG")
    @Expose
    private String U_LONG;
    @SerializedName("GSTIN")
    private String gstIn;
    @SerializedName("TotalSales")
    @Expose
    private String TotalSales;

    @SerializedName("U_U_UTL_Zone")
    @Expose
    private String GroupName;
    /*@SerializedName("U_U_UTL_Zone")
    @Expose
    private String zone;*/


    @TypeConverters(BpAddressConverter.class)
    @SerializedName("BPAddresses")
    @Expose
    private List<BPAddress> bPAddresses = null;

    @TypeConverters(ContactEmployeeConverter.class)
    @SerializedName("ContactEmployees")
    @Expose
    private List<ContactEmployees> contactEmployees = null;
    private final static long serialVersionUID = 3537533647679235231L;

    @SerializedName("Unit")
    @Expose
    String Unit;

    @SerializedName("UnitName")
    @Expose
    String UnitName;

    /**
     * No args constructor for use in serialization
     */
    public BusinessPartnerData() {
    }

    /**
     * @param uRating
     * @param updateDate
     * @param notes
     * @param cardName
     * @param bPAddresses
     * @param contactPerson
     * @param industry
     * @param uParentacc
     * @param phone1
     * @param uCurbal
     * @param emailAddress
     * @param payTermsGrpCode
     * @param uType
     * @param creditLimit
     * @param currency
     * @param id
     * @param uBpgrp
     * @param createDate
     * @param website
     * @param uInvno
     * @param discountPercent
     * @param uAnlrvn
     * @param uContownr
     * @param cardCode
     * @param cardType
     * @param intrestRatePercent
     * @param updateTime
     * @param createTime
     * @param commissionPercent
     * @param uAccnt
     * @param attachmentEntry
     * @param salesPersonCode
     */
    public BusinessPartnerData(Integer id, String cardCode, String cardName, String industry, String cardType, String website, String emailAddress, String phone1, String discountPercent, String currency, String intrestRatePercent, String commissionPercent, String notes, List<PayMentTerm> payTermsGrpCode, String creditLimit, String attachmentEntry, List<SalesEmployeeItem> salesPersonCode, String contactPerson, String uParentacc, String uBpgrp, String uContownr, String uRating, List<UTypeData> uType, String uAnlrvn, String uCurbal, String uAccnt, String uInvno, String createDate, String createTime, String updateDate, String updateTime, String uLat, String U_LONG, List<BPAddress> bPAddresses, List<ContactEmployees> contactEmployees) {
        super();
        this.id = id;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.industry = industry;
        this.cardType = cardType;
        this.website = website;
        this.emailAddress = emailAddress;
        this.phone1 = phone1;
        this.discountPercent = discountPercent;
        this.currency = currency;
        this.intrestRatePercent = intrestRatePercent;
        this.commissionPercent = commissionPercent;
        this.notes = notes;
        this.payTermsGrpCode = payTermsGrpCode;
        this.creditLimit = creditLimit;
        this.attachmentEntry = attachmentEntry;
        this.salesPersonCode = salesPersonCode;
        this.contactPerson = contactPerson;
        this.uParentacc = uParentacc;
        this.uBpgrp = uBpgrp;
        this.uContownr = uContownr;
        this.uRating = uRating;
        this.uType = uType;
        this.uAnlrvn = uAnlrvn;
        this.uCurbal = uCurbal;
        this.uAccnt = uAccnt;
        this.uInvno = uInvno;
        this.createDate = createDate;
        this.createTime = createTime;
        this.updateDate = updateDate;
        this.updateTime = updateTime;
        this.U_LAT = uLat;
        this.U_LONG = U_LONG;
        this.bPAddresses = bPAddresses;
        this.contactEmployees = contactEmployees;
    }

    public String getuParentacc() {
        return uParentacc;
    }

    public void setuParentacc(String uParentacc) {
        this.uParentacc = uParentacc;
    }

    public String getuBpgrp() {
        return uBpgrp;
    }

    public void setuBpgrp(String uBpgrp) {
        this.uBpgrp = uBpgrp;
    }

    public String getuContownr() {
        return uContownr;
    }

    public void setuContownr(String uContownr) {
        this.uContownr = uContownr;
    }

    public String getuRating() {
        return uRating;
    }

    public void setuRating(String uRating) {
        this.uRating = uRating;
    }

    public List<UTypeData> getuType() {
        return uType;
    }

    public void setuType(List<UTypeData> uType) {
        this.uType = uType;
    }

    public String getuAnlrvn() {
        return uAnlrvn;
    }

    public void setuAnlrvn(String uAnlrvn) {
        this.uAnlrvn = uAnlrvn;
    }

    public String getuCurbal() {
        return uCurbal;
    }

    public void setuCurbal(String uCurbal) {
        this.uCurbal = uCurbal;
    }

    public String getuAccnt() {
        return uAccnt;
    }

    public void setuAccnt(String uAccnt) {
        this.uAccnt = uAccnt;
    }

    public String getuInvno() {
        return uInvno;
    }

    public void setuInvno(String uInvno) {
        this.uInvno = uInvno;
    }

    public List<BPAddress> getbPAddresses() {
        return bPAddresses;
    }

    public void setbPAddresses(List<BPAddress> bPAddresses) {
        this.bPAddresses = bPAddresses;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGstIn() {
        return gstIn;
    }

    public void setGstIn(String gstIn) {
        this.gstIn = gstIn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIntrestRatePercent() {
        return intrestRatePercent;
    }

    public void setIntrestRatePercent(String intrestRatePercent) {
        this.intrestRatePercent = intrestRatePercent;
    }

    public String getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(String commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PayMentTerm> getPayTermsGrpCode() {
        return payTermsGrpCode;
    }

    public void setPayTermsGrpCode(List<PayMentTerm> payTermsGrpCode) {
        this.payTermsGrpCode = payTermsGrpCode;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getAttachmentEntry() {
        return attachmentEntry;
    }

    public void setAttachmentEntry(String attachmentEntry) {
        this.attachmentEntry = attachmentEntry;
    }

    public List<SalesEmployeeItem> getSalesPersonCode() {
        return salesPersonCode;
    }

    public void setSalesPersonCode(List<SalesEmployeeItem> salesPersonCode) {
        this.salesPersonCode = salesPersonCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getUParentacc() {
        return uParentacc;
    }

    public void setUParentacc(String uParentacc) {
        this.uParentacc = uParentacc;
    }

    public String getUBpgrp() {
        return uBpgrp;
    }

    public void setUBpgrp(String uBpgrp) {
        this.uBpgrp = uBpgrp;
    }

    public String getUContownr() {
        return uContownr;
    }

    public void setUContownr(String uContownr) {
        this.uContownr = uContownr;
    }

    public String getURating() {
        return uRating;
    }

    public void setURating(String uRating) {
        this.uRating = uRating;
    }

    public List<UTypeData> getUType() {
        return uType;
    }

    public void setUType(List<UTypeData> uType) {
        this.uType = uType;
    }

    public String getUAnlrvn() {
        return uAnlrvn;
    }

    public void setUAnlrvn(String uAnlrvn) {
        this.uAnlrvn = uAnlrvn;
    }

    public String getUCurbal() {
        return uCurbal;
    }

    public void setUCurbal(String uCurbal) {
        this.uCurbal = uCurbal;
    }

    public String getUAccnt() {
        return uAccnt;
    }

    public void setUAccnt(String uAccnt) {
        this.uAccnt = uAccnt;
    }

    public String getUInvno() {
        return uInvno;
    }

    public void setUInvno(String uInvno) {
        this.uInvno = uInvno;
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


    public String getU_LAT() {
        return U_LAT;
    }

    public void setU_LAT(String u_LAT) {
        this.U_LAT = u_LAT;
    }

    public String getU_LONG() {
        return U_LONG;
    }

    public void setU_LONG(String u_LONG) {
        this.U_LONG = u_LONG;
    }

    public List<BPAddress> getBPAddresses() {
        return bPAddresses;
    }

    public void setBPAddresses(List<BPAddress> bPAddresses) {
        this.bPAddresses = bPAddresses;
    }

    public List<ContactEmployees> getContactEmployees() {
        return contactEmployees;
    }

    public void setContactEmployees(List<ContactEmployees> contactEmployees) {
        this.contactEmployees = contactEmployees;
    }

    public String getTotalSales() {
        return TotalSales;
    }

    public void setTotalSales(String totalSales) {
        TotalSales = totalSales;
    }

    public String getCreditLimitLeft() {
        return CreditLimitLeft;
    }

    public void setCreditLimitLeft(String creditLimitLeft) {
        CreditLimitLeft = creditLimitLeft;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }


}
