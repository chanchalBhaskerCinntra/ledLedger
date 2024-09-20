package com.cinntra.ledure.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.adapters.Invoices_Adapter;
import com.cinntra.ledure.adapters.SoldItem_Adapter;
import com.cinntra.ledure.databinding.ActivitySoldItemBasesLayoutBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.FragmentQuotesListBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentClickListener;
import com.cinntra.ledure.model.CustomerItemResponse;
import com.cinntra.ledure.model.ResSubCatItems;
import com.cinntra.ledure.model.SoldItemResponse;
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

public class Sold_ItemBasesActivity extends AppCompatActivity implements LedgerCutomerDetails.MyFragmentListener, LedgerCutomerDetails.MyFragmentCustomerListener {
    RelativeLayout relativeCalView;
    /*    @BindView(R.id.loader)
        ProgressBar loader;*/
    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    //   TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public static String startDate = Globals.firstDateOfFinancialYear();
    public static String endDate = Globals.lastDateOfFinancialYear();


    private SearchView searchView;
    // private Invoices_Adapter adapter;
    int currentpage = 0;
    boolean recallApi = true;

    String cardCode;
    String startDateFrag;
    String endDateFrag;
    String name;
    String subGroupCode = "";
    RelativeLayout toDay;

    int pageNo = 1;

    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    ActivitySoldItemBasesLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoldItemBasesLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);

        if (getIntent() != null) {
            cardCode = getIntent().getStringExtra("cardCode");
            startDateFrag = getIntent().getStringExtra("startDateFrag");
            endDateFrag = getIntent().getStringExtra("endDateFrag");
            name = getIntent().getStringExtra("name");
            subGroupCode = getIntent().getStringExtra("subGroupCode");
        }
        binding.toolbarSoldItemList.headTitle.setText(name);
        binding.toolbarSoldItemList.newQuatos.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.relativeInfoView.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.search.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.filter.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.relativeCalView.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.backPress.setOnClickListener(view ->
        {
            finish();
        });

        relativeCalView = (RelativeLayout) findViewById(R.id.relativeCalView);

        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");

        Log.e("SOLDDATE===>", "onCreateView: start->" + startDate + "enddate" + endDate);
        //    relativeCalView = (RelativeLayout) getActivity().findViewById(R.id.relativeCalView);
        customerOnePageLedger(cardCode, startDate, endDate);
        binding.dateText.setVisibility(View.VISIBLE);
        binding.recyclerview.addOnScrollListener(scrollListener);

        binding.dateText.setVisibility(View.GONE);
        binding.dateText.setOnClickListener(view1 -> {
            showDateBottomSheetDialog(this);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TestB=>", "In Item FragmentResume");
    }


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


    LinearLayoutManager layoutManager;
    ArrayList<SoldItemResponse> AllItemList = new ArrayList<>();
    SoldItem_Adapter adapter;

    private void customerOnePageLedger(String customerCode, String fromDate, String toDate) {
        binding.loader.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", "");
        hde.put("GroupCode", "");
        hde.put("SubGroupCode", subGroupCode);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));


        Call<CustomerItemResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService(this).bpWiseSoldFilterItemsPurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService(this).bpWiseSoldFilterItems(hde);
        }



        call.enqueue(new Callback<CustomerItemResponse>() {
            @Override
            public void onResponse(Call<CustomerItemResponse> call, Response<CustomerItemResponse> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        AllItemList.clear();
                        AllItemList.addAll(response.body().getCustomerLedgerRes());
                        layoutManager = new LinearLayoutManager(Sold_ItemBasesActivity.this, RecyclerView.VERTICAL, false);
                        adapter = new SoldItem_Adapter(Sold_ItemBasesActivity.this, AllItemList, LedgerCutomerDetails.nameCustomer,cardCode,startDateFrag,endDateFrag);
                        binding.recyclerview.setLayoutManager(layoutManager);
                        binding.recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (response.body().getCustomerLedgerRes().size() == 0) {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                        } else {
                            binding.noDatafound.setVisibility(View.INVISIBLE);
                        }
                        binding.loader.loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerItemResponse> call, Throwable t) {
                binding.loader.loader.setVisibility(View.GONE);
            }
        });
    }


    private void customerOnPageBasisLedger(String customerCode, String fromDate, String toDate) {
        binding.loader.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", customerCode);
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", "");
        hde.put("GroupCode", "");
        hde.put("SubGroupCode", subGroupCode);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));




        Call<CustomerItemResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService(this).bpWiseSoldFilterItemsPurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService(this).bpWiseSoldFilterItems(hde);
        }
        call.enqueue(new Callback<CustomerItemResponse>() {
            @Override
            public void onResponse(Call<CustomerItemResponse> call, Response<CustomerItemResponse> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        //  AllItemList.clear();
                        AllItemList.addAll(response.body().getCustomerLedgerRes());
//                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//                        adapter = new SoldItem_Adapter(getContext(), AllItemList, LedgerCutomerDetails.nameCustomer);
//                        recyclerview.setLayoutManager(layoutManager);
//                        recyclerview.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        if (response.body().getCustomerLedgerRes().size() == 0) {
                            pageNo++;
                            //  no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            // no_datafound.setVisibility(View.INVISIBLE);
                        }
                        binding.loader.loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerItemResponse> call, Throwable t) {
                binding.loader.loader.setVisibility(View.GONE);
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
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            //   from_to_date.setText(startDate + " - " + endDate);
            //  Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            // from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());

            //   listener.onDataPassed(startDate,endDate);
            this.binding.dateText.setText("Today");
            customerOnePageLedger(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            this.binding.dateText.setText("Yesterday");
            customerOnePageLedger(cardCode, startDate, endDate);
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
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            this.binding.dateText.setText("" + startDate + "-" + endDate);
            customerOnePageLedger(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            //  from_to_date.setText(startDate + " - " + endDate);
            // Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            //  customerLedger(cardCode,startDate,endDate);
//            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());

            this.binding.dateText.setText("" + startDate + "-" + endDate);
            customerOnePageLedger(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            this.binding.dateText.setText("Last Month");
            customerOnePageLedger(cardCode, startDate, endDate);

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
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();

            this.binding.dateText.setText("This Quarter");
            customerOnePageLedger(cardCode, startDate, endDate);
            //     from_to_date.setText(startDate + " - " + endDate);
            //    Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);

            //  customerLedger(cardCode, startDate, endDate);
            //  from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            //    from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            //   customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            this.binding.dateText.setText("This Year");
            customerOnePageLedger(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            // customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            this.binding.dateText.setText("last Year");
            customerOnePageLedger(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {

            this.binding.dateText.setText("All");
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

        materialDatePicker.show(this.getSupportFragmentManager(), "Tag_Picker");
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
                // from_to_date.setText(startDate + " - " + endDate);
                binding.dateText.setText("" + startDate + "-" + endDate);
                customerOnePageLedger(cardCode, startDate, endDate);
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
        customerOnePageLedger(cardCode, startDate, endDate);
    }

    @Override
    public void onDataPassedCustomer(String startDate, String endDate) {
        Log.e("DATE>>>>>>", "onDataPassedCustomersold: " + startDate + endDate);
        //  Toast.makeText(requireContext(), "" + startDate + endDate, Toast.LENGTH_SHORT).show();
    }


}