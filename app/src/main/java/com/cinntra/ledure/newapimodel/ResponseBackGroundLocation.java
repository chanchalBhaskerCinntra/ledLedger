package com.cinntra.ledure.newapimodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBackGroundLocation {

    public String message;
    public String status;
    public String total_distance;
    public ArrayList<Datum> data;

    public String getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(String total_distance) {
        this.total_distance = total_distance;
    }

    public  class  Datum{
        public String id;
        @SerializedName("SalesEmployeeCode")
        public String salesEmployeeCode;
        @SerializedName("Latitude")
        public String latitude;
        @SerializedName("Longitude")
        public String longitude;
        @SerializedName("Address")
        public String address;
        @SerializedName("Create_Date")
        public String create_Date;
        @SerializedName("Create_Time")
        public String create_Time;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSalesEmployeeCode() {
            return salesEmployeeCode;
        }

        public void setSalesEmployeeCode(String salesEmployeeCode) {
            this.salesEmployeeCode = salesEmployeeCode;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreate_Date() {
            return create_Date;
        }

        public void setCreate_Date(String create_Date) {
            this.create_Date = create_Date;
        }

        public String getCreate_Time() {
            return create_Time;
        }

        public void setCreate_Time(String create_Time) {
            this.create_Time = create_Time;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}
