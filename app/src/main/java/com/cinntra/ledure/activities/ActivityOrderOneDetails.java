package com.cinntra.ledure.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.cinntra.ledure.adapters.InnerItemOrderOneDelieryAdapter;

import com.cinntra.ledure.databinding.ActivityOrderOneDetailsBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataOrderOne;

import com.cinntra.ledure.model.ResPonseOrderOne;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ActivityOrderOneDetails extends AppCompatActivity {
    ActivityOrderOneDetailsBinding binding;
    String orderId = "";
    InnerItemOrderOneDelieryAdapter adapter;
    /***shubh****/
    WebView dialogWeb;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderOneDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();
        orderId = getIntent().getExtras().getString("ID");

        binding.toolbarPendingList.headTitle.setText("Voucher no:" + orderId);
        binding.toolbarPendingList.newQuatos.setVisibility(View.VISIBLE);
        binding.toolbarPendingList.relativeInfoView.setVisibility(View.INVISIBLE);
        binding.toolbarPendingList.search.setVisibility(View.INVISIBLE);
        binding.toolbarPendingList.filter.setVisibility(View.INVISIBLE);
        binding.toolbarPendingList.share.setVisibility(View.VISIBLE);
        binding.toolbarPendingList.back.setOnClickListener(view ->
        {
            finish();
        });
        callorderOne(orderId);
        //  Toast.makeText(this, orderId, Toast.LENGTH_SHORT).show();
        url = Globals.orderOnePdfUr + "id=" + orderId;

        binding.toolbarPendingList.share.setOnClickListener(view -> {
            shareLedgerData();

            //showBottomSheetDialog();
        });


    }


    private void callorderOne(String invoiceID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("id", invoiceID);
                Call<ResPonseOrderOne> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().order_one_purchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService().order_one(hde);
                }


//                Call<ResPonseOrderOne> call = NewApiClient.getInstance().getApiService().order_one(hde);
                try {
                    Response<ResPonseOrderOne> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (response.body().getData().size() > 0){
                                        setData(response.body().getData().get(0));
                                        adapter = new InnerItemOrderOneDelieryAdapter(ActivityOrderOneDetails.this, response.body().getData().get(0).getDocumentLines());
                                        binding.rvBillsItem.setLayoutManager(new LinearLayoutManager(ActivityOrderOneDetails.this, RecyclerView.VERTICAL, false));
                                        binding.rvBillsItem.setAdapter(adapter);
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

    private void setData(DataOrderOne one) {
        binding.toolbarPendingList.headTitle.setText("Voucher no: " + one.getDocEntry());
        binding.tvCustomerName.setText(one.getCardCode() + " " + one.getCardName());
        binding.tvDate.setText("" + Globals.convertDateFormat(one.getCreateDate()));
        binding.tvOrderHash.setText("Order #: " + orderId + "," + orderId);
        binding.tvRefHsh.setText("Ref #: " + orderId);
        binding.tvNetAmountTotals.setText(Globals.numberToK(one.getNetTotal()));

        if (one.getComments().isEmpty()) {
            binding.tvNaration.setText("N/A");
        } else {
            binding.tvNaration.setText(one.getComments());
        }


        binding.tvGrossTotals.setText(Globals.numberToK(one.docTotal));
        binding.tvFinalDestination.setText("N/A");
        binding.tvOrderTerms.setText(one.getDeliveryMode());
        binding.tvDueDate.setText(Globals.convertDateFormat(one.getDocDueDate()));
        binding.tvOrderRef.setText("No Reference");
    }


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = "Share";

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
        binding.headingBottomSheetShareReport.setText("Share");

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

        final ProgressDialog progressDialog = new ProgressDialog(ActivityOrderOneDetails.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ActivityOrderOneDetails.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(ActivityOrderOneDetails.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ActivityOrderOneDetails.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(ActivityOrderOneDetails.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ActivityOrderOneDetails.this, webView, f, fileName, new PdfView.Callback() {

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
                // Setting we View Client
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