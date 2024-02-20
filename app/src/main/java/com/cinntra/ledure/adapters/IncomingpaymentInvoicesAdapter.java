package com.cinntra.ledure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.databinding.ItemBillDetailsBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.IncomingPaymentInvoices;

import java.util.ArrayList;
import java.util.List;

public class IncomingpaymentInvoicesAdapter extends RecyclerView.Adapter<IncomingpaymentInvoicesAdapter.IncomingpaymentInvoicesViewHolder> {
    Context context;
    List<IncomingPaymentInvoices> incomingPaymentInvoicesList;

    public IncomingpaymentInvoicesAdapter(Context context, List<IncomingPaymentInvoices> incomingPaymentInvoicesList) {
        this.context = context;
        this.incomingPaymentInvoicesList = incomingPaymentInvoicesList;

    }

    @NonNull
    @Override
    public IncomingpaymentInvoicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomingpaymentInvoicesViewHolder(ItemBillDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingpaymentInvoicesViewHolder holder, int position) {
        final IncomingPaymentInvoices obj=incomingPaymentInvoicesList.get(position);
        holder.binding.tvBillAmount.setText(Globals.numberToK(obj.getAppliedSys()));
        holder.binding.tvBillCode.setText(obj.getInvoiceDocEntry());


    }

    @Override
    public int getItemCount() {
        return incomingPaymentInvoicesList.size();
    }

    class IncomingpaymentInvoicesViewHolder extends RecyclerView.ViewHolder {

        ItemBillDetailsBinding binding;//Name of the test_list_item.xml in camel case + "Binding"

        public IncomingpaymentInvoicesViewHolder(ItemBillDetailsBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

}
