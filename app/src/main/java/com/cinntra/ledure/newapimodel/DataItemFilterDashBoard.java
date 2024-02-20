package com.cinntra.ledure.newapimodel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.cinntra.roomdb.SubGroupItemConverter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



@Entity(tableName = "item_filter_table")
public class DataItemFilterDashBoard {
    @SerializedName("GroupName")
    public String groupName;

    @NonNull
    @PrimaryKey
    @SerializedName("GroupCode")
    public String groupCode;
    @SerializedName("TotalPrice")
    public double totalPrice;
    @SerializedName("TotalQty")
    public int totalQty;

    @TypeConverters(SubGroupItemConverter.class)
    @SerializedName("SubGroup")
    public ArrayList<SubGroupItemStock> subGroup;


    public ArrayList<SubGroupItemStock> getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(ArrayList<SubGroupItemStock> subGroup) {
        this.subGroup = subGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}
