package com.cinntra.ledure.customUI;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> mValues;

    public MyXAxisValueFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);

        if(index >= mValues.size()){
            index = mValues.size()-1;
        }
        return mValues.get(index);
    }
}