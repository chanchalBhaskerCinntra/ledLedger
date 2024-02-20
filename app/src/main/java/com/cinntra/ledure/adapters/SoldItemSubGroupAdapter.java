package com.cinntra.ledure.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ItemDashboardListActivity;
import com.cinntra.ledure.activities.Sale_Group_Inovice_Reports;
import com.cinntra.ledure.activities.Sold_ItemBasesActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.newapimodel.SubGroupItemStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SoldItemSubGroupAdapter extends RecyclerView.Adapter<SoldItemSubGroupAdapter.ChildViewHolder> {
    private List<SubGroupItemStock> childItemList;
    private String fromwhere = "";
    private String cardCode = "";
    private String startDate = "";
    private String endDate = "";
    private String zonCode, groupFIlter = "";

    public SoldItemSubGroupAdapter(List<SubGroupItemStock> childItemList, String fromwhere, String zonCode, String groupFIlter, String cardCode, String startDate, String endDate) {
        this.childItemList = childItemList;
        this.fromwhere = fromwhere;
        this.zonCode = zonCode;
        this.cardCode = cardCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupFIlter = groupFIlter;
        this.tempList = new ArrayList<SubGroupItemStock>();
    }

// Constructor, onCreateViewHolder, getItemCount, etc.

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_layout, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        SubGroupItemStock childItem = childItemList.get(position);
        holder.name.setText(childItem.getGroupName());

        holder.price.setText("Total Price: " + Globals.numberToK(String.valueOf(childItem.getTotalPrice())));
        holder.quantity.setText("QTY: " + String.valueOf(childItem.getTotalQty()));

        // Bind data to the child item view
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }


    class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;

        ChildViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSUbGroup);
            price = itemView.findViewById(R.id.priceSUbGroup);
            quantity = itemView.findViewById(R.id.quantitySUbGroup);
            itemView.setOnClickListener(view -> {

                if (fromwhere.equalsIgnoreCase("soldSubGroup")) {
                    Intent i = new Intent(itemView.getContext(), Sold_ItemBasesActivity.class);
                    i.putExtra("cardCode", cardCode);
                    i.putExtra("startDateFrag", startDate);
                    i.putExtra("endDateFrag", endDate);
                    i.putExtra("subGroupCode", childItemList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("name", childItemList.get(getAdapterPosition()).getGroupName());
                    itemView.getContext().startActivity(i);
                }

            });
        }


    }


    List<SubGroupItemStock> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        childItemList.clear();
        if (charText.length() == 0) {
            childItemList.addAll(tempList);
        } else {
            for (SubGroupItemStock st : tempList) {
                if (st.getGroupName() != null && !st.getGroupName().isEmpty()) {
                    if (st.getGroupName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        childItemList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }


    public void AllData(List<SubGroupItemStock> tmp) {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }


}
