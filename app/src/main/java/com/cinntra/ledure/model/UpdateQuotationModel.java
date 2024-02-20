package com.cinntra.ledure.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class UpdateQuotationModel implements Parcelable {

    @SerializedName("CardCode")
    @Expose
    private String CardCode;
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
    @SerializedName("Comments")
    @Expose
    String Remarks;
    @SerializedName("SalesPersonCode")
    @Expose
    String SalesPersonCode;

   /* @SerializedName("SalesPersonCode")
    @Expose
    List<SalesEmployeeItem> SalesPersonCode;
    */
    @SerializedName("DiscountPercent")
    @Expose
    float DiscountPercent;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate;
    @SerializedName("CreateTime")
    @Expose
    String CreateTime;
    @SerializedName("UpdateDate")
    @Expose
    String UpdateDate;
    @SerializedName("UpdateTime")
    @Expose
    String UpdateTime;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("U_OPPID")
    @Expose
    String U_OPPID;

    @SerializedName("U_QUOTNM")
    @Expose
    String U_QUOTNM;


    @SerializedName("AddressExtension")
    @Expose
    private AddressExtension addressExtension;

    @SerializedName("DocumentLines")
    @Expose
    private ArrayList<DocumentLines> DocumentLines;



    @SerializedName("CardName")
    @Expose
    String CardName;
    @SerializedName("FreeDelivery")
    @Expose
    String FreeDelivery;
    @SerializedName("PayTermsGrpCode")
    @Expose
    String PayTermsGrpCode;
    @SerializedName("Link")
    @Expose
    String Link;
    @SerializedName("U_LONG")
    @Expose
    String U_LONG;
    @SerializedName("U_LAT")
    @Expose
    String U_LAT;
    @SerializedName("Unit")
    @Expose
    String Unit;
    @SerializedName("DeliveryCharge")
    @Expose
    String DeliveryCharge;
    @SerializedName("TermCondition")
    @Expose
    String TermCondition;
    @SerializedName("AdditionalCharges")
    @Expose
    String AdditionalCharges;
    @SerializedName("DeliveryTerm")
    @Expose
    String DeliveryTerm;
    @SerializedName("DeliveryMode")
    @Expose
    String DeliveryMode;
    @SerializedName("PaymentType")
    @Expose
    String PaymentType;

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

    public ArrayList<DocumentLines> getDocumentLines() {
        return DocumentLines;
    }

    public void setDocumentLines(ArrayList<DocumentLines> documentLines) {
        DocumentLines = documentLines;
    }

    public UpdateQuotationModel()
      {}


    protected UpdateQuotationModel(Parcel in) {
        this.CardCode         = ((String) in.readValue((String.class.getClassLoader())));
        this.DocumentDate     = ((String) in.readValue((String.class.getClassLoader())));
        this.PostingDate      = ((String) in.readValue((String.class.getClassLoader())));
        this.ValidDate        = ((String) in.readValue((String.class.getClassLoader())));
        this.salesPerson      = ((String) in.readValue((String.class.getClassLoader())));
        this.DiscountPercent  = ((Float) in.readValue((String.class.getClassLoader())));
        this.Remarks          = ((String) in.readValue((String.class.getClassLoader())));
        this.SalesPersonCode   = ((String) in.readValue((String.class.getClassLoader())));
        this.CreateDate   = ((String) in.readValue((String.class.getClassLoader())));
        this.CreateTime   = ((String) in.readValue((String.class.getClassLoader())));
        this.UpdateTime   = ((String) in.readValue((String.class.getClassLoader())));
        this.UpdateDate   = ((String) in.readValue((String.class.getClassLoader())));
        this.id   = ((String) in.readValue((String.class.getClassLoader())));
        this.U_OPPID   = ((String) in.readValue((String.class.getClassLoader())));
        this.addressExtension = ((AddressExtension) in.readValue((String.class.getClassLoader())));
        this.DocumentLines    = ((ArrayList) in.readValue((ArrayList.class.getClassLoader())));
        this.U_QUOTNM    = ((String) in.readValue((String.class.getClassLoader())));

        this.CardName    = ((String) in.readValue((String.class.getClassLoader())));
        this.FreeDelivery    = ((String) in.readValue((String.class.getClassLoader())));
        this.PaymentType    = ((String) in.readValue((String.class.getClassLoader())));
        this.Link    = ((String) in.readValue((String.class.getClassLoader())));
        this.U_LONG    = ((String) in.readValue((String.class.getClassLoader())));
        this.U_LAT    = ((String) in.readValue((String.class.getClassLoader())));
        this.Unit         = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryCharge     = ((String) in.readValue((String.class.getClassLoader())));
        this.TermCondition      = ((String) in.readValue((String.class.getClassLoader())));
        this.AdditionalCharges        = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryTerm      = ((String) in.readValue((String.class.getClassLoader())));
        this.DeliveryMode     = ((String) in.readValue((String.class.getClassLoader())));
        this.PaymentType       = ((String) in.readValue((String.class.getClassLoader())));

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(CardCode);
        dest.writeValue(DocumentDate);
        dest.writeValue(PostingDate);
        dest.writeValue(ValidDate);
        dest.writeValue(salesPerson);
        dest.writeValue(Remarks);
        dest.writeValue(DiscountPercent);
        dest.writeValue(SalesPersonCode);
        dest.writeValue(addressExtension);
        dest.writeValue(CreateTime);
        dest.writeValue(CreateDate);
        dest.writeValue(DocumentLines);
        dest.writeValue(UpdateDate);
        dest.writeValue(UpdateTime);
        dest.writeValue(U_OPPID);
        dest.writeValue(id);
        dest.writeValue(U_QUOTNM);

        dest.writeValue(CardName);
        dest.writeValue(FreeDelivery);
        dest.writeValue(PayTermsGrpCode);
        dest.writeValue(Link);
        dest.writeValue(U_LAT);
        dest.writeValue(U_LONG);
        dest.writeValue(Unit);
        dest.writeValue(DeliveryCharge);
        dest.writeValue(TermCondition);
        dest.writeValue(AdditionalCharges);
        dest.writeValue(DeliveryTerm);
        dest.writeValue(DeliveryMode);
        dest.writeValue(PaymentType);



    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpdateQuotationModel> CREATOR = new Creator<UpdateQuotationModel>() {
        @Override
        public UpdateQuotationModel createFromParcel(Parcel in) {
            return new UpdateQuotationModel(in);
        }

        @Override
        public UpdateQuotationModel[] newArray(int size) {
            return new UpdateQuotationModel[size];
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

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getU_OPPID() {
        return U_OPPID;
    }

    public void setU_OPPID(String u_OPPID) {
        U_OPPID = u_OPPID;
    }

    public String getU_QUOTNM() {
        return U_QUOTNM;
    }

    public void setU_QUOTNM(String u_QUOTNM) {
        U_QUOTNM = u_QUOTNM;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getFreeDelivery() {
        return FreeDelivery;
    }

    public void setFreeDelivery(String freeDelivery) {
        FreeDelivery = freeDelivery;
    }

    public String getPayTermsGrpCode() {
        return PayTermsGrpCode;
    }

    public void setPayTermsGrpCode(String payTermsGrpCode) {
        PayTermsGrpCode = payTermsGrpCode;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getU_LONG() {
        return U_LONG;
    }

    public void setU_LONG(String u_LONG) {
        U_LONG = u_LONG;
    }

    public String getU_LAT() {
        return U_LAT;
    }

    public void setU_LAT(String u_LAT) {
        U_LAT = u_LAT;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getTermCondition() {
        return TermCondition;
    }

    public void setTermCondition(String termCondition) {
        TermCondition = termCondition;
    }

    public String getAdditionalCharges() {
        return AdditionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        AdditionalCharges = additionalCharges;
    }

    public String getDeliveryTerm() {
        return DeliveryTerm;
    }

    public void setDeliveryTerm(String deliveryTerm) {
        DeliveryTerm = deliveryTerm;
    }

    public String getDeliveryMode() {
        return DeliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        DeliveryMode = deliveryMode;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }
}
