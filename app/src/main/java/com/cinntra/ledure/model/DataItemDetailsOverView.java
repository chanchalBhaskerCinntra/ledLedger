package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.List;

public class DataItemDetailsOverView implements Serializable {



    private String ItemCode;
    private String UnitPrice;
    private String TotalQty;
    private List<MonthGroupSalesList> MonthGroupSalesList;
    private String TotalItemPrice;
    private String TotalPrice;
    private String ItemName;
    private String TotalItemQty;
    private String NoOfInvoice;
    private String LastSalesDate;

    public String getItemCode() { return ItemCode; }
    public void setItemCode(String value) { this.ItemCode = value; }

    public String getUnitPrice() { return UnitPrice; }
    public void setUnitPrice(String value) { this.UnitPrice = value; }

    public String getTotalQty() { return TotalQty; }
    public void setTotalQty(String value) { this.TotalQty = value; }

    public List<MonthGroupSalesList> getMonthGroupSalesList() { return MonthGroupSalesList; }
    public void setMonthGroupSalesList(List<MonthGroupSalesList> value) { this.MonthGroupSalesList = value; }

    public String getTotalItemPrice() { return TotalItemPrice; }
    public void setTotalItemPrice(String value) { this.TotalItemPrice = value; }

    public String getTotalPrice() { return TotalPrice; }
    public void setTotalPrice(String value) { this.TotalPrice = value; }

    public String getItemName() { return ItemName; }
    public void setItemName(String value) { this.ItemName = value; }

    public String getTotalItemQty() { return TotalItemQty; }
    public void setTotalItemQty(String value) { this.TotalItemQty = value; }

    public String getNoOfInvoice() { return NoOfInvoice; }
    public void setNoOfInvoice(String value) { this.NoOfInvoice = value; }

    public String getLastSalesDate() { return LastSalesDate; }
    public void setLastSalesDate(String value) { this.LastSalesDate = value; }
}
