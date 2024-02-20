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
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.DataCustomerLedger;
import com.cinntra.ledure.model.JournalEntryLineBodyData;
import com.cinntra.ledure.model.LedgerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class LedgerCustomerEntriesAdapter extends RecyclerView.Adapter <LedgerCustomerEntriesAdapter.ContactViewHolder>
{
    Context context;
    List<DataCustomerLedger> branchList;

    public LedgerCustomerEntriesAdapter(Context context1, List<DataCustomerLedger> branchList) {
        this.branchList = branchList;
        this.context = context1;
       this.tempList  = new ArrayList<DataCustomerLedger>();
      this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_ledger, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.name.setText(""+branchList.get(position).cardName);
        holder.amount.setText(context.getResources().getString(R.string.Rs)+" "+ Globals.numberToK(Globals.foo(Double.valueOf(Globals.getRoundOffUpTOTwo(branchList.get(position).totalSales)))));





    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCustomerNameLedgerEntry);
            amount = itemView.findViewById(R.id.tvCustomerAmountLedgerEntry);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });


        }

    }

    List<DataCustomerLedger> tempList = null;

    public void AllData(List<DataCustomerLedger> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataCustomerLedger st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                        Log.e("Search==>",""+branchList.size());
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

//    public void filter(String charText)
//    {
//        charText = charText.toLowerCase(Locale.getDefault());
//        branchList.clear();
//        if (charText.length() == 0) {
//            branchList.addAll(tempList);
//        } else {
//            for (LedgerItem st : tempList) {
//                if (st.getAmount() != null && !st.getAmount().isEmpty()) {
//                    if (st.getAmount().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        branchList.add(st);
//                    }
//                }
//            }
//
//        }
//        notifyDataSetChanged();
//    }
}
