package com.cinntra.ledure.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ItemParticularCustomerSalesTransactionAdapter;
import com.cinntra.ledure.databinding.ActivityItemPurchasedParticularCustomerInfoBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowCustomerDataBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.ResponseItemParticularCustomerInfo;
import com.cinntra.ledure.model.SaleOrder;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ItemPurchasedParticularCustomerInfo extends AppCompatActivity {
    private ActivityItemPurchasedParticularCustomerInfoBinding binding;
    String id, cardCode, email, mobile, address, groupName, creditLimit, creditLimitDays, gstIn, name, contactPersonName;
    String intentCardName = "";

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    ArrayList<SaleOrder> AllitemsList;
    String startDateReverse = "";
    String endDateReverse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemPurchasedParticularCustomerInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AllitemsList = new ArrayList<>();
        // Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();

        hideToolbarMenu();
        intentCardName = getIntent().getStringExtra("cardName");

        binding.toolbarItemDashBoard.headTitle.setText(intentCardName);
        binding.toolbarItemDashBoard.backPress.setOnClickListener(view -> {
            finish();
        });

        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
       /* startDate=Prefs.getString(Globals.FROM_DATE,"");
        endDate=Prefs.getString(Globals.TO_DATE,"");*/


        startDateReverse = Globals.convertDateFormat(startDate);
        endDateReverse = Globals.convertDateFormat(endDate);

        if (Prefs.getString(Globals.FROM_DATE, "").isEmpty() || startDateReverse.isEmpty()) {
            binding.tvDateFrom.setText("All");
        } else {
            binding.tvDateFrom.setText(startDateReverse + " To " + endDateReverse);
        }


        id = getIntent().getStringExtra("itemcode");
        cardCode = getIntent().getStringExtra("cardCode");
        url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=&ToDate=";
        //   binding.tvDateFrom.setText(startDate + " To " + endDate);
        binding.loader.setVisibility(View.VISIBLE);
        callParticularItemLedgerInfo(cardCode, id, startDate, endDate);

        binding.toolbarItemDashBoard.relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(ItemPurchasedParticularCustomerInfo.this);
        });

        binding.toolbarItemDashBoard.search.setOnClickListener(view -> {
//            showBottomSheetDialog();
            shareLedgerData();
        });

        binding.toolbarItemDashBoard.newQuatos.setOnClickListener(view -> {
            //  showDateBottomSheetDialog(ItemPurchasedParticularCustomerInfo.this);
            // showBottomSheetDialog();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            Prefs.putString("where", "Customer");
            Intent intent = new Intent(ItemPurchasedParticularCustomerInfo.this, LedgerReports.class);
            intent.putExtra("cardCode", cardCode);
            intent.putExtra("where", "details");
            startActivity(intent);
        });


        binding.toolbarItemDashBoard.relativeInfoView.setOnClickListener(view -> {
            Globals.showCustomerBottomSheetDialog(ItemPurchasedParticularCustomerInfo.this, name, groupName, creditLimit, creditLimitDays, mobile, address, email, getLayoutInflater(), gstIn, contactPersonName);
        });


    }


    private void shareLedgerData() {
        String title = getString(R.string.share_customer_list);

        //todo pdf
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            url = Globals.itemParticularBpPurchase + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

        } else {
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
        }
        Log.e("PDF=======>", "shareLedgerData: " + url);

        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }


    public void hideToolbarMenu() {
        getSupportActionBar().hide();


        binding.toolbarItemDashBoard.filterView.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.searchIcon.setImageResource(R.drawable.ic_baseline_share_24);

        binding.toolbarItemDashBoard.add.setImageResource(R.drawable.ic_ledger);


    }


    private void showCustomerBottomSheetDialog(Context context, String title, String groupName, String creditLimit, String creditDate, String mobile, String address, String email) {
        BottomSheetDialogShowCustomerDataBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogShowCustomerDataBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });

        binding.tvCustomerNameBottomSheetDialog.setText(title);
        //  Toast.makeText(context, ""+groupName +creditDate +creditLimit, Toast.LENGTH_SHORT).show();
        /***shubh****/
        binding.etGroupName.setText(groupName);
        binding.etCreditLimit.setText(creditLimit);
        binding.etCreditDate.setText(creditDate);
        binding.etEmail.setText(email);
        binding.etMobileNumber.setText(mobile);
        binding.etAddress.setText(address);
        binding.etCstNumber.setText(gstIn);

        bottomSheetDialog.show();

    }


    private void callParticularItemLedgerInfo(String cardcode, String type, String startDate, String endDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardcode);
                hde.put("ItemCode", type);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);


                Call<ResponseItemParticularCustomerInfo> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedParticularCustomerInfo.this).getItemParticularBpLedgerPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedParticularCustomerInfo.this).getItemParticularBpLedger(hde);
                }
                try {
                    Response<ResponseItemParticularCustomerInfo> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                binding.loader.setVisibility(View.GONE);

                                AllitemsList.clear();
                                if (response.body().getData().size() > 0) {
                                    AllitemsList.addAll(response.body().getData().get(0).getSaleOrder());


                                    name = response.body().getData().get(0).getCardName();
                                    email = response.body().getData().get(0).getEmailAddress();
                                    groupName = response.body().getData().get(0).getGroupName();
                                    creditLimit = response.body().getData().get(0).getCreditLimit();
                                    creditLimitDays = response.body().getData().get(0).getCreditLimitDayes();
                                    creditLimitDays = response.body().getData().get(0).getCreditLimitDayes();
                                    gstIn = response.body().getData().get(0).getgSTIN();
                                    mobile = response.body().getData().get(0).getPhone1();
                                    address = response.body().getData().get(0).getbPAddress();
                                    binding.salesvalue.setText("\u20B9 " + Globals.numberToK(response.body().getData().get(0).getTotalPrice()));
                                } else {
                                    AllitemsList.addAll(new ArrayList<>());
                                }


                                ItemParticularCustomerSalesTransactionAdapter adapter =
                                        new ItemParticularCustomerSalesTransactionAdapter
                                                (ItemPurchasedParticularCustomerInfo.this, AllitemsList, "", "");
                                binding.rvItemDash.setLayoutManager(new LinearLayoutManager(ItemPurchasedParticularCustomerInfo.this, RecyclerView.VERTICAL, false));

                                binding.rvItemDash.setAdapter(adapter);

                                // Update UI element here
                                // loader.setVisibility(View.GONE);
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


    /***shubh****/
    WebView dialogWeb;
    String url;

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ItemPurchasedParticularCustomerInfo.this);
        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedParticularCustomerInfo.this);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

        binding.headingBottomSheetShareReport.setText(R.string.sales_report);
        binding.ivForword.setVisibility(View.GONE);

        progressDialog.dismiss();

      /*  WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");*/
        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.sales_report);

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


        binding.linearWhatsappShare.setOnClickListener(view ->
        {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            lab_pdf(dialogWeb, f_name);
        });

        binding.linearOtherShare.setOnClickListener(view ->
                {
                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_other_pdf(dialogWeb, f_name);

                }
        );
        binding.linearGmailShare.setOnClickListener(view -> {

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

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedParticularCustomerInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedParticularCustomerInfo.this, webView, f, fileName, new PdfView.Callback() {

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
                ItemPurchasedParticularCustomerInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedParticularCustomerInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedParticularCustomerInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedParticularCustomerInfo.this, webView, f, fileName, new PdfView.Callback() {

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
                ItemPurchasedParticularCustomerInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedParticularCustomerInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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
                ItemPurchasedParticularCustomerInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedParticularCustomerInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedParticularCustomerInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedParticularCustomerInfo.this, webView, f, fileName, new PdfView.Callback() {

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
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearSslPreferences();
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


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

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
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                // from_to_date.setText(startDate + " - " + endDate);

                //  callApi(startDate, endDate);
                binding.loader.setVisibility(View.VISIBLE);
                callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
                url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
            }
        });


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
            binding.tvDateFrom.setText("Today");
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

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
            // callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            //   from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bindingBottom.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;
            Log.e("TAG", "THISMONTH: " + url);
            binding.tvDateFrom.setText(bindingBottom.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
//            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisYearFirstDate();
            endDate = Globals.thisYearLastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingBottom.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            // callApi(startDate, endDate);
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        bindingBottom.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatelng = Globals.lastyearCal().getTimeInMillis();
                endDatelng = Globals.thisyearCal().getTimeInMillis();
                startDate = Globals.lastYearFirstDate();
                endDate = Globals.getCurrentDateInLastFinancialYear();
                //  from_to_date.setText(startDate + " - " + endDate);
                Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
                // callApi(startDate, endDate);
                binding.loader.setVisibility(View.VISIBLE);
                callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
                url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

                binding.tvDateFrom.setText(bindingBottom.tvLastYearBottomSheetSelectDate.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });

        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            // callApi("", "");
            binding.loader.setVisibility(View.VISIBLE);
            callParticularItemLedgerInfo(cardCode, id, startDate, endDate);
            url = Globals.itemParticularBpSales + "ItemCode=" + id + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate;

            binding.tvDateFrom.setText(bindingBottom.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

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