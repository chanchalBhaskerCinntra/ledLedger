package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CreditOneActivity;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.activities.ReceiptTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.JournalEntryLineBodyData;
import com.pixplicity.easyprefs.library.Prefs;
import java.util.ArrayList;
import java.util.List;


public class LedgerGeneralEntriesAdapter extends RecyclerView.Adapter<LedgerGeneralEntriesAdapter.ContactViewHolder> {
    Context context;
    List<JournalEntryLineBodyData> branchList;
    List<JournalEntryLineBodyData> tempList = null;

    public LedgerGeneralEntriesAdapter(Context context1, List<JournalEntryLineBodyData> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList = new ArrayList<JournalEntryLineBodyData>();
        this.tempList.addAll(branchList);

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.ledger_adapter, parent, false);
    return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.invoice_id.setText("" + branchList.get(position).getReference1());
        String reverseDate= Globals.convertDateFormat(branchList.get(position).getDueDate());
        holder.invoice_date.setText(" " + reverseDate);
        holder.ref_no.setText("" + branchList.get(position).AccountName);
        Double amountDouble=branchList.get(position).getBalance();
        String amountString=Globals.foo(amountDouble);
        holder.total_amount.setText(Globals.numberToK(amountString));

      //  holder.total_amount.setText("( " + Globals.foo(Double.valueOf(Globals.getRoundOffUpTOTwo("" + branchList.get(position).getBalance()) + " )")));
        Double credit = Double.valueOf(Globals.foo(Double.valueOf(Double.parseDouble(branchList.get(position).credit))));
        Double debit = Double.valueOf(Globals.foo(Double.valueOf(Double.parseDouble(branchList.get(position).debit))));

//        if(branchList.get(position).getType().equalsIgnoreCase("Receipt"))
//            holder.received_amount.setTextColor(Color.parseColor("#FF0000"));

        if (credit > 0.0) {
            holder.received_amount.setText( Globals.numberToK(Globals.getRoundOffUpTOTwo("" +branchList.get(position).credit)));
            holder.received_amount.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.received_amount.setText(Globals.numberToK(Globals.getRoundOffUpTOTwo("" + branchList.get(position).debit)));
            holder.received_amount.setTextColor(Color.parseColor("#4ebf08"));
        }


    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView invoice_id, invoice_date, ref_no, total_amount, received_amount;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_id = itemView.findViewById(R.id.invoice_id);
            invoice_date = itemView.findViewById(R.id.invoice_date);
            ref_no = itemView.findViewById(R.id.ref_no);
            received_amount = itemView.findViewById(R.id.received_amount);
            total_amount = itemView.findViewById(R.id.total_amount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttARInvoice") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0")) {
                        Prefs.putString(Globals.Sale_Purchse_Diff,"ttARInvoice");

                        Intent i = new Intent(context, InvoiceTransactionFullInfo.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        i.putExtra("Heading", "");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttAPInvoice") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0")) {
                        Prefs.putString(Globals.Sale_Purchse_Diff,"ttAPInvoice");
                        Intent i = new Intent(context, InvoiceTransactionFullInfo.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        i.putExtra("Heading", "ttAPInvoice");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttReceipt") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0")) {

                        Intent i = new Intent(context, ReceiptTransactionFullInfo.class);
                        i.putExtra("FromWhere", "");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        // i.putExtra("docEntry", "" + branchList.get(getAdapterPosition()).getOrderId());

                        i.putExtra("ReceiptId", "" + branchList.get(getAdapterPosition()).getDocId());


                        i.putExtra("Heading", "");
                        i.putExtra("status", "");
                        context.startActivity(i);
                    }
                    else if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttARCredItnote") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0"))  {

                        Prefs.putString(Globals.Sale_Purchse_Diff,"ttARCreditNote");
                        Intent i = new Intent(context, CreditOneActivity.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        i.putExtra("Heading", "ttARCreditNote");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }
                    else if(branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttAPCreditNote") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0"))
                    {
                        Prefs.putString(Globals.Sale_Purchse_Diff,"ttAPCreditNote");
                        Intent i = new Intent(context, CreditOneActivity.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        i.putExtra("Heading", "ttAPCreditNote");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }
                    else if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttJournalEntry") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0"))
                    {
                        if (clickListener != null) {
                            clickListener.onItemClick(branchList.get(getAdapterPosition()).getId());
                        }
                    }

                    else if (branchList.get(getAdapterPosition()).getOriginalJournal().equalsIgnoreCase("ttVendorPayment") && !branchList.get(getAdapterPosition()).getDocId().trim().equalsIgnoreCase("0")) {

                        Intent i = new Intent(context, ReceiptTransactionFullInfo.class);
                        i.putExtra("FromWhere", "");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getDocId());
                        // i.putExtra("docEntry", "" + branchList.get(getAdapterPosition()).getOrderId());

                        i.putExtra("ReceiptId", "" + branchList.get(getAdapterPosition()).getDocId());


                        i.putExtra("Heading", "ttVendorPayment");
                        i.putExtra("status", "");
                        context.startActivity(i);
                    }


                }
            });


        }

    }



    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }


    String title="";
    private void shareLedgerData() {
        // title =

      /*  WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(((AppCompatActivity)LedgerReports.this)co.getSupportFragmentManager(),
                "");*/
    }


/*
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (JournalEntryLineBodyData st : tempList) {
                if (st.getAmount() != null && !st.getAmount().isEmpty()) {
                    if (st.getAmount().toLowerCase(Locale.getDefault()).contains(charText)||st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                        Log.e("Search==>",""+branchList.size());
                    }
                }
            }

        }
        notifyDataSetChanged();
    }*/

//    public void filter(String charText)
//    {
//        charText = charText.toLowerCase(Locale.getDefault());
//        branchList.clear();
//        if (charText.length() == 0) {
//            branchList.addAll(tempList);
//        } else {
//            for (LedgerItem st : tempList) {
//                if (st.getAmount() != null && !st.getAmount().isEmpty()) {
//                    if (st.getAmount().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        branchList.add(st);
//                    }
//                }
//            }
//
//        }
//        notifyDataSetChanged();
//    }
}
