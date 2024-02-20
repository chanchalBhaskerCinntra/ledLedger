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
import com.cinntra.ledure.activities.ParticularCustomerInfo;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CashDiscountItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CashDisAdapter extends RecyclerView.Adapter <CashDisAdapter.ContactViewHolder>
     {
    Context context;
    List<CashDiscountItem> branchList;

    public CashDisAdapter(Context context1, List<CashDiscountItem> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList  = new ArrayList<CashDiscountItem>();
        this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cash_discount_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText(branchList.get(position).getCustomerName());
        holder.unit_price.setText("Discount : " + branchList.get(position).getDiscountPercentage()+" % ");
        holder.dueDate.setText("DueDate : " + branchList.get(position).getPaymentDueDate());

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title,dueDate;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);
            dueDate = itemView.findViewById(R.id.dueDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });


        }

    }


    List<CashDiscountItem> tempList = null;

    public void filter(String charText)
        {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (CashDiscountItem st : tempList) {
                if (st.getCustomerName() != null && !st.getCustomerName().isEmpty()) {
                    if (st.getCustomerName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
}
