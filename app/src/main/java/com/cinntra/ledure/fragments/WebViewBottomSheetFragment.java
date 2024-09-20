package com.cinntra.ledure.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.cinntra.ledure.WebViewToPdfConverter;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WebViewBottomSheetFragment extends BottomSheetDialogFragment {

    BottomSheetDialogShareReportBinding binding;

    public static WebViewBottomSheetFragment newInstance(WebView dialogWeb, String url, String title) {

        return new WebViewBottomSheetFragment(dialogWeb, url, title);
    }

    public WebViewBottomSheetFragment(WebView dialogWeb, String url, String title) {
        this.dialogWeb = dialogWeb;
        this.url = url;
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.bottom_sheet_dialog_show_customer_data, container, false);
        binding = BottomSheetDialogShareReportBinding.inflate(inflater, container, false);

        //here data must be an instance of the class MarsDataProvider
        binding.headingBottomSheetShareReport.setText(title);
        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);

        binding.ivForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            dismiss();

        });


        binding.linearWhatsappShare.setOnClickListener(view -> {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));

            lab_pdf(dialogWeb, f_name);

        });


        binding.linearOtherShare.setOnClickListener(view -> {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            lab_other_pdf(dialogWeb, f_name);

        });


        binding.linearGmailShare.setOnClickListener(view -> {

            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            lab_gmail_pdf(dialogWeb, f_name);
        });

        return binding.getRoot();
    }

    /***shubh****/
    WebView dialogWeb;
    String url;
    String title;

    private void lab_gmail_pdf(WebView webView, String f_name) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/hana/";
        ;
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        WebViewToPdfConverter.createWebPrintJob(requireActivity(), webView, f, fileName, new WebViewToPdfConverter.Callback() {

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

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(requireContext(), requireActivity().getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

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
        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        WebViewToPdfConverter.createWebPrintJob(getActivity(), webView, f, fileName, new WebViewToPdfConverter.Callback() {

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
        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = null;
        try {
            apkURI = FileProvider.getUriForFile(requireContext(), getActivity().getPackageName() + ".FileProvider", file);
        } catch (Exception e) {
            Log.e("whatsapp", "showBottomSheetDialog: ");
            e.printStackTrace();
        }


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());

        try {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, apkURI);
            if (isAppInstalled("com.whatsapp")) share.setPackage("com.whatsapp");
            else if (isAppInstalled("com.whatsapp.w4b")) share.setPackage("com.whatsapp.w4b");

            startActivity(share);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }


    }

    /***shubh****/
    private void otherShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(requireContext(), getActivity().getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, apkURI);


        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share PDF using"));
        }
    }

    /***shubh****/
    private void lab_pdf(WebView webView, String f_name) {

        String path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/hana/";
        File f = new File(path);

        // if (f.exists()) f.delete();


        //        try {
        //            if (!f.getParentFile().exists())
        //                f.getParentFile().mkdirs();
        //            if (!f.exists())
        //                f.createNewFile();
        //        } catch (IOException e) {
        //            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //        }

        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView obj = new PdfView();

        // PdfViewSHubh.createWebPrintJob(requireActivity(),webView,f,fileName,new );


        WebViewToPdfConverter.createWebPrintJob(requireActivity(), webView, f, fileName, new WebViewToPdfConverter.Callback() {

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

        // webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        webView.getSettings().setBuiltInZoomControls(isZoomAvailable);
        // webView.getSettings().setAppCacheEnabled(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // For API level 21 and above
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            // For API level 20 and below
            //  webView.getSettings().setAppCacheEnabled(false);
            webView.clearCache(true);
        }

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Setting we View Client
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearSslPreferences();
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


    private boolean isAppInstalled(String packageName) {
        try {
            getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }
}
