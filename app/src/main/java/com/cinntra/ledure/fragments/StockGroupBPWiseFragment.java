package com.cinntra.ledure.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.adapters.StockGroupBPWiseAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentClickListener;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockGroupBPWiseFragment extends Fragment implements LedgerCutomerDetails.MyFragmentListener, LedgerCutomerDetails.MyFragmentCustomerListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.dateText)
    EditText dateSelected;


    RelativeLayout relativeCalView;

//    @BindView(R.id.relativeCalView)
//    RelativeLayout relativeCalView;

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    //   TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public static String startDateListener = Globals.firstDateOfFinancialYear();
    public static String endDateListener = Globals.lastDateOfFinancialYear();


    private SearchView searchView;
    // private Invoices_Adapter adapter;
    int currentpage = 0;
    boolean recallApi = true;

    String cardCode;
    String startDateFrag;
    String endDateFrag;
    RelativeLayout toDay;

    FragmentClickListener fragmentClickListener;
    int pageNo = 1;


    public StockGroupBPWiseFragment(String cardCode, String startDate, String endDate, RelativeLayout toDay) {
        // Required empty public constructor
        this.cardCode = cardCode;
        this.startDateFrag = startDate;
        this.endDateFrag = endDate;
        this.toDay = toDay;

    }

    public static StockGroupBPWiseFragment newInstance(String cardCode, String startDate, String endDate, RelativeLayout toDay) {
        StockGroupBPWiseFragment fragment = new StockGroupBPWiseFragment(cardCode, startDate, endDate, toDay);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        //  Toast.makeText(fragment.getActivity(), "In Item Fragment", Toast.LENGTH_SHORT).show();
        Log.e("TestB=>", "In Item Fragment" + startDate);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TestB=>", "In Item FragmentResume");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TestB=>", "In soldItem_Create");
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stock_group_b_p_wise, container, false);
        ButterKnife.bind(this, v);
        relativeCalView = (RelativeLayout) getActivity().findViewById(R.id.relativeCalView);
        fragmentClickListener = (FragmentClickListener) getActivity();
        startDateListener = Prefs.getString(Globals.FROM_DATE, "");
        endDateListener = Prefs.getString(Globals.TO_DATE, "");

        Log.e("SOLDDATE===>", "onCreateView: start->" + startDateListener + "enddate" + endDateListener);
        //    relativeCalView = (RelativeLayout) getActivity().findViewById(R.id.relativeCalView);
        customerOnePageLedger(cardCode, startDateListener, endDateListener);
        dateSelected.setVisibility(View.VISIBLE);
        recyclerview.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(getActivity())) {
                    pageNo = 1;
                    AllItemList.clear();

                    customerOnePageLedger(cardCode, startDateListener, endDateListener);
                    //   callApi(binding.fragmentCustomer.loaderCustomer.loader);

                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });


        return v;
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
                customerOnPageBasisLedger(cardCode, startDateListener, endDateListener);
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


    LinearLayoutManager layoutManager;
    ArrayList<DataItemFilterDashBoard> AllItemList = new ArrayList<>();
    //    ItemOnStockGroupAdapter adapter;
    StockGroupBPWiseAdapter adapter;

    private void customerOnePageLedger(String customerCode, String fromDate, String toDate) {
        pageNo = 1;
        loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hde = new HashMap<>();

        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("CardCode", customerCode);
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", "");
        hde.put("FromDate", fromDate);//startDate
        hde.put("ToDate", toDate);//endDate

        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put(Globals.payLoadOrderByName, "");
        hde.put(Globals.payLoadOrderByAMt, "");

        Call<ResponseItemFilterDashboard> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockBpWisePurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockBpWise(hde);
        }


        call.enqueue(new Callback<ResponseItemFilterDashboard>() {
            @Override
            public void onResponse(Call<ResponseItemFilterDashboard> call, Response<ResponseItemFilterDashboard> response) {
                if (response != null) {
                    if (response.body().getStatus() == 200) {
                        AllItemList.clear();
                        AllItemList.addAll(response.body().getData());
                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        adapter = new StockGroupBPWiseAdapter(getContext(), AllItemList, LedgerCutomerDetails.nameCustomer, cardCode, startDateFrag, endDateFrag);
                        recyclerview.setLayoutManager(layoutManager);
                        recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                        if (response.body().getData().size() == 0) {
                            no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            no_datafound.setVisibility(View.INVISIBLE);
                        }
                        loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseItemFilterDashboard> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }


    private void customerOnPageBasisLedger(String customerCode, String fromDate, String toDate) {
        loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hde = new HashMap<>();

        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("CardCode", customerCode);
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", "");
        hde.put("FromDate", startDateListener);
        hde.put("ToDate", endDateListener);

        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put(Globals.payLoadOrderByName, "");
        hde.put(Globals.payLoadOrderByAMt, "");
        Call<ResponseItemFilterDashboard> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockBpWisePurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockBpWise(hde);
        }
        call.enqueue(new Callback<ResponseItemFilterDashboard>() {
            @Override
            public void onResponse(Call<ResponseItemFilterDashboard> call, Response<ResponseItemFilterDashboard> response) {
                if (response != null) {
                    if (response.body().getStatus() == 200) {
                        //  AllItemList.clear();
                        AllItemList.addAll(response.body().getData());
//                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//                        adapter = new SoldItem_Adapter(getContext(), AllItemList, LedgerCutomerDetails.nameCustomer);
//                        recyclerview.setLayoutManager(layoutManager);
//                        recyclerview.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        if (response.body().getData().size() == 0) {
                            pageNo++;
                            //  no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            // no_datafound.setVisibility(View.INVISIBLE);
                        }
                        loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseItemFilterDashboard> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateSelected.setVisibility(View.GONE);
        dateSelected.setOnClickListener(view1 -> {
            showDateBottomSheetDialog(requireContext());
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
            //   from_to_date.setText(startDate + " - " + endDate);
            //  Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            // from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());

            //   listener.onDataPassed(startDate,endDate);
            dateSelected.setText("Today");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
            endDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
            dateSelected.setText("Yesterday");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            //  from_to_date.setText(startDate + " - " + endDate);
            //   Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.thisWeekfirstDayOfMonth();
            endDateListener = Globals.thisWeekLastDayOfMonth();
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            dateSelected.setText("" + startDateListener + "-" + endDateListener);
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.firstDateOfMonth();
            endDateListener = Globals.lastDateOfMonth();
            //  from_to_date.setText(startDate + " - " + endDate);
            // Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            //  customerLedger(cardCode,startDate,endDate);
//            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());

            dateSelected.setText("" + startDateListener + "-" + endDateListener);
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDateListener = Globals.lastMonthFirstDate();
            endDateListener = Globals.lastMonthlastDate();
            dateSelected.setText("Last Month");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);

            //   from_to_date.setText(startDate + " - " + endDate);
            //   Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode, startDate, endDate);
//            callledgerapi(reportType, startDate, endDate);
            //  from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.lastQuarterFirstDate();
            endDateListener = Globals.lastQuarterlastDate();

            dateSelected.setText("This Quarter");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            //     from_to_date.setText(startDate + " - " + endDate);
            //    Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);

            //  customerLedger(cardCode, startDate, endDate);
            //  from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDateListener = Globals.firstDateOfFinancialYear();
            endDateListener = Globals.lastDateOfFinancialYear();
            //    from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);
//            callledgerapi(reportType, startDate, endDate);
            //   customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            dateSelected.setText("This Year");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDateListener = Globals.lastYearFirstDate();
            endDateListener = Globals.lastYearLastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDateListener + "  endDate=>" + endDateListener);
//            callledgerapi(reportType, startDate, endDate);
            // customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            dateSelected.setText("last Year");
            customerOnePageLedger(cardCode, startDateListener, endDateListener);
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {

            dateSelected.setText("All");
            customerOnePageLedger(cardCode, "", "");
            // from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
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
                startDateListener = Globals.Date_yyyy_mm_dd(startDatelng);
                endDateListener = Globals.Date_yyyy_mm_dd(endDatelng);
                // from_to_date.setText(startDate + " - " + endDate);
                dateSelected.setText("" + startDateListener + "-" + endDateListener);
                customerOnePageLedger(cardCode, startDateListener, endDateListener);
                //   customerLedger(cardCode, startDate, endDate);
            }
        });


    }

    @Override
    public void onDataPassed(String startDate, String endDate) {
        Log.e("DATE>>>>>>", "onDataPassedsold: " + startDate + endDate);
//        Prefs.putString(Globals.FROM_DATE,startDate);
//        Prefs.putString(Globals.TO_DATE,endDate);
        //   Toast.makeText(requireContext(), "sold" + startDate + endDate, Toast.LENGTH_SHORT).show();
        startDateListener = startDate;
        endDateListener = endDate;
        customerOnePageLedger(cardCode, startDateListener, endDateListener);
    }

    @Override
    public void onDataPassedCustomer(String startDate, String endDate) {
        Log.e("DATE>>>>>>", "onDataPassedCustomersold: " + startDate + endDate);
        //  Toast.makeText(requireContext(), "" + startDate + endDate, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onDataPassed(String startDate, String endDate) {
//        startDateFrag=startDate;
//        endDateFrag=endDate;
//    }


}