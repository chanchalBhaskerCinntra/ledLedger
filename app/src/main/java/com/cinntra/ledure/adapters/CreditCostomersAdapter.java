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
import com.cinntra.ledure.activities.ParticularBpCreditNoteActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreditCostomersAdapter extends RecyclerView.Adapter <CreditCostomersAdapter.ContactViewHolder> {


    Context context;
    List<BusinessPartnerData> branchList;

    public CreditCostomersAdapter(Context context1, List<BusinessPartnerData> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList  = new ArrayList<BusinessPartnerData>();
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

        holder.title.setText(branchList.get(position).getCardCode()+" " + branchList.get(position).getCardName());
        holder.unit_price.setText("₹ " + Globals.numberToK(branchList.get(position).getTotalSales()));

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
                    Prefs.putString(Globals.Sale_Purchse_Diff,"ttARCreditNote");

                    Intent i =new Intent(context, ParticularBpCreditNoteActivity.class);
                    i.putExtra("FromWhere","Credits");
                    i.putExtra("code",branchList.get(getAdapterPosition()).getCardCode());
                    i.putExtra("name",branchList.get(getAdapterPosition()).getCardName());
                     context.startActivity(i);


                }
            });


        }

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
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode() != null && !st.getCardCode().isEmpty()) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void AllData(List<BusinessPartnerData> tmp)
    {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }


}
