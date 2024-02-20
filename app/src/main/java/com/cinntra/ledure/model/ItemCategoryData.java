package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemCategoryData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CreatedTime")
    @Expose
    private String createdTime;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("UpdatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("CatID")
    @Expose
    private String CatID;
    @SerializedName("PriceListId")
    @Expose
    private String PriceListId;
    @SerializedName("Number")
    @Expose
    private String Number;
    @SerializedName("ItemsGroupCode")
    @Expose
    private String ItemsGroupCode;
    private final static long serialVersionUID = 2807572649633418845L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ItemCategoryData() {
    }

    /**
     *
     * @param updatedTime
     * @param createdDate
     * @param createdTime
     * @param id
     * @param updatedDate
     * @param categoryName
     * @param status
     */
    public ItemCategoryData(Integer id, String categoryName, Integer status, String createdDate, String createdTime, String updatedDate, String updatedTime,String CatID, String ItemsGroupCode) {
        super();
        this.id = id;
        this.categoryName = categoryName;
        this.status = status;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.updatedDate = updatedDate;
        this.updatedTime = updatedTime;
        this.CatID = CatID;
        this.ItemsGroupCode = ItemsGroupCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCatID() {
        return CatID;
    }

    public void setCatID(String catID) {
        CatID = catID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPriceListId() {
        return PriceListId;
    }

    public void setPriceListId(String priceListId) {
        PriceListId = priceListId;
    }

    public String getItemsGroupCode() {
        return ItemsGroupCode;
    }

    public void setItemsGroupCode(String itemsGroupCode) {
        ItemsGroupCode = itemsGroupCode;
    }
}