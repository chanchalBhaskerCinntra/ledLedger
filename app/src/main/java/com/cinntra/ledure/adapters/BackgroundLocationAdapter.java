package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.databinding.ItemLocationBackgroundBinding;
import com.cinntra.ledure.newapimodel.ResponseBackGroundLocation;

import java.util.List;


public class BackgroundLocationAdapter extends RecyclerView.Adapter<BackgroundLocationAdapter.ItemViewHolder> {

    private List<ResponseBackGroundLocation.Datum> itemList;
    private Context context;

    public BackgroundLocationAdapter(List<ResponseBackGroundLocation.Datum> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLocationBackgroundBinding binding = ItemLocationBackgroundBinding.inflate(inflater, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ResponseBackGroundLocation.Datum item = itemList.get(position);
        holder.binding.tvTime.setText(item.getCreate_Time());
        holder.binding.tvLocation.setText(item.getAddress());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemLocationBackgroundBinding binding;

        public ItemViewHolder(ItemLocationBackgroundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

