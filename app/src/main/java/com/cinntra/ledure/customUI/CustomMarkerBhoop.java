package com.cinntra.ledure.customUI;

import android.content.Context;

import android.graphics.Canvas;
import android.widget.TextView;


import com.cinntra.ledure.R;
import com.cinntra.ledure.model.GraphModel;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


import java.util.List;


public class CustomMarkerBhoop extends MarkerView {

    private List<String> runs;
    private final TextView tvContentView;


    public CustomMarkerBhoop(List<String> runs, Context c, int layoutId) {
        super(c, layoutId);
        this.runs = runs;
        tvContentView = (TextView) findViewById(R.id.tvContentBhoop);

    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-getWidth() / 2f, -getHeight());
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //  tvContentView.setText("" + runs);

      /*  if (e == null) {
            return;
        }*/

        int curRunId = (int) e.getX();
        String run = runs.get(curRunId);
        tvContentView.setText("24,23,98,80,876");

    }


    @Override
    public void setOffset(float offsetX, float offsetY) {
        super.setOffset(-(getWidth() / 2), -getHeight());
    }






}
