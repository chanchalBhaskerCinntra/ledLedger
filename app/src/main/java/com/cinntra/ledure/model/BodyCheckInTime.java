package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

public class BodyCheckInTime implements Serializable {

    @SerializedName("BPType")
    public RequestBody bPType;
    @SerializedName("BPName")
    public RequestBody bPName;
    @SerializedName("CardCode")
    public RequestBody cardCode;
    @SerializedName("SalesPersonCode")
    public RequestBody salesPersonCode;
    @SerializedName("ModeOfTransport")
    public RequestBody modeOfTransport;
    @SerializedName("CheckInDate")
    public RequestBody checkInDate;
    @SerializedName("CheckInTime")
    public RequestBody checkInTime;
    @SerializedName("CheckInLat")
    public RequestBody checkInLat;
    @SerializedName("CheckInLong")
    public RequestBody checkInLong;
    @SerializedName("CheckInRemarks")
    public RequestBody checkInRemarks;
    @SerializedName("CheckInAttach")
    public MultipartBody.Part checkInAttach;


    public RequestBody getbPType() {
        return bPType;
    }

    public void setbPType(RequestBody bPType) {
        this.bPType = bPType;
    }

    public RequestBody getbPName() {
        return bPName;
    }

    public void setbPName(RequestBody bPName) {
        this.bPName = bPName;
    }

    public RequestBody getCardCode() {
        return cardCode;
    }

    public void setCardCode(RequestBody cardCode) {
        this.cardCode = cardCode;
    }

    public RequestBody getSalesPersonCode() {
        return salesPersonCode;
    }

    public void setSalesPersonCode(RequestBody salesPersonCode) {
        this.salesPersonCode = salesPersonCode;
    }

    public RequestBody getModeOfTransport() {
        return modeOfTransport;
    }

    public void setModeOfTransport(RequestBody modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    public RequestBody getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(RequestBody checkInDate) {
        this.checkInDate = checkInDate;
    }

    public RequestBody getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(RequestBody checkInTime) {
        this.checkInTime = checkInTime;
    }

    public RequestBody getCheckInLat() {
        return checkInLat;
    }

    public void setCheckInLat(RequestBody checkInLat) {
        this.checkInLat = checkInLat;
    }

    public RequestBody getCheckInLong() {
        return checkInLong;
    }

    public void setCheckInLong(RequestBody checkInLong) {
        this.checkInLong = checkInLong;
    }

    public RequestBody getCheckInRemarks() {
        return checkInRemarks;
    }

    public void setCheckInRemarks(RequestBody checkInRemarks) {
        this.checkInRemarks = checkInRemarks;
    }

    public MultipartBody.Part getCheckInAttach() {
        return checkInAttach;
    }

    public void setCheckInAttach(MultipartBody.Part checkInAttach) {
        this.checkInAttach = checkInAttach;
    }
}
