package com.cinntra.ledure.activities;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AbsListView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.MonthlyItemSaleAdapter;
import com.cinntra.ledure.databinding.ActivityItemDetailsDashboardBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataItemDetailsOverView;
import com.cinntra.ledure.model.ResponseItemOverView;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Response;

public class ItemDetailsDashboard extends AppCompatActivity {
    private ActivityItemDetailsDashboardBinding binding;
    String id, name;
    MonthlyItemSaleAdapter adapter;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        id = getIntent().getStringExtra("itemcode");
        name = getIntent().getStringExtra("itemname");
        binding.toolbarItemDetailsDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarItemDetailsDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarItemDetailsDashBoard.filterView.setVisibility(View.GONE);
        binding.toolbarItemDetailsDashBoard.search.setVisibility(View.GONE);
        binding.toolbarItemDetailsDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarItemDetailsDashBoard.headTitle.setText(name);
        layoutManager=new LinearLayoutManager(ItemDetailsDashboard.this, RecyclerView.VERTICAL, false);

        binding.rvMonthlyList.addOnScrollListener(scrollListener);
        binding.toolbarItemDetailsDashBoard.backPress.setOnClickListener(view ->
           {
            finish();
        });

        binding.loader.setVisibility(View.VISIBLE);
        callApi();

        binding.linearPurchase.setOnClickListener(view ->
           {
            if (binding.rvMonthlyList.getVisibility()==View.GONE){
                binding.rvMonthlyList.setVisibility(View
                        .VISIBLE);
                 binding.ivArrow.setImageResource(R.drawable.ic_arrow_drop_up_24);
            }else {
                binding.rvMonthlyList.setVisibility(View
                        .GONE);
                 binding.ivArrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            }

        });

        binding.toolbarItemDetailsDashBoard.relativeCalView.setOnClickListener(view ->
          {
            showDateBottomSheetDialog(this);
        });



    }
    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        bindingDate = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingDate.getRoot());
        bindingDate.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {

            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingDate.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            binding.loader.setVisibility(View.VISIBLE);
            callApi();


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisYearFirstDate();
            endDate = Globals.thisYearLastDate();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDate="";
            endDate="";
            binding.loader.setVisibility(View.VISIBLE);
            callApi();

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector()
            {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                binding.loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                binding.loader.setVisibility(View.VISIBLE);
                callApi();


            }
        });


    }
    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;
    LinearLayoutManager layoutManager;
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener()
            {

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
                callApiPageBasis();
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

    int pageNo = 1;
    private void callApiPageBasis()
       {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("ItemCode", id);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                Call<ResponseItemOverView> call = NewApiClient.getInstance().getApiService().getItemOverView(hde);
                try {
                    Response<ResponseItemOverView> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    binding.loader.setVisibility(View.GONE);
                                    if (response.body().getData().size() > 0) {
                                        setData(response.body().getData().get(0));
                                        adapter = new MonthlyItemSaleAdapter(ItemDetailsDashboard.this, response.body().getData().get(0).getMonthGroupSalesList(), response.body().getData().get(0).getItemCode());
                                        binding.rvMonthlyList.setLayoutManager(layoutManager);
                                        binding.rvMonthlyList.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }

                }
                catch (IOException e) {
                    // Handle exception
                    binding.loader.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    private void callApi()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("ItemCode", id);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);

           Call<ResponseItemOverView> call = NewApiClient.getInstance().getApiService().getItemOverView(hde);
                try {
                    Response<ResponseItemOverView> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    binding.loader.setVisibility(View.GONE);
                                    if (response.body().getData().size() > 0) {
                                        setData(response.body().getData().get(0));
                                        adapter = new MonthlyItemSaleAdapter(ItemDetailsDashboard.this, response.body().getData().get(0).getMonthGroupSalesList(), response.body().getData().get(0).getItemCode());
                                        binding.rvMonthlyList.setLayoutManager(layoutManager);
                                        binding.rvMonthlyList.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }

                }
                catch (IOException e) {
                    // Handle exception
                    binding.loader.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    private void setData(DataItemDetailsOverView datum){
        binding.tvTotalPurchase.setText(""+datum.getTotalPrice());
        binding.tvTotalSales.setText(""+datum.getTotalPrice());
        binding.tvLastSaleDate.setText(""+datum.getLastSalesDate());
        binding.tvNumberOfInvoice.setText(""+datum.getUnitPrice());
        binding.tvTotalSaleQty.setText(""+datum.getTotalQty());


    }
}