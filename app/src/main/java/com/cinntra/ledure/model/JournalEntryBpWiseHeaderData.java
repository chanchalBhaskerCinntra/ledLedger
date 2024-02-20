package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class JournalEntryBpWiseHeaderData implements Serializable {
    @SerializedName("CardCode")
    public String cardCode;
    @SerializedName("CardName")
    public String cardName;
    @SerializedName("OpeningBalance")
    public String openingBalance;
    @SerializedName("ClosingBalance")
    public String closingBalance;



    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public ArrayList<JournalEntryLineBodyData> getJournalEntryLines() {
        return journalEntryLines;
    }

    public void setJournalEntryLines(ArrayList<JournalEntryLineBodyData> journalEntryLines) {
        this.journalEntryLines = journalEntryLines;
    }

    @SerializedName("JournalEntryLines")
    public ArrayList<JournalEntryLineBodyData> journalEntryLines;

}
