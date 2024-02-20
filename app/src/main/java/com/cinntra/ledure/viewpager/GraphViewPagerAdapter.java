package com.cinntra.ledure.viewpager;



import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cinntra.ledure.databinding.ItemGraphsOnDashboardBinding;

import java.util.List;

public class GraphViewPagerAdapter extends RecyclerView.Adapter<GraphViewPagerAdapter.FakeLiveMatchViewHolder> {
    private List<Integer> mList;
    private Handler myHandler = new Handler();

    public GraphViewPagerAdapter(List<Integer> list) {
        mList = list;
    }

    static class FakeLiveMatchViewHolder extends RecyclerView.ViewHolder {
        ItemGraphsOnDashboardBinding binding;

        FakeLiveMatchViewHolder(@NonNull ItemGraphsOnDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    @NonNull
    @Override
    public FakeLiveMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FakeLiveMatchViewHolder(
                ItemGraphsOnDashboardBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FakeLiveMatchViewHolder holder, int position) {
        int cur = mList.get(position);


        // Rest of your code for handling the view updates and animations
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

