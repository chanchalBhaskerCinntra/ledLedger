package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class DataOrderOne implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("TaxDate")
    public String taxDate;
    @SerializedName("DocDueDate")
    public String docDueDate;
    @SerializedName("DocNum")
    public String DocNum;


//    @SerializedName("ContactPersonCode")
//    public ArrayList<Object> contactPersonCode;
    @SerializedName("DiscountPercent")
    public String discountPercent;
    @SerializedName("DocDate")
    public String docDate;
    @SerializedName("CardCode")
    public String cardCode;
    @SerializedName("Comments")
    public String comments;
    @SerializedName("SalesPersonCode")
    public ArrayList<EmpDetails> salesPersonCode;
    @SerializedName("DocumentStatus")
    public String documentStatus;
    @SerializedName("DocCurrency")
    public String docCurrency;
    @SerializedName("DocTotal")
    public String docTotal;
    @SerializedName("CancelStatus")
    public String cancelStatus;
    @SerializedName("NetTotal")
    public String netTotal;
    @SerializedName("CardName")
    public String cardName;
    @SerializedName("VatSum")
    public String vatSum;
    @SerializedName("CreationDate")
    public String creationDate;
    @SerializedName("DocEntry")
    public String docEntry;
    @SerializedName("U_QUOTNM")
    public String u_QUOTNM;
    @SerializedName("U_QUOTID")
    public String u_QUOTID;
    @SerializedName("U_OPPID")
    public String u_OPPID;
    @SerializedName("U_OPPRNM")
    public String u_OPPRNM;
    @SerializedName("CreateDate")
    public String createDate;
    @SerializedName("CreateTime")
    public String createTime;
    @SerializedName("UpdateDate")
    public String updateDate;
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("PaymentType")
    public String paymentType;
    @SerializedName("DeliveryMode")
    public String deliveryMode;
    @SerializedName("DeliveryTerm")
    public String deliveryTerm;
    @SerializedName("AdditionalCharges")
    public String additionalCharges;
    @SerializedName("TermCondition")
    public String termCondition;
    @SerializedName("DeliveryCharge")
    public String deliveryCharge;
    @SerializedName("Unit")
    public String unit;
    @SerializedName("U_LAT")
    public String u_LAT;
    @SerializedName("U_LONG")
    public String u_LONG;
    @SerializedName("Link")
    public String link;
    @SerializedName("PayTermsGrpCode")
    public ArrayList<PayMentTerm> payTermsGrpCode;
    @SerializedName("ApproverId")
    public String approverId;
    @SerializedName("ApprovalStatus")
    public String approvalStatus;
    @SerializedName("FreeDelivery")
    public String freeDelivery;
//    @SerializedName("CreatedBy")
//    public ArrayList<CreatedBy> createdBy;
  /*  @SerializedName("AddressExtension")
    public AddressExtension addressExtension;*/
    @SerializedName("DocumentLines")
    public ArrayList<DocumentLines> documentLines;




//    @SerializedName("Attach")
//    public ArrayList<Object> attach;


    public DataOrderOne() {
    }


//    public String getComments() {
//        return Comments;
//    }
//
//    public void (String Comments) {
//        this.Comments = Comments;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(String taxDate) {
        this.taxDate = taxDate;
    }

    public String getDocDueDate() {
        return docDueDate;
    }

    public void setDocDueDate(String docDueDate) {
        this.docDueDate = docDueDate;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<EmpDetails> getSalesPersonCode() {
        return salesPersonCode;
    }

    public void setSalesPersonCode(ArrayList<EmpDetails> salesPersonCode) {
        this.salesPersonCode = salesPersonCode;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocCurrency() {
        return docCurrency;
    }

    public void setDocCurrency(String docCurrency) {
        this.docCurrency = docCurrency;
    }

    public String getDocTotal() {
        return docTotal;
    }

    public void setDocTotal(String docTotal) {
        this.docTotal = docTotal;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getVatSum() {
        return vatSum;
    }

    public void setVatSum(String vatSum) {
        this.vatSum = vatSum;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(String docEntry) {
        this.docEntry = docEntry;
    }

    public String getU_QUOTNM() {
        return u_QUOTNM;
    }

    public void setU_QUOTNM(String u_QUOTNM) {
        this.u_QUOTNM = u_QUOTNM;
    }

    public String getU_QUOTID() {
        return u_QUOTID;
    }

    public void setU_QUOTID(String u_QUOTID) {
        this.u_QUOTID = u_QUOTID;
    }

    public String getU_OPPID() {
        return u_OPPID;
    }

    public void setU_OPPID(String u_OPPID) {
        this.u_OPPID = u_OPPID;
    }

    public String getU_OPPRNM() {
        return u_OPPRNM;
    }

    public void setU_OPPRNM(String u_OPPRNM) {
        this.u_OPPRNM = u_OPPRNM;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getDeliveryTerm() {
        return deliveryTerm;
    }

    public void setDeliveryTerm(String deliveryTerm) {
        this.deliveryTerm = deliveryTerm;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getTermCondition() {
        return termCondition;
    }

    public void setTermCondition(String termCondition) {
        this.termCondition = termCondition;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getU_LAT() {
        return u_LAT;
    }

    public void setU_LAT(String u_LAT) {
        this.u_LAT = u_LAT;
    }

    public String getU_LONG() {
        return u_LONG;
    }

    public void setU_LONG(String u_LONG) {
        this.u_LONG = u_LONG;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<PayMentTerm> getPayTermsGrpCode() {
        return payTermsGrpCode;
    }

    public void setPayTermsGrpCode(ArrayList<PayMentTerm> payTermsGrpCode) {
        this.payTermsGrpCode = payTermsGrpCode;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(String freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

  /*  public AddressExtension getAddressExtension() {
        return addressExtension;
    }

    public void setAddressExtension(AddressExtension addressExtension) {
        this.addressExtension = addressExtension;
    }*/

    public ArrayList<DocumentLines> getDocumentLines() {
        return documentLines;
    }

    public void setDocumentLines(ArrayList<DocumentLines> documentLines) {
        this.documentLines = documentLines;
    }

    public String getDocNum() {
        return DocNum;
    }

    public void setDocNum(String docNum) {
        DocNum = docNum;
    }
}

