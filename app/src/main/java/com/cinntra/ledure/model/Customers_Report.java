package com.cinntra.ledure.model;

import java.io.Serializable;

public class Customers_Report implements Serializable {
    String customerName;
    String price;

    public Customers_Report(String customerName,String price)
       {
     this.customerName = customerName;
     this.price        = price;
        }


    public String getCustomerName() {
        return customerName;
     }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
