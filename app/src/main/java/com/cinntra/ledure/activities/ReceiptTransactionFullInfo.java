package com.cinntra.ledure.activities;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CustomersItemsAdapter;
import com.cinntra.ledure.adapters.IncomingpaymentInvoicesAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.LayoutBillDetailsBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.LedgerCustomerResponse;
import com.cinntra.ledure.model.ReceiptHead;
import com.cinntra.ledure.model.ReceiptResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ReceiptTransactionFullInfo extends MainBaseActivity {


    LayoutBillDetailsBinding binding;
    private IncomingpaymentInvoicesAdapter adapter;

    /***shubh****/
    WebView dialogWeb;
    String url;

    String ReceiptId;
    String heading = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutBillDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarCustom.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        String id = getIntent().getStringExtra("ID");
        ReceiptId = getIntent().getStringExtra("ReceiptId");
        heading = getIntent().getStringExtra("Heading");

        url = Globals.receiptVoucherPdf + "ReceiptId=" + id;


        /***shubh****/
        //   url = Globals.LedgerUrl + "CardCode=9580&FromDate=12-4-2023&ToDate=23-4-2023";
        callOneInvoice(Integer.parseInt(id));
        /**end**/

        //setList();
        // RecyclerView item_recyclerview = findViewById(R.id.item_recyclerview);

        CustomersItemsAdapter adapter = new CustomersItemsAdapter(this, itemList);
        binding.rvBillsItem.setAdapter(adapter);


        /***shubh****/
        binding.btnShareInVoice.setOnClickListener(view ->
        {
            //showBottomSheetDialog();

            shareLedgerData();
        });
    }

    ArrayList<DocumentLines> itemList = new ArrayList<>();


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
        MenuItem clearallFilter = menu.findItem(R.id.clearAllFilter);
        searchItem.setVisible(false);
        calenderItem.setVisible(false);
        shareItem.setVisible(false);
        share.setVisible(false);
        atoz.setVisible(false);
        ztoa.setVisible(false);
        clearallFilter.setVisible(false);


        /**shubh***/
        infoItem.setVisible(false);
        return true;


    }


    /***shubh****/
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
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


    private void callOneInvoice(int invoiceID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Integer> hde = new HashMap<>();
                hde.put("ReceiptId", invoiceID);
                Call<ReceiptResponse> call;
                if (heading.equalsIgnoreCase("ttVendorPayment")) {
                    call = NewApiClient.getInstance().getApiService().purchaseOneReceipt(hde);
                } else {

                    if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                        call = NewApiClient.getInstance().getApiService().purchaseOneReceipt(hde);
                    } else {
                        call = NewApiClient.getInstance().getApiService().oneReceipt(hde);
                    }

                    //  call = NewApiClient.getInstance().getApiService().oneReceipt(hde);
                }

                try {
                    Response<ReceiptResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (response.body().getValue().size()>0){
                                        /***shubh****/
                                        setUpRecieiptData(response.body().getValue().get(0));

                                        adapter = new IncomingpaymentInvoicesAdapter(ReceiptTransactionFullInfo.this, response.body().getValue().get(0).getIncomingPaymentInvoices());

                                        // Attach the adapter to the recyclerview to populate items
                                        binding.rvBillsItem.setAdapter(adapter);
                                        // Set layout manager to position the items


                                        binding.rvBillsItem.setLayoutManager(new LinearLayoutManager(ReceiptTransactionFullInfo.this));
                                    }



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

    /***shubh****/
    private void setUpRecieiptData(ReceiptHead recieiptData) {
        String reverseDate = Globals.convertDateFormat(recieiptData.getDocDate());

        binding.tvDateOfReceipt.setText(reverseDate + " | Receipt");
        binding.toolbarCustom.toolbar.setTitle("Voucher No. " + recieiptData.getDocNum());
        try {
            String result = java.net.URLDecoder.decode(recieiptData.getCardCode() + " " + recieiptData.getCardName(), StandardCharsets.UTF_8.name());
            binding.tvNameCompany.setText(result);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }

        binding.tvNarration.setText(getResources().getString(R.string.na));
        //todo no comment key in purchase
           /* String resultNar = java.net.URLDecoder.decode(recieiptData.getComments(), StandardCharsets.UTF_8.name());
            if (recieiptData.getComments().isEmpty()) {
                binding.tvNarration.setText(getResources().getString(R.string.na));
            } else {
                binding.tvNarration.setText(resultNar);
            }*/

        binding.tvBankName.setText("BANK OF BARODA");
        binding.tvPaymentMode.setText("CHEQUE");
        binding.tvPaymentMode.setText("CHQ date: " + recieiptData.getTransferDate());
        binding.tvPaymentMode.setText("Mode: Transported");
        binding.tvTotalBillAmount.setText(Globals.numberToK(recieiptData.getTransferSum()));
        binding.tvGrandTotals.setText(Globals.numberToK(recieiptData.getTransferSum()));


    }


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = getString(R.string.share_reciept);

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

        binding.headingBottomSheetShareReport.setText(R.string.share_reciept);

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

        final ProgressDialog progressDialog = new ProgressDialog(ReceiptTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ReceiptTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(ReceiptTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ReceiptTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(ReceiptTransactionFullInfo.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ReceiptTransactionFullInfo.this, webView, f, fileName, new PdfView.Callback() {

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
            return false;
        }
    }
}
