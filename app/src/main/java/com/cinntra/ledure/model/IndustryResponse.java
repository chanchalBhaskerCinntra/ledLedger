package com.cinntra.ledure.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IndustryResponse implements Serializable {
    @SerializedName("data")
    ArrayList<IndustryItem> value;

    public ArrayList<IndustryItem> getValue()
     {
     return value;
     }
    public void setValue(ArrayList<IndustryItem> value)
      {
    this.value = value;
      }
}
