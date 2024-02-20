package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DocumentLines;

import java.util.ArrayList;

public class CustomersItemsAdapter extends RecyclerView.Adapter <CustomersItemsAdapter.ContactViewHolder>{


    Context context;


    ArrayList<DocumentLines> branchList = new ArrayList<>();
    public CustomersItemsAdapter(Context context1, ArrayList<DocumentLines> branchList){
        this.branchList.addAll(branchList);
        this.context = context1;

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.customer_item,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

     holder.item_name.setText(branchList.get(position).getItemDescription());
     holder.unit_price.setText("₹ "+ Globals.numberToK(branchList.get(position).getUnitPrice()));
     holder.item_quantity.setText(" Qty : "+branchList.get(position).getQuantity());

    }





    @Override
    public int getItemCount() {
     return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView unit_price,item_name,item_quantity;
        ImageView edit;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name= itemView.findViewById( R.id.item_name);
            unit_price= itemView.findViewById( R.id.unit_price);
            item_quantity= itemView.findViewById( R.id.item_quantity);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  ParticularCustomerInfo fragment = new ParticularCustomerInfo();
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.headerLay, fragment).addToBackStack(null);
                    transaction.commit();*/

                }
            });


        }

    }
}
