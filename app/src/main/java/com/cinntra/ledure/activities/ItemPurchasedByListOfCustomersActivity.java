package com.cinntra.ledure.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CustomerPurchasedItemAdapter;
import com.cinntra.ledure.databinding.ActivityItemPurchasedByListOfCustomersBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BpList;
import com.cinntra.ledure.model.ResponseItemInvoices;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ItemPurchasedByListOfCustomersActivity extends AppCompatActivity {
    private static final String TAG = "ITEMPURCHASE";
    private ActivityItemPurchasedByListOfCustomersBinding binding;
    String id, name;
    CustomerPurchasedItemAdapter adapter;
    ArrayList<BpList> AllitemsList;

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    String reportType = "Gross";
    private String searchTextValue = "";
    String startDateReverse = "";
    String endDateReverse = "";
    String filterByAmount = "";
    String filterByName = "";
    String zoneCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemPurchasedByListOfCustomersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideToolbarMenu();
        AllitemsList = new ArrayList<>();
       /*
        startDate = Prefs.getString(Globals.FROM_DATE,"");
        endDate = Prefs.getString(Globals.TO_DATE,"");*/
        binding.loader.setVisibility(View.VISIBLE);

        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        Log.e(TAG, "onCreate: " + startDate);

        startDateReverse = Globals.convertDateFormat(startDate);
        endDateReverse = Globals.convertDateFormat(endDate);

        if (Prefs.getString(Globals.FROM_DATE, "").isEmpty() || startDateReverse.isEmpty()) {
            binding.tvDateFrom.setText("All");
        } else {
            binding.tvDateFrom.setText(startDateReverse + " To " + endDateReverse);
        }

        //binding.tvDateFrom.setText(startDate + " To " + endDate);
        layoutManager = new LinearLayoutManager(ItemPurchasedByListOfCustomersActivity.this, RecyclerView.VERTICAL, false);
        binding.rvItemDash.addOnScrollListener(scrollListener);

        binding.toolbarItemDashBoard.backPress.setOnClickListener(view -> {
            finish();
        });


        id = getIntent().getStringExtra("itemcode");
        name = getIntent().getStringExtra("itemname");
        zoneCode = getIntent().getStringExtra("zoneCode");

        binding.toolbarItemDashBoard.headTitle.setText(name);


        url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;

        Log.e(TAG, "onCreate: " + url);

        callApi(startDate, endDate);
        //callLedgerInvoice("Gross",startDate,endDate);

        binding.toolbarItemDashBoard.relativeInfoView.setOnClickListener(view -> {
            //showBottomSheetDialog();
            shareLedgerData();
        });

        binding.toolbarItemDashBoard.relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(ItemPurchasedByListOfCustomersActivity.this);
        });

        binding.toolbarItemDashBoard.search.setOnClickListener(view -> {
            binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.GONE);
            binding.toolbarItemDashBoard.searchLay.setVisibility(View.VISIBLE);

            binding.toolbarItemDashBoard.searchView.setIconifiedByDefault(true);
            binding.toolbarItemDashBoard.searchView.setFocusable(true);
            binding.toolbarItemDashBoard.searchView.setIconified(false);
            binding.toolbarItemDashBoard.searchView.requestFocusFromTouch();
        });

        binding.toolbarItemDashBoard.filterView.setOnClickListener(view -> {
            openpopup();

            openNewPopUpCustomize();
        });

        binding.toolbarItemDashBoard.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: ");
                binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = query;

                if (Globals.checkInternet(ItemPurchasedByListOfCustomersActivity.this)) {
                    callApi(startDate, endDate);
                }

                //  cashDiscountApiTest(searchTextValue);
                return true;


                //  return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = newText;

                if (Globals.checkInternet(ItemPurchasedByListOfCustomersActivity.this)) {
                    callApi(startDate, endDate);
                }
                return true;
            }
        });


    }


    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(ItemPurchasedByListOfCustomersActivity.this, binding.toolbarItemDashBoard.filterView);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu_pending_order, popupMenu.getMenu());

        // Set a menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // Handle menu item clicks
                switch (menuItem.getItemId()) {

                    case R.id.menuAToz:
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        searchTextValue = "";
                        return true;
                    case R.id.menuZtoA:
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        searchTextValue = "";
                        return true;

                    case R.id.menuAmountDesc:
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        searchTextValue = "";
                        return true;

                    case R.id.menuAmountAsc:
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        searchTextValue = "";

                        return true;

                    case R.id.menuAllFilter:
                        filterByAmount = "";
                        filterByName = "";
                        searchTextValue = "";
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


    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Desc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Asc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Clear All filter", R.drawable.ic_filter_black, false)) // aad an item list.
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
        powerMenu.showAsDropDown(binding.toolbarItemDashBoard.filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {

                    case 0:
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        searchTextValue = "";

                        break;
                    case 1:
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        searchTextValue = "";

                        break;
                    case 2:
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        searchTextValue = "";
                        break;
                    case 3:
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        searchTextValue = "";
                        break;
                    case 4:
                        filterByAmount = "";
                        filterByName = "";
                        searchTextValue = "";
                        break;

                }
                pageNo = 1;
                callApi(startDate, endDate);
                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    /***shubh****/
    WebView dialogWeb;
    String url;


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title = getString(R.string.share_customer_list);


        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ItemPurchasedByListOfCustomersActivity.this);
        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedByListOfCustomersActivity.this);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

        progressDialog.dismiss();
        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.share_customer_list);

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

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedByListOfCustomersActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedByListOfCustomersActivity.this, webView, f, fileName, new PdfView.Callback() {

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
                ItemPurchasedByListOfCustomersActivity.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedByListOfCustomersActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedByListOfCustomersActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedByListOfCustomersActivity.this, webView, f, fileName, new PdfView.Callback() {

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
                ItemPurchasedByListOfCustomersActivity.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedByListOfCustomersActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

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
                ItemPurchasedByListOfCustomersActivity.this,

                getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(ItemPurchasedByListOfCustomersActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(ItemPurchasedByListOfCustomersActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(ItemPurchasedByListOfCustomersActivity.this, webView, f, fileName, new PdfView.Callback() {

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


    private void callApi(String fromDate, String toDate) {
        binding.loader.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("ItemCode", id);
                hde.put("FromDate", fromDate);
                hde.put("ToDate", toDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("Zone", zoneCode);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);

                Call<ResponseItemInvoices> call;

                if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedByListOfCustomersActivity.this).getItemInvoicesPurchase(hde);
                }else {
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedByListOfCustomersActivity.this).getItemInvoices(hde);
                }
                try {
                    Response<ResponseItemInvoices> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    binding.loader.setVisibility(View.GONE);
                                    if (response.body().getData().size() > 0) ;
                                    //  setData(response.body().getData().get(0));
                                    AllitemsList.clear();
                                    if (response.body().getData().isEmpty()) {
                                    } else {
                                        AllitemsList.addAll(response.body().getData().get(0).getSaleOrder());
                                        binding.salesvalue.setText("\u20B9 " + Globals.numberToK((response.body().getData().get(0).getTotalPrice())));
                                        adapter = new CustomerPurchasedItemAdapter(ItemPurchasedByListOfCustomersActivity.this, AllitemsList, response.body().getData().get(0).itemCode, startDate, endDate);
                                        adapter.AllData(AllitemsList);

                                        adapter.notifyDataSetChanged();
                                        binding.rvItemDash.setLayoutManager(layoutManager);
                                        binding.rvItemDash.setAdapter(adapter);
                                    }


                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                        binding.loader.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    public void hideToolbarMenu() {
        getSupportActionBar().hide();


        binding.toolbarItemDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.ivInfoView.setImageResource(R.drawable.ic_baseline_share_24);
        binding.toolbarItemDashBoard.filterView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.search.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.newQuatos.setVisibility(View.GONE);


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
                // from_to_date.setText(startDate + " - " + endDate);
                binding.loader.setVisibility(View.VISIBLE);
                startDateReverse = Globals.convertDateFormat(startDate);
                endDateReverse = Globals.convertDateFormat(endDate);
                if (startDate.isEmpty())
                    binding.tvDateFrom.setText(" All ");
                else
                    binding.tvDateFrom.setText(startDateReverse + " To " + endDateReverse);
                pageNo = 1;
                callApi(startDate, endDate);
                url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            startDateReverse = Globals.convertDateFormat(startDate);
            endDateReverse = Globals.convertDateFormat(endDate);
            binding.tvDateFrom.setText(startDateReverse + " - " + endDateReverse);
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi(startDate, endDate);
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
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
                binding.loader.setVisibility(View.VISIBLE);
                pageNo = 1;
                callApi(startDate, endDate);
                url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;
                binding.tvDateFrom.setText(bindingBottom.tvLastYearBottomSheetSelectDate.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });
        bindingBottom.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            callApi("", "");
            url = Globals.itemSaleReportsPdf + id + "&FromDate=" + startDate + "&ToDate=" + endDate;

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
                binding.loader.setVisibility(View.VISIBLE);
                pageNo++;
                callPageBases(startDate, endDate);

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


    int pageNo = 1;

    private void callPageBases(String fromDate, String toDate) {
        Log.e("TAG====>", "callApi: " + searchTextValue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("ItemCode", id);
                hde.put("FromDate", fromDate);
                hde.put("ToDate", toDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("Zone", zoneCode);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);


                Call<ResponseItemInvoices> call;

                if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedByListOfCustomersActivity.this).getItemInvoicesPurchase(hde);
                }else {
                    call = NewApiClient.getInstance().getApiService(ItemPurchasedByListOfCustomersActivity.this).getItemInvoices(hde);
                }
                try {
                    Response<ResponseItemInvoices> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.loader.setVisibility(View.GONE);
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    //  setData(response.body().getData().get(0));
                                    AllitemsList.addAll(response.body().getData().get(0).getSaleOrder());

                                    adapter.notifyDataSetChanged();


                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                        binding.loader.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


}