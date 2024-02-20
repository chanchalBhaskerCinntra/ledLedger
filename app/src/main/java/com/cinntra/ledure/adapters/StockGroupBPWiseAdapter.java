package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.SoldItemSubGroupActivity;
import com.cinntra.ledure.activities.Sold_ItemBasesActivity;
import com.cinntra.ledure.activities.SubGroupStockActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;

import java.util.ArrayList;

public class StockGroupBPWiseAdapter extends RecyclerView.Adapter<StockGroupBPWiseAdapter.ViewHolder> {
    Context context;
    ArrayList<DataItemFilterDashBoard> AllItemList;
    String name;

    String cardCode;
    String startDateFrag;
    String endDateFrag;

    public StockGroupBPWiseAdapter(Context context, ArrayList<DataItemFilterDashBoard> AllItemList, String name, String cardCode, String startDateFrag, String endDateFrag) {
        this.context = context;
        this.AllItemList = AllItemList;
        this.name = name;
        this.cardCode = cardCode;
        this.startDateFrag = startDateFrag;
        this.endDateFrag = endDateFrag;

    }

    @NonNull
    @Override
    public StockGroupBPWiseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_stock_group, parent, false);
        return new StockGroupBPWiseAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockGroupBPWiseAdapter.ViewHolder holder, int position) {
        holder.itemName.setText("" + AllItemList.get(position).getGroupName());
        holder.itemPriceIndividual.setText("Std price : " + AllItemList.get(position).getTotalPrice());
        holder.itemPriceTotal.setVisibility(View.VISIBLE);
        holder.quantity.setText("Qty : " + Globals.numberToK(String.valueOf(AllItemList.get(position).getTotalQty())));//Nos :
        holder.itemPriceTotal.setText(" " + Globals.numberToK(String.valueOf(AllItemList.get(position).getTotalPrice())));


    }

    @Override
    public int getItemCount() {
        return AllItemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPriceTotal, itemPriceIndividual, quantity;
        ConstraintLayout constraintLayout;
        ImageView edit;
        RecyclerView recyclerView;
        ImageButton ivArrow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvItemName);
            itemPriceTotal = itemView.findViewById(R.id.tvtotAlPrice);
            itemPriceIndividual = itemView.findViewById(R.id.tvStandardPrice);
            quantity = itemView.findViewById(R.id.tvQuantityNos);
            recyclerView = itemView.findViewById(R.id.rvInnerStockItem);
            constraintLayout = itemView.findViewById(R.id.constraintInnerItem);
            ivArrow = itemView.findViewById(R.id.ivArrowDrop);
            itemPriceIndividual.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, SoldItemSubGroupActivity.class);
                    i.putExtra("fromwhere", "soldSubGroup");
                    i.putExtra("zoneName", "");
                    i.putExtra("code", cardCode);
                    i.putExtra("startDate", startDateFrag);
                    i.putExtra("endDate", endDateFrag);
                    i.putExtra("ItemGroupCode", "" + AllItemList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + AllItemList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);




                }
            });


        }
    }
}
