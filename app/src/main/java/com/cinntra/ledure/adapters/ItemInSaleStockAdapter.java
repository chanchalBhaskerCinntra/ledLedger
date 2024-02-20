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
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.DataItemInSalesCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemInSaleStockAdapter extends RecyclerView.Adapter<ItemInSaleStockAdapter.ContactViewHolder> {
    Context context;
    List<DataItemInSalesCard> branchList;


    public ItemInSaleStockAdapter(Context context1, List<DataItemInSalesCard> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList=new ArrayList<DataItemInSalesCard>();


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_dashboard, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.itemName.setText("" + branchList.get(position).itemName);
        holder.itemPriceIndividual.setText("Std price : " + branchList.get(position).unitPrice);
        holder.itemPriceTotal.setText("Total Price: " + Globals.numberToK(branchList.get(position).totalPrice));
        holder.quantity.setText(" " + branchList.get(position).totalQty + " nos");


//        int amount = Integer.parseInt(branchList.get(position).UnitPrice) * Integer.parseInt(branchList.get(position).Quantity);

        //   String stAmount=String.valueOf(amount);
        //  holder.pendingQuantity.setText(" " +stAmount );


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
//
                   /* Intent i = new Intent(context, ItemDetailsDashboard.class);
                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                    //   i.putExtra("FromWhere",fromWhere);
                    i.putExtra("itemcode", "" +branchList.get(getAdapterPosition()).itemCode);
                    i.putExtra("itemname", "" +branchList.get(getAdapterPosition()).itemName);
                    // i.putExtra("Heading",heading);
                    // i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);*/




                    Intent i = new Intent(context, ItemPurchasedByListOfCustomersActivity.class);

                    i.putExtra("itemcode", "" +branchList.get(getAdapterPosition()).itemCode);
                    i.putExtra("itemname", "" +branchList.get(getAdapterPosition()).itemName);

                    context.startActivity(i);


                }
            });


        }

    }


    List<DataItemInSalesCard> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataItemInSalesCard st : tempList) {
                if (st.getItemName() != null && !st.getItemName().isEmpty()) {
                    if (st.getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void AllData(List<DataItemInSalesCard> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }
}
