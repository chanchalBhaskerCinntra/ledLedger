package com.cinntra.ledure.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {
    private List<ParentItem> parentItemList;

    // Constructor, onCreateViewHolder, getItemCount, etc.


    public ParentAdapter(List<ParentItem> parentItemList) {
        this.parentItemList = parentItemList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item_layout, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParentViewHolder holder, int position) {
        ParentItem parentItem = parentItemList.get(position);
        holder.parentTitleTextView.setText(parentItem.getTitle());

        // Bind data to the parent item view

       // ChildAdapter childAdapter = new ChildAdapter(parentItem.getChildItemList());
     //   holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return parentItemList.size();
    }

    class ParentViewHolder extends RecyclerView.ViewHolder {
        RecyclerView childRecyclerView;
        TextView parentTitleTextView;

        ParentViewHolder(View itemView) {
            super(itemView);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
            parentTitleTextView = itemView.findViewById(R.id.parentTitleTextView);
        }
    }
}

