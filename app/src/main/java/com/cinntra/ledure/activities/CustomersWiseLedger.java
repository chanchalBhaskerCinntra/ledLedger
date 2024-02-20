package com.cinntra.ledure.activities;


import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.LedgerCustomersAdapter;
import com.cinntra.ledure.databinding.ActivityLedgerCustomerBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomersWiseLedger extends MainBaseActivity implements View.OnClickListener {

    @BindView(R.id.head_title)
    TextView head_title;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.filterView)
    RelativeLayout filterView;
    @BindView(R.id.filter)
    ImageView ledgerImage;

    @BindView(R.id.relativeInfoView)
    RelativeLayout relativeInfoView;

    @BindView(R.id.relativeCalView)
    RelativeLayout relativeCalView;
    @BindView(R.id.share_pdf)
    RelativeLayout share_pdf;
    @BindView(R.id.iv_share_pdf)
    ImageView iv_share_pdf;

    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.customer_lead_List)
    RecyclerView customer_lead_List;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    ActivityLedgerCustomerBinding binding;

    LinearLayoutManager layoutManager;
    int skipSize = 20;
    int pageSize = 100;
    int pageNo = 1;
    /***shubh****/
    WebView dialogWeb;
    String url;
    private String searchTextValue = "";
    private String Zones = "";

    //private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLedgerCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        url = Globals.allCustomerPdfUrl + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=";
        Log.e("PDF URL===>:", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + url);
        title.setVisibility(View.GONE);
        Zones = getIntent().getStringExtra("Zones");

        setDefaults();

        eventSearchManager();


    }

    @Override
    public void onBackPressed() {
        if (mainHeaderLay.getVisibility() == View.GONE) {
            searchLay.setVisibility(View.GONE);
            searchView.setQuery("", false);
            mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void eventSearchManager() {
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        relativeInfoView.setVisibility(View.INVISIBLE);
        relativeCalView.setVisibility(View.INVISIBLE);
        filterView.setVisibility(View.VISIBLE);
        if (Prefs.getString(Globals.Role, "").equalsIgnoreCase("admin")) {
            share_pdf.setVisibility(View.VISIBLE);
        } else {
            share_pdf.setVisibility(View.GONE);
        }

        iv_share_pdf.setVisibility(View.VISIBLE);
        searchLay.setOnClickListener(view -> {

            searchView.setFocusable(true);
        });

        share_pdf.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        filterView.setOnClickListener(view -> {
            openpopup();
        });



        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("NEWSHUBH", "onQueryTextSubmit: " + query);
                searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = query;

                if (!searchTextValue.isEmpty())
                    itemOnOnePage(searchTextValue);
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                // Perform API call or any other action with the newText
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = newText;
                if (!searchTextValue.isEmpty())
                    itemOnOnePage(searchTextValue);

            }
        });


    }


    /***shubh****/
    private void showBottomSheetDialog()
       {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(CustomersWiseLedger.this);
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

        final ProgressDialog progressDialog = new ProgressDialog(CustomersWiseLedger.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CustomersWiseLedger.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(CustomersWiseLedger.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CustomersWiseLedger.this, webView, f, fileName, new PdfView.Callback() {

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

        final ProgressDialog progressDialog = new ProgressDialog(CustomersWiseLedger.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(CustomersWiseLedger.this, webView, f, fileName, new PdfView.Callback() {

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


    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
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
        powerMenu.showAsDropDown(filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {
                    /*case 0:
                    Prefs.putString(Globals.BussinessPageType,"DashBoard");
                    Prefs.putString(Globals.AddBp,"");
                    startActivity(new Intent(MainActivity_B2C.this, AddBPCustomer.class));
                    break;*/
                    case 0:
                        Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s1.compareToIgnoreCase(s2);

                            }

                        });
                        adapter.notifyDataSetChanged();

                        break;
                    case 1:
                        Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s2.compareToIgnoreCase(s1);
                            }

                        });
                        adapter.notifyDataSetChanged();

                        break;


                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
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
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                pageNo++;
                itemOnPageBasis(pageNo);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDefaults() {
        binding.quoteHeader.headTitle.setText(getResources().getString(R.string.customers));
        //   head_title.setText(getResources().getString(R.string.customers));
        back_press.setOnClickListener(this);
        search.setOnClickListener(this);


        new_quatos.setVisibility(View.GONE);
        //  loader.setVisibility(View.VISIBLE);
        //   binding.loader.setVisibility(View.VISIBLE);
        AllitemsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(CustomersWiseLedger.this, RecyclerView.VERTICAL, false);
        customer_lead_List.setLayoutManager(layoutManager);
        adapter = new LedgerCustomersAdapter(CustomersWiseLedger.this, AllitemsList);
        customer_lead_List.setAdapter(adapter);
        customer_lead_List.addOnScrollListener(scrollListener);


        if (Globals.checkInternet(CustomersWiseLedger.this)) {
            // loader.setVisibility(View.VISIBLE);
            binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            AllitemsList.clear();
            itemOnOnePage("");
            // callApi(binding.fragmentCustomer.loaderCustomer.loader);
            // callApi(loader);
        }

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(CustomersWiseLedger.this)) {
                    pageNo = 1;
                    AllitemsList.clear();
                    // if(searchView.getVisibility()==View.GONE)
                    if (searchLay.getVisibility() == View.VISIBLE)
                        searchTextValue = searchView.getQuery().toString();
                    else {
                        searchView.setQuery("", false);

                        searchTextValue = "";

                    }
                    itemOnOnePage(searchTextValue);
                    //   callApi(binding.fragmentCustomer.loaderCustomer.loader);

                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_press:

                finish();
                break;
            case R.id.search:
                mainHeaderLay.setVisibility(View.GONE);
                searchLay.setVisibility(View.VISIBLE);

                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                break;

        }
    }

    ArrayList<BusinessPartnerData> AllitemsList;
    LedgerCustomersAdapter adapter;


    private void itemOnOnePage(String searchValue) {
        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
        pageNo = 1;

        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchValue);
        hde.put("Zones", Zones);


        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getAllBusinessPartnerWithPagination(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {


                if (response.body().getStatus() == 200) {
                    if (response.body().getData().size() > 0) {

                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);

                        AllitemsList.clear();
                        AllitemsList.addAll(response.body().getData());
                        adapter.AllData(AllitemsList);

                        adapter.notifyDataSetChanged();


                    } else {
                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                        no_datafound.setVisibility(View.VISIBLE);
                    }
                    swipeRefreshLayout.setRefreshing(false);

                    if (adapter != null && adapter.getItemCount() > 0)
                        no_datafound.setVisibility(View.GONE);
                    //  adapter.notifyDataSetChanged();


//                if(response.body().getValue().size()==0){
//                    dataoverFromAPI = false;
//                    pageNo++;
//                }
                    binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                } else if (response.code() == 404) {
                    Toast.makeText(CustomersWiseLedger.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(CustomersWiseLedger.this, "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CustomersWiseLedger.this, "An unknown error occurred", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                Toast.makeText(CustomersWiseLedger.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("api_failure--->", t.getMessage());
            }
        });
    }


    private void itemOnPageBasis(int pageSize) {


        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put("PageNo", String.valueOf(pageSize));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchTextValue);
        hde.put("Zones", Zones);
        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getAllBusinessPartnerWithPagination(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {

                if (response.code() == 200) {
                    AllitemsList.addAll(response.body().getData());
                    adapter.AllData(AllitemsList);
//                layoutManager = new LinearLayoutManager(ItemsList.this, RecyclerView.VERTICAL, false);
//                itemsRecycler.setLayoutManager(layoutManager);
//                adapter = new ItemsAdapter(ItemsList.this, AllitemsList);
//                itemsRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

//                adapter.notifyDataSetChanged();
                    if (response.body().getData().size() == 0) {
                        // dataoverFromAPI = false;
                        pageNo++;
                    }
                    binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);

                } else if (response.code() == 404) {
                    Toast.makeText(CustomersWiseLedger.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(CustomersWiseLedger.this, "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CustomersWiseLedger.this, "An unknown error occurred", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
            }
        });
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