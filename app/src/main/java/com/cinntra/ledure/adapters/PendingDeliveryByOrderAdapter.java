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
import com.cinntra.ledure.activities.ActivityOrderOneDetails;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.activities.PendingByOrderActivity;
import com.cinntra.ledure.activities.ReceiptTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataDeliveryNotePendingByOrder;
import com.cinntra.ledure.model.OrderWisePendingOrderData;
import com.cinntra.ledure.model.PendingOrderList;

import java.util.List;

public class PendingDeliveryByOrderAdapter extends RecyclerView.Adapter<PendingDeliveryByOrderAdapter.ContactViewHolder> {
    Context context;
    List<DataDeliveryNotePendingByOrder> branchList;
    String orderId;

    public PendingDeliveryByOrderAdapter(Context context1, List<DataDeliveryNotePendingByOrder> branchList, String orderId) {
        this.branchList = branchList;
        this.context = context1;
        this.orderId = orderId;


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_by_order, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText("Order Desc : " + branchList.get(position).itemDescription);
        holder.customerName.setText("Pending Amount :₹ " + Globals.numberToK(String.valueOf(branchList.get(position).pendingAmount)));
        holder.dueDate.setText("Due date : " + Globals.convertDateFormat(branchList.get(position).docDueDate));
        holder.pendingQuantity.setText("Pending Quantity : " + branchList.get(position).quantity);

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, title, dueDate, pendingQuantity;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            customerName = itemView.findViewById(R.id.customerName);
            dueDate = itemView.findViewById(R.id.dueDate);
            pendingQuantity = itemView.findViewById(R.id.pendingQuantitiy);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    Intent i = new Intent(context, ActivityOrderOneDetails.class);
                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                    //   i.putExtra("FromWhere",fromWhere);
                    i.putExtra("ID", "" + orderId);
                    //  i.putExtra("Heading",heading);
                    // i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);


                }
            });


        }

    }


    List<PendingOrderList> tempList = null;


}
