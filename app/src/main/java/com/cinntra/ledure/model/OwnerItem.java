package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;

public class OwnerItem implements Serializable {
    @SerializedName("EmployeeID")
    @Expose
    String EmployeeID;
    @SerializedName("FirstName")
    @Expose
    String FirstName;
    @SerializedName("MiddleName")
    @Nullable
    @Expose
    String MiddleName;
    @SerializedName("LastName")
    @Expose

    String LastName;

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        if(MiddleName==null)
        return "";
        else
            return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
