package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.PendingOrderSubListActivity;
import com.cinntra.ledure.activities.SoldItemDetailsList;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CashDiscountItem;
import com.cinntra.ledure.model.PartyWisePendingOrder;
import com.cinntra.ledure.model.PendingOrderList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ContactViewHolder> {
    Context context;
    List<PartyWisePendingOrder> branchList;

    public PendingOrderAdapter(Context context1, List<PartyWisePendingOrder> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList = new ArrayList<PartyWisePendingOrder>();
        this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.title.setVisibility(View.GONE);


       // holder.title.setText("Order ID : " + branchList.get(position).getPartywise().get(position).cardCode);
        holder.customerName.setText(branchList.get(position).cardCode+" " + branchList.get(position).cardName);
        holder.dueDate.setText("Pending Amount : ₹ " +Globals.numberToK(String.valueOf(branchList.get(position).pendingAmount)));

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, title, dueDate;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            customerName = itemView.findViewById(R.id.customerName);
            dueDate = itemView.findViewById(R.id.dueDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PendingOrderSubListActivity.class);
                    Bundle bundle = new Bundle();
                    i.putExtra("CardCode",branchList.get(getAdapterPosition()).getCardCode());
                    i.putExtra("CardName",branchList.get(getAdapterPosition()).getCardName());
                   // bundle.putSerializable("content",branchList.get(getAdapterPosition()).getPartywise().get(getAdapterPosition()));
                  //  i.putExtras(bundle);

                    context.startActivity(i);



                }
            });


        }

    }


    List<PartyWisePendingOrder> tempList = null;


    public void AllData(List<PartyWisePendingOrder> tmp)
    {
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
            for (PartyWisePendingOrder st : tempList) {
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
