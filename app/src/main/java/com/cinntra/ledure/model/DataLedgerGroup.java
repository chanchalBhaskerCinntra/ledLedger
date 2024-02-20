package com.cinntra.ledure.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "ledger_group")
public class DataLedgerGroup implements Serializable {

    @SerializedName("GroupName")
    public String groupName;
    @NonNull
    @PrimaryKey
    @SerializedName("GroupCode")
    public String groupCode;
    @SerializedName("TotalSales")
    public double totalSales;

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

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}
