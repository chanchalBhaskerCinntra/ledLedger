package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.MapsActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {
    Context context;
    List<BusinessPartnerData> customerList;
    public CustomersAdapter(Context context, List<BusinessPartnerData> customerList)
       {
    this.context      = context;
    this.customerList = customerList;
    this.tempList  = new ArrayList<BusinessPartnerData>();
     this.tempList.addAll(customerList);
      }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.customers_item,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {
   final BusinessPartnerData obj=getItem(position);
   holder.customerName.setText(obj.getCardName());
   holder.cardNumber.setText(obj.getCardCode());
    }

    @Override
    public int getItemCount() {
    return customerList.size();
    }
    public BusinessPartnerData getItem(int position)
    {
        return customerList.get(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName,cardNumber;
        ImageView map_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            cardNumber   = itemView.findViewById(R.id.cardNumber);
            map_icon     = itemView.findViewById(R.id.map_icon);

          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent();
                  intent.putExtra(Globals.CustomerItemData, customerList.get(getAdapterPosition()));
                  ((AppCompatActivity)context).setResult(RESULT_OK, intent);
                  ((AppCompatActivity)context).finish();

              }
          });


            map_icon.setOnClickListener(new View.OnClickListener()
              {
                @Override
                public void onClick(View v) {
//               Intent i = new Intent(context, MapsActivity.class);
//               context.startActivity(i);

                }
            });

        }
    }


    List<BusinessPartnerData>  tempList =null ;
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        customerList.clear();
        if (charText.length() == 0) {
            customerList.addAll(tempList);
        } else {
            for (BusinessPartnerData st : tempList) {
                if(st.getCardName()!=null&&!st.getCardName().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        customerList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }



    public void AllData()
      {
        customerList.addAll(tempList);
        notifyDataSetChanged();
    }
    public void Customerfilter()
      {
     customerList.addAll(tempList);
     Collections.sort(customerList, new Comparator<BusinessPartnerData>() {
      @Override
            public int compare(BusinessPartnerData o1, BusinessPartnerData o2) {
                return o1.getCardName().compareTo(o2.getCardName());
            }
        });

        notifyDataSetChanged();

    }
}
