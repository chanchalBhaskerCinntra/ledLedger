package com.cinntra.ledure.model;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class NewLogINResponse implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("token")
    private String token;
    @SerializedName("data")
    @Expose
    private ArrayList<NewLoginData> LogInDetail = new ArrayList<>();

    @SerializedName("SAP")
    @Expose
    private Sap sap;

    @SerializedName("TripExpenses")
    public ArrayList<DataAllTripExpense> tripExpenses;

    public final static Creator<NewLogINResponse> CREATOR = new Creator<NewLogINResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NewLogINResponse createFromParcel(android.os.Parcel in) {
            return new NewLogINResponse(in);
        }

        public NewLogINResponse[] newArray(int size) {
            return (new NewLogINResponse[size]);
        }

    };

    protected NewLogINResponse(android.os.Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((int) in.readValue((int.class.getClassLoader())));
        this.LogInDetail = ((ArrayList<NewLoginData>) in.readValue((NewLoginData.class.getClassLoader())));
        this.sap = ((Sap) in.readValue((Sap.class.getClassLoader())));
        // this.tripExpenses = ((DataAllTripExpense) in.readValue((DataAllTripExpense.class.getClassLoader())));

    }

    /**
     * No args constructor for use in serialization
     */
    public NewLogINResponse() {
    }

    /**
     * @param sap
     * @param LogInDetail
     * @param message
     * @param status
     */
    public NewLogINResponse(String message, int status, ArrayList<NewLoginData> LogInDetail, Sap sap) {
        super();
        this.message = message;
        this.status = status;
        this.LogInDetail = LogInDetail;
        this.sap = sap;
    }


    public ArrayList<DataAllTripExpense> getTripExpenses() {
        return tripExpenses;
    }

    public void setTripExpenses(ArrayList<DataAllTripExpense> tripExpenses) {
        this.tripExpenses = tripExpenses;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<NewLoginData> getLogInDetail() {
        return LogInDetail;
    }

    public void setLogInDetail(ArrayList<NewLoginData> LogInDetail) {
        this.LogInDetail = LogInDetail;
    }

    public Sap getSap() {
        return sap;
    }

    public void setSap(Sap sap) {
        this.sap = sap;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(message);
        dest.writeValue(token);
        dest.writeValue(status);
        dest.writeValue(LogInDetail);
        dest.writeValue(sap);
    }

    public int describeContents() {
        return 0;
    }

}
