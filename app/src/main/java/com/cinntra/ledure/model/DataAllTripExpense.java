package com.cinntra.ledure.model;

import java.util.List;

public class DataAllTripExpense {


        private String  id;
        private String BPType;
        private String BPName;
        private String CardCode;
        private String SalesPersonCode;
        private String ModeOfTransport;
        private String CheckInDate;
        private String CheckInTime;
        private String CheckInLat;
        private String CheckInLong;
        private String CheckInAttach;
        private String CheckInRemarks;
        private String CheckOutDate;
        private String CheckOutTime;
        private String CheckOutLat;
        private String CheckOutLong;
        private String CheckOutAttach;
        private String CheckOutRemarks;
        private String TotalDistanceAuto;
        private String TotalDistanceManual;
        private String TotalExpenses;
        private List<ExpenseNewDataModel> Expenses;




        // Getter Methods


    public List<ExpenseNewDataModel> getExpenses() {
        return Expenses;
    }

    public void setExpenses(List<ExpenseNewDataModel> expenses) {
        Expenses = expenses;
    }

    public String getId() {
            return id;
        }

        public String getBPType() {
            return BPType;
        }

        public String getBPName() {
            return BPName;
        }

        public String getCardCode() {
            return CardCode;
        }

        public String getSalesPersonCode() {
            return SalesPersonCode;
        }

        public String getModeOfTransport() {
            return ModeOfTransport;
        }

        public String getCheckInDate() {
            return CheckInDate;
        }

        public String getCheckInTime() {
            return CheckInTime;
        }

        public String getCheckInLat() {
            return CheckInLat;
        }

        public String getCheckInLong() {
            return CheckInLong;
        }

        public String getCheckInAttach() {
            return CheckInAttach;
        }

        public String getCheckInRemarks() {
            return CheckInRemarks;
        }

        public String getCheckOutDate() {
            return CheckOutDate;
        }

        public String getCheckOutTime() {
            return CheckOutTime;
        }

        public String getCheckOutLat() {
            return CheckOutLat;
        }

        public String getCheckOutLong() {
            return CheckOutLong;
        }

        public String getCheckOutAttach() {
            return CheckOutAttach;
        }

        public String getCheckOutRemarks() {
            return CheckOutRemarks;
        }

        public String getTotalDistanceAuto() {
            return TotalDistanceAuto;
        }

        public String getTotalDistanceManual() {
            return TotalDistanceManual;
        }

        public String getTotalExpenses() {
            return TotalExpenses;
        }

        // Setter Methods

        public void setId(String id) {
            this.id = id;
        }

        public void setBPType(String BPType) {
            this.BPType = BPType;
        }

        public void setBPName(String BPName) {
            this.BPName = BPName;
        }

        public void setCardCode(String CardCode) {
            this.CardCode = CardCode;
        }

        public void setSalesPersonCode(String SalesPersonCode) {
            this.SalesPersonCode = SalesPersonCode;
        }

        public void setModeOfTransport(String ModeOfTransport) {
            this.ModeOfTransport = ModeOfTransport;
        }

        public void setCheckInDate(String CheckInDate) {
            this.CheckInDate = CheckInDate;
        }

        public void setCheckInTime(String CheckInTime) {
            this.CheckInTime = CheckInTime;
        }

        public void setCheckInLat(String CheckInLat) {
            this.CheckInLat = CheckInLat;
        }

        public void setCheckInLong(String CheckInLong) {
            this.CheckInLong = CheckInLong;
        }

        public void setCheckInAttach(String CheckInAttach) {
            this.CheckInAttach = CheckInAttach;
        }

        public void setCheckInRemarks(String CheckInRemarks) {
            this.CheckInRemarks = CheckInRemarks;
        }

        public void setCheckOutDate(String CheckOutDate) {
            this.CheckOutDate = CheckOutDate;
        }

        public void setCheckOutTime(String CheckOutTime) {
            this.CheckOutTime = CheckOutTime;
        }

        public void setCheckOutLat(String CheckOutLat) {
            this.CheckOutLat = CheckOutLat;
        }

        public void setCheckOutLong(String CheckOutLong) {
            this.CheckOutLong = CheckOutLong;
        }

        public void setCheckOutAttach(String CheckOutAttach) {
            this.CheckOutAttach = CheckOutAttach;
        }

        public void setCheckOutRemarks(String CheckOutRemarks) {
            this.CheckOutRemarks = CheckOutRemarks;
        }

        public void setTotalDistanceAuto(String TotalDistanceAuto) {
            this.TotalDistanceAuto = TotalDistanceAuto;
        }

        public void setTotalDistanceManual(String TotalDistanceManual) {
            this.TotalDistanceManual = TotalDistanceManual;
        }

        public void setTotalExpenses(String TotalExpenses) {
            this.TotalExpenses = TotalExpenses;
        }

}
