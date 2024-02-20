package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

public class WareHouseData {
    @SerializedName("BusinessPlaceID")
    public String businessPlaceID;
    @SerializedName("WarehouseCode")
    public String warehouseCode;
    @SerializedName("WarehouseName")
    public String warehouseName;

    public String getBusinessPlaceID() {
        return businessPlaceID;
    }

    public void setBusinessPlaceID(String businessPlaceID) {
        this.businessPlaceID = businessPlaceID;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
