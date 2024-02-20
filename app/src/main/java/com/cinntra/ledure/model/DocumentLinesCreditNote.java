package com.cinntra.ledure.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DocumentLinesCreditNote implements Parcelable, Serializable {
    @SerializedName("ItemCode")
    @Expose
    public String ItemCode;
    @SerializedName("id")
    @Expose
    public String id ;
    @SerializedName("Quantity")
    @Expose
    public String Quantity;
    @SerializedName("TaxCode")
    @Expose
    public String TaxCode;
    @SerializedName("UnitPrice")
    @Expose
    public String UnitPrice;
    @SerializedName("DiscountPercent")
    @Expose
   public float DiscountPercent;
    @SerializedName("WarehouseCode")
    @Expose
    public String WarehouseCode;
    @SerializedName("ItemDescription")
    @Expose
    public String ItemDescription;
    @SerializedName("ItemName")
    @Expose
    public String ItemName;
    @SerializedName("UomNo")
    @Expose
    public String UomNo;
    @SerializedName("UoS")
    @Expose
    public String Uos;
    @SerializedName("FreeText")
    @Expose
    String FreeText;
    @SerializedName("UnitWeight")
    @Expose
    public  String UnitWeight;
    @SerializedName("UnitPriceown")
    @Expose
      String UnitPriceown;
    @SerializedName("TaxRate")
    @Expose
    public  String TaxRate;




    public float getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getItemCode() {
    return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTaxCode() {
        return TaxCode;
    }

    public void setTaxCode(String taxCode) {
        TaxCode = taxCode;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }

    public DocumentLinesCreditNote()
      {}
    public DocumentLinesCreditNote(String itemName, String unitPrice)
      {
          this.ItemName = itemName;
          this.UnitPrice = unitPrice;
      }


    protected DocumentLinesCreditNote(Parcel in) {
        this.ItemCode         = ((String) in.readValue((String.class.getClassLoader())));
        this.Quantity         = ((String) in.readValue((String.class.getClassLoader())));
        this.TaxCode          = ((String) in.readValue((String.class.getClassLoader())));
        this.UnitPrice        = ((String) in.readValue((String.class.getClassLoader())));
        this.WarehouseCode    = ((String) in.readValue((String.class.getClassLoader())));
        this.ItemDescription    = ((String) in.readValue((String.class.getClassLoader())));
        this.ItemName    = ((String) in.readValue((String.class.getClassLoader())));
        this.DiscountPercent  = ((float) in.readValue((String.class.getClassLoader())));

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ItemCode);
        dest.writeValue(Quantity);
        dest.writeValue(TaxCode);
        dest.writeValue(UnitPrice);
        dest.writeValue(WarehouseCode);
        dest.writeValue(DiscountPercent);
        dest.writeValue(ItemDescription);
        dest.writeValue(ItemName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocumentLinesCreditNote> CREATOR = new Creator<DocumentLinesCreditNote>() {
        @Override
        public DocumentLinesCreditNote createFromParcel(Parcel in) {
            return new DocumentLinesCreditNote(in);
        }

        @Override
        public DocumentLinesCreditNote[] newArray(int size) {
            return new DocumentLinesCreditNote[size];
        }
    };

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getUomNo() {
        return UomNo;
    }

    public void setUomNo(String uomNo) {
        UomNo = uomNo;
    }

    public String getFreeText() {
        return FreeText;
    }

    public void setFreeText(String freeText) {
        FreeText = freeText;
    }

    public String getUnitWeight() {
        return UnitWeight;
    }

    public void setUnitWeight(String unitWeight) {
        UnitWeight = unitWeight;
    }

    public String getUnitPriceown() {
        return UnitPriceown;
    }

    public void setUnitPriceown(String unitPriceown) {
        UnitPriceown = unitPriceown;
    }

    public String getUos() {
        return Uos;
    }

    public void setUos(String uos) {
        Uos = uos;
    }

    public String getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(String taxRate) {
        TaxRate = taxRate;
    }
}
