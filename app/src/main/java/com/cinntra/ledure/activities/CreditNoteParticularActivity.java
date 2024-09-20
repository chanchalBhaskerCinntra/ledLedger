package com.cinntra.ledure.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cinntra.ledure.R;
import com.cinntra.ledure.databinding.ActivityCreditNotePerticularBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;

public class CreditNoteParticularActivity extends AppCompatActivity {
    ActivityCreditNotePerticularBinding binding;
    String fromWhere="";
    String code="";
    String name="";
    int pageNo=1;

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreditNotePerticularBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        fromWhere=getIntent().getStringExtra("FromWhere");
        code=getIntent().getStringExtra("code");
        name=getIntent().getStringExtra("name");
        binding.receivePendingLayout.setVisibility(View.GONE);
        setUpToolbar();




    }

    private void setUpToolbar() {
        binding.toolbarCreditNoteDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.filterView.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.search.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.sharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.ivSharePdf.setVisibility(View.VISIBLE);
        binding.toolbarCreditNoteDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarCreditNoteDashBoard.headTitle.setText(name);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            // binding
            pageNo = 1;
            //  callDashboardCounter();
          //  TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
            //TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
         //   TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
          //  TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
          //  TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
         //   TotalCreditNotesOnePage(startDate, endDate);
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
//            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            binding.loader.loader.setVisibility(View.VISIBLE);
          //  TotalCreditNotesOnePage(startDate, endDate);
            //   callDashboardCounter();
            // callledgerOneapi(reportType, startDate, endDate);
            //   url = Globals.receiptAllPdfUl + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            // Log.e("receiptAllPdfUl", "onCreateView: " + url);
            // from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            binding.loader.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
          //  TotalCreditNotesOnePage(startDate, endDate);
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

                binding.loader.loader.setVisibility(View.VISIBLE);
               // TotalCreditNotesOnePage(startDate, endDate);

            }
        });


    }

}