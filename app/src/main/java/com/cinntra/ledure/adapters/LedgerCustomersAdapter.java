package com.cinntra.ledure.adapters;


import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.model.BusinessPartnerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class LedgerCustomersAdapter extends RecyclerView.Adapter<LedgerCustomersAdapter.ViewHolder> {
    Context context;
    List<BusinessPartnerData> customerList;

    public LedgerCustomersAdapter(Context context, List<BusinessPartnerData> customerList) {
        this.context = context;
        this.customerList = customerList;
        this.tempList = new ArrayList<BusinessPartnerData>();
        this.tempList.addAll(customerList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.customer_ledger_adapter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BusinessPartnerData obj = getItem(position);
        holder.title.setText(obj.getCardCode() + "  " + obj.getCardName());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();

        if (customerList.get(position).getCardName().isEmpty()) {
            Drawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4) /* thickness in px */
                    .endConfig()
                    .buildRound(
                            String.valueOf(customerList.get(position).getCardCode().charAt(0)).toUpperCase(Locale.getDefault()), color1
                    );
            holder.profile_pic.setImageDrawable(drawable);
        } else {
            Drawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4) /* thickness in px */
                    .endConfig()
                    .buildRound(
                            String.valueOf(customerList.get(position).getCardName().charAt(0)).toUpperCase(Locale.getDefault()), color1
                    );
            holder.profile_pic.setImageDrawable(drawable);
        }

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public BusinessPartnerData getItem(int position) {
        return customerList.get(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView profile_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            profile_pic = itemView.findViewById(R.id.profile_pic);


            // int parentPosition = 0; // Replace this with your actual parent position


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, LedgerCutomerDetails.class);
                    i.putExtra("cardCode", customerList.get(getAdapterPosition()).getCardCode());
                    i.putExtra("cardName", customerList.get(getAdapterPosition()).getCardName());
                    /***shubh****/
                    i.putExtra("group_name", customerList.get(getAdapterPosition()).getUBpgrp());
                    i.putExtra("credit_limit", customerList.get(getAdapterPosition()).getCreditLimit());
                    i.putExtra("credit_date", customerList.get(getAdapterPosition()).getCreateDate());
                    i.putExtra("phone", customerList.get(getAdapterPosition()).getPhone1());
                    i.putExtra("email", customerList.get(getAdapterPosition()).getEmailAddress());
                    i.putExtra("email", customerList.get(getAdapterPosition()).getEmailAddress());

                    context.startActivity(i);
                }
            });


        }
    }


    List<BusinessPartnerData> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        customerList.clear();
        if (charText.length() == 0) {
            customerList.addAll(tempList);
        } else {
            for (BusinessPartnerData st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText) || st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        customerList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }


    public void AllData(List<BusinessPartnerData> tmp) {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }

    public void Customerfilter() {
        customerList.addAll(tempList);
        Collections.sort(customerList, new Comparator<BusinessPartnerData>() {
            @Override
            public int compare(BusinessPartnerData o1, BusinessPartnerData o2) {
                return o1.getCardName().compareTo(o2.getCardName());
            }
        });

        notifyDataSetChanged();

    }
}
