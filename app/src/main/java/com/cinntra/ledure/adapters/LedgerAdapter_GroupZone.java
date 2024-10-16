package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ParticularCustomerInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LedgerAdapter_GroupZone extends RecyclerView.Adapter<LedgerAdapter_GroupZone.ContactViewHolder> {


    Context context;


    List<BusinessPartnerData> branchList ;

    public LedgerAdapter_GroupZone(Context context1, List<BusinessPartnerData> branchList) {
       // this.branchList.addAll(branchList);
        this.branchList=branchList;
//        Log.e("COnstructor", "LedgerAdapter: "+branchList.size());
        this.context = context1;
        tempList = new ArrayList<>();
        tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_report_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//        Log.e("Bind=>" + position, branchList.get(position).getCardName());
        holder.title.setText(branchList.get(position).getCardCode()+" "+branchList.get(position).getCardName());
        holder.unit_price.setText("₹ " + Globals.numberToK(branchList.get(position).getTotalSales()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs.putString("FromLedger", "Ledger");


                    Intent i = new Intent(context, ParticularCustomerInfo.class);
                    i.putExtra("FromWhere", "Ledger");
                    i.putExtra("cardName", branchList.get(holder.getAdapterPosition()).getCardName());
                    i.putExtra("SalesData", branchList.get(holder.getAdapterPosition()));
                    i.putExtra("cardCode", branchList.get(holder.getAdapterPosition()).getCardCode());

                    /***shubh****/
                    i.putExtra("group_name", branchList.get(holder.getAdapterPosition()).getUBpgrp());
                    i.putExtra("credit_limit", branchList.get(holder.getAdapterPosition()).getCreditLimit());
                    i.putExtra("credit_date", branchList.get(holder.getAdapterPosition()).getCreateDate());
                    context.startActivity(i);
               // }


            }
        });

    }


    @Override
    public int getItemCount() {
//        Log.e("TAG==>ADAPETR", "getItemCount: "+branchList.size() );
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);


        }

    }


    public void AllData(List<BusinessPartnerData> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }
    List<BusinessPartnerData> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (BusinessPartnerData st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()&&st.getCardCode() != null && !st.getCardCode().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void sortingA2Z(String type) {
        Collections.sort(branchList, new Comparator<BusinessPartnerData>() {
            @Override
            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                String s1 = item.getCardName();
                String s2 = t1.getCardName();
//                Log.e("T1=>", t1.getCardName());
                if (type.equalsIgnoreCase("AtoZ"))
                    return s1.compareToIgnoreCase(s2);
                else
                    return s2.compareToIgnoreCase(s1);


            }

        });
        notifyDataSetChanged();
    }


    public void sortingAmount() {
//        Collections.sort(branchList, new Comparator<BusinessPartnerData>() {
//            @Override
//            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
//                String s1 = item.getTotalSales();
//                String s2 = t1.getTotalSales();
//
//                Double int1=Double.valueOf(item.getTotalSales());
//                Double int2=Double.valueOf(item.getTotalSales());
//                Log.e("T1=>",t1.getCardName());
//
//                return int1.compareTo(int2);
//
//
//
//            }
//
//        });

        Comparator<BusinessPartnerData> doubleComparator = new Comparator<BusinessPartnerData>() {
            @Override
            public int compare(BusinessPartnerData obj1, BusinessPartnerData obj2) {
                // Compare the double values of the objects
                return Double.compare(Double.valueOf(obj2.getTotalSales()), Double.valueOf(obj1.getTotalSales()));
            }
        };

// Sort the list using the custom Comparator
        Collections.sort(branchList, doubleComparator);
        notifyDataSetChanged();
    }


    public void sortingByDate() {
        Collections.sort(branchList, new Comparator<BusinessPartnerData>() {
            @Override
            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                String s1 = item.getCreateDate();
                String s2 = t1.getCreateDate();
//                Log.e("T1=>", t1.getCardName());

                return s1.compareToIgnoreCase(s2);
//                if(type.equalsIgnoreCase("AtoZ"))
//                    return s1.compareToIgnoreCase(s2);
//                else
//                    return s2.compareToIgnoreCase(s1);


            }

        });
        notifyDataSetChanged();
    }

}
