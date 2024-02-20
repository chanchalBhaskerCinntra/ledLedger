package com.cinntra.ledure.model;

public class BodyItemList {

    public Integer PriceListId;
    public Integer PageNo;
    public Integer MaxSize;
    public Integer CatID;
    public String  WarehouseCode;

    public Integer getPriceListId() {
        return PriceListId;
    }

    public void setPriceListId(Integer priceListId) {
        PriceListId = priceListId;
    }

    public Integer getPageNo() {
        return PageNo;
    }

    public void setPageNo(Integer pageNo) {
        PageNo = pageNo;
    }

    public Integer getMaxSize() {
        return MaxSize;
    }

    public void setMaxSize(Integer maxSize) {
        MaxSize = maxSize;
    }

    public Integer getCatID() {
        return CatID;
    }

    public void setCatID(Integer catID) {
        CatID = catID;
    }

    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }
}
