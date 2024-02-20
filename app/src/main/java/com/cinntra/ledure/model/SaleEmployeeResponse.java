package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SaleEmployeeResponse implements Serializable {


    /******************** DB Manager ***************************/
    public static final String TABLE_NAME = "SalesEmployees";

    public static final String COLUMN_SalesEmployeeCode = "_SalesEmployeeCode";
    public static final String COLUMN_SalesEmployeeName = "_SalesEmployeeName";



    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SalesEmployeeCode + " TEXT,"
                    + COLUMN_SalesEmployeeName + " TEXT,"
                    + ")";

    public SaleEmployeeResponse() {
    }
    public SaleEmployeeResponse(String v1,String v2) {
    }




    @SerializedName("data")
    ArrayList<SalesEmployeeItem> value;

    public ArrayList<SalesEmployeeItem> getValue() {
        return value;
    }

    public void setValue(ArrayList<SalesEmployeeItem> value) {
        this.value = value;
    }
}
