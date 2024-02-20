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
import com.cinntra.ledure.activities.CreditOneActivity;
import com.cinntra.ledure.activities.InvoiceTransactionFullInfo;
import com.cinntra.ledure.activities.ReceiptTransactionFullInfo;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.Receivable_JE_Credit;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;


public class PaymentDue_JE_CreditAdapter extends RecyclerView.Adapter<PaymentDue_JE_CreditAdapter.ContactViewHolder> {
    Context context;
    List<Receivable_JE_Credit> branchList;
    List<Receivable_JE_Credit> tempList = null;

    public PaymentDue_JE_CreditAdapter(Context context1, List<Receivable_JE_Credit> branchList) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList = new ArrayList<Receivable_JE_Credit>();
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

        holder.invoice_id.setText("Doc Num : " + branchList.get(position).getDocNum());
        String reverseDate = Globals.convertDateFormat(branchList.get(position).getDocDate());
        holder.invoice_date.setText(" " + reverseDate);
        //holder.ref_no.setText("" + branchList.get(position).AccountName);
        Double amountDouble = Double.valueOf(branchList.get(position).getDocTotal());
        String amountString = Globals.foo(amountDouble);
        holder.total_amount.setText(Globals.numberToK(amountString));
        holder.tvTypeOfnote.setVisibility(View.VISIBLE);


        if (branchList.get(position).getDocType().equalsIgnoreCase("ttARInvoice") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("ARInvoice");
        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttAPInvoice") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("APInvoice");
        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttReceipt") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("Receipt");

        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttARCredItnote") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("ARCredItnote");

        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttAPCreditNote") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("APCreditNote");
        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttJournalEntry") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("JournalEntry");
        } else if (branchList.get(position).getDocType().equalsIgnoreCase("ttVendorPayment") && !branchList.get(position).getId().trim().equalsIgnoreCase("0")) {
            holder.tvTypeOfnote.setText("VendorPayment");
        }


    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView invoice_id, invoice_date, ref_no, total_amount, received_amount, tvTypeOfnote;
        ImageView edit;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_id = itemView.findViewById(R.id.invoice_id);
            invoice_date = itemView.findViewById(R.id.invoice_date);
            ref_no = itemView.findViewById(R.id.ref_no);
            received_amount = itemView.findViewById(R.id.received_amount);
            tvTypeOfnote = itemView.findViewById(R.id.tvTypeOfnote);
            total_amount = itemView.findViewById(R.id.total_amount);
            received_amount.setVisibility(View.INVISIBLE);
            ref_no.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttARInvoice") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {
                        Prefs.putString(Globals.Sale_Purchse_Diff, "ttARInvoice");

                        Intent i = new Intent(context, InvoiceTransactionFullInfo.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        i.putExtra("Heading", "");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttAPInvoice") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {
                        Prefs.putString(Globals.Sale_Purchse_Diff, "ttAPInvoice");
                        Intent i = new Intent(context, InvoiceTransactionFullInfo.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        i.putExtra("Heading", "ttAPInvoice");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttReceipt") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {

                        Intent i = new Intent(context, ReceiptTransactionFullInfo.class);
                        i.putExtra("FromWhere", "");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        // i.putExtra("docEntry", "" + branchList.get(getAdapterPosition()).getOrderId());

                        i.putExtra("ReceiptId", "" + branchList.get(getAdapterPosition()).getId());


                        i.putExtra("Heading", "");
                        i.putExtra("status", "");
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttARCredItnote") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {

                        Prefs.putString(Globals.Sale_Purchse_Diff, "ttARCreditNote");
                        Intent i = new Intent(context, CreditOneActivity.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        i.putExtra("Heading", "ttARCreditNote");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttAPCreditNote") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {
                        Prefs.putString(Globals.Sale_Purchse_Diff, "ttAPCreditNote");
                        Intent i = new Intent(context, CreditOneActivity.class);
                        i.putExtra("FromWhere", "Ledger");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        i.putExtra("Heading", "ttAPCreditNote");
                        i.putExtra("status", branchList.get(getAdapterPosition()));
                        context.startActivity(i);
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttJournalEntry") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {
                        if (clickListener != null) {
                            clickListener.onItemClick(branchList.get(getAdapterPosition()).getId());
                        }
                    } else if (branchList.get(getAdapterPosition()).getDocType().equalsIgnoreCase("ttVendorPayment") && !branchList.get(getAdapterPosition()).getId().trim().equalsIgnoreCase("0")) {

                        Intent i = new Intent(context, ReceiptTransactionFullInfo.class);
                        i.putExtra("FromWhere", "");
                        i.putExtra("ID", "" + branchList.get(getAdapterPosition()).getId());
                        // i.putExtra("docEntry", "" + branchList.get(getAdapterPosition()).getOrderId());

                        i.putExtra("ReceiptId", "" + branchList.get(getAdapterPosition()).getId());


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


}
