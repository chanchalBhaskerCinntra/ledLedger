package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.model.EventValue;

import java.util.ArrayList;
import java.util.List;


public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    Context context;
    ArrayList<EventValue> recentlist ;
    public RecentAdapter(Context context, List<EventValue> recentlist)
      {
    this.context    = context;
    this.recentlist    = (ArrayList<EventValue>) recentlist;

        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
      {
     View rootView = LayoutInflater.from(context).inflate(R.layout.coming_upadapter,parent,false);
     return new ViewHolder(rootView);
       }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {

        holder.title.setText(recentlist.get(position).getTitle());
        holder.details.setText(recentlist.get(position).getComment());
        holder.date.setText(recentlist.get(position).getCreateDate());



    }
    @Override
    public int getItemCount()
      {
    return recentlist.size();
      }



    class ViewHolder extends RecyclerView.ViewHolder
        {

        TextView date,details,title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            date = itemView.findViewById(R.id.date);


        }
    }
}
