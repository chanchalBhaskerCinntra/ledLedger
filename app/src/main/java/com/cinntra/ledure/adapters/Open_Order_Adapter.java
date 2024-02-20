package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.WebViewToPdf;
import com.cinntra.ledure.fragments.Order_Update_Fragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.QuotationItem;
import com.pixplicity.easyprefs.library.Prefs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class  Open_Order_Adapter extends RecyclerView.Adapter<Open_Order_Adapter.ViewHolder> {
    Context context;

    List<QuotationItem> itemsList;
    public Open_Order_Adapter(Context context,List<QuotationItem> itemsList)
       {
   this.context   = context;
   this.itemsList = itemsList;
    this.tempList  = new ArrayList<QuotationItem>();
     this.tempList.addAll(itemsList);
      }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(context).inflate(R.layout.order_new_screen,parent,false);
    return new ViewHolder(rootView);
     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {
          QuotationItem obj = getItem(position);
          holder.title.setText(obj.getCardName());
          holder.doc_date.setText(obj.getDocDueDate());
      //    holder.amount.setText(Globals.getAmmount(obj.getDocCurrency(),obj.getDocTotal()));
       //   holder.status.setText(Globals.viewStatus(obj.getDocumentStatus()));

          if(Globals.viewStatus(obj.getDocumentStatus()) == "Open"){
              holder.status.setText("Open");
              holder.status.setBackgroundResource(R.drawable.openroundedgreen);
          }else{
              holder.status.setText("Closed");
              holder.status.setBackgroundResource(R.drawable.saffron_rounded);
          }
          holder.preview_file.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(context, WebViewToPdf.class);
                  i.putExtra("PDfFrom","Order");
                  i.putExtra("PdfID",obj.getId());
                  context.startActivity(i);
              }
          });
      }

    @Override
    public int getItemCount()
     {
    return itemsList.size();
     }
     public QuotationItem getItem(int po)
     {
         return itemsList.get(po);
     }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,doc_date,amount,status;
        ImageView preview_file;
     public ViewHolder(@NonNull View itemView)
           {

      super(itemView);
      title =itemView.findViewById(R.id.title);
      doc_date =itemView.findViewById(R.id.doc_date);
      amount =itemView.findViewById(R.id.amount);
      status =itemView.findViewById(R.id.status);
       preview_file =itemView.findViewById(R.id.preview_file);

      itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          ;
          if(Prefs.getString("FromLedger","").equalsIgnoreCase("Ledger"))
          {
              Bundle b = new Bundle();
              b.putSerializable(Globals.QuotationItem,itemsList.get(getAdapterPosition()));
              //Quotation_Update_Fragment fragment = new Quotation_Update_Fragment();
              Order_Update_Fragment fragment = new Order_Update_Fragment();
              fragment.setArguments(b);
              FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
              transaction.replace(R.id.MainReport, fragment);
              transaction.addToBackStack("Test");
              transaction.commit();
          }
          else{
              Bundle b = new Bundle();
              b.putSerializable(Globals.QuotationItem,itemsList.get(getAdapterPosition()));
              //Quotation_Update_Fragment fragment = new Quotation_Update_Fragment();
              Order_Update_Fragment fragment = new Order_Update_Fragment();
              fragment.setArguments(b);
              FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
              transaction.replace(R.id.main_container, fragment);
              transaction.addToBackStack("Test");
              transaction.commit();
          }



          }
       });
            }
    }


    List<QuotationItem>  tempList =null ;
    public void filter(String charText)
       {

    charText = charText.toLowerCase(Locale.getDefault());
    itemsList.clear();
     if (charText.length() == 0) {
     itemsList.addAll(tempList);
      } else {
      for (QuotationItem st : tempList) {
       if(st.getCardName()!=null&&!st.getCardName().isEmpty()) {
          if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText)) {
       itemsList.add(st);
            }
          }
        }

        }
        notifyDataSetChanged();
    }

    public void filterCustomer(String charText)
    {

        tempList.addAll(itemsList);
        charText = charText.toLowerCase(Locale.getDefault());
        itemsList.clear();
        if (charText.length() == 0) {
            itemsList.addAll(tempList);
        } else {
            for (QuotationItem st : tempList) {
                if(st.getCardCode()!=null&&!st.getCardCode().isEmpty()) {
                    if (st.getCardCode().toLowerCase(Locale.getDefault()).trim().equalsIgnoreCase(charText.trim())) {
                        itemsList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ValidDate() {
        itemsList.clear();
        itemsList.addAll(tempList);
        Collections.sort(itemsList, new Comparator<QuotationItem>() {
            @Override
            public int compare(QuotationItem o1, QuotationItem o2) {
                return o1.getDocDueDate().compareTo(o2.getDocDueDate());
            }
        });
        notifyDataSetChanged();


        notifyDataSetChanged();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void PostingDate(LocalDate afterdate, LocalDate dateObj)
       {
        itemsList.clear();
        for (QuotationItem st : tempList) {

            if(st.getCreationDate()!=null&&!st.getCreationDate().isEmpty()) {
                String sDate1 = st.getCreationDate();
                LocalDate date1=LocalDate.parse(sDate1);
                if((date1.isBefore(afterdate)||date1.isEqual(afterdate)) && date1.isAfter(dateObj)  ){
                    Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();
                    itemsList.add(st);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void Customerfilter()
       {
           itemsList.clear();
        itemsList.addAll(tempList);
        Collections.sort(itemsList, new Comparator<QuotationItem>() {
            @Override
            public int compare(QuotationItem o1, QuotationItem o2) {
                return o1.getCardName().compareTo(o2.getCardName());
            }
        });
        notifyDataSetChanged();

    }
    public void allData()
       {
           itemsList.clear();
        itemsList.addAll(tempList);
        notifyDataSetChanged();
    }



}
