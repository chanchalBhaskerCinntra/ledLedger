package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class InventoryResponse implements Serializable {
    @SerializedName("value")
    ArrayList<Inventory> value;

    public ArrayList<Inventory> getValue()
     {
     return value;
     }
    public void setValue(ArrayList<Inventory> value)
      {
    this.value = value;
      }
}
