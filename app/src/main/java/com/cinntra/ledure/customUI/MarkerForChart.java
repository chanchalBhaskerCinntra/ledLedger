package com.cinntra.ledure.customUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.cinntra.ledure.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

@SuppressLint("ViewConstructor")
public class MarkerForChart extends MarkerView {

    private final TextView tvContent;

    public MarkerForChart (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY()); // set the entry-value as the display text
    }

//    @Override
//    public int getXOffset(float xpos) {
//        // this will center the marker-view horizontally
//        return -(getWidth() / 2);
//    }


    @Override
    public MPPointF getOffset() {
        return super.getOffset();
    }


}
