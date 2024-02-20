package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaxItem implements Serializable {
    @SerializedName("Code")
    String Code;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
