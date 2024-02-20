package com.cinntra.roomdb;

import androidx.room.TypeConverter;

import com.cinntra.ledure.model.BPAddress;
import com.cinntra.ledure.model.OrderWisePendingOrderData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TypeConvertorOrderWisePendingOrder {

    private static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<OrderWisePendingOrderData> fromString(String value) {
        return gson.fromJson(value, new TypeToken<List<OrderWisePendingOrderData>>() {}.getType());
    }

    @TypeConverter
    public static String fromList(List<OrderWisePendingOrderData> list) {
        return gson.toJson(list);
    }
}
