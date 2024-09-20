package com.cinntra.ledure.customUI;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.cinntra.ledure.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private List<String> dayList;
    private List<String> secondDayList; // Second list for the second dataset

    public CustomMarkerView(Context context, int layoutResource, List<String> dayList, List<String> secondDayList) {
        super(context, layoutResource);
        this.dayList = dayList;
        this.secondDayList = secondDayList;
        tvContent = findViewById(R.id.tvContent);
    }

    // This method will be called every time the MarkerView is redrawn
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        BarEntry barEntry = (BarEntry) e;
        int dataSetIndex = highlight.getDataSetIndex();
        int index = (int) barEntry.getX();

        // Debugging logs
        Log.d("CustomMarkerView", "DataSetIndex: " + dataSetIndex);
        Log.d("CustomMarkerView", "Entry X: " + barEntry.getX());
        Log.d("CustomMarkerView", "Entry Y: " + barEntry.getY());

        // Display content based on dataset
        if (dataSetIndex == 0) {
            if (index >= 0 && index < dayList.size()) {
                tvContent.setText(dayList.get(index));
            } else {
                tvContent.setText("" + barEntry.getY());
            }
        } else if (dataSetIndex == 1) {
            if (index >= 0 && index < secondDayList.size()) {
                tvContent.setText(secondDayList.get(index));
            } else {
                tvContent.setText("" + barEntry.getY());
            }
        } else {
            tvContent.setText("" + barEntry.getY());
        }

        super.refreshContent(e, highlight);
    }



    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        // Center the MarkerView horizontally, adjust position vertically
        float offset = -getWidth() / 2f;
        super.draw(canvas, posX + offset, posY - 60f);
    }


}
