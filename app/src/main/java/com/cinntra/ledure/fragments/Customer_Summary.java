package com.cinntra.ledure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.activities.ParticularBpCreditNoteActivity;
import com.cinntra.ledure.activities.PendingOrderSubListActivity;
import com.cinntra.ledure.adapters.PurchaseLedgerAdapter;
import com.cinntra.ledure.adapters.ReceiptLedgerAdapter;
import com.cinntra.ledure.adapters.ReceivableLedgerAdapter;
import com.cinntra.ledure.adapters.SaleLedgerAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentClickListener;
import com.cinntra.ledure.model.CustomerLedgerRes;
import com.cinntra.ledure.model.CustomerLedgerResponse;
import com.cinntra.ledure.model.MonthGroupPurchaseList;
import com.cinntra.ledure.model.MonthGroupSalesList;
import com.cinntra.ledure.model.UnderList;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Customer_Summary extends Fragment implements View.OnClickListener, LedgerCutomerDetails.MyFragmentListener, LedgerCutomerDetails.MyFragmentCustomerListener {

    @BindView(R.id.last_sale_date)
    TextView last_sale_date;
    @BindView(R.id.last_receipt_date)
    TextView last_receipt_date;
    @BindView(R.id.no_of_invoices)
    TextView no_of_invoices;
    @BindView(R.id.avg_sale)
    TextView avg_sale;
    @BindView(R.id.sale_amount)
    TextView sale_amount;
    @BindView(R.id.receipt_amount)
    TextView receipt_amount;

    @BindView(R.id.tvCreditLimitAndDysCustomerSummary)
    TextView tvCreditLimitAndDysCustomerSummary;
    @BindView(R.id.tvJeCredit)
    TextView tvJeCredit;
    @BindView(R.id.receivable_amount)
    TextView receivable_amount;

    @BindView(R.id.sale_divider)
    View sale_divider;
    @BindView(R.id.purchase_divider)
    View purchase_divider;
    @BindView(R.id.receipt_divider)
    View receipt_divider;

    @BindView(R.id.receivable_recyclerview)
    RecyclerView receivable_recyclerview;
    @BindView(R.id.receivable_arrow)
    ImageView receivable_arrow;
    @BindView(R.id.receivableLay)
    LinearLayout receivableLay;
    @BindView(R.id.receipt_recyclerview)
    RecyclerView receipt_recyclerview;
    @BindView(R.id.receipt_arrow)
    ImageView receipt_arrow;
    @BindView(R.id.receiptLay)
    LinearLayout receiptLay;
    @BindView(R.id.sale_recyclerview)
    RecyclerView sale_recyclerview;

    @BindView(R.id.purchase_recyclerview)
    RecyclerView purchase_recyclerview;
    @BindView(R.id.purchase_arrow)
    ImageView purchase_arrow;
    @BindView(R.id.sale_arrow)
    ImageView sale_arrow;
    @BindView(R.id.saleLay)
    LinearLayout saleLay;
    @BindView(R.id.linearPendingSaleOrder)
    LinearLayout linearPendingSaleOrder;

    @BindView(R.id.tvAvgPaymentDays)
    TextView tvAvgPaymentDays;

    @BindView(R.id.tvPendingSaleOrder)
    TextView tvPendingSaleOrder;

    @BindView(R.id.tvCreditNoteSummary)
    TextView tvCreditNoteSummary;

    @BindView(R.id.tvje)
    TextView tvje;

    @BindView(R.id.ibOverviewArrow)
    ImageButton ibOverviewArrow;

    @BindView(R.id.loader)
    ProgressBar loader;


    @BindView(R.id.purchase_amount)
    TextView purchase_amount;
    @BindView(R.id.purchase_receipt_amount)
    TextView purchase_receipt_amount;
    @BindView(R.id.purchase_receivable_amount)
    TextView purchase_receivable_amount;
    @BindView(R.id.purchaseLay)
    LinearLayout purchaseLay;
    @BindView(R.id.purchaseSection)
    LinearLayout purchaseSection;

    @BindView(R.id.linearCreditReturn)
    LinearLayout linearCreditReturn;

    @BindView(R.id.linearCreditNote)
    LinearLayout linearCreditNote;
    @BindView(R.id.creditView)
    LinearLayout creditView;

    @BindView(R.id.upperLay)
    LinearLayout upperLay;

    @BindView(R.id.linearOverView)
    LinearLayout linearOverView;


    RelativeLayout relativeCalView;

    RelativeLayout toDay;

    FragmentClickListener fragmentClickListener;
    String reportType = "Gross";
    String cardCode;
    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    public Customer_Summary(String cardCode, TextView from_to_date, RelativeLayout toDay) {
        // Required empty public constructor
        this.cardCode = cardCode;
        this.from_to_date = from_to_date;
        this.toDay = toDay;

    }

    public static Customer_Summary newInstance(String cardCode, TextView from_to_date, RelativeLayout toDay) {
        Customer_Summary fragment = new Customer_Summary("", null, null);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        //Toast.makeText(fragment.getActivity(), "In Customer_Summary", Toast.LENGTH_SHORT).show();
        Log.e("TestB=>", "In Customer_Summary");
        //  customerLedger(cardCode, "", "");
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TestB=>", "In Customer_SummaryResume");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("TestB=>", "In Customer_SummaryCreate");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_summary, container, false);
        ButterKnife.bind(this, v);

        fragmentClickListener = (FragmentClickListener) getActivity();
        //  relativeCalView = (RelativeLayout) getActivity().findViewById(R.id.relativeCalView);

        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        Log.e("SUMMARYDATE===>", "onCreateView: start->" + startDate + "enddate" + endDate);
        customerLedger(cardCode, startDate, endDate);
        saleLay.setOnClickListener(this);
        receiptLay.setOnClickListener(this);
        receivableLay.setOnClickListener(this);
        purchaseLay.setOnClickListener(this);
        linearOverView.setVisibility(View.VISIBLE);
        ibOverviewArrow.setImageResource(R.drawable.ic_arrow_down);

        ibOverviewArrow.setOnClickListener(view -> {
            if (linearOverView.getVisibility() == View.VISIBLE) {
                linearOverView.setVisibility(View.GONE);
                ibOverviewArrow.setImageResource(R.drawable.ic_arrow_up);
            } else {
                linearOverView.setVisibility(View.VISIBLE);
                ibOverviewArrow.setImageResource(R.drawable.ic_arrow_down);
            }
        });

        upperLay.setOnClickListener(view -> {
            if (linearOverView.getVisibility() == View.VISIBLE) {
                linearOverView.setVisibility(View.GONE);
                ibOverviewArrow.setImageResource(R.drawable.ic_arrow_up);
            } else {
                linearOverView.setVisibility(View.VISIBLE);
                ibOverviewArrow.setImageResource(R.drawable.ic_arrow_down);
            }
        });

        linearCreditNote.setOnClickListener(view -> {
            Prefs.putString(Globals.Sale_Purchse_Diff, "ttARCreditNote");
            Intent intent = new Intent(getActivity(), ParticularBpCreditNoteActivity.class);
            intent.putExtra("FromWhere", "Credits");
            intent.putExtra("code", cardCode);
            intent.putExtra("name", cardName);
            startActivity(intent);
        });

        linearCreditReturn.setOnClickListener(view -> {
            Prefs.putString(Globals.Sale_Purchse_Diff, "ttAPCreditNote");
            Intent intent = new Intent(getActivity(), ParticularBpCreditNoteActivity.class);
            intent.putExtra("FromWhere", "return");
            intent.putExtra("code", cardCode);
            intent.putExtra("name", cardName);
            startActivity(intent);
        });


        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.saleLay) {
            int count = 0;
            if (saleAdapter != null)
                count = saleAdapter.getItemCount();
            setViewChange(sale_recyclerview, sale_arrow, sale_divider, count);
        } else if (view.getId() == R.id.purchaseLay) {
            int countPurchase = 0;
            if (purchaseLedgerAdapter != null)
                countPurchase = purchaseLedgerAdapter.getItemCount();
            setViewChange(purchase_recyclerview, purchase_arrow, purchase_divider, countPurchase);


        } else if (view.getId() == R.id.receiptLay) {
            int count = 0;
            if (receiptAdapter != null)
                count = receiptAdapter.getItemCount();
            setViewChange(receipt_recyclerview, receipt_arrow
                    , receipt_divider, count);
        } else if (view.getId() == R.id.receivableLay) {
            setViewChangeReceivable(receivable_recyclerview, receivable_arrow, creditView, null, 0);
        }
    }

    private void setViewChange(RecyclerView recyclerView, ImageView imageView, View divider, int count) {

        if (recyclerView.getVisibility() == View.VISIBLE) {
            //   imageView.setRotation(180);
            recyclerView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_arrow_down);
            if (divider != null && count > 0)
                divider.setVisibility(View.VISIBLE);
        } else {
            // imageView.setRotation(180);
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_arrow_up);

            if (divider != null && count > 0)
                divider.setVisibility(View.GONE);
        }

    }

    private void setViewChangeReceivable(RecyclerView recyclerView, ImageView imageView, LinearLayout textView, View divider, int count) {


        if (recyclerView.getVisibility() == View.VISIBLE) {
            //  imageView.setRotation(180);
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_arrow_down);
            if (divider != null && count > 0)
                divider.setVisibility(View.VISIBLE);
        } else {
            // imageView.setRotation(180);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_arrow_up);
            if (divider != null && count > 0)
                divider.setVisibility(View.GONE);

        }

    }


    private void customerLedger(String customerCode, String fromDate, String toDate) {
        loader.setVisibility(View.VISIBLE);
        startDate = Prefs.getString(Globals.FROM_DATE, fromDate);
        endDate = Prefs.getString(Globals.TO_DATE, toDate);
        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);

        Call<CustomerLedgerResponse> call = NewApiClient.getInstance().getApiService().BPLedgerDetails(hde);
        call.enqueue(new Callback<CustomerLedgerResponse>() {
            @Override
            public void onResponse(Call<CustomerLedgerResponse> call, Response<CustomerLedgerResponse> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        loader.setVisibility(View.GONE);
                        if (response.body().getCustomerLedgerRes().size() > 0)
                            setData(response.body().getCustomerLedgerRes().get(0));
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerLedgerResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    SaleLedgerAdapter saleAdapter;
    PurchaseLedgerAdapter purchaseLedgerAdapter;
    ReceiptLedgerAdapter receiptAdapter;
    ReceivableLedgerAdapter receivableAdapter;
    private String cardName = "";

    private void setData(CustomerLedgerRes res) {

        last_sale_date.setText(Globals.convertDateFormat(res.getLastSalesDate()));
        last_receipt_date.setText(Globals.convertDateFormat(res.getLastRecipetDate()));
        no_of_invoices.setText(res.getInvoiceCount());
        avg_sale.setText("₹ " + Globals.numberToK(String.valueOf(res.getAvgInvoiceAmount())));
        sale_amount.setText("₹ " + Globals.numberToK(String.valueOf(res.getTotalSales())));
        receipt_amount.setText("₹ " + Globals.numberToK(String.valueOf(res.getTotalReceipt())));
        double ress = Double.valueOf(res.getTotalReceivable()) + Double.valueOf(res.getTotalJECreditNote());
        receivable_amount.setText("₹ " + Globals.numberToK(String.valueOf(ress)));
        tvAvgPaymentDays.setText("" + res.getAvgPayDays());
        tvPendingSaleOrder.setText("₹ " + Globals.numberToK(String.valueOf(res.getPendingAmount())));
        tvCreditNoteSummary.setText("₹ " + Globals.numberToK(String.valueOf(res.getTotalCreditNote())));
        tvje.setText("₹ " + Globals.numberToK(String.valueOf(res.getTotalJECreditNote())));

        cardName = res.getCardName();
        linearPendingSaleOrder.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), PendingOrderSubListActivity.class);
            intent.putExtra("CardCode", res.getCardCode());
            intent.putExtra("CardName", res.getCardName());
            startActivity(intent);

        });

        tvCreditLimitAndDysCustomerSummary.setText("Credit: " + Globals.numberToK(res.getCreditLimit()) + " | " + res.getCreditLimitLeft());
        tvJeCredit.setText("JE/Credit: " + "₹ " + Globals.numberToK(String.valueOf(res.getTotalJECreditNote())));


        List<MonthGroupSalesList> salesmonthlist = monthSorting(res.getMonthGroupSalesList());
        saleAdapter = new SaleLedgerAdapter(getActivity(), salesmonthlist, res.getCardCode(), res.getCardName());
        List<MonthGroupSalesList> receiptList = monthSorting(res.getMonthGroupReceiptList());
        receiptAdapter = new ReceiptLedgerAdapter(getActivity(), receiptList, res.getCardCode(), res.getCardName());

        List<MonthGroupPurchaseList> purchasemonthlist = res.getMonthGroupPurchaseList();

        SimpleDateFormat formatter = new SimpleDateFormat("MMM yy");

        Collections.sort(purchasemonthlist, new Comparator<MonthGroupPurchaseList>() {
            @Override
            public int compare(MonthGroupPurchaseList item1, MonthGroupPurchaseList item2) {
                Date date1, date2;
                try {
                    date1 = formatter.parse(item1.getMonth());
                    date2 = formatter.parse(item2.getMonth());
                } catch (ParseException e) {
                    // Handle the parsing exception if necessary.
                    return 0;
                }
                return date1.compareTo(date2);
            }
        });

        purchaseLedgerAdapter = new PurchaseLedgerAdapter(getActivity(), purchasemonthlist, res.getCardCode(), res.getCardName());

        receipt_recyclerview.setAdapter(receiptAdapter);
        sale_recyclerview.setAdapter(saleAdapter);
        purchase_recyclerview.setAdapter(purchaseLedgerAdapter);


        List<MonthGroupSalesList> over_under = new ArrayList<>();
        MonthGroupSalesList obj_over = new MonthGroupSalesList();
        MonthGroupSalesList obj_over0 = new MonthGroupSalesList();
        MonthGroupSalesList obj_over30 = new MonthGroupSalesList();
        MonthGroupSalesList obj_over60 = new MonthGroupSalesList();

        if (res.getOverList() == null || res.getOverList().size() == 0) {
            obj_over.setMonth(">0 Days");
            //obj_over.setDocTotal("0.0");
            obj_over.setDocTotal(setOverDue0(res.getOverList()));
            over_under.add(obj_over);
        } else {


            obj_over60.setMonth(">60 Days");
            obj_over60.setDocTotal(setOverDue60(res.getOverList()));
            over_under.add(obj_over60);

            obj_over30.setMonth(">30 Days");
            obj_over30.setDocTotal(setOverDue30(res.getOverList()));
            over_under.add(obj_over30);

            obj_over.setMonth(">0 Days");
            //obj_over.setDocTotal("0.0");
            obj_over.setDocTotal(setOverDue0(res.getOverList()));
            over_under.add(obj_over);


        }

        MonthGroupSalesList obj_under = new MonthGroupSalesList();
        if (res.getUnderList() == null || res.getUnderList().size() == 0) {
            obj_under.setMonth("Not Due");
            obj_under.setDocTotal("0.0");
        } else {
            obj_under.setMonth("Not Due");
            obj_under.setDocTotal(setOverDue(res.getUnderList()));
        }
        over_under.add(obj_under);

        receivableAdapter = new ReceivableLedgerAdapter(getActivity(), over_under, res.getCardCode(), res.getCardName());
        receivable_recyclerview.setAdapter(receivableAdapter);


        /************************* Purchase Management ************************************/
        if (res.getLinkedBusinessPartner().equalsIgnoreCase("None") || res.getLinkedBusinessPartner().isEmpty()) {
            purchaseSection.setVisibility(View.GONE);
        } else {
            purchaseSection.setVisibility(View.VISIBLE);
            purchase_amount.setText("₹ " + Globals.numberToK(res.getTotalPurchases()));
            purchase_receipt_amount.setText("₹ " + Globals.numberToK(res.getTotalPurchasesReceipt()));
            purchase_receivable_amount.setText("₹ " + Globals.numberToK(res.getPurchaseCreditNote()));


        }


    }

    private String setOverDue(List<UnderList> list) {
        double doctotal = 0;
        if (list != null) {


            for (int i = 0; i < list.size(); i++) {
                doctotal = doctotal + Double.valueOf(list.get(i).getDocTotal().trim());
            }
        }
        return String.valueOf(doctotal);
    }

    private String setOverDue0(List<UnderList> list) {
        double doctotal = 0;
        if (list != null) {
            //data.getOverDueDays() < 60 && data.getOverDueDays() != 0
            for (int i = 0; i < list.size(); i++) {
                //  if (list.get(i).getOverDueGroup().trim().equalsIgnoreCase("0"))
                if (Integer.valueOf(list.get(i).getOverDueGroup()) == 0) {
                    doctotal = doctotal + Double.valueOf(list.get(i).getDocTotal().trim());
                }

            }
        }
        return String.valueOf(doctotal);
    }

    private String setOverDue30(List<UnderList> list) {
        double doctotal = 0;
        if (list != null) {


            for (int i = 0; i < list.size(); i++) {
                int temp = Integer.valueOf(list.get(i).getOverDueGroup());
                if (temp <= 30 && temp != 0) {
                    doctotal = doctotal + Double.valueOf(list.get(i).getDocTotal().trim());
                }


            }
        }
        return String.valueOf(doctotal);
    }

    private String setOverDue60(List<UnderList> list) {
        double doctotal = 0;
        if (list != null) {


            for (int i = 0; i < list.size(); i++) {
                // if (Integer.valueOf(list.get(i).getOverDueGroup().trim()) >= 60)
                int temp = Integer.valueOf(list.get(i).getOverDueGroup());
                if (temp <= 60 && temp != 0 && temp > 30) {
                    doctotal = doctotal + Double.valueOf(list.get(i).getDocTotal().trim());
                }
            }
        }
        return String.valueOf(doctotal);
    }


    @Override
    public void onDataPassed(String startDate, String endDate) {
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        Log.e("SUMMARYDATE===>", "onCreateView: start->" + startDate + "enddate" + endDate);
        Log.e("DATE>>>>>>", "onDataPassedSummary: " + startDate + endDate);
        customerLedger(cardCode, startDate, endDate);
        // Toast.makeText(requireContext(), "summary" + startDate + endDate, Toast.LENGTH_SHORT).show();
        // customerLedger(cardCode,startDate,endDate);
    }

    @Override
    public void onDataPassedCustomer(String startDate, String endDate) {
        Log.e("DATE>>>>>>", "onDataCustomerPassedSummary: " + startDate + endDate);
        Toast.makeText(requireContext(), "" + startDate + endDate, Toast.LENGTH_SHORT).show();
        customerLedger(cardCode, startDate, endDate);
    }


    private List<MonthGroupSalesList> monthSorting(List<MonthGroupSalesList> list) {


        SimpleDateFormat formatter = new SimpleDateFormat("MMM yy");

        Collections.sort(list, new Comparator<MonthGroupSalesList>() {
            @Override
            public int compare(MonthGroupSalesList item1, MonthGroupSalesList item2) {
                Date date1, date2;
                try {
                    date1 = formatter.parse(item1.getMonth());
                    date2 = formatter.parse(item2.getMonth());
                } catch (ParseException e) {
                    // Handle the parsing exception if necessary.
                    return 0;
                }
                return date1.compareTo(date2);
            }
        });

        return list;
    }

}