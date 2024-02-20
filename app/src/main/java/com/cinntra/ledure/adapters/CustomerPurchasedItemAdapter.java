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
import com.cinntra.ledure.activities.ItemPurchasedParticularCustomerInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BpList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerPurchasedItemAdapter extends RecyclerView.Adapter<CustomerPurchasedItemAdapter.ContactViewHolder> {
    Context context;
    List<BpList> branchList;
    String itemcode;
    String startDate;
    String endDate;

    public CustomerPurchasedItemAdapter(Context context1, List<BpList> branchList, String itemcode,String startDate,String endDate) {
        this.branchList = branchList;
        this.context = context1;
        this.itemcode = itemcode;
        this.startDate = startDate;
        this.endDate = endDate;
        tempList = new ArrayList<BpList>();
        tempList.addAll(branchList);


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchased_by_customers, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.itemName.setText("" + branchList.get(position).getCardName());
//        holder.itemPriceIndividual.setText("Std price : " + branchList.get(position).unitPrice);
        holder.itemPriceTotal.setText("Total Price : ₹ "+ Globals.numberToK(branchList.get(position).getTotalPrice()));
        holder.tvQuantitiyPrice.setText("Qty: " + Globals.numberToK(branchList.get(position).getTotalQty()));



    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPriceTotal, tvQuantitiyPrice;
        ImageView edit;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.title);
            itemPriceTotal = itemView.findViewById(R.id.unit_price);
            tvQuantitiyPrice = itemView.findViewById(R.id.tvQuantityCustomer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    Intent i = new Intent(context, ItemPurchasedParticularCustomerInfo.class);
                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                    //   i.putExtra("FromWhere",fromWhere);
                    i.putExtra("FromWhere", "item");
                    i.putExtra("cardCode", branchList.get(getAdapterPosition()).getCardCode());
                    i.putExtra("cardName", branchList.get(getAdapterPosition()).getCardName());
                    i.putExtra("itemcode", "" + itemcode);
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


    List<BpList> tempList = null;

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        branchList.clear();
//        if (charText.length() == 0) {
//            branchList.addAll(tempList);
//        } else {
//            for (PendingOrderList st : tempList) {
////                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
////                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
////                        branchList.add(st);
////                    }
////                }
//            }
//
//        }
//        notifyDataSetChanged();
//    }

    public void AllData(List<BpList> tmp) {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (BpList st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
}
