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
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;


public class ParticularCustomerTransactionAdapter extends RecyclerView.Adapter<ParticularCustomerTransactionAdapter.ContactViewHolder> {


    Context context;
    String fromWhere;
    String heading;
    ArrayList<LedgerCustomerData> branchList = new ArrayList<>();

    public ParticularCustomerTransactionAdapter(Context context1, ArrayList<LedgerCustomerData> branchList, String customerName, String fromWhere) {
        this.branchList=branchList;
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

        String reverseDate= Globals.convertDateFormat(branchList.get(position).getCreateDate());
        holder.date.setText(reverseDate);

        holder.status.setVisibility(View.INVISIBLE);




        if (fromWhere.trim().equalsIgnoreCase("Receivable") || fromWhere.trim().equalsIgnoreCase("ReceivableLedger")) {
            holder.unit_price.setText(context.getResources()
                    .getString(R.string.Rs)+""+Globals.numberToK(branchList.get(position).getDifferenceAmount()));
            holder.orderAmount.setText("order amount:");
            holder.orderAmount.setVisibility(View.GONE);
        } else {
            holder.unit_price.setText(context.getResources()
                    .getString(R.string.Rs)+" "+Globals.numberToK(branchList.get(position).getOrderAmount()));
            holder.orderAmount.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title, status, orderAmount;
        TextView date;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            orderAmount = itemView.findViewById(R.id.orderAmount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prefs.putString(Globals.Sale_Purchse_Diff,heading);
                    Intent i = new Intent(context, InvoiceTransactionFullInfo.class);
                    i.putExtra("FromWhere", fromWhere);
                    i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getOrderId());
                    i.putExtra("Heading", heading);
                    /***shubh****/
                    i.putExtra("status", branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);



                }
            });


        }

    }
}
