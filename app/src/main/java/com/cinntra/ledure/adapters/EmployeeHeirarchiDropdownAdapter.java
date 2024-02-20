package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.model.EmployeeValue;


import java.util.List;


public class EmployeeHeirarchiDropdownAdapter extends BaseAdapter {
    Context context;
    List<EmployeeValue> stagesList;
    LayoutInflater inflter;
   public EmployeeHeirarchiDropdownAdapter(Context context, List<EmployeeValue> stagesList)
      {
   this.context    = context;
   this.stagesList = stagesList;
   inflter = (LayoutInflater.from(context));
      }
    @Override
    public int getCount() {
        return stagesList.size();
    }

    @Override
    public EmployeeValue getItem(int position) {
        return stagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = inflter.inflate(R.layout.stages_spinner_item, null);
        TextView title = (TextView)v.findViewById(R.id.title);
//        if(!stagesList.get(position).getRole().equals("admin"))
        title.setText(stagesList.get(position).getSalesEmployeeName());
        return v;
    }
}
