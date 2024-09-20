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
import com.cinntra.ledure.activities.ItemPurchasedByListOfCustomersActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.MonthGroupSalesList;
import com.cinntra.ledure.model.PendingOrderList;
import com.pixplicity.easyprefs.library.Prefs;
import java.util.Calendar;
import java.util.List;

public class MonthlyItemSaleAdapter extends RecyclerView.Adapter<MonthlyItemSaleAdapter.ContactViewHolder> {
    Context context;
    List<MonthGroupSalesList> branchList;
    String itemcode;


    public MonthlyItemSaleAdapter(Context context1, List<MonthGroupSalesList> branchList,String itemcode) {
        this.branchList = branchList;
        this.context = context1;
        this.itemcode=itemcode;


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monthly_sale, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.itemName.setText("" + branchList.get(position).getMonth());
//        holder.itemPriceIndividual.setText("Std price : " + branchList.get(position).unitPrice);
        holder.itemPriceTotal.setText(" " + branchList.get(position).getDocTotal());



//        int amount = Integer.parseInt(branchList.get(position).UnitPrice) * Integer.parseInt(branchList.get(position).Quantity);

        //   String stAmount=String.valueOf(amount);
        //  holder.pendingQuantity.setText(" " +stAmount );


    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPriceTotal;
        ImageView edit;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvMonthSale);
            itemPriceTotal = itemView.findViewById(R.id.tvTotalPurchaseMonth);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arr[]=branchList.get(getAdapterPosition()).getMonth().split(" ");
                   // findIndex(month_arr,arr[0].trim());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MONTH,findIndex(month_arr,arr[0].trim())-1);
                    int month =calendar.get(Calendar.MONTH)+1;
                   int startDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                   int endDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                   String start = "20"+arr[1].trim()+"-"+String.format("%02d", month)+"-"+String.format("%02d", startDate);
                   String end = "20"+arr[1].trim()+"-"+String.format("%02d", month)+"-"+String.format("%02d", endDate);

                    Prefs.putString(Globals.FROM_DATE,start);
                    Prefs.putString(Globals.TO_DATE,end);

//                    Log.e("StartDate>",""+start);
//                    Log.e("EndDate=>",""+end);

                    Intent i = new Intent(context, ItemPurchasedByListOfCustomersActivity.class);
                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                    //   i.putExtra("FromWhere",fromWhere);
                    i.putExtra("itemcode", "" +itemcode);
                    i.putExtra("itemname", "" +itemName);
                    i.putExtra("zoneCode", "" );
//                    i.putExtra("itemname", "" +branchList.get(getAdapterPosition()).itemName);
                    //  i.putExtra("Heading",heading);
                    // i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
                    context.startActivity(i);


                }
            });


        }

    }

    String month_arr[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static int findIndex(String arr[], String t)
       {

           int index = 1;

           int i = 0;
           while(i < arr.length) {
               if(arr[i].equalsIgnoreCase(t)) {
                   index += i;
                   break;
               }
               i++;
           }
return index;
    }

    List<PendingOrderList> tempList = null;

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        branchList.clear();
//        if (charText.length() == 0) {
//            branchList.addAll(tempList);
//        } else {
//            for (PendingOrderList st : tempList) {
////                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
////                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
////                        branchList.add(st);
////                    }
////                }
//            }
//
//        }
//        notifyDataSetChanged();
//    }
}
