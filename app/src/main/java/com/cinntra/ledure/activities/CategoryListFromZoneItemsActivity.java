package com.cinntra.ledure.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ItemOnStockGroupAdapter;
import com.cinntra.ledure.adapters.ItemStockAdapter;
import com.cinntra.ledure.databinding.ActivityCategoryListFromZoneItemsBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.ResponseItemDashboard;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;


public class CategoryListFromZoneItemsActivity extends AppCompatActivity {
    private static final String TAG = "CategoryListFromZoneIte";
    ActivityCategoryListFromZoneItemsBinding binding;

    String fromwhere = "";
    String zoneStock = "";
    // ItemOnDashboardAdapter adapter;
    ItemStockAdapter adapter;
    private String searchTextValue = "";
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String filterByName = Globals.ATOZ;
    String filterByAmount = "";


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    int pageNo = 1;

    ArrayList<DataItemDashBoard> AllitemsList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;
    String groupType = "Zone";

    ArrayList<DataItemFilterDashBoard> AllItemsGroupStockList = new ArrayList<>();
    ItemOnStockGroupAdapter stockGroupAdapter;

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
                binding.loader.setVisibility(View.VISIBLE);


                if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                    pageNo++;
                    callAllPagesApi_GroupItemStock();

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

    String StockGroupCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryListFromZoneItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        //todo new change
        Prefs.putString(Globals.PrefsItemATOZ, "");
        Prefs.putString(Globals.PrefsItemAmount, "");
        fromwhere = getIntent().getStringExtra("fromwhere");
        zoneStock = getIntent().getStringExtra("zoneName");

        filterByName = Prefs.getString(Globals.PrefsItemATOZ, "");
        filterByAmount = Prefs.getString(Globals.PrefsItemAmount, "");
        binding.filterAtozShubh.setText("Filter : A To Z");
        binding.filterAtozShubh.setVisibility(View.VISIBLE);
        if (filterByName.equalsIgnoreCase(Globals.ATOZ)) {
            binding.filterAtozShubh.setText("Filter : A To Z");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);

        } else if (filterByName.equalsIgnoreCase(Globals.ZTOA)) {
            binding.filterAtozShubh.setText("Filter : Z To A");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);
        } else if (filterByAmount.equalsIgnoreCase(Globals.DESC)) {
            binding.filterAtozShubh.setText("Filter : By Amount");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);
        } else {
            binding.filterAtozShubh.setText("Filter : Nothing");
            binding.filterAtozShubh.setVisibility(View.GONE);
        }

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");

        if (startDate.isEmpty()) {
            binding.tvSelectedDate.setText("All");
        } else {
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
        }


        hideToolbarMenu();

        binding.loader.setVisibility(View.GONE);
        setRecyclerViewAdapter();
        //  setUPSpinnerGroup();


        binding.swipeRefreshItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                    pageNo = 1;
                    searchTextValue = "";
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");


                }

            }
        });

        binding.toolbarItemDashBoard.search.setOnClickListener(viewe -> {
            binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.GONE);
            binding.toolbarItemDashBoard.searchLay.setVisibility(View.VISIBLE);

            binding.toolbarItemDashBoard.searchView.setIconifiedByDefault(true);
            binding.toolbarItemDashBoard.searchView.setFocusable(true);
            binding.toolbarItemDashBoard.searchView.setIconified(false);
            binding.toolbarItemDashBoard.searchView.requestFocusFromTouch();

        });
        binding.toolbarItemDashBoard.searchLay.setOnClickListener(viewe -> {
            binding.toolbarItemDashBoard.searchView.setFocusable(true);
        });

        // binding.toolbarItemDashBoard.searchLay.setBackgroundColor(Color.parseColor("#00000000"));
        binding.toolbarItemDashBoard.searchLay.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.searchView.setVisibility(View.VISIBLE);

        binding.toolbarItemDashBoard.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: ");
                binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = query;

                if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {

                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                }

                //  cashDiscountApiTest(searchTextValue);
                return true;


                //  return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = newText;

                if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                    pageNo = 1;
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                }
                return true;
            }
        });

        binding.toolbarItemDashBoard.headTitle.setText(zoneStock);

        binding.toolbarItemDashBoard.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.toolbarItemDashBoard.searchLay.setVisibility(View.GONE);
                binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.VISIBLE);
                return true;
            }
        });


        binding.rvItemDash.addOnScrollListener(scrollListener);
        binding.toolbarItemDashBoard.relativeCalView.setOnClickListener(viewe -> {
            showDateBottomSheetDialog(CategoryListFromZoneItemsActivity.this);
        });
    }


    public void hideToolbarMenu() {
        //  getSupportActionBar().hide();


        binding.toolbarItemDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.filterView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.search.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.back.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.filterView.setOnClickListener(view -> {
            //      openpopup();

            openNewPopUpCustomize();
        });
    }


    private void setRecyclerViewAdapter() {

        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");

      /*  try {
            if (db.myDataDao().getAll().size() > 0) {
                AllitemsList.addAll(db.myDataDao().getAll());
            } else {

                if (Globals.checkInternet(requireContext())) {
                    callApi("");
                }

            }

            adapter = new ItemStockAdapter(requireContext(), AllitemsList);
            adapter.AllData(AllitemsList);
            layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            binding.rvItemDash.setLayoutManager(layoutManager);
            binding.rvItemDash.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("TAG", "run: " + e.getMessage());
        }*/



              /*  if (Globals.checkInternet(requireContext())) {
                    callApi("");
                }
*/


    /*    adapter = new ItemStockAdapter(requireContext(), AllitemsList);
        adapter.AllData(AllitemsList);
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.rvItemDash.setLayoutManager(layoutManager);
        binding.rvItemDash.setAdapter(adapter);*/


    }


    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(CategoryListFromZoneItemsActivity.this, binding.toolbarItemDashBoard.filterView);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu_pending_order, popupMenu.getMenu());

        // Set a menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // Handle menu item clicks
                switch (menuItem.getItemId()) {

                    case R.id.menuAToz:
                        binding.filterAtozShubh.setText("Filter : A To Z");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ATOZ);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);

                        if (Prefs.getString(Globals.PrefsItemATOZ, "").equalsIgnoreCase(Globals.ATOZ)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {

                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        return true;
                    case R.id.menuZtoA:
                        binding.filterAtozShubh.setText("Filter : Z To A");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ZTOA);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {

                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        if (Prefs.getString(Globals.PrefsItemATOZ, "").equalsIgnoreCase(Globals.ZTOA)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }
                        return true;

                    case R.id.menuAmountDesc:
                        binding.filterAtozShubh.setText("Filter : Amount Desc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {

                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        if (Prefs.getString(Globals.PrefsItemAmount, "").equalsIgnoreCase(Globals.DESC)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }

                        return true;

                    case R.id.menuAmountAsc:
                        binding.filterAtozShubh.setText("Filter : Amount Asc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {

                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }

                        return true;

                    case R.id.menuAllFilter:
                        binding.filterAtozShubh.setText("");
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = "";
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.GONE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {

                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        menuItem.setChecked(!menuItem.isChecked());
                        return true;
                    // Add more cases as needed
                    default:
                        return false;
                }
            }
        });

        // Show the PopupMenu
        popupMenu.show();
    }

    private void callApiGroupItemStock(String searchValue, String startDate, String endDate, String type, String zone) {
        binding.loader.setVisibility(View.VISIBLE);
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                binding.loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                if (zoneStock.equalsIgnoreCase("")) {
                    hde.put("Filter", "");
                } else {
                    hde.put("Filter", "Stock");
                    hde.put("Zone", zoneStock);
                }

                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);



                Call<ResponseItemFilterDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService().getFilterGroupItemStock(hde);
                }
                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    binding.swipeRefreshItems.setRefreshing(false);
                                    //  AllitemsList.clear();
                                    AllItemsGroupStockList.clear();
                                    if (response.body().getData().size() > 0) {
                                        binding.noDatafound.setVisibility(View.GONE);
                                        AllItemsGroupStockList.addAll(response.body().getData());
                                        //  itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);

                                    } else {
                                        binding.noDatafound.setVisibility(View.VISIBLE);
                                    }


                                    binding.loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    try {
                                        stockGroupAdapter = new ItemOnStockGroupAdapter(CategoryListFromZoneItemsActivity.this, AllItemsGroupStockList, "zoneStock", zoneStock);
                                    } catch (Exception e) {
                                        Log.e(TAG, "runGRoupFIlter: " + e.getMessage());
                                    }
                                    stockGroupAdapter.AllData(AllItemsGroupStockList);
                                    try {
                                        layoutManager = new LinearLayoutManager(CategoryListFromZoneItemsActivity.this, RecyclerView.VERTICAL, false);
                                    } catch (Exception e) {
                                        Log.e(TAG, "runGroupLINEAR: " + e.getMessage());
                                    }
                                    binding.rvItemDash.setLayoutManager(layoutManager);
                                    binding.rvItemDash.setAdapter(stockGroupAdapter);
                                    stockGroupAdapter.notifyDataSetChanged();

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void callAllPagesApi_GroupItemStock() {
        binding.loader.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "Stock");
                hde.put("Zone", zoneStock);
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);


                Call<ResponseItemFilterDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getFilterGroupItemStockPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService().getFilterGroupItemStock(hde);
                }


                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllItemsGroupStockList.addAll(response.body().getData());
                                    stockGroupAdapter.AllData(AllItemsGroupStockList);
                                    // itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);
                                    binding.loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    stockGroupAdapter.notifyDataSetChanged();

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
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
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Today", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    // callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Yesterday", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    //   callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }


            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Week", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    // callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Month", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    // callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }
            bottomSheetDialog.dismiss();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Last Month", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    // callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }


            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Quarter", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    //  callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            binding.loader.setVisibility(View.VISIBLE);
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
            pageNo = 1;
            searchTextValue = "";
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    //   callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    //  callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDate = "";
            endDate = "";
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "All", binding.tvSelectedDate);
            if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    // callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }

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
                pageNo = 1;
                searchTextValue = "";
                Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
                if (Globals.checkInternet(CategoryListFromZoneItemsActivity.this)) {

                    if (groupType.equalsIgnoreCase("Items")) {
                        setRecyclerViewAdapter();
                        //callApi("");
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }
                }


            }
        });


    }


}