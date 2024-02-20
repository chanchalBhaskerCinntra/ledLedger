package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.model.CampaignModel;
import com.cinntra.ledure.model.MemberList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {
    Context context;
    List<MemberList> leadValueList;
    public MemberListAdapter(Context c, List<MemberList> leadValueList) {
        this.context =c;
        this.leadValueList = leadValueList;
        this.tempList=new ArrayList<>();
        tempList.addAll(leadValueList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.campaign_adapter_screen,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberList lv = leadValueList.get(position);
        holder.customerName.setText(lv.getName());
        holder.date.setText("Phone : "+lv.getPhone());
        holder.cardNumber.setText("Email : "+lv.getEmail());


        holder.assigned_view.setVisibility(View.GONE);
        holder.assigned.setVisibility(View.GONE);
        holder.ship_to.setVisibility(View.GONE);

        //holder.amount.setText("Rs:" + lv.getTurnover());

    }








    @Override
    public int getItemCount() {
        return leadValueList.size();
    }

    List<MemberList>  tempList =null ;
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        leadValueList.clear();
        if (charText.length() == 0) {
            leadValueList.addAll(tempList);
        } else {
            for (MemberList st : tempList) {
                if(st.getName()!=null&&!st.getName().isEmpty()) {
                    if (st.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        leadValueList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName,cardNumber,date,assigned,ship_to,date_txt;
        LinearLayout assigned_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.item_title);
            cardNumber   = itemView.findViewById(R.id.price);
            date         = itemView.findViewById(R.id.item_date);
            date_txt     = itemView.findViewById(R.id.date);
            assigned     = itemView.findViewById(R.id.assigned);
            assigned_view   = itemView.findViewById(R.id.assigned_view);
            ship_to   = itemView.findViewById(R.id.ship_to);


            date_txt.setVisibility(View.GONE);


        }

    }
}