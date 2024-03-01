package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ParticularCustomerTransactionAdapter;
import com.cinntra.ledure.adapters.ParticularCustomerpaymentDueAdapter;
import com.cinntra.ledure.adapters.Receivable_JE_CreditAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogDueBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogOverDueSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowInReceivableBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.cinntra.ledure.model.LedgerCustomerResponse;
import com.cinntra.ledure.model.Receivable_JE_Credit;
import com.cinntra.ledure.newapimodel.DataparticularCustomerpaymentDue;
import com.cinntra.ledure.newapimodel.ResponseParticularCustomerPaymentDue;
import com.cinntra.ledure.newapimodel.ResponsePayMentDueCounter;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class ParticularCustomerPaymentDueInfo extends MainBaseActivity {
    RecyclerView customerRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.total_amount)
    TextView total_amount;
    @BindView(R.id.salesamount)
    TextView salesamount;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.type_dropdown)
    Spinner type_dropdown;
    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;
    @BindView(R.id.from_to_date)
    TextView from_to_date;
    @BindView(R.id.tvCreditNote)
    TextView tvCreditNote;

    @BindView(R.id.cardCreditNote)
    CardView cardCreditNote;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    BusinessPartnerData cde;
    String fromWhere = "";
    String reportType = "Gross";

    @BindView(R.id.btnRemindNow)
    Button btnRemindNow;


    String cardCode, cardName;
    /***shubh****/
    String groupName;
    String creditLimit;
    String creditDate;
    String gstNo;
    String mobile;
    String email;
    String address, contactPersonName;

    /***shubh****/
    WebView dialogWeb;
    String url;
    String title = "Share";


    BottomSheetDialogOverDueSelectDateBinding binding;
    ParticularCustomerpaymentDueAdapter adapter;
    LinearLayoutManager layoutManager;

    private void shareLedgerData() {
        url = Globals.perticularBpPaymentCollection + "CardCode=" + cardCode + "&DueDaysGroup=" + overdueDaysFilter;
        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customertransaction_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        receive_pending_layout.setVisibility(View.VISIBLE);
        type_dropdown.setVisibility(View.GONE);
        fromWhere = getIntent().getStringExtra("FromWhere");
        overdueDaysFilter = getIntent().getStringExtra("filterValue");

        if (Prefs.getString("ForReports", "").equalsIgnoreCase("overDue")) {
            overdueDaysFilter = "-1";

            from_to_date.setText("overdue");
        }else {
            if (Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("7")) {
                from_to_date.setText("7");
                overdueDaysFilter = "7";

            } else if (Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("5")) {
                from_to_date.setText("5");
                overdueDaysFilter = "5";
            } else if (Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("2")) {
                from_to_date.setText(" 2");
                overdueDaysFilter = "2";
            } else if (Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("All")) {
                from_to_date.setText("All");
                overdueDaysFilter = "";
            } else {
                from_to_date.setText("All");
                overdueDaysFilter = "";
            }

//
//            overdueDaysFilter = "7";
//
//            from_to_date.setText("7");
        }



        if (fromWhere.equalsIgnoreCase("Receivable")) {
            startDate = "";
            endDate = "";
            //   from_to_date.setText("All");
        } else {
            startDate = Prefs.getString(Globals.FROM_DATE, "");
            endDate = Prefs.getString(Globals.TO_DATE, "");
            //   from_to_date.setText(startDate + " - " + endDate);
        }


        cardCreditNote.setOnClickListener(view -> {
            if (receivable_je_creditArrayList.size() > 0) {
                showListBottomSheetDialog(this);
            } else {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
            //

        });


        cardCode = getIntent().getStringExtra("cardCode");
        cardName = getIntent().getStringExtra("cardName");
        loader.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle(cardCode);
        toolbar.setTitle(cardCode);


        customerRecyclerView = (RecyclerView) findViewById(R.id.customers_recyclerview);
        customerRecyclerView.addOnScrollListener(scrollListener);


        btnRemindNow.setOnClickListener(view -> {
            shareLedgerData();
            //  Toast.makeText(this, "UnAble To Share\nThis Section is Under Maintenance", Toast.LENGTH_SHORT).show();

            // showBottomSheetDialog();
        });

        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                reportType = type_dropdown.getSelectedItem().toString();
                if (fromWhere.trim().equalsIgnoreCase("Receivable")) {
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, "", "");
                    loader.setVisibility(View.VISIBLE);
                    url = Globals.receivableParticular + "Type=Gross&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&DueDaysGroup=" + overdueDaysFilter;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue")) {
//            from_to_date.setText("Not due");
//            overdueDaysFilter = "-1";
//
//        }
//        if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0")) {
//            from_to_date.setText(">0");
//            overdueDaysFilter = "0";
//
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30")) {
//            from_to_date.setText("> 30");
//            overdueDaysFilter = "30";
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60")) {
//            from_to_date.setText("> 60");
//            overdueDaysFilter = "60";
//
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All")) {
//            from_to_date.setText("All");
//            overdueDaysFilter = "";
//        } else {
//            from_to_date.setText("All");
//            overdueDaysFilter = "";
//        }

        if (Globals.checkInternet(this)) {
            callCustomerOnePageReceivable(cardCode, cardName, reportType, startDate, endDate);
        }


    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = "";
    String endDate = "";
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    private void dateRangeSelector() {
        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);

                if (fromWhere.trim().equalsIgnoreCase("Receivable")) {
                    from_to_date.setText(startDate + " - " + endDate);
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, startDate, endDate);
                    loader.setVisibility(View.VISIBLE);
                    url = Globals.receivableParticular + "Type=Gross&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&DueDaysGroup=" + overdueDaysFilter;

                }

            }
        });


    }

    Receivable_JE_CreditAdapter receivable_je_creditAdapter;

    public void showListBottomSheetDialog(Context context) {
        BottomSheetDialogShowInReceivableBinding bindingcustomer;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bindingcustomer = BottomSheetDialogShowInReceivableBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingcustomer.getRoot());
        bindingcustomer.tvCustomerNameBottomSheetDialog.setText(cardName);
        bindingcustomer.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        receivable_je_creditAdapter = new Receivable_JE_CreditAdapter(this, receivable_je_creditArrayList);
        bindingcustomer.rvReceivableList.setAdapter(receivable_je_creditAdapter);
        bindingcustomer.rvReceivableList.setLayoutManager(new LinearLayoutManager(this));

        receivable_je_creditAdapter.setOnItemClickListener(new Receivable_JE_CreditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                String url = Globals.journalVoucher + id;
                String tite = getResources().getString(R.string.share_journal);
                WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                        WebViewBottomSheetFragment.newInstance(dialogWeb, url, tite);
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "");
                //  shareLedgerData();
            }
        });


        bottomSheetDialog.show();

    }


    private ArrayList<DataparticularCustomerpaymentDue> allLedgerCustomerData = new ArrayList<>();
    private ArrayList<LedgerCustomerData> filteredLedgerCustomerData = new ArrayList<>();
    int pageNo = 1;
    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleitempositon = layoutManager.findFirstVisibleItemPosition(); //first item
            int visibleItemCount = layoutManager.getChildCount(); //total number of visible item
            int totalItemCount = layoutManager.getItemCount();   //total number of item

            boolean isNotLoadingAndNotLastPage = !isLoading && !islastPage;
            boolean isAtLastItem = firstVisibleitempositon + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleitempositon >= 0;
            boolean isTotaolMoreThanVisible = totalItemCount >= Globals.QUERY_PAGE_SIZE;
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                            && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                callCustomerALlPageReceivable(cardCode, cardName, reportType, startDate, endDate);

                pageNo++;
                isScrollingpage = false;
                url = Globals.receivableParticular + "Type=Gross&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&DueDaysGroup=" + overdueDaysFilter;


            } else {
                // Log.d(TAG, "onScrolled:not paginate");
                recyclerView.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //it means we are scrolling
                isScrollingpage = true;
            }
        }
    };


    List<Receivable_JE_Credit> receivable_je_creditArrayList = new ArrayList<>();
    String overdueDaysFilter = "7";

    private void callCustomerOnePageReceivable(String cardCode, String cardName, String reportType, String startDate, String endDate) {
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
//                {
//                    "CardCode": "9224",
//                        "OrderByName": "",
//                        "OrderByAmt": "",
//                        "DueDaysGroup": "7",
//                        "PageNo": "1",
//                        "MaxSize": "20"
//                }

                HashMap<String, String> hde = new HashMap<>();

                hde.put("CardCode", cardCode);
                hde.put("OrderByName", "");
                hde.put("OrderByAmt", "");

                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put(Globals.payLoadDueDaysGroup, overdueDaysFilter);

                Call<ResponseParticularCustomerPaymentDue> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getPaymentDueDashboardParticularCustomerPurchase(hde);
                } else {
                    call =  NewApiClient.getInstance().getApiService().getPaymentDueDashboardParticularCustomer(hde);
                }
                try {
                    Response<ResponseParticularCustomerPaymentDue> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                allLedgerCustomerData.clear();
                                if (response.body().getDifferenceAmount() == 0.0) {
                                    salesamount.setText(getResources().getString(R.string.Rs) + "0");
                                    total_amount.setText(getResources().getString(R.string.Rs) + "0");
                                } else {
                                    salesamount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getDifferenceAmount())));
                                    total_amount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getDifferenceAmount())));
                                }

                                //todo need to check
                                 receivable_je_creditArrayList.addAll(response.body().getDataList());
                                double je_cedit = Double.valueOf(response.body().getDataListTotal());
                                double diff = Double.valueOf(response.body().getDifferenceAmount());
                                double total = je_cedit + diff;
                                total_amount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(total)));

                                tvCreditNote.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(je_cedit)));

                                if (total <= 0)
                                    btnRemindNow.setVisibility(View.GONE);


                                /***shubh****/
                                if (response.body().getbPData().size() > 0) {


                                    creditDate = response.body().getbPData().get(0).getCreditLimitDayes();
                                    creditLimit = response.body().getbPData().get(0).getCreditLimit();
                                    groupName = response.body().getbPData().get(0).getGroupName();
                                    gstNo = response.body().getbPData().get(0).getgSTIN();
                                    mobile = response.body().getbPData().get(0).getPhone1();
                                    email = response.body().getbPData().get(0).getEmailAddress();
                                    address = response.body().getbPData().get(0).getbPAddress();
                                    contactPersonName = response.body().getbPData().get(0).getContactPerson();
                                }


                                allLedgerCustomerData.addAll(response.body().getData());
                                adapter = new ParticularCustomerpaymentDueAdapter(ParticularCustomerPaymentDueInfo.this, allLedgerCustomerData, cardName, fromWhere);
                                customerRecyclerView.setAdapter(adapter);
                                customerRecyclerView.setLayoutManager(layoutManager);

                                if (response.body().getbPData().size() == 0)
                                    btnRemindNow.setVisibility(View.GONE);
                                // Update UI element here
                                loader.setVisibility(View.GONE);
                                if (response.body().getData().isEmpty()) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);
                                }

                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }

    private void callCustomerALlPageReceivable(String cardCode, String cardName, String reportType, String startDate, String endDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardCode);
                hde.put("OrderByName", "");
                hde.put("OrderByAmt", "");

                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put(Globals.payLoadDueDaysGroup, overdueDaysFilter);

                Call<ResponseParticularCustomerPaymentDue> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getPaymentDueDashboardParticularCustomerPurchase(hde);
                } else {
                    call =  NewApiClient.getInstance().getApiService().getPaymentDueDashboardParticularCustomer(hde);
                }

                try {
                    Response<ResponseParticularCustomerPaymentDue> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                allLedgerCustomerData.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (response.body().getData().size() == 0) {
                                    pageNo++;
                                    // no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    // no_datafound.setVisibility(View.INVISIBLE);
                                }
                                // Update UI element here
                                loader.setVisibility(View.GONE);
                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        menu.findItem(R.id.share_received).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.calendar).setVisible(true);
        MenuItem clearallfilter = menu.findItem(R.id.clearAllFilter);
        clearallfilter.setVisible(false);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ledger:
                Intent i = new Intent(this, LedgerReports.class);
                i.putExtra("cardCode", cardCode);
                i.putExtra("where", "particular");
                startActivity(i);
                //startActivity(new Intent(this,LedgerReports.class));
                break;
            case R.id.calendar:
                // Globals.selectDat(this);
                /***shubh****/
                if (Prefs.getString("ForReports", "").equalsIgnoreCase("overDue")) {

                }else {
                    showOverDueDateBottomSheetDialog(ParticularCustomerPaymentDueInfo.this);
                }


                break;
            case R.id.info_trans:
                Globals.showCustomerBottomSheetDialog(ParticularCustomerPaymentDueInfo.this, cardName, groupName, creditLimit, creditDate, mobile, address, email, getLayoutInflater(), gstNo, contactPersonName);

                break;

            case R.id.share_received:
                // Globals.selectDat(this);
/***shubh****/
                showBottomSheetDialog();
                // showCustomerBottomSheetDialog(ParticularCustomerReceivableInfo.this, cardName, groupName, creditLimit, creditDate, gstNo, mobile, address, email);
                break;

        }
        return true;
    }


    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        url = Globals.receivableParticular + "Type=Gross&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&DueDaysGroup=" + overdueDaysFilter;


        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.share);


        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        binding.ivForword.setOnClickListener(view -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


        binding.linearWhatsappShare.setOnClickListener(view ->
        {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            lab_pdf(dialogWeb, f_name);
        });

        binding.linearOtherShare.setOnClickListener(view ->
                {
                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_other_pdf(dialogWeb, f_name);

                }
        );
        binding.linearGmailShare.setOnClickListener(view -> {

                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_gmail_pdf(dialogWeb, f_name);
                }
        );

    }

    /***shubh****/
    private void lab_gmail_pdf(WebView webView, String f_name) {
        // String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(this, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                gmailShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }

    /***shubh****/
    private void gmailShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(this, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, apkURI);

        // share.setData(Uri.parse("mailto:" + recipientEmail));


        share.setPackage("com.google.android.gm");

        startActivity(share);
    }

    /***shubh****/
    private void lab_other_pdf(WebView webView, String f_name) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(this, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                otherShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    /***shubh****/
    private void whatsappShare(String fName) {
        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                this,
                ParticularCustomerPaymentDueInfo.this.getPackageName() + ".FileProvider", file);


//        Uri apkURI = FileProvider.getUriForFile(
//                this,
//                ParticularCustomerReceivableInfo.this.getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(this, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());
        try {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, apkURI);
            if (isAppInstalled("com.whatsapp"))
                share.setPackage("com.whatsapp");
            else if (isAppInstalled("com.whatsapp.w4b"))
                share.setPackage("com.whatsapp.w4b");

            startActivity(share);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }
    }


    /***shubh****/
    private void otherShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                this,
                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(this, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, apkURI);


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share PDF using"));
        }
    }

    /***shubh****/
    private void lab_pdf(WebView webView, String f_name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        //        try {
        //            if (!f.getParentFile().exists())
        //                f.getParentFile().mkdirs();
        //            if (!f.exists())
        //                f.createNewFile();
        //        } catch (IOException e) {
        //            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //        }
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(this, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                whatsappShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();
                Toast.makeText(ParticularCustomerPaymentDueInfo.this, "error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    /***shubh****/
    private void setUpWebViewDialog(WebView webView, String url, Boolean isZoomAvailable, ProgressBar dialog, LinearLayout whatsapp, LinearLayout gmail, LinearLayout other) {

        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.getSettings().setBuiltInZoomControls(isZoomAvailable);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        // Setting we View Client
        whatsapp.setEnabled(false);
        gmail.setEnabled(false);
        other.setEnabled(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap btm) {
                super.onPageStarted(view, url, null);
                dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // initializing the printWeb Object
                dialog.setVisibility(View.GONE);
                dialogWeb = webView;


                whatsapp.setEnabled(true);
                gmail.setEnabled(true);
                other.setEnabled(true);
            }
        });


        webView.loadUrl(url);
    }


    public boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    private static final String TAG = "ParticularCustomerRecei";

    private void showOverDueDateBottomSheetDialog(Context context) {
        BottomSheetDialogDueBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogDueBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });

        binding.rbSeven.setChecked(true);


//        binding.rblessThan0.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0"));
//        binding.rbNonDue.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue"));
//
//        binding.rblessThan30.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30"));
//
//        binding.rblessThan60.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60"));
//
//        binding.rbAll.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All"));

        binding.rbSeven.setChecked(Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("7"));

        binding.rbFive.setChecked(Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("5"));
        binding.rbTwo.setChecked(Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("2"));


        binding.rbAll.setChecked(Prefs.getString(Globals.OVER_DUE_DATE_PREF, "").equalsIgnoreCase("All"));


        binding.rgOverDue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbSeven.getId()) {
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getTodaysDate();
                    pageNo = 1;
                    overdueDaysFilter = "7";
                    //   Prefs.putString(Globals.FROM_DATE_receivable, "nondue");
                    Prefs.putString(Globals.OVER_DUE_DATE_PREF, "7");
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, "", "");
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("7");

                    Log.e(TAG, "onCheckedChanged: " + Globals.getTodaysDate());

                } else if (i == binding.rbFive.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(0));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(0);
                    pageNo = 1;
                    // Prefs.putString(Globals.FROM_DATE_receivable, "0");
                    Prefs.putString(Globals.OVER_DUE_DATE_PREF, "5");
                    overdueDaysFilter = "5";
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, "", "");
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("5");

                } else if (i == binding.rbTwo.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-30));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-30);
                    pageNo = 1;
                    // Prefs.putString(Globals.FROM_DATE_receivable, "30");
                    overdueDaysFilter = "2";
                    Prefs.putString(Globals.OVER_DUE_DATE_PREF, "2");
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, "", "");
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("2");

                }else if (i == binding.rbAll.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-30));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-30);
                    pageNo = 1;
                    // Prefs.putString(Globals.FROM_DATE_receivable, "30");
                    Prefs.putString(Globals.OVER_DUE_DATE_PREF, "All");
                    overdueDaysFilter = "";
                    callCustomerOnePageReceivable(cardCode, cardName, reportType, "", "");
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("All");

                }
            }
        });
        bottomSheetDialog.show();

    }


}