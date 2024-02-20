package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.SoldItem;
import com.cinntra.ledure.model.SoldItemResponse;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;


public class SoldItemDetails_Adapter extends RecyclerView.Adapter<SoldItemDetails_Adapter.ViewHolder> {
    Context context;
    ArrayList<SoldItem> AllItemList;
    String fromWhere;
    public SoldItemDetails_Adapter(Context context, ArrayList<SoldItem> AllItemList,String fromWhere)
       {
    this.context = context;
    this.AllItemList = AllItemList;
    this.fromWhere=fromWhere;

      }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.sold_item_adapter,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(AllItemList.get(position).getInvoiceID());
        holder.last_sold_date.setText("Sold Date : "+ Globals.convertDateFormat(AllItemList.get(position).getSoldDate()));
        holder.quantity.setText("Total Quantity : "+AllItemList.get(position).getTotalQty());
        holder.unit_price.setText("Unit Price : "+AllItemList.get(position).getUnitPirce()+" /Nos");


    }

    @Override
    public int getItemCount() {
        return AllItemList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,last_sold_date,quantity,unit_price;


       public ViewHolder(@NonNull View itemView) {
            super(itemView);

           title= itemView.findViewById( R.id.title);
           quantity= itemView.findViewById( R.id.quantity);
           last_sold_date= itemView.findViewById( R.id.item_id);
           unit_price= itemView.findViewById( R.id.unit_price);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Prefs.putString(Globals.Sale_Purchse_Diff,"");
                   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
                   i.putExtra("FromWhere",fromWhere);
                   i.putExtra("ID",""+AllItemList.get(getAdapterPosition()).getInvoiceID());
                   i.putExtra("Heading",AllItemList.get(getAdapterPosition()));
                   i.putExtra("status",AllItemList.get(getAdapterPosition()));
                   /***shubh****/
                 //  i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
                   i.putExtra("status","STATUS");
                   context.startActivity(i);

               }
           });



        }
    }
}
