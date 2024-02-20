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
import com.cinntra.ledure.activities.PendingByOrderActivity;
import com.cinntra.ledure.activities.SoldItemDetailsList;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.OrderWisePendingOrderData;
import com.cinntra.ledure.model.PendingOrderList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PendingSubListOrderAdapter extends RecyclerView.Adapter<PendingSubListOrderAdapter.ContactViewHolder> {
    Context context;
    List<OrderWisePendingOrderData> branchList;
    String cardName;

    public PendingSubListOrderAdapter(Context context1, List<OrderWisePendingOrderData> branchList, String cardName) {
        this.branchList = branchList;
        this.context = context1;
        this.cardName = cardName;


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.dueDateOrder.setVisibility(View.VISIBLE);
        holder.title.setText("Order ID : " + branchList.get(position).DocNum);
        holder.dueDateOrder.setText("Due Date : " + Globals.convertDateFormat(branchList.get(position).getDocDueDate()));
        holder.customerName.setText("Pending Quantity : " + branchList.get(position).pendingQty);
        holder.dueDate.setText("Pending Amount : ₹ " + Globals.numberToK(String.valueOf(branchList.get(position).pendingAmount)));

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, title, dueDate, dueDateOrder;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            customerName = itemView.findViewById(R.id.customerName);
            dueDate = itemView.findViewById(R.id.dueDate);
            dueDateOrder = itemView.findViewById(R.id.dueDateOrder);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PendingByOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("byOrder", branchList.get(getAdapterPosition()));
                    i.putExtra("cardName", cardName);
                    i.putExtras(bundle);

                    context.startActivity(i);


                }
            });


        }

    }


}
