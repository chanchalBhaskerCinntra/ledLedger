package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Toast;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ParticularBpCreditNoteAdapter;
import com.cinntra.ledure.databinding.ActivityParticularBpCreditNoteBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.cinntra.ledure.model.LedgerCustomerResponse;
import com.cinntra.ledure.webservices.NewApiClient;
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



public class ParticularBpCreditNoteActivity extends AppCompatActivity {
    private static final String TAG="ParticularBpCreditNoteA";
    ActivityParticularBpCreditNoteBinding binding;

    String cardCode, cardName,fromwhere;
    int pageNo = 1;
    ParticularBpCreditNoteAdapter adapter;
    LinearLayoutManager layoutManager;
    ArrayList AllItemList = new ArrayList<LedgerCustomerData>();
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String url="";
    WebView dialogWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParticularBpCreditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");


        layoutManager=new LinearLayoutManager(this);
        binding.cardBig.setVisibility(View.GONE);
        binding.receivePendingLayout.setVisibility(View.GONE);
        cardCode = getIntent().getStringExtra("code");
        cardName = getIntent().getStringExtra("name");
        fromwhere = getIntent().getStringExtra("FromWhere");
        
        
        setUpToolbar();

        binding.fromToDate.setText("" + startDate + " To " + endDate);
        
        
        if (Globals.checkInternet(this)) {
            totalOnePageCredits(cardCode, startDate, endDate);
            if (fromwhere.equalsIgnoreCase("return")){

                url = Globals.perticularPurchaseCreditNote+ "CardCode="+cardCode+ "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            }else {
                url = Globals.perticularCreditNote+ "CardCode="+cardCode+ "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;



            }
            Log.e("PDF URL===>:", "onCreate: "+url);
         

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        binding.rvListOfCustomer.addOnScrollListener(scrollListener);
        binding.toolbarCreditNoteDashBoard.relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(this);
        });
        binding.toolbarCreditNoteDashBoard.sharePdf.setOnClickListener(view -> {
          //  showBottomSheetDialog();
            shareLedgerData();
        });
        binding.toolbarCreditNoteDashBoard.backPress.setOnClickListener(view -> {
            finish();
        });


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
              //  (cardCode, startDate, endDate);
                if (fromwhere.equalsIgnoreCase("return")){

                }else {
                    totalPageBasisCredits(cardCode, startDate, endDate);
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
            binding.fromToDate.setText("Today");

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            // binding
            pageNo = 1;
            //  callDashboardCounter();
            totalOnePageCredits(cardCode, startDate, endDate);
             bottomSheetDialog.dismiss();
        });

        bindingBottom.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText("Yesterday");

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText(startDate + " - " + endDate);

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });


        bindingBottom.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText(startDate + " - " + endDate);

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText(startDate + " - " + endDate);

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
             bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            pageNo = 1;
            //  from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText(startDate + " - " + endDate);

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            pageNo = 1;
            // from_to_date.setText(startDate + " - " + endDate);
            binding.fromToDate.setText(startDate + " - " + endDate);

            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            pageNo = 1;
            binding.fromToDate.setText(startDate + " - " + endDate);

            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            totalOnePageCredits(cardCode, startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            binding.loader.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            binding.fromToDate.setText("All");

            totalOnePageCredits(cardCode, startDate, endDate);
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
                binding.fromToDate.setText(startDate + " - " + endDate);
                binding.loader.loader.setVisibility(View.VISIBLE);
                totalOnePageCredits(cardCode, startDate, endDate);
                // callDashboardCounter();
                // callledgerOneapi(reportType, startDate, endDate);
            }
        });


    }


    Call<LedgerCustomerResponse> call;
    private void totalOnePageCredits(String cardCode, String startDate, String endDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardCode);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));

                if (fromwhere.equalsIgnoreCase("return")){
                    call = NewApiClient.getInstance().getApiService().purchase_bp_credit_note(hde);
                }else {
                    call = NewApiClient.getInstance().getApiService().bp_credit_note(hde);
                }


                try {
                    Response<LedgerCustomerResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.body().getData().isEmpty()) {
                                    binding.noDatafound.setVisibility(View.VISIBLE);
                                    AllItemList.clear();
                                } else {
                                    binding.noDatafound.setVisibility(View.GONE);
                                    AllItemList.clear();

                                }
                                AllItemList.addAll(response.body().getData());
                                Log.e(TAG, "run: "+AllItemList.size());
                                binding.salesAmount.setText(getResources().getString(R.string.Rs)+" " + Globals.numberToK(response.body().getTotalReceivePayment()));
                                binding.salesvalue.setText(getResources().getString(R.string.Rs)+" " + Globals.numberToK(response.body().getTotalReceivePayment()));
                                adapter = new ParticularBpCreditNoteAdapter(ParticularBpCreditNoteActivity.this, AllItemList, cardName, "");
                                binding.rvListOfCustomer.setAdapter(adapter);
                                binding.rvListOfCustomer.setLayoutManager(layoutManager);
                                adapter.notifyDataSetChanged();

                                // Update UI element here
                                binding.loader.loader.setVisibility(View.GONE);
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

    private void totalPageBasisCredits(String cardCode, String startDate, String endDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardCode);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                if (fromwhere.equalsIgnoreCase("return")){
                    call = NewApiClient.getInstance().getApiService().purchase_bp_credit_note(hde);
                }else {
                    call = NewApiClient.getInstance().getApiService().bp_credit_note(hde);
                }
                try {
                    Response<LedgerCustomerResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                salesamount.setText("Rs." + response.body().getTotalReceivePayment());
//                                total_amount.setText("Rs." + response.body().getTotalReceivePayment());
//
                                // Update UI element here
                                AllItemList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                binding.loader.loader.setVisibility(View.GONE);

//                                if (response.body().getData().size() == 0) {
//                                    pageNo++;
//                                    // no_datafound.setVisibility(View.VISIBLE);
//                                } else {
//                                    // no_datafound.setVisibility(View.INVISIBLE);
//                                }
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


    private void setUpToolbar() {
        binding.toolbarCreditNoteDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.filterView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.search.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.sharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.ivSharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.headTitle.setText(cardName);
    }



    private void shareLedgerData()
    {
        String title="share list";

        if (fromwhere.equalsIgnoreCase("return")){
            //  Log.e(TAG, "onCreate: ", );
            //  Toast.makeText(this, "return", Toast.LENGTH_SHORT).show();
            url = Globals.perticularPurchaseCreditNote+ "CardCode="+cardCode+ "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        }else {
            url = Globals.perticularCreditNote+ "CardCode="+cardCode+ "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("PDF URL===>:", "onCreate: "+url);


        }
        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb,url,title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    /***shubh****/



}