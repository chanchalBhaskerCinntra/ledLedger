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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ParticularCustomerInfo;
import com.cinntra.ledure.activities.ParticularCustomerReceiptInfo;
import com.cinntra.ledure.activities.ParticularCustomerReceivableInfo;
import com.cinntra.ledure.fragments.Invoices_Customervice;
import com.cinntra.ledure.fragments.Invoices_Override_Fragment;
import com.cinntra.ledure.fragments.Ledger_Open_Order_Customervice;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ReceiptBusinessPartnerData;
import com.cinntra.ledure.model.ReceivableCustomerData;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class BillsLedgerAdapter extends RecyclerView.Adapter <BillsLedgerAdapter.ContactViewHolder> {


    Context context;
    List<ReceiptBusinessPartnerData> branchList;
    String fromWhere;

    public BillsLedgerAdapter(Context context1, List<ReceiptBusinessPartnerData> branchList,String fromWhere) {
        this.branchList = branchList;
        this.context = context1;
        this.fromWhere = fromWhere;
        this.tempList  = new ArrayList<ReceiptBusinessPartnerData>();
        this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_report_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText(branchList.get(position).getCardCode()+" "+branchList.get(position).getCardName());
        holder.unit_price.setText("₹ " + Globals.numberToK( branchList.get(position).getTotalSales()));

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if(fromWhere.equalsIgnoreCase("Receipt"))
                    {
                        Intent i =new Intent(context, ParticularCustomerReceiptInfo.class);
                        i.putExtra("FromWhere","ReceiptLedger");
                        i.putExtra("cardCode",branchList.get(getAdapterPosition()).getCardCode());
                        i.putExtra("cardName",branchList.get(getAdapterPosition()).getCardName());
//                        i.putExtra("group_name",branchList.get(getAdapterPosition()).getUBpgrp());
//                        i.putExtra("credit_limit",branchList.get(getAdapterPosition()).getCreditLimit());
//                        i.putExtra("credit_date",branchList.get(getAdapterPosition()).getCreateDate());
                        context.startActivity(i);
                    }
                    else
                    {
                        String filterValue="";
                        Intent i =new Intent(context, ParticularCustomerReceivableInfo.class);
                        i.putExtra("FromWhere","Receivable");
                        i.putExtra("cardCode",branchList.get(getAdapterPosition()).getCardCode());
                        i.putExtra("cardName",branchList.get(getAdapterPosition()).getCardName());
                        i.putExtra("filterValue",filterValue);
//                        i.putExtra("group_name",branchList.get(getAdapterPosition()).getUBpgrp());
//                        i.putExtra("credit_limit",branchList.get(getAdapterPosition()).getCreditLimit());
//                        i.putExtra("credit_date",branchList.get(getAdapterPosition()).getCreateDate());
                        context.startActivity(i);
                    }



                }
            });


        }

    }

    public void AllData(List<ReceiptBusinessPartnerData> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }

    List<ReceiptBusinessPartnerData> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (ReceiptBusinessPartnerData st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()&&st.getCardCode() != null && !st.getCardCode().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void sortingA2Z(String type)
    {
        Collections.sort(branchList, new Comparator<ReceiptBusinessPartnerData>() {
            @Override
            public int compare(ReceiptBusinessPartnerData item, ReceiptBusinessPartnerData t1) {
                String s1 = item.getCardName();
                String s2 = t1.getCardName();
                Log.e("T1=>",t1.getCardName());
                if(type.equalsIgnoreCase("AtoZ"))
                    return s1.compareToIgnoreCase(s2);
                else
                    return s2.compareToIgnoreCase(s1);


            }

        });
        notifyDataSetChanged();
    }


    public void sortingAmount()
    {
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

        Comparator<ReceiptBusinessPartnerData> doubleComparator = new Comparator<ReceiptBusinessPartnerData>() {
            @Override
            public int compare(ReceiptBusinessPartnerData obj1, ReceiptBusinessPartnerData obj2) {
                // Compare the double values of the objects
                return Double.compare(Double.valueOf(obj2.getTotalSales()), Double.valueOf(obj1.getTotalSales()));
            }
        };

// Sort the list using the custom Comparator
        Collections.sort(branchList, doubleComparator);
        notifyDataSetChanged();
    }
}
