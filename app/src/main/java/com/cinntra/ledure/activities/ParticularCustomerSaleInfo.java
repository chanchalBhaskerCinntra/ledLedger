package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ParticularCustomerSalesTransactionAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowCustomerDataBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.cinntra.ledure.model.LedgerCustomerResponse;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class ParticularCustomerSaleInfo extends MainBaseActivity {
    RecyclerView customerRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.total_amount)
    TextView total_amount;
    @BindView(R.id.salesamount)
    TextView salesamount;
    @BindView(R.id.tvSalesCardSmall)
    TextView tvSalesCardSmall;

    @BindView(R.id.pending_amount)
    TextView pending_amount;

    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.type_dropdown)
    Spinner type_dropdown;
    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;

    @BindView(R.id.btnRemindNow)
    Button btnRemindNow;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    @BindView(R.id.from_to_date)
    TextView from_to_date;

    @BindView(R.id.cardCreditNote)
    CardView cardCreditNote;
    // BusinessPartnerData cde;
    String fromWhere = "";
    String summary = "";
    String reportType = "Gross";
    String cardCode;
    String cardName;

    /***shubh****/
    WebView dialogWeb;
    String url;
    String urlRemindNow;

    String groupName;

    String creditLimit;
    String creditDate;
    String gstNo;
    String mobile;
    String email;
    String address, contactPersonName;
    private String startDateReverseFormat = "";
    private String endDateReverseFormat = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customertransaction_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        fromWhere = getIntent().getStringExtra("FromWhere");
        summary = getIntent().getStringExtra("summary");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
//        startDate = Prefs.getString(Globals.FROM_DATE,"");
//        endDate = Prefs.getString(Globals.TO_DATE,"");
        startDateReverseFormat = Globals.convertDateFormat(startDate);
        endDateReverseFormat = Globals.convertDateFormat(endDate);
        from_to_date.setText(startDateReverseFormat + " To " + endDateReverseFormat);

        cardCode = getIntent().getStringExtra("cardCode");
        cardName = getIntent().getStringExtra("cardName");

        btnRemindNow.setVisibility(View.GONE);

        if (Prefs.getBoolean(Globals.ISPURCHASE, true)) {
            tvSalesCardSmall.setText("Purchase");
            pending_amount.setText("JE/Debit Note");
        }

        url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        toolbar.setTitle(cardName);
        //setList();
        customerRecyclerView = (RecyclerView) findViewById(R.id.customers_recyclerview);
        loader.setVisibility(View.VISIBLE);
        if (fromWhere.trim().equalsIgnoreCase("SaleLedger")) {
            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
        }
        customerRecyclerView.addOnScrollListener(scrollListener);

        cardCreditNote.setOnClickListener(view -> {
            Intent i = new Intent(this, ParticularBpCreditNoteActivity.class);
            i.putExtra("FromWhere", "Credits");
            i.putExtra("code", cardCode);
            i.putExtra("name", cardName);
            startActivity(i);
        });


        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                reportType = type_dropdown.getSelectedItem().toString();
                if (fromWhere.trim().equalsIgnoreCase("SaleLedger")) {
                    callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
                    url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void showCustomerBottomSheetDialog(Context context, String title, String groupName,
                                               String creditLimit, String creditDate, String gstNo, String mobile, String address, String email) {
        BottomSheetDialogShowCustomerDataBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogShowCustomerDataBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });

        binding.tvCustomerNameBottomSheetDialog.setText(title);

        // Toast.makeText(context, ""+address +mobile +email, Toast.LENGTH_SHORT).show();

        binding.etGroupName.setText(groupName);
        binding.etCreditLimit.setText(creditLimit);
        binding.etCreditDate.setText(creditDate);
        binding.etCstNumber.setText(gstNo);
        binding.etMobileNumber.setText(mobile);
        binding.etAddress.setText(address);
        binding.etEmail.setText(email);

        bottomSheetDialog.show();

    }


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = "Share";

        //todo pdf
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            url = Globals.particularBpPurchase + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        } else {
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        }
        Log.e("PDF=======>", "shareLedgerData: " + url);


//        url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ParticularCustomerSaleInfo.this);
        final ProgressDialog progressDialog = new ProgressDialog(ParticularCustomerSaleInfo.this);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

        url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ParticularCustomerSaleInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ParticularCustomerSaleInfo.this, webView, f, fileName, new PdfView.Callback() {

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
                ParticularCustomerSaleInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ParticularCustomerSaleInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ParticularCustomerSaleInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ParticularCustomerSaleInfo.this, webView, f, fileName, new PdfView.Callback() {

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
                ParticularCustomerSaleInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ParticularCustomerSaleInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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
                ParticularCustomerSaleInfo.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ParticularCustomerSaleInfo.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ParticularCustomerSaleInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ParticularCustomerSaleInfo.this, webView, f, fileName, new PdfView.Callback() {

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


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

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
                loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);

                if (fromWhere.trim().equalsIgnoreCase("SaleLedger")) {
                    startDateReverseFormat = Globals.convertDateFormat(startDate);
                    endDateReverseFormat = Globals.convertDateFormat(endDate);
                    from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
                    callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
                    url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

                }


            }
        });


    }

    int pageNo = 1;
    ParticularCustomerSalesTransactionAdapter adapter;
    ArrayList<LedgerCustomerData> AllItemList = new ArrayList<>();

    Call<LedgerCustomerResponse> call;

    private void callparticularledgerOnePageinfo(String cardcode, String type, String startDate, String endDate) {
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardcode);
                hde.put("Type", type);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));

                if (summary.equalsIgnoreCase("purchase")) {
                    tvSalesCardSmall.setText("Purchase");
                    Prefs.putString(Globals.Sale_Purchse_Diff, "ttAPInvoice");
                    call = NewApiClient.getInstance().getApiService(ParticularCustomerSaleInfo.this).getBppurchasedetails(hde);
                } else {
                    Prefs.putString(Globals.Sale_Purchse_Diff, "ttARInvoice");
                    call = NewApiClient.getInstance().getApiService(ParticularCustomerSaleInfo.this).getparticularledgerdetails(hde);
                }

                try {
                    Response<LedgerCustomerResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                salesamount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(response.body().getTotalSales()));
                                total_amount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(response.body().getTotalSales()));
                                if (response.body().getbPData().size() > 0) {
                                    creditDate = response.body().getbPData().get(0).getCreditLimitDayes();
                                    creditLimit = response.body().getbPData().get(0).getCreditLimit();
                                    groupName = response.body().getbPData().get(0).getGroupName();
                                    gstNo = response.body().getbPData().get(0).getGstIn();
                                    email = response.body().getbPData().get(0).getEmailAddress();
                                    mobile = response.body().getbPData().get(0).getPhone1();
                                    address = response.body().getbPData().get(0).getBPAddress();
                                    contactPersonName = response.body().getbPData().get(0).getContactPerson();
                                }
                                AllItemList.clear();
                                AllItemList.addAll(response.body().getData());
                                adapter = new ParticularCustomerSalesTransactionAdapter(ParticularCustomerSaleInfo.this, AllItemList, cardName, fromWhere);
                                customerRecyclerView.setAdapter(adapter);
                                // Update UI element here
                                loader.setVisibility(View.GONE);

                                if (response.body().getData().isEmpty()) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);
                                }
                            }
                        });


                    } else {

                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }

    private void callparticularledgerAllPageinfo(String cardcode, String type, String startDate, String endDate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("CardCode", cardcode);
                hde.put("Type", type);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                //  Call<LedgerCustomerResponse> call = NewApiClient.getInstance().getApiService(this).getparticularledgerdetails(hde);

                if (summary.equalsIgnoreCase("purchase")) {
                    call = NewApiClient.getInstance().getApiService(ParticularCustomerSaleInfo.this).getBppurchasedetails(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(ParticularCustomerSaleInfo.this).getparticularledgerdetails(hde);
                }
                try {
                    Response<LedgerCustomerResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                salesamount.setText("Rs." + response.body().getTotalSales());
//                                total_amount.setText("Rs." + response.body().getTotalSales());
//                                /***shubh****/
//                                creditDate = response.body().getbPData().get(0).getCreditLimitDayes();
//                                creditLimit = response.body().getbPData().get(0).getCreditLimit();
//                                groupName = response.body().getbPData().get(0).getGroupName();
//                                gstNo = response.body().getbPData().get(0).getGstIn();
//                                mobile = response.body().getbPData().get(0).getPhone1();
//                                email = response.body().getbPData().get(0).getEmailAddress();
//                                address = response.body().getbPData().get(0).getBPAddress();
//                                name = response.body().getbPData().get(0).getCardName();
//                                //  name = response.body().getbPData().get(0).getGstIn();
//                                AllItemListSales.clear();
                                AllItemList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (response.body().getData().size() == 0) {
                                    pageNo++;
                                    //   no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    //  no_datafound.setVisibility(View.INVISIBLE);
                                }//

//                                adapterSales = new ParticularCustomerSalesTransactionAdapter(ParticularCustomerInfo.this, AllItemListSales, cde.getCardName(), fromWhere);
//
//                                customerRecyclerView.setAdapter(adapter);
//                                customerRecyclerView.setLayoutManager(layoutManager);
                                // Update UI element here
                                loader.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.share_received);
        MenuItem ledger = menu.findItem(R.id.ledger);
        menuItem.setVisible(true);
        ledger.setVisible(true);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ledger:
                Prefs.putString(Globals.FROM_DATE, startDate);
                Prefs.putString(Globals.TO_DATE, endDate);
                Intent i = new Intent(this, LedgerReports.class);
                i.putExtra("cardCode", cardCode);
                i.putExtra("where", "particular");
                startActivity(i);
                // startActivity(new Intent(this,LedgerReports.class));
                break;
            case R.id.calendar:
                // Globals.selectDat(this);
//                dateRangeSelector();
                showDateBottomSheetDialog(ParticularCustomerSaleInfo.this);
                break;

            case R.id.info_trans:
                // Globals.selectDat(this);
//                showCustomerBottomSheetDialog(ParticularCustomerSaleInfo.this,cardName,groupName,creditLimit
//                ,creditDate,gstNo,mobile,address,email);

                Globals.showCustomerBottomSheetDialog(ParticularCustomerSaleInfo.this, cardName, groupName, creditLimit, creditDate, mobile, address, email, getLayoutInflater(), gstNo, contactPersonName);

                break;

            case R.id.share_received:
                // showBottomSheetDialog();
                shareLedgerData();
                break;
        }
        return true;
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
            loader.setVisibility(View.VISIBLE);
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.getCurrentDateInLastFinancialYear();
            from_to_date.setText(startDate + " - " + endDate);

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view ->
        {
            loader.setVisibility(View.VISIBLE);

            startDate = "";
            endDate = "";

            callparticularledgerOnePageinfo(cardCode, reportType, startDate, endDate);
            url = Globals.particularBpSales + "Type=" + reportType + "&CardCode=" + cardCode + "&FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


            from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

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
                callparticularledgerAllPageinfo(cardCode, reportType, startDate, endDate);
                pageNo++;
                isScrollingpage = false;

//                if (fromWhere.trim().equalsIgnoreCase("Ledger")) {
//                    callparticularledgerAllPageinfo(cde.getCardCode(), reportType, startDate, endDate);
//                    pageNo++;
//                    isScrollingpage = false;
//
//                } else if (fromWhere.trim().equalsIgnoreCase("Bills")) {
//                    callAllPageBills(cde.getCardCode(), cde.getCardName(), reportType, startDate, endDate);
//                    pageNo++;
//                    isScrollingpage = false;
//                } else if (fromWhere.trim().equalsIgnoreCase("Credits")) {
//                    type_dropdown.setVisibility(View.GONE);
//                    receive_pending_layout.setVisibility(View.GONE);
//                    pageNo++;
//                    totalPageBasisCredits(cde.getCardCode(), startDate, endDate);
//                    isScrollingpage = false;
//                } else {
//
//
//                }


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


    public boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }


}