package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CustomersItemsAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.InvoiceNewData;
import com.cinntra.ledure.model.InvoiceResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class InvoiceTransactionFullInfo extends MainBaseActivity {


    @BindView(R.id.total_amount)
    TextView total_amount;

    @BindView(R.id.headingDateMemo)
    TextView headingDateMemo;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.invoice_date)
    TextView invoice_date;
    @BindView(R.id.due_date)
    TextView due_date;
    @BindView(R.id.net_amount)
    TextView net_amount;
    @BindView(R.id.cgst)
    TextView cgst;
    @BindView(R.id.sgst)
    TextView sgst;
    @BindView(R.id.gross_total)
    TextView gross_total;
    @BindView(R.id.status_back)
    LinearLayout status_back;
    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.item_recyclerview)
    RecyclerView item_recyclerview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnShareInVoice)
    Button btnShareInVoice;
    String paymentStatus;

    WebView dialogWeb;
    String url;
    String id = "";
    String heading = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = getIntent().getStringExtra("ID");
        heading = getIntent().getStringExtra("Heading");
        paymentStatus = getIntent().getStringExtra("status");


//
        callOneInvoice(id);
        //setList();

        btnShareInVoice.setOnClickListener(view ->
        {
            shareLedgerData();
            //showBottomSheetDialog();
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem calenderItem = menu.findItem(R.id.calendar);
        MenuItem shareItem = menu.findItem(R.id.ledger);
        MenuItem infoItem = menu.findItem(R.id.info_trans);
        MenuItem ledger = menu.findItem(R.id.ledger);
        MenuItem share = menu.findItem(R.id.share_received);
        MenuItem atoz = menu.findItem(R.id.filterAtoZ);
        MenuItem ztoa = menu.findItem(R.id.filterZtoA);
        MenuItem clearAllFilter = menu.findItem(R.id.clearAllFilter);
        searchItem.setVisible(false);
        calenderItem.setVisible(false);
        shareItem.setVisible(false);
        share.setVisible(false);
        atoz.setVisible(false);
        ztoa.setVisible(false);
        clearAllFilter.setVisible(false);


        /**shubh***/
        infoItem.setVisible(false);
        //   ledger.setVisible(true);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.calendar:
                Globals.selectDat(this);
                break;

        }
        return true;
    }



    private void setData(InvoiceNewData invoiceNewData) {
        try {
            String result = java.net.URLDecoder.decode(invoiceNewData.getCardCode() + " " + invoiceNewData.getCardName(), StandardCharsets.UTF_8.name());
            company_name.setText(result);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
        total_amount.setText("₹ " + Globals.numberToK(invoiceNewData.getDocTotal()));
        String reverseInvoiceDate = Globals.convertDateFormat(invoiceNewData.getDocDate());
        String reverseDocDueDate = Globals.convertDateFormat(invoiceNewData.getDocDueDate());
        invoice_date.setText(reverseInvoiceDate);
        due_date.setText(reverseDocDueDate);
        if (Prefs.getString(Globals.Sale_Purchse_Diff, "").equalsIgnoreCase("ttAPInvoice")) {
            toolbar.setTitle("Voucher no: " + invoiceNewData.getDocNum());
        } else {
            toolbar.setTitle("Voucher no: " + invoiceNewData.getDocNum());
        }

        float net = Float.valueOf(invoiceNewData.getDocTotal()) - Float.valueOf(invoiceNewData.getVatSum());
        net_amount.setText("₹ " + Globals.numberToK(String.valueOf(net)));
        // cgst.setText("₹ "+invoiceNewData.getDocTotal());
        sgst.setText("₹ " + Globals.numberToK(invoiceNewData.getVatSum()));
        gross_total.setText("₹ " + Globals.numberToK(invoiceNewData.getDocTotal()));
        CustomersItemsAdapter adapter = new CustomersItemsAdapter(this, invoiceNewData.getDocumentLines());
        item_recyclerview.setAdapter(adapter);

        if (Prefs.getString(Globals.Sale_Purchse_Diff, "").equalsIgnoreCase("ttAPInvoice") || Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            url = Globals.apInvoiceUrl + "id=" + id;
            headingDateMemo.setText("Invoice Date");
            btnShareInVoice.setText("Share Invoice");
        } else if (Prefs.getString(Globals.Sale_Purchse_Diff, "").equalsIgnoreCase("ttAPCreditNote")) {
            headingDateMemo.setText("Debit Memo Date");
            btnShareInVoice.setText("Share Debit Note");
            title = getString(R.string.share_debit_note);
            url = Globals.debitNoteUrl + "id=" + id;

        } else if (invoiceNewData.getGSTTransactionType().equalsIgnoreCase("gsttrantyp_GSTDebitMemo")) {
            headingDateMemo.setText("Debit Memo Date");
            btnShareInVoice.setText("Share Debit Note");
            title = getString(R.string.share_debit_note);
            url = Globals.debitNoteUrl + "id=" + id;


        } else {
            title = getString(R.string.share_invoice);
            url = Globals.invoiceUrl + "id=" + id;
            Log.e("INVOICE", "setData: " + id);
            headingDateMemo.setText("Invoice Date");
            btnShareInVoice.setText("Share Invoice");

        }


        /***shubh****/
        if (invoiceNewData.getPaymentStatus().equalsIgnoreCase("Partially Paid")) {
            status.setText(invoiceNewData.getPaymentStatus());
            status.setTextColor(Color.parseColor("#ffffff"));
            status_back.getBackground().setTint(getResources().getColor(R.color.main_orange_color));
            status.setVisibility(View.VISIBLE);
        } else if (invoiceNewData.getPaymentStatus().equalsIgnoreCase("Unpaid")) {
            status.setText(invoiceNewData.getPaymentStatus());
            status.setTextColor(Color.parseColor("#ffffff"));
            status_back.getBackground().setTint(getResources().getColor(R.color.red));
            status.setVisibility(View.VISIBLE);
        } else if (invoiceNewData.getPaymentStatus().equalsIgnoreCase("Paid")) {
            status.setText(invoiceNewData.getPaymentStatus());
            status.setTextColor(Color.parseColor("#ffffff"));
            status_back.getBackground().setTint(getResources().getColor(R.color.graph_green));
            status.setVisibility(View.VISIBLE);
        } else {
            status.setText("Unpaid");
        }


    }

    Call<InvoiceResponse> call;

    private void callOneInvoice(String invoiceID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("id", invoiceID);

                if (Prefs.getString(Globals.Sale_Purchse_Diff, "").equalsIgnoreCase("ttAPInvoice")) {
                    call = NewApiClient.getInstance().getApiService(InvoiceTransactionFullInfo.this).purchase_invoice_One(hde);
                } else {
                    if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                        call = NewApiClient.getInstance().getApiService(InvoiceTransactionFullInfo.this).purchase_invoice_One(hde);

                    } else {
                        call = NewApiClient.getInstance().getApiService(InvoiceTransactionFullInfo.this).invoice_One(hde);
                    }


                }

                try {
                    Response<InvoiceResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (response.body().getValue().size() > 0)
                                        setData(response.body().getValue().get(0));
                                }
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

    String title = "";

    private void shareLedgerData() {
        // title =

        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }


    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());


        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.share_invoice);

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
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

        final ProgressDialog progressDialog = new ProgressDialog(InvoiceTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(InvoiceTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(InvoiceTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(InvoiceTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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

            if (isAppInstalled("com.whatsapp")) {

                share.setPackage("com.whatsapp");

            } else if (isAppInstalled("com.whatsapp.w4b")) {
                share.setPackage("com.whatsapp.w4b");
            }


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

        final ProgressDialog progressDialog = new ProgressDialog(InvoiceTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(InvoiceTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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

    public boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            Log.e("NameNotFoundException=>", "ignored");
            return false;
        }
    }


}
