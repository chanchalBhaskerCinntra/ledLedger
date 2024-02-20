package com.cinntra.ledure.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderWisePendingOrderData implements Serializable {

        @SerializedName("OrderID")
        @Expose
        public String orderID;
        @SerializedName("OrderDocEntry")
        @Expose
        public String orderDocEntry;
        @SerializedName("CardCode")
        @Expose
        public String cardCode;
        @SerializedName("CardName")
        @Expose
        public String cardName;
        @SerializedName("PendingAmount")
        @Expose
        public String pendingAmount;

        @SerializedName("PendingQty")
        @Expose
        public String pendingQty;
        @SerializedName("DocNum")
        @Expose
        public String DocNum;
 @SerializedName("DocDueDate")
        @Expose
        public String DocDueDate;

        public String getDocDueDate() {
                return DocDueDate;
        }

        public void setDocDueDate(String docDueDate) {
                DocDueDate = docDueDate;
        }

        public String getOrderID() {
                return orderID;
        }

        public void setOrderID(String orderID) {
                this.orderID = orderID;
        }

        public String getOrderDocEntry() {
                return orderDocEntry;
        }

        public void setOrderDocEntry(String orderDocEntry) {
                this.orderDocEntry = orderDocEntry;
        }

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

        public String getPendingAmount() {
                return pendingAmount;
        }

        public void setPendingAmount(String pendingAmount) {
                this.pendingAmount = pendingAmount;
        }

        public String getPendingQty() {
                return pendingQty;
        }

        public void setPendingQty(String pendingQty) {
                this.pendingQty = pendingQty;
        }

        public String getDocNum() {
                return DocNum;
        }

        public void setDocNum(String docNum) {
                DocNum = docNum;
        }
}
