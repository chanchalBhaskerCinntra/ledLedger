package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ItemPurchasedByListOfCustomersActivity;
import com.cinntra.ledure.activities.Sale_Group_Inovice_Reports;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataItemDashBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemListSelectingCategoryFromZoneAdapter extends RecyclerView.Adapter<ItemListSelectingCategoryFromZoneAdapter.ContactViewHolder> {
    Context context;
    String zoneCode;
    String groupFilter;
    List<DataItemDashBoard> branchList;


    public ItemListSelectingCategoryFromZoneAdapter(Context context1, List<DataItemDashBoard> branchList, String zoneCode, String groupFilter) {
        this.branchList = branchList;
        this.context = context1;
        this.zoneCode = zoneCode;
        this.groupFilter = groupFilter;
        this.tempList=new ArrayList<DataItemDashBoard>();


    }

    @NonNull
    @Override
    public ItemListSelectingCategoryFromZoneAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_dashboard, parent, false);
        return new ItemListSelectingCategoryFromZoneAdapter.ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemListSelectingCategoryFromZoneAdapter.ContactViewHolder holder, int position) {

        holder.itemName.setText(branchList.get(position).itemCode+" - " + branchList.get(position).itemName);

        holder.itemPriceIndividual.setText("Std price : " + Globals.numberToK(branchList.get(position).unitPrice));
        holder.itemPriceTotal.setText("Total Price: " + Globals.numberToK(branchList.get(position).totalPrice));
        holder.quantity.setText(" " +Globals.numberToK( branchList.get(position).totalQty) + "");
        if (Globals.numberToK( branchList.get(position).totalQty).equalsIgnoreCase("0")){
            holder.quantity.setVisibility(View.INVISIBLE);
        }else {
            holder.quantity.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPriceTotal, itemPriceIndividual, quantity;
        ImageView edit;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvItemName);
            itemPriceTotal = itemView.findViewById(R.id.tvtotAlPrice);
            itemPriceIndividual = itemView.findViewById(R.id.tvStandardPrice);
            quantity = itemView.findViewById(R.id.tvQuantityNos);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, ItemPurchasedByListOfCustomersActivity.class);
                    i.putExtra("itemcode", "" +branchList.get(getAdapterPosition()).itemCode);
                    i.putExtra("zoneCode", "" + zoneCode);
                    i.putExtra("itemname", "" +branchList.get(getAdapterPosition()).itemName);

                    /*Intent intent = new Intent(itemView.getContext(), Sale_Group_Inovice_Reports.class);
                    intent.putExtra("group", groupFilter);
                    intent.putExtra("code", zoneCode);
                    intent.putExtra("groupname", zoneCode);
                    intent.putExtra("stockFormWhere", "zonesub");
                    itemView.getContext().startActivity(intent);*///todo redirect to customer list screen --

                    context.startActivity(i);


                }
            });


        }

    }


    List<DataItemDashBoard> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataItemDashBoard st : tempList) {
                if (st.getItemName() != null && !st.getItemName().isEmpty()) {
                    if (st.getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void AllData(List<DataItemDashBoard> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }
}

