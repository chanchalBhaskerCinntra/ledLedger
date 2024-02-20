package com.cinntra.ledure.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "ledger_zone")
public class DataZoneGroup implements Serializable {

    @SerializedName("GroupName")
    public String groupName;
    @NonNull
    @PrimaryKey
    @SerializedName("GroupCode")
    public String groupCode;
    @SerializedName("TotalSales")
    public float totalSales;

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

    public float getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(float totalSales) {
        this.totalSales = totalSales;
    }
}
