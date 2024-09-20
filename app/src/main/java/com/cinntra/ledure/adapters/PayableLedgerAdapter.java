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
import com.cinntra.ledure.activities.ParticularCustomerReceivableInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.MonthGroupSalesList;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

public class PayableLedgerAdapter extends RecyclerView.Adapter <PayableLedgerAdapter.ContactViewHolder> {

    String cardCode;
    String cardName;
    Context context;
    List<MonthGroupSalesList> branchList;

    public PayableLedgerAdapter(Context context1, List<MonthGroupSalesList> branchList, String cardCode, String cardName) {
        this.branchList = branchList;
        this.context = context1;
        this.cardCode = cardCode;
        this.cardName = cardName;
       /* this.tempList  = new ArrayList<BusinessPartnerData>();
        this.tempList.addAll(branchList);*/

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sale_receipt_receivable_adapter, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText(branchList.get(position).getMonth());
        holder.unit_price.setText("₹ " + Globals.numberToK(branchList.get(position).getDocTotal()));

    }


    @Override
    public int getItemCount()
         {
        return branchList.size();
          }

    public class ContactViewHolder extends RecyclerView.ViewHolder
           {
        TextView unit_price, title;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            unit_price = itemView.findViewById(R.id.unit_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filterValue = "";

                  if(branchList.get(getAdapterPosition()).getMonth().equalsIgnoreCase("Not Due"))
                  {
                      filterValue ="-1";
                      Prefs.putString(Globals.FROM_DATE_receivable, "nondue");
                  }
                  else if(branchList.get(getAdapterPosition()).getMonth().equalsIgnoreCase(">30 Days"))
                  {
                      filterValue ="30";
                      Prefs.putString(Globals.FROM_DATE_receivable, "30");
                  }
                  else if(branchList.get(getAdapterPosition()).getMonth().equalsIgnoreCase(">60 Days"))
                  {
                      filterValue ="60";
                      Prefs.putString(Globals.FROM_DATE_receivable, "60");
                  }
                  else if(branchList.get(getAdapterPosition()).getMonth().equalsIgnoreCase(">0 Days")){

                      filterValue ="";
                      Prefs.putString(Globals.FROM_DATE_receivable, "All");
                  }

                    String arr[]=branchList.get(getAdapterPosition()).getMonth().split(" ");


//                    Log.e("filterValue=>",filterValue);
                    Intent i =new Intent(context, ParticularCustomerReceivableInfo.class);
                    i.putExtra("FromWhere","Receivable");
                    i.putExtra("cardCode",cardCode);
                    i.putExtra("cardName",cardName);
                    i.putExtra("filterValue",filterValue);
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

    /*List<BusinessPartnerData> tempList = null;

    public void filter(String charText)
       {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (BusinessPartnerData st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }*/
}
