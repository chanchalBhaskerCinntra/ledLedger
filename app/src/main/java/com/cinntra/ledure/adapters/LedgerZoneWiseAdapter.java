package com.cinntra.ledure.adapters;



import android.content.Context;
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
import com.cinntra.ledure.model.DataLedgerGroup;
import com.cinntra.ledure.model.DataZoneGroup;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LedgerZoneWiseAdapter extends RecyclerView.Adapter<LedgerZoneWiseAdapter.ContactViewHolder> {


    Context context;
    public interface OnItemGroupClickListener {
        void onItemGroupClick(int position,String code,String name);
    }


    List<DataZoneGroup> branchList = new ArrayList<>();
    private OnItemGroupClickListener clickListener;

    // Constructor and other methods...

    public void setOnItemClickListener(OnItemGroupClickListener listener) {
        this.clickListener = listener;
    }

    public LedgerZoneWiseAdapter(Context context1, List<DataZoneGroup> branchList) {
        this.branchList=branchList;
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
        Log.e("Bind=>" + position, branchList.get(position).getGroupName());
        holder.title.setText(branchList.get(position).getGroupName());
       // holder.unit_price.setText("₹ " + numberToK(String.valueOf(branchList.get(position).getTotalSales())));
        holder.unit_price.setText("₹ " + Globals.numberToK(foo(branchList.get(position).getTotalSales())));


        holder.unit_price.setOnClickListener(view -> {
            Log.e("group", "onBindViewHolder: "+position);
            if (clickListener!=null){
                clickListener.onItemGroupClick(position,branchList.get(position).getGroupCode(),branchList.get(position).getGroupName());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs.putString("FromLedger", "Ledger");
                Log.e("group", "onBindViewHolder: "+position);
                if (clickListener!=null){
                    clickListener.onItemGroupClick(position,branchList.get(position).getGroupCode(),branchList.get(position).getGroupName());
                }

//                if (branchList.get(holder.getAdapterPosition()).getGroupCode().equalsIgnoreCase("10006")) {
//                    Log.e("10006", "onClick: ");
//                } else {
//                    Prefs.putString("ForReports","Group");
//                    Intent i = new Intent(context, Sale_Inovice_Reports.class);
//                    i.putExtra("FromWhere", "Group");
//                    i.putExtra("cardName", branchList.get(holder.getAdapterPosition()).getGroupName());
//
//                    i.putExtra("cardCode", branchList.get(holder.getAdapterPosition()).getGroupCode());
//
//
//                    context.startActivity(i);

//                    Intent intent = new Intent();
//                    context.startActivityForResult(intent,RESULT_OK);
        //        }


            }
        });

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


        }

    }

    List<DataZoneGroup> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataZoneGroup st : tempList) {
                if (st.getGroupName() != null && !st.getGroupName().isEmpty()) {
                    if (st.getGroupName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void sortingA2Z(String type) {
        Collections.sort(branchList, new Comparator<DataZoneGroup>() {
            @Override
            public int compare(DataZoneGroup item, DataZoneGroup t1) {
                String s1 = item.getGroupName();
                String s2 = t1.getGroupName();
                Log.e("T1=>", t1.getGroupName());
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

        Comparator<DataZoneGroup> doubleComparator = new Comparator<DataZoneGroup>() {
            @Override
            public int compare(DataZoneGroup obj1, DataZoneGroup obj2) {
                // Compare the double values of the objects
                return Double.compare(Double.valueOf(obj2.getTotalSales()), Double.valueOf(obj1.getTotalSales()));
            }
        };

// Sort the list using the custom Comparator
        Collections.sort(branchList, doubleComparator);
        notifyDataSetChanged();
    }


//    public void sortingByDate() {
//        Collections.sort(branchList, new Comparator<DataLedgerGroup>() {
//            @Override
//            public int compare(DataLedgerGroup item, DataLedgerGroup t1) {
//                String s1 = item.getCreateDate();
//                String s2 = t1.getCreateDate();
//                Log.e("T1=>", t1.getCardName());
//
//                return s1.compareToIgnoreCase(s2);
////                if(type.equalsIgnoreCase("AtoZ"))
////                    return s1.compareToIgnoreCase(s2);
////                else
////                    return s2.compareToIgnoreCase(s1);
//
//
//            }
//
//        });
//        notifyDataSetChanged();
//    }


    public void AllData(List<DataZoneGroup> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }

    public String foo(double value) //Got here 6.743240136E7 or something..
    {
        DecimalFormat formatter;

        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0.00"); //Here you can also deal with rounding if you wish..
        else
            formatter = new DecimalFormat("0");

        return formatter.format(value);
    }
}

