package com.cinntra.ledure.fragments;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.activities.Sold_ItemBasesActivity;
import com.cinntra.ledure.adapters.SoldItem_Adapter;
import com.cinntra.ledure.adapters.StockGroupBPWiseAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.FragmentItemsTabFromPartyBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.interfaces.FragmentClickListener;
import com.cinntra.ledure.model.CustomerItemResponse;
import com.cinntra.ledure.model.SoldItemResponse;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsTabFromPartyFragment extends Fragment implements LedgerCutomerDetails.MyFragmentListener, LedgerCutomerDetails.MyFragmentCustomerListener {

    FragmentItemsTabFromPartyBinding binding;

    RelativeLayout relativeCalView;
    String cardCode;
    String startDateFrag;
    String endDateFrag;
    RelativeLayout toDay;

    FragmentClickListener fragmentClickListener;
    int pageNo = 1;
    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    //   TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public static String startDateListener = Globals.firstDateOfFinancialYear();
    public static String endDateListener = Globals.lastDateOfFinancialYear();

    WebView dialogWeb;

    public ItemsTabFromPartyFragment(String cardCode, String startDate, String endDate, RelativeLayout toDay) {
        // Required empty public constructor
        this.cardCode = cardCode;
        this.startDateFrag = startDate;
        this.endDateFrag = endDate;
        this.toDay = toDay;

    }

    String searchTextValue = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentItemsTabFromPartyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relativeCalView = (RelativeLayout) getActivity().findViewById(R.id.relativeCalView);
        fragmentClickListener = (FragmentClickListener) getActivity();
        startDateListener = Prefs.getString(Globals.FROM_DATE, "");
        endDateListener = Prefs.getString(Globals.TO_DATE, "");

        binding.dateText.setVisibility(View.VISIBLE);
        binding.recyclerview.addOnScrollListener(scrollListener);

        customerOnePageLedger(cardCode, startDateListener, endDateListener);

        binding.showPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLedgerData();
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(getActivity())) {
                    pageNo = 1;
                    AllItemList.clear();
                    customerOnePageLedger(cardCode, startDateListener, endDateListener);

                } else
                    binding.swipeRefreshLayout.setRefreshing(false);

            }
        });

        binding.dateText.setVisibility(View.GONE);
        binding.dateText.setOnClickListener(view1 -> {
            showDateBottomSheetDialog(requireContext());
        });


        //todo search items--

        binding.searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchView.setIconifiedByDefault(true);
                binding.searchView.setFocusable(true);
                binding.searchView.setIconified(false);
                binding.searchView.requestFocusFromTouch();
            }
        });


        SearchViewUtils.setupSearchView(binding.searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.e("QUERY>>>>>>>", "Chanchal: " + query);
                binding.searchView.clearFocus();
                searchTextValue = query;

                binding.loaderLayout.loader.setVisibility(View.VISIBLE);
                binding.searchView.setQueryHint("Search Items");

                pageNo = 1;

                customerOnePageLedger(cardCode, startDateFrag, endDateFrag);
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                binding.searchView.clearFocus();
                searchTextValue = newText;
                binding.loaderLayout.loader.setVisibility(View.VISIBLE);
                binding.searchView.setQueryHint("Search Items");

                pageNo = 1;

                customerOnePageLedger(cardCode, startDateFrag, endDateFrag);
            }
        });


    }


    String url = "";

    private void shareLedgerData() {
        String title = "Share ";
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            url = Globals.itemInPartySectionPdf + "SalesPersonCode=" + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=" + "&ToDate=" + endDateFrag + "&FromDate=" + startDateFrag + "&CardCode=" + cardCode + "&Type=" + "Purchase";

        } else {
            url = Globals.itemInPartySectionPdf + "SalesPersonCode=" + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=" + "&ToDate=" + endDateFrag + "&FromDate=" + startDateFrag + "&CardCode=" + cardCode + "&Type=" + "Sales";

        }
        //   ListItem1.html?SalesPersonCode=-1&PageNo=1&MaxSize=40&SearchText=&ToDate=2024-03-31&FromDate=2023-04-01&CardCode=10093&Type=Sales


//        Log.e("SALESREPORTSHARINGDRO", "onCreateView: " + url);

        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");
    }

    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    LinearLayoutManager layoutManager;

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
                customerOnPageBasisLedger(cardCode, startDateFrag, endDateFrag);
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


    ArrayList<SoldItemResponse> AllItemList = new ArrayList<>();
    SoldItem_Adapter adapter;

    private void customerOnePageLedger(String customerCode, String fromDate, String toDate) {
        binding.loaderLayout.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchTextValue);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<CustomerItemResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService(getActivity()).itemListFromPurchaseParty(hde);
        } else {
            call = NewApiClient.getInstance().getApiService(getActivity()).itemListFromParty(hde);
        }


        call.enqueue(new Callback<CustomerItemResponse>() {
            @Override
            public void onResponse(Call<CustomerItemResponse> call, Response<CustomerItemResponse> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        AllItemList.clear();
                        AllItemList.addAll(response.body().getCustomerLedgerRes());
                        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        adapter = new SoldItem_Adapter(getContext(), AllItemList, LedgerCutomerDetails.nameCustomer, cardCode, startDateFrag, endDateFrag);
                        binding.recyclerview.setLayoutManager(layoutManager);
                        binding.recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (response.body().getCustomerLedgerRes().size() == 0) {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                        } else {
                            binding.noDatafound.setVisibility(View.INVISIBLE);
                        }
                        binding.loaderLayout.loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerItemResponse> call, Throwable t) {
                binding.loaderLayout.loader.setVisibility(View.GONE);
            }
        });
    }


    private void customerOnPageBasisLedger(String customerCode, String fromDate, String toDate) {
        binding.loaderLayout.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchTextValue);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));


        Call<CustomerItemResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService(getActivity()).itemListFromPurchaseParty(hde);
        } else {
            call = NewApiClient.getInstance().getApiService(getActivity()).itemListFromParty(hde);
        }
        call.enqueue(new Callback<CustomerItemResponse>() {
            @Override
            public void onResponse(Call<CustomerItemResponse> call, Response<CustomerItemResponse> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        //  AllItemList.clear();
                        AllItemList.addAll(response.body().getCustomerLedgerRes());

                        adapter.notifyDataSetChanged();
                        if (response.body().getCustomerLedgerRes().size() == 0) {
                            pageNo++;
                        } else {
                        }
                        binding.loaderLayout.loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerItemResponse> call, Throwable t) {
                binding.loaderLayout.loader.setVisibility(View.GONE);
            }
        });
    }


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
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
            endDateListener = Globals.Date_yyyy_mm_dd(endDatelng);
            this.binding.dateText.setText("Today");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
            endDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
            this.binding.dateText.setText("Yesterday");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);

            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.thisWeekfirstDayOfMonth();
            endDateListener = Globals.thisWeekLastDayOfMonth();
            // from_to_date.setText(startDate + " - " + endDate);
//            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);

            this.binding.dateText.setText("" + startDateListener + "-" + endDateListener);
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.firstDateOfMonth();
            endDateListener = Globals.lastDateOfMonth();
            this.binding.dateText.setText("" + startDateListener + "-" + endDateListener);
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDateListener = Globals.lastMonthFirstDate();
            endDateListener = Globals.lastMonthlastDate();
            this.binding.dateText.setText("Last Month");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.lastQuarterFirstDate();
            endDateListener = Globals.lastQuarterlastDate();

            this.binding.dateText.setText("This Quarter");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);

            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.firstDateOfFinancialYear();
            endDateListener = Globals.lastDateOfFinancialYear();
//            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);

            this.binding.dateText.setText("This Year");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDateListener = Globals.lastYearFirstDate();
            endDateListener = Globals.lastYearLastDate();
//            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);
            this.binding.dateText.setText("last Year");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {

            this.binding.dateText.setText("All");
            customerOnePageLedger(cardCode, "", "");
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }
//        materialDatePicker.surface

        materialDatePicker.show(this.getActivity().getSupportFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
                endDateListener = Globals.Date_yyyy_mm_dd(endDatelng);
                // from_to_date.setText(startDate + " - " + endDate);
                binding.dateText.setText("" + startDateListener + "-" + endDateListener);
                customerOnePageLedger(cardCode, startDateListener, endDateListener);
                //   customerLedger(cardCode, startDate, endDate);
            }
        });


    }

    @Override
    public void onDataPassed(String startDate, String endDate) {
//        Log.e("DATE>>>>>>", "onDataPassedsold: " + startDate + endDate);

        startDateListener = startDate;
        endDateListener = endDate;
        customerOnePageLedger(cardCode, startDateListener, endDateListener);
    }

    @Override
    public void onDataPassedCustomer(String startDate, String endDate) {
//        Log.e("DATE>>>>>>", "onDataPassedCustomersold: " + startDate + endDate);
    }


}