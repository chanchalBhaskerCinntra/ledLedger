package com.cinntra.ledure.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingOrderHeaderOrderWiseData implements Serializable {

    public ArrayList<OrderWisePendingOrderData> orderwise;

    public ArrayList<OrderWisePendingOrderData> getOrderwise() {
        return orderwise;
    }

    public void setOrderwise(ArrayList<OrderWisePendingOrderData> orderwise) {
        this.orderwise = orderwise;
    }
}
