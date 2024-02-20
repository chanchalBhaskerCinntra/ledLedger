package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.activities.ReceiptTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LedgerCustomerData;

import java.util.ArrayList;
import java.util.List;


public class ParticularCustomerReciptTransactionAdapter extends RecyclerView.Adapter<ParticularCustomerReciptTransactionAdapter.ContactViewHolder> {


    Context context;
    String fromWhere;
    String heading;
    List<LedgerCustomerData> branchList;

    public ParticularCustomerReciptTransactionAdapter(Context context1, List<LedgerCustomerData> branchList, String customerName, String fromWhere) {

        this.context = context1;
        this.heading = customerName;
        this.fromWhere = fromWhere;
        this.branchList=branchList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_transaction_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText("" + branchList.get(position).getDocNum());

        String reverseDate= Globals.convertDateFormat(branchList.get(position).getCreateDate());
        holder.date.setText(reverseDate);
        holder.status.setVisibility(View.GONE);
        if (branchList.get(position).getPaymentStatus().equalsIgnoreCase("Partially Paid")) {
            holder.status.setText(branchList.get(position).getPaymentStatus());
            holder.status.setTextColor(Color.parseColor("#ffa500"));
            holder.status.setVisibility(View.VISIBLE);
        } else if (branchList.get(position).getPaymentStatus().equalsIgnoreCase("Unpaid")) {
            holder.status.setText(branchList.get(position).getPaymentStatus());
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            holder.status.setVisibility(View.VISIBLE);
        } else if (branchList.get(position).getPaymentStatus().equalsIgnoreCase("Paid")) {
            holder.status.setText(branchList.get(position).getPaymentStatus());
            holder.status.setTextColor(Color.parseColor("#00ff00"));
            holder.status.setVisibility(View.VISIBLE);
        } else {
            holder.status.setText("Unpaid");
        }


        if (fromWhere.trim().equalsIgnoreCase("Receivable") || fromWhere.trim().equalsIgnoreCase("ReceivableLedger")) {
            holder.unit_price.setText(Globals.numberToK(branchList.get(position).getDifferenceAmount()));
        } else {
            holder.unit_price.setText("₹ "+Globals.numberToK(branchList.get(position).getOrderAmount()));
        }

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title, status;
        TextView date;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(context, ReceiptTransactionFullInfo.class);
                    i.putExtra("FromWhere", fromWhere);
                    i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getOrderId());
                   // i.putExtra("docEntry", "" + branchList.get(getAdapterPosition()).getOrderId());
                    if (branchList.get(getAdapterPosition()).getIncomingPaymentInvoices().size()>0){
                        i.putExtra("ReceiptId", "" + branchList.get(getAdapterPosition()).getIncomingPaymentInvoices().get(0).getReceiptId());
                    }else {
                        i.putExtra("ReceiptId", "N/A" );
                    }

                    i.putExtra("Heading", heading);
                    i.putExtra("status", branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);



                }
            });


        }

    }
}
