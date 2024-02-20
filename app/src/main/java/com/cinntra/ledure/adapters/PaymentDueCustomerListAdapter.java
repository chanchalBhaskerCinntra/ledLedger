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
import com.cinntra.ledure.activities.ParticularCustomerPaymentDueInfo;
import com.cinntra.ledure.activities.ParticularCustomerReceiptInfo;
import com.cinntra.ledure.activities.ParticularCustomerReceivableInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.ReceivableCustomerData;
import com.cinntra.ledure.newapimodel.DataPaymentDueDashboardCustomerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PaymentDueCustomerListAdapter extends RecyclerView.Adapter<PaymentDueCustomerListAdapter.ContactViewHolder> {


    Context context;
    List<DataPaymentDueDashboardCustomerList> branchList;
    String fromWhere;

    private static final String TAG = "ReceivableCustomerAdapt";

    public PaymentDueCustomerListAdapter(Context context1, List<DataPaymentDueDashboardCustomerList> branchList, String fromWhere) {
        this.branchList = branchList;
        this.context = context1;
        this.fromWhere = fromWhere;
        this.tempList = new ArrayList<DataPaymentDueDashboardCustomerList>();
        this.tempList.addAll(branchList);
        Log.e(TAG, "ReceivableCustomerAdapter: " + branchList.size());


    }


    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_report_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Log.e(TAG, "ReceivableCustomerAdapter: " + branchList.get(position).getCardName());
        holder.title.setText(branchList.get(position).getCardCode() + " " + branchList.get(position).getCardName());
        holder.unit_price.setText("₹ " + Globals.numberToK(String.valueOf(branchList.get(position).getTotalSales())));

        holder.creditLimitDays.setVisibility(View.GONE);
        holder.avgDays.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price, title, avgDays, creditLimitDays;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);
            avgDays = itemView.findViewById(R.id.tvAvgDays);
            creditLimitDays = itemView.findViewById(R.id.tvCreditLimitAndDys);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (fromWhere.equalsIgnoreCase("Receipt")) {
                        Intent i = new Intent(context, ParticularCustomerReceiptInfo.class);
                        i.putExtra("FromWhere", "ReceiptLedger");
                        i.putExtra("cardCode", branchList.get(getAdapterPosition()).getCardCode());
                        i.putExtra("cardName", branchList.get(getAdapterPosition()).getCardName());
                        context.startActivity(i);
                    } else {

                        String filterValue = "";
                        Intent i = new Intent(context, ParticularCustomerPaymentDueInfo.class);
                        i.putExtra("FromWhere", "Receivable");
                        i.putExtra("cardCode", branchList.get(getAdapterPosition()).getCardCode());
                        i.putExtra("cardName", branchList.get(getAdapterPosition()).getCardName());
                        i.putExtra("filterValue", filterValue);
                        context.startActivity(i);
                    }


                }
            });


        }

    }

    public void AllData(List<DataPaymentDueDashboardCustomerList> tmp) {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }

    List<DataPaymentDueDashboardCustomerList> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataPaymentDueDashboardCustomerList st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty() && st.getCardCode() != null && !st.getCardCode().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText) || st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void sortingA2Z(String type) {
        Collections.sort(branchList, new Comparator<DataPaymentDueDashboardCustomerList>() {
            @Override
            public int compare(DataPaymentDueDashboardCustomerList item, DataPaymentDueDashboardCustomerList t1) {
                String s1 = item.getCardName();
                String s2 = t1.getCardName();
                Log.e("T1=>", t1.getCardName());
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

        Comparator<DataPaymentDueDashboardCustomerList> doubleComparator = new Comparator<DataPaymentDueDashboardCustomerList>() {
            @Override
            public int compare(DataPaymentDueDashboardCustomerList obj1, DataPaymentDueDashboardCustomerList obj2) {
                // Compare the double values of the objects
                return Double.compare(Double.valueOf(obj2.getTotalSales()), Double.valueOf(obj1.getTotalSales()));
            }
        };

// Sort the list using the custom Comparator
        Collections.sort(branchList, doubleComparator);
        notifyDataSetChanged();
    }

}
