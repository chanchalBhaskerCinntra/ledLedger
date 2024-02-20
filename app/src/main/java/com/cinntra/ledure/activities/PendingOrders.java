package com.cinntra.ledure.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.PendingOrderAdapter;
import com.cinntra.ledure.databinding.ActivityPendingOrdersBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.PartyWisePendingOrder;
import com.cinntra.ledure.model.PendingOrderResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.PendingOrderDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendingOrders extends MainBaseActivity implements View.OnClickListener {


    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.filterView)
    RelativeLayout filterView;

    @BindView(R.id.relativeInfoView)
    RelativeLayout relativeInfoView;

    @BindView(R.id.relativeCalView)
    RelativeLayout relativeCalView;
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.customer_lead_List)
    RecyclerView customer_lead_List;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    LinearLayoutManager layoutManager;


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    //    String startDate = Globals.firstDateOfFinancialYear();
//    String endDate = Globals.lastDateOfFinancialYear();
    //  TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public static String startDate = Globals.firstDateOfFinancialYear();
    public static String endDate = Globals.lastDateOfFinancialYear();


    ;

    int skipSize = 20;
    int pageSize = 100;
    int pageNo = 1;
    private String searchTextValue = "";

    ActivityPendingOrdersBinding binding;
    //private int currentPage = PAGE_START;
    boolean seachable = false;

    PendingOrderDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);

        filterByName = Prefs.getString(Globals.PrefsPendingATOZ, "");
        filterByAmount = Prefs.getString(Globals.PrefsPendingAmount, "");
        filterByName = Globals.ATOZ;
        Prefs.putString(Globals.PrefsPendingATOZ, Globals.ATOZ);

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
        filterView.setVisibility(View.VISIBLE);
        new_quatos.setVisibility(View.GONE);
        relativeInfoView.setVisibility(View.GONE);
        relativeCalView.setVisibility(View.GONE);
        db = PendingOrderDatabase.getDatabase(this);
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        loader.setVisibility(View.GONE);
        setRecyclerViewAdapter();
        setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "");

        setDefaults();

        eventSearchManager();


    }


    String filterByName = "";
    String filterByAmount = "";

    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Desc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Asc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Clear All Filter", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.START)
                .setTextSize(12)
                .setTextTypeface(Typeface.createFromAsset(getAssets(), "poppins_regular.ttf"))
                .setSelectedTextColor(Color.BLACK)
                .setWidth(Globals.pxFromDp(this, 220f))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build();
        powerMenu.showAsDropDown(filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item
                // TODO: 19-09-2023 Add cashdiscount api on every call on item click right now only text is going to chnage on the basis of click

                switch (position) {
                    /*case 0:
                    Prefs.putString(Globals.BussinessPageType,"DashBoard");
                    Prefs.putString(Globals.AddBp,"");
                    startActivity(new Intent(MainActivity_B2C.this, AddBPCustomer.class));
                    break;*/
                    case 0:
                        binding.filterAtozShubh.setText("Filter : A To Z");
                        Prefs.putString(Globals.PrefsPendingATOZ, Globals.ATOZ);
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);

                        //  cashDiscountOneApi(searchTextValue);

                        break;
                    case 1:
                        binding.filterAtozShubh.setText("Filter : Z To A");
                        Prefs.putString(Globals.PrefsPendingATOZ, Globals.ZTOA);
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);

                        break;
                    case 2:
                        binding.filterAtozShubh.setText("Filter : Amount Desc");
                        Prefs.putString(Globals.PrefsPendingAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);

                        break;
                    case 3:
                        binding.filterAtozShubh.setText("Filter : Amount Asc");
                        Prefs.putString(Globals.PrefsPendingAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);

                        break;

                    case 4:
                        binding.filterAtozShubh.setText("");
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = "";
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.GONE);
                        cashDiscountOneApi(searchTextValue);

                        break;


                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private static final String TAG = "PendingOrders";

    private void setRecyclerViewAdapter() {
        db.myDataDao().deleteAll();
        try {
            if (db.myDataDao().getAll().size() > 0) {
                Log.e(TAG, "LOCALL====>: " + cashList.size());
                cashList.addAll(db.myDataDao().getAll());
            } else {
                Log.e(TAG, "setRecyclerViewAdapter: " + cashList.size());
                if (Globals.checkInternet(this)) {
                    cashDiscountOneApi(searchTextValue);
                }

            }

            adapter = new PendingOrderAdapter(this, cashList);
            adapter.AllData(cashList);
            layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            customer_lead_List.setLayoutManager(layoutManager);
            customer_lead_List.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("TAG", "run: " + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        Log.e("Back==>", "Pending Order");
        if (mainHeaderLay.getVisibility() == View.GONE) {
            searchLay.setVisibility(View.GONE);
            mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    private void eventSearchManager() {
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);


        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Log.e("NEWSHUBH", "onQueryTextSubmit: " + query);
                // Handle search query submission
                // db.myDataDao().deleteAll();
                searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = query;
                seachable = true;
                if (!searchTextValue.isEmpty())
                    cashDiscountOneApi(searchTextValue);
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                // Perform API call or any other action with the newText
                // Log.e("NEWSHUBH", "onQueryTextChange: " + newText);
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.clearFocus();
                //AllitemsList

                pageNo = 1;
                searchTextValue = newText;
                // db.myDataDao().deleteAll();
                seachable = true;
                /*    if (!searchTextValue.isEmpty())*/
                cashDiscountOneApi(searchTextValue);

            }
        });

        relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(this);
        });

        filterView.setOnClickListener(view -> {
            // openpopup();
            openNewPopUpCustomize();
        });

    }

    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(PendingOrders.this, filterView);

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
                        Prefs.putString(Globals.PrefsPendingATOZ, Globals.ATOZ);
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);
                        return true;
                    case R.id.menuZtoA:
                        binding.filterAtozShubh.setText("Filter : Z To A");
                        Prefs.putString(Globals.PrefsPendingATOZ, Globals.ZTOA);
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);
                        return true;

                    case R.id.menuAmountDesc:
                        binding.filterAtozShubh.setText("Filter : Amount Desc");
                        Prefs.putString(Globals.PrefsPendingAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);
                        return true;

                    case R.id.menuAmountAsc:
                        binding.filterAtozShubh.setText("Filter : Amount Asc");
                        Prefs.putString(Globals.PrefsPendingAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        cashDiscountOneApi(searchTextValue);

                        return true;

                    case R.id.menuAllFilter:
                        binding.filterAtozShubh.setText("");
                        Prefs.putString(Globals.PrefsPendingAmount, "");
                        Prefs.putString(Globals.PrefsPendingATOZ, "");
                        filterByAmount = "";
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.GONE);
                        cashDiscountOneApi(searchTextValue);
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


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingBottom;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bindingBottom = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());
        bindingBottom.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            // Toast.makeText(context, "today", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingBottom.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            //binding.tvDateFrom.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            setUpDateTextView(startDate, endDate, true, "Today");
            cashDiscountOneApi("");
            // callApi(startDate, endDate);
            // from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        bindingBottom.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            cashDiscountOneApi("");
            setUpDateTextView(startDate, endDate, true, "Yesterday");
            // callApi(startDate, endDate);
            //   binding.tvDateFrom.setText(bindingBottom.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            cashDiscountOneApi("");
            setUpDateTextView(startDate, endDate, true, "This Week");
            //   from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //callApi(startDate, endDate);
            // binding.tvDateFrom.setText(bindingBottom.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bindingBottom.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            setUpDateTextView(startDate, endDate, true, "This Month");
            cashDiscountOneApi("");
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  callApi(startDate, endDate);
            //   binding.tvDateFrom.setText(bindingBottom.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            cashDiscountOneApi("");
            setUpDateTextView(startDate, endDate, true, "Last Month");
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  callApi(startDate, endDate);
            //   binding.tvDateFrom.setText(bindingBottom.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            cashDiscountOneApi("");
            setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "");
//            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            //  binding.tvDateFrom.setText(bindingBottom.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            cashDiscountOneApi("");
            setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "");
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //   callApi(startDate, endDate);
            //   binding.tvDateFrom.setText(bindingBottom.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            cashDiscountOneApi("");
            setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "");
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            //  binding.tvDateFrom.setText(bindingBottom.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            cashDiscountOneApi("");
            setUpDateTextView(startDate, endDate, true, "All");


            //  callApi("", "");

            //   binding.tvDateFrom.setText(bindingBottom.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDefaults() {

        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            head_title.setText(getResources().getString(R.string.pendng));
        } else {
            head_title.setText(getResources().getString(R.string.pendngs_orders_sale));
        }

        back.setOnClickListener(this);
        search.setOnClickListener(this);


        new_quatos.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);

//        if (Globals.checkInternet(PendingOrders.this)) {
//            //  binding.loaderCustomer.setVisibility(View.VISIBLE);
//            binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
//            cashDiscountOneApi("");
//        }
        customer_lead_List.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(PendingOrders.this)) {
                    pageNo = 1;
                    cashList.clear();
                    //  db.myDataDao().deleteAll();
                    // if(searchView.getVisibility()==View.GONE)
                    if (searchLay.getVisibility() == View.VISIBLE)
                        searchTextValue = searchView.getQuery().toString();
                    else {
                        searchView.setQuery("", false);

                        searchTextValue = "";

                    }
                    seachable = false;
                    cashDiscountOneApi(searchTextValue);

                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.search:
                mainHeaderLay.setVisibility(View.GONE);
                searchLay.setVisibility(View.VISIBLE);

                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                break;

        }

    }


    boolean dataoverFromAPI = true;


    PendingOrderAdapter adapter;
    private ArrayList<PartyWisePendingOrder> cashList = new ArrayList<>();


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

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                if (cashList.size() != 0)
                    pageNo = cashList.size() / Globals.QUERY_PAGE_SIZE;
                pageNo++;
                if (Globals.checkInternet(PendingOrders.this)) {
                    cashDiscountALlPageApi();
                }
                //  itemOnPageBasis(pageNo);
                //  customerOnPageBasisLedger(cardCode, startDateFrag, endDateFrag);
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

    private void cashDiscountOneApi(String searchValue) {
        pageNo = 1;
        if (!seachable)
            db.myDataDao().deleteAll();
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hde = new HashMap<>();
        //hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put(Globals.payLoadOrderByName, filterByName);
        hde.put(Globals.payLoadOrderByAMt, filterByAmount);
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<PendingOrderResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService().getPendingOrderPurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService().getPendingOrder(hde);
        }

//        Call<PendingOrderResponse> call = NewApiClient.getInstance().getApiService().getPendingOrder(hde);
        call.enqueue(new Callback<PendingOrderResponse>() {
            @Override
            public void onResponse(Call<PendingOrderResponse> call, Response<PendingOrderResponse> response) {
                if (response.code() == 200) {
                    binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                    if (response.body().getData().get(0).getPartywise() == null || response.body().getData().get(0).getPartywise().size() == 0) {
                        Globals.setmessage(getApplicationContext());
                        cashList.clear();
                        no_datafound.setVisibility(View.VISIBLE);
                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                        cashList.addAll(response.body().getData().get(0).getPartywise());
                        adapter = new PendingOrderAdapter(PendingOrders.this, cashList);
                        adapter.AllData(cashList);
                        customer_lead_List.setLayoutManager(layoutManager);
                        customer_lead_List.setAdapter(adapter);

                       /* if (response.body().getData().size() > 0&&!seachable) {
                          cashList.addAll(response.body().getData().get(0).getPartywise());
                          db.myDataDao().insertAll(cashList);
                        }*/

                        adapter.notifyDataSetChanged();
                    } else {
                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                        cashList.clear();
                        cashList.addAll(response.body().getData().get(0).getPartywise());
                        if (response.body().getData().size() > 0 && !seachable) {

                            db.myDataDao().insertAll(cashList);
                        }


                        adapter.notifyDataSetChanged();

                        no_datafound.setVisibility(View.GONE);

                    }

                    loader.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<PendingOrderResponse> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                Toast.makeText(PendingOrders.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cashDiscountALlPageApi() {
        HashMap<String, String> hde = new HashMap<>();
        //hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchTextValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put(Globals.payLoadOrderByName, filterByName);
        hde.put(Globals.payLoadOrderByAMt, filterByAmount);
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<PendingOrderResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            call = NewApiClient.getInstance().getApiService().getPendingOrderPurchase(hde);
        } else {
            call = NewApiClient.getInstance().getApiService().getPendingOrder(hde);
        }

//        Call<PendingOrderResponse> call = NewApiClient.getInstance().getApiService().getPendingOrder(hde);
        call.enqueue(new Callback<PendingOrderResponse>() {
            @Override
            public void onResponse(Call<PendingOrderResponse> call, Response<PendingOrderResponse> response) {
                if (response.code() == 200) {
                    binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                    if (response.body().getData().get(0).getPartywise() == null || response.body().getData().get(0).getPartywise().size() == 0) {
//                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.GONE);
                    } else {
                        // cashList.clear();
                        cashList.addAll(response.body().getData().get(0).getPartywise());
                        adapter.AllData(cashList);
                        if (!seachable) {
                            db.myDataDao().insertAll(response.body().getData().get(0).getPartywise());
                        }

                        // db.myDataDao().insertAll(AllitemsList);
                        Log.e("LOCALSIZE====>", "Size: " + db.myDataDao().getAll().size());
                        // setData(response.body().getData().get(0));
//                        adapter = new PendingOrderAdapter(PendingOrders.this, cashList);
//                        customer_lead_List.setLayoutManager(layoutManager);
//                        customer_lead_List.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        Log.e("Adapter===>", "onResponse: " + cashList);
                        if (response.body().getData().size() == 0) {
                            pageNo++;
                            // no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            //  no_datafound.setVisibility(View.INVISIBLE);
                        }
                        no_datafound.setVisibility(View.GONE);

                    }


                }
            }

            @Override
            public void onFailure(Call<PendingOrderResponse> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                Toast.makeText(PendingOrders.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cashDiscountApiTest() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<String> call = NewApiClient.getInstance().getApiService().cashDiscountListTest();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE);
                   /* if(response.body().getValue() == null || response.body().getValue().size() == 0){
                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.VISIBLE);
                    }else {
                        cashList.clear();
                        cashList.addAll(response.body().getValue());
                        CashDisAdapter adapter = new CashDisAdapter(CashDiscount.this,cashList);
                        itemsRecycler.setLayoutManager(new LinearLayoutManager(CashDiscount.this, RecyclerView.VERTICAL,false));
                        itemsRecycler.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        no_datafound.setVisibility(View.GONE);

                    }*/


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PendingOrders.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");
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
                cashDiscountOneApi("");
                setUpDateTextView(startDate, endDate, false, "");

                // from_to_date.setText(startDate + " - " + endDate);

                //   customerLedger(cardCode, startDate, endDate);
            }
        });


    }


    void setUpDateTextView(String startDate, String endDate, boolean shouldShowSimpleText, String simpleText) {
        if (shouldShowSimpleText) {
            // binding.tvSelectedDate.setText(simpleText);
        } else {
            //  binding.tvSelectedDate.setText(startDate + " to " + endDate);
        }


    }

}

