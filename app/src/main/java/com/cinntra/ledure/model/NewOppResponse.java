package com.cinntra.ledure.model;

import java.util.List;
import android.os.Parcelable;

import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOppResponse implements Parcelable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<NewOpportunityRespose> data;
    public final static Creator<NewOppResponse> CREATOR = new Creator<NewOppResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NewOppResponse createFromParcel(android.os.Parcel in) {
            return new NewOppResponse(in);
        }

        public NewOppResponse[] newArray(int size) {
            return (new NewOppResponse[size]);
        }

    }
            ;

    protected NewOppResponse(android.os.Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (NewOpportunityRespose.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public NewOppResponse() {
    }

    /**
     *
     * @param data
     * @param message
     * @param status
     */
    public NewOppResponse(String message, String status, List<NewOpportunityRespose> data) {
        super();
        this.message = message;
        this.status = status;
        this.data = data;
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

    public List<NewOpportunityRespose> getData() {
        return data;
    }

    public void setData(List<NewOpportunityRespose> data) {
        this.data = data;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(message);
        dest.writeValue(status);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}