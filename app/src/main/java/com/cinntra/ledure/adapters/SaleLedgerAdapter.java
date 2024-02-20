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
import com.cinntra.ledure.activities.ParticularCustomerSaleInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.MonthGroupSalesList;
import java.util.Calendar;
import java.util.List;


public class SaleLedgerAdapter extends RecyclerView.Adapter <SaleLedgerAdapter.ContactViewHolder> {


    Context context;
    List<MonthGroupSalesList> branchList;
    String cardCode;
    String cardName;
    public SaleLedgerAdapter(Context context1, List<MonthGroupSalesList> branchList, String cardCode, String cardName) {
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
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position)
        {

        holder.title.setText(branchList.get(position).getMonth());
        holder.unit_price.setText("₹ " + Globals.numberToK(Globals.changeDecemal(branchList.get(position).getDocTotal())));

    }


    @Override
    public int getItemCount() {
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

                    String arr[]=branchList.get(getAdapterPosition()).getMonth().split(" ");

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MONTH,findIndex(month_arr,arr[0].trim())-1);
                    int month =calendar.get(Calendar.MONTH)+1;
                    int startDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                    int endDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String start = "20"+arr[1].trim()+"-"+String.format("%02d", month)+"-"+String.format("%02d", startDate);
                    String end = "20"+arr[1].trim()+"-"+String.format("%02d", month)+"-"+String.format("%02d", endDate);


                    Intent i=  new Intent(context, ParticularCustomerSaleInfo.class);
                    i.putExtra("FromWhere","SaleLedger");
                    i.putExtra("cardCode",cardCode);
                    i.putExtra("cardName",cardName);
                    i.putExtra("summary","sale");
                    i.putExtra("startDate",start);
                    i.putExtra("endDate",end);

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

}
