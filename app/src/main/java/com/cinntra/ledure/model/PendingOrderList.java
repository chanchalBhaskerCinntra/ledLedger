package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingOrderList implements Serializable {

    @SerializedName("Total")
    String total;
    @SerializedName("Partywise")
    public ArrayList<PartyWisePendingOrder> partywise;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<PartyWisePendingOrder> getPartywise() {
        return partywise;
    }

    public void setPartywise(ArrayList<PartyWisePendingOrder> partywise) {
        this.partywise = partywise;
    }
}
