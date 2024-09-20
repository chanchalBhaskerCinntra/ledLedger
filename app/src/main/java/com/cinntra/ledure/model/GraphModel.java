package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GraphModel implements Serializable {

    @SerializedName("MonthlySales")
    String MonthlySales;

    @SerializedName("last_MonthlySales")
    String lastMonthlySales;
    @SerializedName("FinanYr")
    String FinanYr;
   @SerializedName("Month")
    String Month;

    @SerializedName("Year")
    String Year;

    public String getLastMonthlySales() {
        return lastMonthlySales;
    }

    public void setLastMonthlySales(String lastMonthlySales) {
        this.lastMonthlySales = lastMonthlySales;
    }

    public String getMonthlySales() {
        return MonthlySales;
    }

    public void setMonthlySales(String monthlySales) {
        MonthlySales = monthlySales;
    }

    public String getFinanYr() {
        return FinanYr;
    }

    public void setFinanYr(String finanYr) {
        FinanYr = finanYr;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
