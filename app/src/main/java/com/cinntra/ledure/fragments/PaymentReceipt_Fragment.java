package com.cinntra.ledure.fragments;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;
import static com.cinntra.ledure.globals.Globals.getRoundOffUpTOTwo;
import static com.cinntra.ledure.globals.Globals.numberToK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinntra.ledure.R;

import com.cinntra.ledure.adapters.BillsLedgerAdapter;
import com.cinntra.ledure.adapters.ItemStockAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;

import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.model.DashboardCounterResponse;
import com.cinntra.ledure.model.ReceiptBusinessPartnerData;
import com.cinntra.ledure.model.ReceiptCustomerBusinessRes;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;

import com.cinntra.roomdb.ReceiptDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentReceipt_Fragment extends Fragment implements Toolbar.OnMenuItemClickListener {


    ArrayList<Customers_Report> customerList = new ArrayList<>();

    @BindView(R.id.customers_recyclerview)
    RecyclerView customerRecyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.swipeRefreshItem)
    SwipeRefreshLayout swipeRefreshItem;
    @BindView(R.id.sales_amount)
    TextView sales_amount;
    @BindView(R.id.slaes_amount)
    TextView slaes_amount;
    @BindView(R.id.pending_amount)
    TextView pending_amount;
    @BindView(R.id.pending_amount_value)
    TextView pending_amount_value;


    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;

    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;

//    @BindView(R.id.nestedView)
//    NestedScrollView nestedView;

    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
    String reportType = "Gross";
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();


    Toolbar toolbar;
    /***shubh****/
    WebView dialogWeb;
    String url;

    String searchTextValue = "";
    private String startDateReverseFormat = "";
    private String endDateReverseFormat = "";

    Context context;

    private ReceiptDatabase db;

    public PaymentReceipt_Fragment(TextView salesvalue, TextView from_to_date, Spinner type_dropdown) {
        // Required empty public constructor
        this.salesvalue = salesvalue;
        this.from_to_date = from_to_date;
        this.type_dropdown = type_dropdown;
    }


    Menu menuOut;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menuOut = menu;
        inflater.inflate(R.menu.transaction_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem share = menu.findItem(R.id.share_received);
        //  share.setVisible()


        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });


        toolbar.addView(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.calendar:

                showDateBottomSheetDialog(requireContext());
                return true;

            case R.id.share_received:

                shareLedgerData();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = getString(R.string.share_customer_list);

        //todo pdf
        if (Prefs.getBoolean(Globals.ISPURCHASE,false)) {
            url = Globals.receiptAllPdfUlPurchase + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE+ "&OrderByName=" + orderBYName + "&OrderByAmt=" + orderBYAmt + "&SalesPersonCode=" + Prefs.getString(Globals.SalesEmployeeCode, "");

        } else {
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE+ "&OrderByName=" + orderBYName + "&OrderByAmt=" + orderBYAmt + "&SalesPersonCode=" + Prefs.getString(Globals.SalesEmployeeCode, "");
        }

        Log.e("PDF====>", "shareLedgerData: "+url);

//        url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");
    }

    String orderBYName = "a-z";
    String orderBYAmt = "";


    private void showDateBottomSheetDialog(Context context) {
        db.myDataDao().deleteAll();
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
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, startDate, endDate);
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatelng = Globals.lastyearCal().getTimeInMillis();
                endDatelng = Globals.thisyearCal().getTimeInMillis();
                startDate = Globals.lastYearFirstDate();
                endDate = Globals.getCurrentDateInLastFinancialYear();
                from_to_date.setText(startDate + " - " + endDate);
                Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
                loader.setVisibility(View.VISIBLE);

                callDashboardCounter();
                callledgerOneapi(reportType, startDate, endDate);
                url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
                Log.e("receiptAllPdfUl", "onCreateView: " + url);
                from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });



        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            loader.setVisibility(View.VISIBLE);

            callDashboardCounter();
            callledgerOneapi(reportType, "", "");
            url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("receiptAllPdfUl", "onCreateView: " + url);
            from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }
    // TODO: Rename and change types and number of parameters
   /* public static Bills_Fragment newInstance(String param1, String param2) {
        Bills_Fragment fragment = new Bills_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            customerList = (ArrayList<Customers_Report>) b.getSerializable(Globals.LedgerCompanyData);
        }


    }


    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            Log.e("SROLLER====>", "onScrolled: ");
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

            Boolean cond = isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount);
            Log.e("TAG BBBB", "BOOLEAN===>: " + cond);

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                loader.setVisibility(View.VISIBLE);
                if (AllItemList.size() != 0)
                    pageNo = AllItemList.size() / Globals.QUERY_PAGE_SIZE;
                pageNo++;


                if (Globals.checkInternet(requireContext())) {
                    callledgerAllPageApi(reportType, startDate, endDate, pageNo);
                }
                isScrollingpage = false;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this, v);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.transaction_menu);
        toolbar.setOnMenuItemClickListener(this);
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        receive_pending_layout.setVisibility(View.GONE);
        type_dropdown.setVisibility(View.GONE);
        slaes_amount.setText("Received Amount");
        pending_amount.setText("Pending Amount");
        startDateReverseFormat = Globals.convertDateFormat(startDate);
        endDateReverseFormat = Globals.convertDateFormat(endDate);
        db = ReceiptDatabase.getDatabase(requireContext());
        loader.setVisibility(View.GONE);
        setRecyclerViewAdapter();
        //    Prefs.putString(Globals.ATOZ);

        if (startDate.isEmpty() && endDate.isEmpty()) {
            from_to_date.setText("All");
        } else {
            from_to_date.setText(startDateReverseFormat + " to " + endDateReverseFormat);
        }

        url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
        Log.e("receiptAllPdfUl", "onCreateView: " + url);

        from_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateBottomSheetDialog(requireContext());
            }
        });

        swipeRefreshItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Globals.checkInternet(requireContext())) {
                    pageNo = 1;
                    orderBYName = "";
                    orderBYAmt = "";
                    db.myDataDao().deleteAll();
                    callledgerOneapi(reportType, startDate, endDate);
                }

            }
        });

        return v;
    }


    private void setRecyclerViewAdapter() {
        db.myDataDao().deleteAll();
        try {
            if (db.myDataDao().getAll().size() > 0) {
                AllItemList.addAll(db.myDataDao().getAll());
            } else {

                if (Globals.checkInternet(requireContext())) {
                    callledgerOneapi(reportType, startDate, endDate);
                }

            }

            adapter = new BillsLedgerAdapter(requireContext(), AllItemList, "Receipt");
            adapter.AllData(AllItemList);
            layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            customerRecyclerView.setLayoutManager(layoutManager);
            customerRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("TAG", "run: " + e.getMessage());
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        BillAdapter adapter = new BillAdapter(requireContext(), customerList);
//        customerRecyclerView.setAdapter(adapter);
        Log.e("onViewCreated===>", "onViewCreated: ");
        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });


        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                loader.setVisibility(View.VISIBLE);
                //  callledgerOneapi(type_dropdown.getSelectedItem().toString(), "", "");
                Log.e("SCROLLTYEP===>", "onItemSelected: ");
                // customerRecyclerView.addOnScrollListener(scrollListener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setQueryHint("search customer");

        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                searchTextValue = query;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    callledgerOneapi(reportType, startDate, endDate);
                }
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                searchView.clearFocus();

                searchTextValue = newText;

                pageNo = 1;
                callledgerOneapi(reportType, startDate, endDate);

            }
        });


        // now create the instance of the material date
        // picker
        callDashboardCounter();
        //  callledgerOneapi(reportType, startDate, endDate);
        customerRecyclerView.addOnScrollListener(scrollListener);


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

        materialDatePicker.show(getActivity().getSupportFragmentManager(), "Tag_Picker");
       /* materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Log.e("FromDate=>",String.valueOf(selection));

            }
        });
*/
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                startDateReverseFormat = Globals.convertDateFormat(startDate);
                endDateReverseFormat = Globals.convertDateFormat(endDate);
                from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
                loader.setVisibility(View.VISIBLE);

                callDashboardCounter();
                callledgerOneapi(reportType, startDate, endDate);
            }
        });


    }

    BillsLedgerAdapter adapter = null;

    ArrayList<ReceiptBusinessPartnerData> AllItemList = new ArrayList<>();

    int pageNo = 1;
    LinearLayoutManager layoutManager;


    private void callDashboardCounter() {
        salesvalue.setText("Loading...");
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        HashMap obj = new HashMap<String, String>();


        Call<DashboardCounterResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            obj.put("Filter", "");
            obj.put("Code", "");
            obj.put("Type", "Gross");
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            obj.put("SearchText", "");
            obj.put("OrderByName", "");
            obj.put("OrderByAmt", "");
            obj.put("PageNo", "");
            obj.put("MaxSize", "");
            obj.put("DueDaysGroup", "");
            call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForPurchaseLedger(obj);
        } else {
            obj.put("Filter", "");
            obj.put("Code", "");
            obj.put("Type", reportType);
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForLedger(obj);
        }
//        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE
                    );
                    //    alertDialog.dismiss();
                    //setCounter(response.body().getData().get(0));
                    salesvalue.setText(context.getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalReceivePayment()));
                    sales_amount.setText(context.getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalReceivePayment()));


                    //  lead_spiner.setAdapter(leadAdapter);
                    //  leadAdapter.notifyDataSetChanged();
                } else {
                    //    alertDialog.dismiss();

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                //   alertDialog.dismiss();
                loader.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }


    private void callledgerOneapi(String reportType, String startDate, String endDate) {
        db.myDataDao().deleteAll();

        loader.setVisibility(View.VISIBLE);
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "");
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));

                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ReceiptCustomerBusinessRes> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard_purchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard(hde);
                }

//                Call<ReceiptCustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard(hde);
                try {
                    Response<ReceiptCustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {


                                AllItemList.clear();
                                if (response.body().getData().size() > 0) {
                                    no_datafound.setVisibility(View.GONE);
                                    AllItemList.addAll(response.body().getData());
                                    if (searchTextValue.isEmpty())
                                        db.myDataDao().insertAll(AllItemList);
                                } else {
                                    no_datafound.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                                swipeRefreshItem.setRefreshing(false);
                                loader.setVisibility(View.GONE);

                                //Update UI element here
                            }
                        });
                        // Handle successful response

                    } else {
                        swipeRefreshItem.setRefreshing(false);
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    swipeRefreshItem.setRefreshing(false);
                    // Handle exception
                }
            }
        }).start();
    }

    private void callledgerAllPageApi(String reportType, String startDate, String endDate, Integer pageno) {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "");
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageno));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ReceiptCustomerBusinessRes> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard_purchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard(hde);
                }

//                Call<ReceiptCustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).receipt_dashboard(hde);


                try {
                    Response<ReceiptCustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {


                                    if (response.body().getData().size() > 0) {
                                        AllItemList.addAll(response.body().getData());
                                        adapter.AllData(AllItemList);
                                        loader.setVisibility(View.GONE);
                                        if (searchTextValue.isEmpty())
                                            db.myDataDao().insertAll(response.body().getData());
                                        // db.myDataDao().insertAll(AllitemsList);
                                        Log.e("LOCALSIZE====>", "Size: " + db.myDataDao().getAll().size());
                                        // setData(response.body().getData().get(0));
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    loader.setVisibility(View.GONE);
                    // Handle exception
                }
            }
        }).start();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.calendar:
                // Globals.selectDat(this);
                menuItem.setChecked(!menuItem.isChecked());
                showDateBottomSheetDialog(requireContext());

                break;
            case R.id.filter:
                // Globals.selectDat(this);
                //dateRangeSelector();
                break;

            case R.id.share_received:
                // Globals.selectDat(this);
                //dateRangeSelector();
                //  Toast.makeText(requireContext(), "click search", Toast.LENGTH_SHORT).show();
                menuItem.setChecked(!menuItem.isChecked());
                //showBottomSheetDialog();
                shareLedgerData();
                break;

            case R.id.filterAtoZ:
                // Globals.selectDat(this);
                /***shubh****/
                //  Toast.makeText(requireContext(), "click search", Toast.LENGTH_SHORT).show();

//                menuItem.setChecked(!menuItem.isChecked());
//                adapter.sortingA2Z("AtoZ");
                orderBYName = Globals.ATOZ;
                orderBYAmt = "";

                callledgerOneapi(reportType, startDate, endDate);


                menuItem.setChecked(!menuItem.isChecked());

                break;

            case R.id.filterZtoA:
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";

                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.search:
                //  adapter.sortingA2Z("ZtoA");
                if (searchLay.getVisibility() == View.GONE) {
                    searchLay.setVisibility(View.VISIBLE);


                } else {
                    searchLay.setVisibility(View.GONE);
                }

                // Toast.makeText(requireContext(), "shubh", Toast.LENGTH_SHORT).show();
                break;

            case R.id.filterAmount:
                orderBYName = "";
                orderBYAmt = Globals.DESC;
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;


            case R.id.filterAmountAsc:
                orderBYName = "";
                orderBYAmt = Globals.ASC;
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.clearAllFilter:
                menuItem.setChecked(!menuItem.isChecked());
                searchTextValue = "";
                searchView.setQuery("", false);
                searchView.setFocusable(false);
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;


        }
        return false;
    }


    private boolean isAppInstalled(String packageName) {
        try {
            getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }


}