package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.LedgerCustomerEntriesAdapter;
import com.cinntra.ledure.adapters.LedgerGeneralEntriesAdapter;
import com.cinntra.ledure.adapters.LedgersAdapter;

import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.DataCustomerLedger;
import com.cinntra.ledure.model.LeadFilter;
import com.cinntra.ledure.model.LedgerItem;
import com.cinntra.ledure.model.LedgerResponse;
import com.cinntra.ledure.model.ResponseCustomerLedger;
import com.cinntra.ledure.model.ResponseJournalEntryBpWise;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LedgerReports extends MainBaseActivity implements View.OnClickListener {

    private static final String TAG = "LedgerReports";
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    Context mContext;
    @BindView(R.id.customer_lead_List)
    RecyclerView recyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    //    @BindView(R.id.idPBLoading)
//    ProgressBar idPBLoading;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.closing_balance)
    TextView closing_balance;

    @BindView(R.id.opening_balance)
    TextView opening_balance;

    @BindView(R.id.opening_text)
    TextView heading_opening_balance;

    @BindView(R.id.closing_txt)
    TextView heading_closing_balance;


    String type = "Gross";
    String cardCode = "";
    String searchTextValue = "";

    List<LeadValue> leadValueList = new ArrayList<>();

    LedgersAdapter adapter;
    LeadFilter value = new LeadFilter();

    int pageno = 1;
    boolean recallapi = true;
    String where;
    String filter = "";
    String code = "";

    public LedgerReports() {
        // Required empty public constructor
    }


    LedgerCustomerEntriesAdapter customeAdapter;

    // TODO: Rename and change types and number of parameters


    private void SetUPDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading....")
                .setMessage("Please Wait")
                .setCancelable(false);


        alertDialog = builder.create();
    }

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledger_reports);

        ButterKnife.bind(this);
        loader.setVisibility(View.VISIBLE);
        SetUPDialog();
        alertDialog.show();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);
        layoutManager = new LinearLayoutManager(this);
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");

        Log.e(TAG, "onCreate: " + startDate + endDate);
        mContext = LedgerReports.this;
        value.setAssignedTo(Globals.TeamEmployeeID);
        value.setLeadType("All");
        cardCode = getIntent().getStringExtra("cardCode");

        where = getIntent().getStringExtra("where");
        //  ledgerGeneralEntryReport(cardCode, "", "");

        if (where.equalsIgnoreCase("customer")) {
            filter = getIntent().getStringExtra("filter");
            code = getIntent().getStringExtra("code");
            ledgerCustomerOnePageReport();
            heading_opening_balance.setText("Received");
            heading_closing_balance.setText("Total Sales");
            url = Globals.customerPdfUrl + "Type=Gross&Filter=&FromDate=" + Globals.firstDateOfFinancialYear() + "&ToDate=" + Globals.lastDateOfFinancialYear() + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Code=";
            recyclerView.addOnScrollListener(scrollListener);
        } else {
            /***shubh****/
            //  url = Globals.LedgerUrl + "CardCode=9580&FromDate=12-4-2023&ToDate=23-4-2023";
            url = Globals.LedgerUrl + "CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            ledgerGeneralEntryReport(cardCode, startDate, endDate);

            //  LedgerReport(cardCode, "", "");//"9580"
        }


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(LedgerReports.this)) {
                    leadValueList.clear();
                    pageno = 1;
                    recallapi = true;
                    // callApi(value);
                    if (where.equalsIgnoreCase("customer")) {
                        ledgerCustomerOnePageReport();
                        heading_opening_balance.setText("Received");
                        heading_closing_balance.setText("Total Sales");
                        url = Globals.customerPdfUrl + "Type=Gross&Filter=&FromDate=" + Globals.firstDateOfFinancialYear() + "&ToDate=" + Globals.lastDateOfFinancialYear() + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Code=";

                    } else {
                        /***shubh****/
                        //  url = Globals.LedgerUrl + "CardCode=9580&FromDate=12-4-2023&ToDate=23-4-2023";
                        url = Globals.LedgerUrl + "CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

                        ledgerGeneralEntryReport(cardCode, startDate, endDate);

                        //  LedgerReport(cardCode, "", "");//"9580"
                    }

                    swipeRefreshLayout.setRefreshing(false);
                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Globals.checkInternet(mContext)) {
            AllItemList.clear();
            //loader.setVisibility(View.GONE);
            leadValueList.clear();
            pageno = 1;
            recallapi = true;
            // callApi(value);
            if (where.equalsIgnoreCase("customer")) {
                ledgerCustomerOnePageReport();
                heading_opening_balance.setText("Received");
                heading_closing_balance.setText("Total Sales");
                url = Globals.customerPdfUrl + "Type=Gross&Filter=&FromDate=" + Globals.firstDateOfFinancialYear() + "&ToDate=" + Globals.lastDateOfFinancialYear() + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Code=";

                recyclerView.addOnScrollListener(scrollListener);
            } else {
                /***shubh****/
                //  url = Globals.LedgerUrl + "CardCode=9580&FromDate=12-4-2023&ToDate=23-4-2023";
                url = Globals.LedgerUrl + "CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
                //  ledgerGeneralEntryReport(cardCode, startDate, endDate);

                //  LedgerReport(cardCode, "", "");//"9580"
            }


        } else {
            Toasty.error(mContext, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


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
                pageNo++;
                //  itemOnPageBasis(pageNo);
                //  customerOnPageBasisLedger(cardCode, startDateFrag, endDateFrag);

                if (where.equalsIgnoreCase("customer")) {
                    loader.setVisibility(View.VISIBLE);
                    ledgerCustomerAllPageReport();
                    isScrollingpage = false;
                }

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

    private void ledgerGeneralEntryReport(String customerCode, String fromDate, String toDate) {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);

        loader.setVisibility(View.GONE);
        Call<ResponseJournalEntryBpWise> call = NewApiClient.getInstance().getApiService(this).bp_general_entries(hde);
        call.enqueue(new Callback<ResponseJournalEntryBpWise>() {
            @Override
            public void onResponse(Call<ResponseJournalEntryBpWise> call, Response<ResponseJournalEntryBpWise> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        alertDialog.dismiss();
                        loader.setVisibility(View.GONE);

                        if (response.body().getData().size() > 0) {
                            String closing = Globals.foo(Double.valueOf(response.body().getData().get(0).closingBalance));
                            String opening = Globals.foo(Double.valueOf(response.body().getData().get(0).openingBalance));

                            if (closing == null || closing.isEmpty())
                                closing = "0";
                            if (opening == null || opening.isEmpty())
                                opening = "0";

                            closing_balance.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(closing));
                            opening_balance.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(opening));
                            getSupportActionBar().setTitle("Ledger:" + response.body().getData().get(0).getCardName());
                            // Collections.reverse(response.body().data.get(0).getJournalEntryLines());
                        }
//                        LedgerGeneralEntriesAdapter adapter = new LedgerGeneralEntriesAdapter(LedgerReports.this, response.body().data.get(0).getJournalEntryLines());//todo comment by me--

                        LedgerGeneralEntriesAdapter adapter = new LedgerGeneralEntriesAdapter(LedgerReports.this, response.body().data.get(0).getJournalEntryLines(),alertDialog);

                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new LedgerGeneralEntriesAdapter.OnItemClickListener() {
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

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseJournalEntryBpWise> call, Throwable t) {
                loader.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });
    }

    LinearLayoutManager layoutManager;
    int pageNo = 1;
    ArrayList<DataCustomerLedger> AllItemList = new ArrayList<>();


    private void ledgerCustomerOnePageReport() {

        HashMap<String, String> hde = new HashMap<>();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("Type", "Gross");
        jsonObject.addProperty("FromDate", startDate);
        jsonObject.addProperty("ToDate", endDate);
        jsonObject.addProperty("Filter", filter);
        jsonObject.addProperty("SearchText", searchTextValue);
        jsonObject.addProperty("PageNo", String.valueOf(pageNo));
        jsonObject.addProperty("Code", code);
        jsonObject.addProperty("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));


        Call<ResponseCustomerLedger> call = NewApiClient.getInstance().getApiService(this).ledger_dashboard2(jsonObject);
        call.enqueue(new Callback<ResponseCustomerLedger>() {
            @Override
            public void onResponse(Call<ResponseCustomerLedger> call, Response<ResponseCustomerLedger> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        Double received = Double.valueOf(Globals.foo(Double.valueOf(response.body().totalReceivePayment)));
                        Double salesDouble = Double.valueOf(response.body().totalSales);


                        opening_balance.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(received)));
                        closing_balance.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(Globals.foo(response.body().totalSales)));
                        loader.setVisibility(View.GONE);
                        alertDialog.dismiss();
                        AllItemList.clear();
                        AllItemList.addAll(response.body().getData());

                        customeAdapter = new LedgerCustomerEntriesAdapter(LedgerReports.this, AllItemList);

                        customeAdapter.AllData(AllItemList);

                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(customeAdapter);

                    } else {
                        loader.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseCustomerLedger> call, Throwable t) {
                loader.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });
    }


    private void ledgerCustomerAllPageReport() {
        HashMap<String, String> hde = new HashMap<>();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Type", "Gross");
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("FromDate", startDate);
        jsonObject.addProperty("ToDate", endDate);
        jsonObject.addProperty("SearchText", searchTextValue);
        jsonObject.addProperty("Filter", filter);
        jsonObject.addProperty("PageNo", String.valueOf(pageNo));
        jsonObject.addProperty("Code", code);
        jsonObject.addProperty("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));


        Call<ResponseCustomerLedger> call = NewApiClient.getInstance().getApiService(this).ledger_dashboard2(jsonObject);
        call.enqueue(new Callback<ResponseCustomerLedger>() {
            @Override
            public void onResponse(Call<ResponseCustomerLedger> call, Response<ResponseCustomerLedger> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        AllItemList.addAll(response.body().getData());
//                        opening_balance.setText("Rs. " + response.body().totalReceivePayment);
//                        closing_balance.setText("Rs. " + response.body().totalSales);
                        alertDialog.dismiss();
                        loader.setVisibility(View.GONE);
                        customeAdapter.AllData(AllItemList);
                        customeAdapter.notifyDataSetChanged();
                        if (response.body().getData().size() == 0) {
                            pageNo++;
                            no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            no_datafound.setVisibility(View.INVISIBLE);
                        }
                        loader.setVisibility(View.GONE);
//                        customeAdapter = new LedgerCustomerEntriesAdapter(LedgerReports.this, response.body().getData());
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(customeAdapter);

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseCustomerLedger> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                break;
            case R.id.calender:
                // Globals.selectDat(this);
//                dateRangeSelector();
                showDateBottomSheetDialog(LedgerReports.this);
                break;
            case R.id.share:
                /***shubh****/
                //  showBottomSheetDialog();
                shareLedgerData();
                break;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ledger_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem calender = menu.findItem(R.id.calender);
        calender.setVisible(true);
        if (where.equalsIgnoreCase("customer")) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        SearchView searchView = new SearchView(((LedgerReports) mContext).getSupportActionBar().getThemedContext());

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setQueryHint("Search ");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchTextValue = query;
                if (where.equalsIgnoreCase("customer")) {
                    if (!searchTextValue.isEmpty()) {
                        pageNo = 1;
                        ledgerCustomerOnePageReport();
                    }
                } else {

                }

               /* pageNo =1;
                searchTextValue = query;
                itemOnOnePage(searchTextValue);*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("Test==>", newText);

                if (customeAdapter != null)
                    customeAdapter.filter(newText);
                else if (adapter != null)
                    adapter.filter(newText);


                return true;
            }
        });


        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    public void nodatafoundimage() {
        if (adapter.getItemCount() == 0) {
            no_datafound.setVisibility(View.VISIBLE);
        } else {
            no_datafound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }


    private String startDateReverseFormat = "";
    private String endDateReverseFormat = "";


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        binding.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            // Toast.makeText(context, "today", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });


        binding.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            alertDialog.show();
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            alertDialog.show();
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            alertDialog.show();
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
//            from_to_date.setText(startDate + " - " + endDate);

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view ->
        {
            alertDialog.show();

            startDate = "";
            endDate = "";

            ledgerGeneralEntryReport(cardCode, startDate, endDate);
//            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");

   /*     materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);

                if (fromWhere.trim().equalsIgnoreCase("SaleLedger")) {
                    startDateReverseFormat = Globals.convertDateFormat(startDate);
                    endDateReverseFormat = Globals.convertDateFormat(endDate);
                    from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
                    callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
                    url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

                }


            }
        });*/


    }


    WebView printWeb;


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

        final ProgressDialog progressDialog = new ProgressDialog(LedgerReports.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(LedgerReports.this, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                whatsappShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = "Share";

        if (where.equalsIgnoreCase("customer")) {
            url = Globals.customerPdfUrl + "Type=Gross&Filter=&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Code=";

        } else {
            url = Globals.LedgerUrl + "CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
        }
        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        if (where.equalsIgnoreCase("customer")) {
            url = Globals.customerPdfUrl + "Type=Gross&Filter=&FromDate=" + Globals.firstDateOfFinancialYear() + "&ToDate=" + Globals.lastDateOfFinancialYear() + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Code=";

        } else {
            url = Globals.LedgerUrl + "CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
        }


        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
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
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(LedgerReports.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(LedgerReports.this, webView, f, fileName, new PdfView.Callback() {

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
                getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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

        final ProgressDialog progressDialog = new ProgressDialog(LedgerReports.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(LedgerReports.this, webView, f, fileName, new PdfView.Callback() {

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
                getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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
                getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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
    WebView dialogWeb;
    String url;


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


}