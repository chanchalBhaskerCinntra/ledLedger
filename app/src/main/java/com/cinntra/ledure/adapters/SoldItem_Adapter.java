package com.cinntra.ledure.adapters;

import static com.cinntra.ledure.globals.Globals.numberToK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ItemPurchasedParticularCustomerInfo;
import com.cinntra.ledure.activities.SoldItemDetailsList;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.SoldItemResponse;

import java.util.ArrayList;


public class SoldItem_Adapter extends RecyclerView.Adapter<SoldItem_Adapter.ViewHolder> {
    Context context;
    ArrayList<SoldItemResponse> AllItemList;
    String name;
    String cardCode = "";

    String startDate = "";
    String endDate = "";


    public SoldItem_Adapter(Context context, ArrayList<SoldItemResponse> AllItemList, String name, String cardCode, String startDate, String endDate) {
        this.context = context;
        this.AllItemList = AllItemList;
        this.name = name;
        this.cardCode = cardCode;
        this.startDate = startDate;
        this.endDate = endDate;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.sold_item_adapter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(AllItemList.get(position).getItemCode() + " " + AllItemList.get(position).getItemName());
      //  holder.last_sold_date.setText("Total Price : " + AllItemList.get(position).getTotalPrice());
        holder.last_sold_date.setText("Total Price: "+context.getResources().getString(R.string.Rs) + " " + numberToK(String.valueOf(AllItemList.get(position).getTotalPrice())));
        holder.quantity.setText("Total Quantity : " + AllItemList.get(position).getTotalQty());
        holder.unit_price.setVisibility(View.INVISIBLE);
        holder.unit_price.setText("Unit Price : " + AllItemList.get(position).getUnitPirce());


    }

    @Override
    public int getItemCount() {
        return AllItemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, last_sold_date, quantity, unit_price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            quantity = itemView.findViewById(R.id.quantity);
            last_sold_date = itemView.findViewById(R.id.item_id);
            unit_price = itemView.findViewById(R.id.unit_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*       Intent i = new Intent(context, SoldItemDetailsList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("content", AllItemList.get(getAdapterPosition()));
                    i.putExtras(bundle);
                    i.putExtra("name", name);
                    context.startActivity(i);*/


                    Intent i = new Intent(context, ItemPurchasedParticularCustomerInfo.class);
                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                    //   i.putExtra("FromWhere",fromWhere);
                    i.putExtra("FromWhere", "item");
                    i.putExtra("cardCode", cardCode);
                    i.putExtra("cardName", Globals.cardNameGlobal);
                    i.putExtra("itemcode", "" + AllItemList.get(getAdapterPosition()).getItemCode());
                    i.putExtra("startDate", startDate);
                    i.putExtra("endDate", endDate);

                    // i.putExtra("itemname", "" +branchList.get(getAdapterPosition()).itemName);
                    //  i.putExtra("Heading",heading);
                    // i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);

                }
            });


        }
    }
}
