package com.cinntra.ledure.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class AddQuotation implements Parcelable {

    @SerializedName("CardCode")
    @Expose
    private String CardCode;
    @SerializedName("CardName")
    @Expose
    private String CardName;
    @SerializedName("U_OPPRNM")
    @Expose
    String OpportunityName;

    @SerializedName("DocDate")
    @Expose
    String PostingDate;
    @SerializedName("DocDueDate")
    @Expose
    String ValidDate;
    @SerializedName("TaxDate")
    @Expose
    String DocumentDate;
    @SerializedName("ContactPersonCode")
    @Expose
    String salesPerson;
    @SerializedName("SalesPersonCode")
    @Expose
    String SalesPersonCode;

    @SerializedName("Comments")
    @Expose
    String Remarks;

    @SerializedName("BPL_IDAssignedToInvoice")
    @Expose
    String BPL_IDAssignedToInvoice;
    @SerializedName("BPLName")
    @Expose
    String BPLName;
    @SerializedName("UpdateTime")
    @Expose
    String UpdateTime;
    @SerializedName("UpdateDate")
    @Expose
    String UpdateDate;
    @SerializedName("CreateTime")
    @Expose
    String CreateTime;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate;
    @SerializedName("U_OPPID")
    @Expose
    String U_OPPID;
    @SerializedName("U_QUOTNM")
    @Expose
    String U_QUOTNM;
    @SerializedName("PaymentType")
    @Expose
    String PaymentType;
    @SerializedName("TermCondition")
    @Expose
    String TermCondition;
    @SerializedName("DeliveryCharge")
    @Expose
    String DeliveryCharge;
    @SerializedName("Unit")
    @Expose
    String Unit;
    @SerializedName("U_LAT")
    @Expose
    String U_LAT;
    @SerializedName("U_LONG")
    @Expose
    String U_LONG;
    @SerializedName("DeliveryMode")
    @Expose
    String DeliveryMode;
    @SerializedName("DeliveryTerm")
    @Expose
    String DeliveryTerm;
    @SerializedName("AdditionalCharges")
    @Expose
    String AdditionalCharges;
    @SerializedName("CreatedBy")
    @Expose
    String CreatedBy;
    @SerializedName("Link")
    @Expose
    String Link;
    @SerializedName("FreeDelivery")
    @Expose
    String FreeDelivery;
    @SerializedName("PayTermsGrpCode")
    @Expose
    String PayTermsGrpCode;
    @SerializedName("DiscountPercent")
    @Expose
    float DiscountPercent;

    @SerializedName("AddressExtension")
    @Expose
    private AddressExtension addressExtension;

    @SerializedName("DocumentLines")
    @Expose
    private ArrayList<DocumentLines> DocumentLines;

    @SerializedName("U_QUOTID")
    @Expose
    String U_QUOTID;


    @SerializedName("WarehouseCode")
    @Expose
    String WarehouseCode;


    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public ArrayList<com.cinntra.ledure.model.DocumentLines> getDocumentLines() {
        return DocumentLines;
    }



    public void setDocumentLines(ArrayList<com.cinntra.ledure.model.DocumentLines> documentLines) {
        DocumentLines = documentLines;
    }

    public AddQuotation()
      {}


    protected AddQuotation(Parcel in) {
        this.CardCode         = ((String) in.readValue((String.class.getClassLoader())));
        this.DocumentDate     = ((String) in.readValue((String.class.getClassLoader())));
        this.PostingDate      = ((String) in.readValue((String.class.getClassLoader())));
        this.ValidDate        = ((String) in.readValue((String.class.getClassLoader())));
        this.salesPerson      = ((String) in.readValue((String.class.getClassLoader())));
        this.Remarks          = ((String) in.readValue((String.class.getClassLoader())));
        this.BPL_IDAssignedToInvoice  = ((String) in.readValue((String.class.getClassLoader())));
        this.BPLName          = ((String) in.readValue((String.class.getClassLoader())));
        this.DiscountPercent  = ((Float) in.readValue((String.class.getClassLoader())));
        this.addressExtension = ((AddressExtension) in.readValue((String.class.getClassLoader())));
        this.DocumentLines    = ((ArrayList) in.readValue((ArrayList.class.getClassLoader())));
        this.U_OPPID          = ((String) in.readValue((String.class.getClassLoader())));
        this.CreateDate       = ((String) in.readValue((String.class.getClassLoader())));
        this.CreateTime       = ((String) in.readValue((String.class.getClassLoader())));
        this.UpdateDate       = ((String) in.readValue((String.class.getClassLoader())));
        this.UpdateTime       = ((String) in.readValue((String.class.getClassLoader())));
        this.CardName         = ((String) in.readValue((String.class.getClassLoader())));
        this.U_QUOTNM   = ((String) in.readValue((String.class.getClassLoader())));
        this.PaymentType   = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryMode   = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryTerm   = ((String) in.readValue((String.class.getClassLoader())));
        this.AdditionalCharges   = ((String) in.readValue((String.class.getClassLoader())));
        this.TermCondition   = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryCharge   = ((String) in.readValue((String.class.getClassLoader())));
        this.Unit   = ((String) in.readValue((String.class.getClassLoader())));
        this.U_LAT   = ((String) in.readValue((String.class.getClassLoader())));
        this.U_LONG   = ((String) in.readValue((String.class.getClassLoader())));
        this.CreatedBy   = ((String) in.readValue((String.class.getClassLoader())));
        this.Link   = ((String) in.readValue((String.class.getClassLoader())));
        this.U_QUOTID   = ((String) in.readValue((String.class.getClassLoader())));

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(CardCode);
        dest.writeValue(DocumentDate);
        dest.writeValue(BPL_IDAssignedToInvoice);
        dest.writeValue(BPLName);
        dest.writeValue(PostingDate);
        dest.writeValue(ValidDate);
        dest.writeValue(salesPerson);
        dest.writeValue(Remarks);
        dest.writeValue(DiscountPercent);
        dest.writeValue(addressExtension);
        dest.writeValue(DocumentLines);
        dest.writeValue(U_OPPID);
        dest.writeValue(CreateDate);
        dest.writeValue(CreateTime);
        dest.writeValue(UpdateTime);
        dest.writeValue(UpdateDate);
        dest.writeValue(CardName);
        dest.writeValue(U_QUOTNM);
        dest.writeValue(PaymentType);
        dest.writeValue(DeliveryMode);
        dest.writeValue(DeliveryTerm);
        dest.writeValue(AdditionalCharges);
        dest.writeValue(TermCondition);
        dest.writeValue(DeliveryCharge);
        dest.writeValue(Unit);
        dest.writeValue(U_LAT);
        dest.writeValue(U_LONG);
        dest.writeValue(U_QUOTID);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddQuotation> CREATOR = new Creator<AddQuotation>() {
        @Override
        public AddQuotation createFromParcel(Parcel in) {
            return new AddQuotation(in);
        }

        @Override
        public AddQuotation[] newArray(int size) {
            return new AddQuotation[size];
        }
    };


    public String getOpportunityName() {
        return OpportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        OpportunityName = opportunityName;
    }

    public String getPostingDate() {
        return PostingDate;
    }

    public void setPostingDate(String postingDate) {
        PostingDate = postingDate;
    }

    public String getValidDate() {
        return ValidDate;
    }

    public void setValidDate(String validDate) {
        ValidDate = validDate;
    }

    public String getDocumentDate() {
        return DocumentDate;
    }

    public void setDocumentDate(String documentDate) {
        DocumentDate = documentDate;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public AddressExtension getAddressExtension() {
        return addressExtension;
    }

    public void setAddressExtension(AddressExtension addressExtension) {
        this.addressExtension = addressExtension;
    }

    public String getBPL_IDAssignedToInvoice() {
        return BPL_IDAssignedToInvoice;
    }

    public void setBPL_IDAssignedToInvoice(String BPL_IDAssignedToInvoice) {
        this.BPL_IDAssignedToInvoice = BPL_IDAssignedToInvoice;
    }

    public String getBPLName() {
        return BPLName;
    }

    public void setBPLName(String BPLName) {
        this.BPLName = BPLName;
    }
    public String getSalesPersonCode() {
        return SalesPersonCode;
    }

    public void setSalesPersonCode(String salesPersonCode) {
        SalesPersonCode = salesPersonCode;
    }

    public float getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getU_OPPID() {
        return U_OPPID;
    }

    public void setU_OPPID(String u_OPPID) {
        U_OPPID = u_OPPID;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getU_QUOTNM() {
        return U_QUOTNM;
    }

    public void setU_QUOTNM(String u_QUOTNM) {
        U_QUOTNM = u_QUOTNM;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getDeliveryMode() {
        return DeliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        DeliveryMode = deliveryMode;
    }

    public String getDeliveryTerm() {
        return DeliveryTerm;
    }

    public void setDeliveryTerm(String deliveryTerm) {
        DeliveryTerm = deliveryTerm;
    }

    public String getAdditionalCharges() {
        return AdditionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        AdditionalCharges = additionalCharges;
    }

    public String getTermCondition() {
        return TermCondition;
    }

    public void setTermCondition(String termCondition) {
        TermCondition = termCondition;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
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

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getLink() {
        return Link;
    }

    public String getU_QUOTID() {
        return U_QUOTID;
    }

    public void setU_QUOTID(String u_QUOTID) {
        U_QUOTID = u_QUOTID;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getPayTermsGrpCode() {
        return PayTermsGrpCode;
    }

    public void setPayTermsGrpCode(String payTermsGrpCode) {
        PayTermsGrpCode = payTermsGrpCode;
    }

    public String getFreeDelivery() {
        return FreeDelivery;
    }

    public void setFreeDelivery(String freeDelivery) {
        FreeDelivery = freeDelivery;
    }

}
