package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JournalEntryLineBodyData implements Serializable {

    @SerializedName("Debit")
    public String debit;
    @SerializedName("Credit")
    public String credit;
    @SerializedName("Balance")
    public double balance;
    @SerializedName("DueDate")
    public String dueDate;
    @SerializedName("Reference1")
    public String reference1;

    @SerializedName("Original")
    public String Original;
    @SerializedName("OriginalJournal")
    public String OriginalJournal;
    @SerializedName("DocId")
    public String DocId;

    @SerializedName("id")
    public String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReference1() {
        return reference1;
    }

    public void setReference1(String reference1) {
        this.reference1 = reference1;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getOriginal() {
        return Original;
    }

    public void setOriginal(String original) {
        Original = original;
    }

    public String getOriginalJournal() {
        return OriginalJournal;
    }

    public void setOriginalJournal(String originalJournal) {
        OriginalJournal = originalJournal;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    @SerializedName("EntryType")
    public String entryType;

    @SerializedName("AccountName")
    public String AccountName;

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }
}
