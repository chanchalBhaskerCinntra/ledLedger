package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


  public class IndustryItem implements Serializable {
    @SerializedName("IndustryName")
    String IndustryName;
    @SerializedName("IndustryCode")
    String IndustryCode;
    @SerializedName("IndustryDescription")
    String IndustryDescription;

    public String getIndustryName() {
        return IndustryName;
    }

    public void setIndustryName(String industryName) {
        IndustryName = industryName;
    }

    public String getIndustryCode() {
        return IndustryCode;
    }

    public void setIndustryCode(String industryCode) {
        IndustryCode = industryCode;
    }

    public String getIndustryDescription() {
        return IndustryDescription;
    }

    public void setIndustryDescription(String industryDescription) {
        IndustryDescription = industryDescription;
    }
}
