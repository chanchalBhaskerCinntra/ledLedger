package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CreditOneActivity;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;


public class ParticularBpCreditNoteAdapter extends RecyclerView.Adapter<ParticularBpCreditNoteAdapter.ContactViewHolder> {


    Context context;
    String fromWhere;
    String heading;
    ArrayList<LedgerCustomerData> branchList = new ArrayList<>();

    public ParticularBpCreditNoteAdapter(Context context1, ArrayList<LedgerCustomerData> branchList, String customerName, String fromWhere) {
        this.branchList.addAll(branchList);
        this.context = context1;
        this.heading = customerName;
        this.fromWhere = fromWhere;

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_transaction_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText("" + branchList.get(position).getDocEntry());

        holder.date.setText(Globals.convertDateFormat(branchList.get(position).getCreateDate()));
        holder.unit_price.setText(branchList.get(position).getOrderAmount());

        holder.status.setVisibility(View.INVISIBLE);
        holder.tvRemark.setVisibility(View.VISIBLE);
        holder.tvRemark.setText(" " + branchList.get(position).getComments());




        if (fromWhere.trim().equalsIgnoreCase("Receivable") || fromWhere.trim().equalsIgnoreCase("ReceivableLedger")) {
            holder.unit_price.setText(branchList.get(position).getDifferenceAmount());
            holder.orderAmount.setText("order amount:" +"₹ "+ Globals.numberToK(branchList.get(position).getOrderAmount()));
            holder.orderAmount.setVisibility(View.VISIBLE);
        } else {
            holder.unit_price.setText("₹ "+ Globals.numberToK(branchList.get(position).getOrderAmount()));
            holder.orderAmount.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title, status, orderAmount, tvRemark;
        TextView date;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            orderAmount = itemView.findViewById(R.id.orderAmount);
            tvRemark = itemView.findViewById(R.id.tvRemark);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, CreditOneActivity.class);
                           i.putExtra("ID", branchList.get(getAdapterPosition()).getOrderId().toString());
                           i.putExtra("status", branchList.get(getAdapterPosition()).getPaymentStatus());
                           i.putExtra("FromWhere", "Ledger");
                           i.putExtra("Heading", "ttARCreditNote");
                          context.startActivity(i);

                }
            });




        }

    }
}
