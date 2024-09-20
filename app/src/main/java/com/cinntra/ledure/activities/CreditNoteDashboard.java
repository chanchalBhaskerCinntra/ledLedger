package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CreditCostomersAdapter;
import com.cinntra.ledure.databinding.ActivityCreditNoteDashboardBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditNoteDashboard extends AppCompatActivity {
    ActivityCreditNoteDashboardBinding binding;
    int pageNo = 1;
    String type="";
    String groupType="";
    String code="";
    CreditCostomersAdapter adapter;
    ArrayList<BusinessPartnerData> AllItemList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String url = "";
    String searchTextValue = "";
    WebView dialogWeb;


    private void openpopup() {


        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("Invoice Credit note", R.drawable.ic_ledger, false)) // aad an item list.
                .addItem(new PowerMenuItem("Purchased Credit Note", R.drawable.ic_ledger, false)) // aad an item list.
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
        powerMenu.showAsDropDown(binding.toolbarCreditNoteDashBoard.filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {

                    case 0:
                        Toast.makeText(CreditNoteDashboard.this, "0", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:

                        Toast.makeText(CreditNoteDashboard.this, "1", Toast.LENGTH_SHORT).show();
                        break;


                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreditNoteDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        type=getIntent().getStringExtra("Type");
        groupType=getIntent().getStringExtra("filter");
        code=getIntent().getStringExtra("code");

        setUpToolbar();
        url = Globals.allCustomerPdfUrl + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=";
        Log.e("PDF URL===>:", "onCreate: " + url);

        binding.receivePendingLayout.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(this);
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        TotalCreditNotesOnePage(startDate, endDate);
        binding.rvListOfCustomer.addOnScrollListener(scrollListener);
        binding.toolbarCreditNoteDashBoard.relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(this);
        });

        binding.toolbarCreditNoteDashBoard.backPress.setOnClickListener(view -> {
            finish();
        });

        binding.toolbarCreditNoteDashBoard.sharePdf.setOnClickListener(view -> {
            //  showBottomSheetDialog();
            shareLedgerData();
        });


        binding.toolbarCreditNoteDashBoard.filterView.setOnClickListener(view -> {
            openpopup();
        });

        binding.toolbarCreditNoteDashBoard.search.setOnClickListener(view -> {
            binding.toolbarCreditNoteDashBoard.mainHeaderLay.setVisibility(View.GONE);
            binding.toolbarCreditNoteDashBoard.searchLay.setVisibility(View.VISIBLE);

            binding.toolbarCreditNoteDashBoard.searchView.setIconifiedByDefault(true);
            binding.toolbarCreditNoteDashBoard.searchView.setFocusable(true);
            binding.toolbarCreditNoteDashBoard.searchView.setIconified(false);
            binding.toolbarCreditNoteDashBoard.searchView.requestFocusFromTouch();
        });

        eventSearchManager();

    }


    private void eventSearchManager() {
        binding.toolbarCreditNoteDashBoard.searchView.setBackgroundColor(Color.parseColor("#00000000"));
        binding.toolbarCreditNoteDashBoard.searchLay.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.searchView.setVisibility(View.VISIBLE);

//        binding.toolbarCreditNoteDashBoard.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e("TAG", "onQueryTextSubmit: ");
//                binding.toolbarCreditNoteDashBoard.searchView.clearFocus();
//
//                searchTextValue = query;
//                if (!searchTextValue.isEmpty()) {
//                    pageNo = 1;
//                    TotalCreditNotesOnePage(startDate, endDate);
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.e("TAG", "onQueryTextChange: " + newText);
//                if (adapter != null) {
//                    adapter.filter(newText);
//                }
//                return true;
//            }
//        });

        SearchViewUtils.setupSearchView(binding.toolbarCreditNoteDashBoard.searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.toolbarCreditNoteDashBoard.searchView.clearFocus();

                searchTextValue = query;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    TotalCreditNotesOnePage(startDate, endDate);
                }

                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                binding.toolbarCreditNoteDashBoard.searchView.clearFocus();

                searchTextValue = newText;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    TotalCreditNotesOnePage(startDate, endDate);
                }


            }
        });

//        relativeCalView.setOnClickListener(view -> {
//            //showDateBottomSheetDialog(this);
//        });

    }


    @Override
    public void onBackPressed() {

        if (binding.toolbarCreditNoteDashBoard.mainHeaderLay.getVisibility() == View.GONE) {
            binding.toolbarCreditNoteDashBoard.searchLay.setVisibility(View.GONE);
            binding.toolbarCreditNoteDashBoard.mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingBottom;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
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
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            // binding
            pageNo = 1;
            //  callDashboardCounter();
            TotalCreditNotesOnePage(startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            // Log.e("receiptAllPdfUl", "onCreateView: " + url);
            // from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        bindingBottom.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            ///   callDashboardCounter();
            //  callledgerOneapi(reportType, startDate, endDate);
            // url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            //  Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //  from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            //    callDashboardCounter();
            //  callledgerOneapi(reportType, startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            //   Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //  from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bindingBottom.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            //callDashboardCounter();
            //  callledgerOneapi(reportType, startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            // Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //  from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);

            //callDashboardCounter();
            // callledgerOneapi(reportType, startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            //  Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //  from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            // callDashboardCounter();
            // callledgerOneapi(reportType, startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            // Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //   from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisYearFirstDate();
            endDate = Globals.thisYearLastDate();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            //  callDashboardCounter();
            // callledgerOneapi(reportType, startDate, endDate);
            //  url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            //  Log.e("receiptAllPdfUl", "onCreateView: " + url);
            //  from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            TotalCreditNotesOnePage(startDate, endDate);
            //   callDashboardCounter();
            // callledgerOneapi(reportType, startDate, endDate);
            //   url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            // Log.e("receiptAllPdfUl", "onCreateView: " + url);
            // from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        bindingBottom.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatelng = Globals.lastyearCal().getTimeInMillis();
                endDatelng = Globals.thisyearCal().getTimeInMillis();
                startDate = Globals.lastYearFirstDate();
                endDate = Globals.getCurrentDateInLastFinancialYear();
                pageNo = 1;
                //  from_to_date.setText(startDate + " - " + endDate);
                Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
                binding.loader.loader.setVisibility(View.VISIBLE);
                TotalCreditNotesOnePage(startDate, endDate);
                //   callDashboardCounter();
                // callledgerOneapi(reportType, startDate, endDate);
                //   url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
                // Log.e("receiptAllPdfUl", "onCreateView: " + url);
                // from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
                bottomSheetDialog.dismiss();

            }
        });


        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            binding.loader.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            TotalCreditNotesOnePage(startDate, endDate);
            //  callDashboardCounter();
            //  callledgerOneapi(reportType, "", "");
            //   url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            //   Log.e("receiptAllPdfUl", "onCreateView: " + url);
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
                pageNo = 1;
                // from_to_date.setText(startDate + " - " + endDate);
                binding.loader.loader.setVisibility(View.VISIBLE);
                TotalCreditNotesOnePage(startDate, endDate);
                // callDashboardCounter();
                // callledgerOneapi(reportType, startDate, endDate);
            }
        });


    }

    private void setUpToolbar() {
        binding.toolbarCreditNoteDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.filterView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.search.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.sharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.ivSharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.headTitle.setText(getResources().getString(R.string.credit_notes));

        //todo toolbar
        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            binding.toolbarCreditNoteDashBoard.headTitle.setText(getResources().getString(R.string.debit_note));
        }else {
            binding.toolbarCreditNoteDashBoard.headTitle.setText(getResources().getString(R.string.credit_notes));
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
                binding.loader.loader.setVisibility(View.VISIBLE);
                pageNo++;
                //  itemOnPageBasis(pageNo);
                TotalCreditNotesALlPageBasis(startDate, endDate);
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


    private void TotalCreditNotesOnePage(String fromDate, String toDate) {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("Type", type);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("SearchText", searchTextValue);
        hde.put("Code", code);
        hde.put("Filter", groupType);
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Prefs.putString(Globals.FROM_DATE, fromDate);
        Prefs.putString(Globals.TO_DATE, toDate);

        Call<CustomerBusinessRes> call;

        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard_purchase(hde);
        }else {
            call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard(hde);
        }

//        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                if (response != null) {
                    //  binding.sa.setText("Rs. " + response.body().getDifferenceAmount());
                    binding.loader.loader.setVisibility(View.GONE);
                    AllItemList.clear();
                    if (response.body().getData().size() > 0) {
                        binding.noDatafound.setVisibility(View.GONE);
                        AllItemList.addAll(response.body().getData());

                    } else {
                        binding.noDatafound.setVisibility(View.VISIBLE);
                        Toast.makeText(CreditNoteDashboard.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                    adapter = new CreditCostomersAdapter(CreditNoteDashboard.this, AllItemList);


                    binding.rvListOfCustomer.setAdapter(adapter);
                    binding.rvListOfCustomer.setLayoutManager(layoutManager);
                    adapter.AllData(AllItemList);

                } else {

                    Toast.makeText(CreditNoteDashboard.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                Toast.makeText(CreditNoteDashboard.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void TotalCreditNotesALlPageBasis(String fromDate, String toDate) {
        HashMap<String, String> hde = new HashMap<>();


        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("Type", type);
        hde.put("SearchText", searchTextValue);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("Code", code);
        hde.put("Filter", groupType);
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<CustomerBusinessRes> call;

        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard_purchase(hde);
        }else {
            call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard(hde);
        }

//        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(this).credit_note_dashboard(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                if (response != null) {
                    if (response.body().getStatus() == 200) {
                        //  AllItemList.clear();
                        AllItemList.addAll(response.body().getData());
                        adapter.AllData(AllItemList);
                        adapter.notifyDataSetChanged();

                        binding.loader.loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {

            }
        });
    }

    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
//        String title = getString(R.string.credit_notes);

        String title="";
        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){

            title = getString(R.string.debit_note);
            url = Globals.allDebitNote + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        }else {
            title = getString(R.string.credit_notes);
            url = Globals.allCreditNote + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        }

        // url = Globals.particularBpSales + "Type="+reportType+"&CardCode=" + cardCode + "&FromDate="+startDate+"&ToDate="+endDate+"&"+PAGE_NO_STRING+""+pageNo+Globals.QUERY_MAX_PAGE_PDF+Globals.QUERY_PAGE_SIZE;
//        url = Globals.allCreditNote + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(CreditNoteDashboard.this);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding bindingBottom;
        bindingBottom = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());


        setUpWebViewDialog(bindingBottom.webViewBottomSheetDialog, url, false, bindingBottom.loader, bindingBottom.linearWhatsappShare, bindingBottom.linearGmailShare, bindingBottom.linearOtherShare);


        bottomSheetDialog.show();
        bindingBottom.headingBottomSheetShareReport.setText(R.string.share_customer_list);
        bindingBottom.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


        bindingBottom.linearWhatsappShare.setOnClickListener(view ->
        {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            lab_pdf(dialogWeb, f_name);
        });

        bindingBottom.linearOtherShare.setOnClickListener(view ->
                {
                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_other_pdf(dialogWeb, f_name);

                }
        );
        bindingBottom.linearGmailShare.setOnClickListener(view -> {

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

        final ProgressDialog progressDialog = new ProgressDialog(CreditNoteDashboard.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CreditNoteDashboard.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(CreditNoteDashboard.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CreditNoteDashboard.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(CreditNoteDashboard.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CreditNoteDashboard.this, webView, f, fileName, new PdfView.Callback() {

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