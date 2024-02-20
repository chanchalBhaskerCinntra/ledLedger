package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.model.StateData;

import java.util.List;

public class StateAdapter extends BaseAdapter {

    Context context;
    List<StateData> statelist;
    LayoutInflater inflater;
    public StateAdapter(Context context, List<StateData> statelist) {
        this.context = context;
        this.statelist = statelist;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return statelist.size();
    }

    @Override
    public Object getItem(int position) {
        return statelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = inflater.inflate(R.layout.stages_spinner_item, null);
        TextView title = (TextView)v.findViewById(R.id.title);
        title.setText(statelist.get(position).getName());
        return v;
    }
}
