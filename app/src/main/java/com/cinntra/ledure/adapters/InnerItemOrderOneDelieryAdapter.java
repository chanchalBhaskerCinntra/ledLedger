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
import com.cinntra.ledure.activities.ActivityOrderOneDetails;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataDeliveryNotePendingByOrder;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.PendingOrderList;

import java.util.List;

public class InnerItemOrderOneDelieryAdapter extends RecyclerView.Adapter<InnerItemOrderOneDelieryAdapter.ContactViewHolder> {
    Context context;
    List<DocumentLines> branchList;


    public InnerItemOrderOneDelieryAdapter(Context context1, List<DocumentLines> branchList) {
        this.branchList = branchList;
        this.context = context1;



    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inner_pending_by_order, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.title.setText("" + branchList.get(position).ItemDescription);
        holder.customerName.setText("Qty : " + branchList.get(position).Quantity + " Nos.");
        holder.dueDate.setText("Rate :  \u20B9 " + Globals.numberToK(branchList.get(position).UnitPrice) + " / Nos.");



//        int amount = Integer.parseInt(branchList.get(position).UnitPrice) * Integer.parseInt(branchList.get(position).Quantity);

     //   String stAmount=String.valueOf(amount);
      //  holder.pendingQuantity.setText(" " +stAmount );



    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, title, dueDate, pendingQuantity;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            customerName = itemView.findViewById(R.id.customerName);
            dueDate = itemView.findViewById(R.id.dueDate);
            pendingQuantity = itemView.findViewById(R.id.pendingQuantitiy);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    Intent i = new Intent(context, ActivityOrderOneDetails.class);
//                    //   Intent i=  new Intent(context, InvoiceTransactionFullInfo.class);
//                    //   i.putExtra("FromWhere",fromWhere);
//                    i.putExtra("ID", "" + orderId);
//                    //  i.putExtra("Heading",heading);
//                    // i.putExtra("status",branchList.get(getAdapterPosition()).getPaymentStatus());
//                    context.startActivity(i);


                }
            });


        }

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
