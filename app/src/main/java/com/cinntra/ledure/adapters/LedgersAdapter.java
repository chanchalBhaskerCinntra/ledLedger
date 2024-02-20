package com.cinntra.ledure.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LedgerItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LedgersAdapter extends RecyclerView.Adapter <LedgersAdapter.ContactViewHolder>
     {
    Context context;
    List<LedgerItem> branchList;

    public LedgersAdapter(Context context1, List<LedgerItem> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList  = new ArrayList<LedgerItem>();
        this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ledger_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.invoice_id.setText( ""+branchList.get(position).getOrderId());
        holder.invoice_date.setText( " " + branchList.get(position).getCreateDate());
        holder.ref_no.setText( "" + branchList.get(position).getType());
        holder.received_amount.setText( "" + Globals.numberToK( branchList.get(position).getAmount()));
        holder.total_amount.setText( "( " + Globals.numberToK( branchList.get(position).getBalance())+" )");

        if(branchList.get(position).getType().equalsIgnoreCase("Receipt"))
            holder.received_amount.setTextColor(Color.parseColor("#FF0000"));
    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView invoice_id, invoice_date,ref_no,total_amount,received_amount;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_id = itemView.findViewById(R.id.invoice_id);
            invoice_date = itemView.findViewById(R.id.invoice_date);
            ref_no = itemView.findViewById(R.id.ref_no);
            received_amount = itemView.findViewById(R.id.received_amount);
            total_amount = itemView.findViewById(R.id.total_amount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });


        }

    }


    List<LedgerItem> tempList = null;

    public void filter(String charText)
        {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (LedgerItem st : tempList) {
                if (st.getAmount() != null && !st.getAmount().isEmpty()) {
                    if (st.getAmount().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                        Log.e("Search==>",""+branchList.size());
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
}
