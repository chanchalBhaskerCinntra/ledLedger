package com.cinntra.ledure.activities;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

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
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CustomersWiseLedger;
import com.cinntra.ledure.activities.MainActivity_B2C;
import com.cinntra.ledure.activities.PendingOrders;
import com.cinntra.ledure.adapters.LedgerCustomersAdapter;
import com.cinntra.ledure.databinding.ActivityLedgerCustomerBinding;
import com.cinntra.ledure.databinding.ActivityPartyBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.FragmentPartyBinding;
import com.cinntra.ledure.globals.Globals;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPartyBinding binding;

    LinearLayoutManager layoutManager;
    int skipSize = 20;
    int pageSize = 100;
    int pageNo = 1;
    /***shubh****/
    WebView dialogWeb;
    String url;
    private String searchTextValue = "";
    private String Zones = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPartyBinding.inflate(getLayoutInflater());

//        ButterKnife.bind(this);

        setContentView(binding.getRoot());

        getSupportActionBar().hide();

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);*/

        url = Globals.allCustomerPdfUrl + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=";
        Log.e("PDF URL===>:", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + url);
        binding.title.setVisibility(View.GONE);
        Zones = getIntent().getStringExtra("Zones");

        setDefaults();

        eventSearchManager();

    }


    private void eventSearchManager() {
        binding.quoteHeader.searchView.setBackgroundColor(Color.parseColor("#00000000"));
        binding.quoteHeader.searchLay.setVisibility(View.VISIBLE);
        binding.quoteHeader.searchView.setVisibility(View.VISIBLE);
        binding.quoteHeader.relativeInfoView.setVisibility(View.INVISIBLE);
        binding.quoteHeader.relativeCalView.setVisibility(View.INVISIBLE);
        binding.quoteHeader.filterView.setVisibility(View.VISIBLE);
        if (Prefs.getString(Globals.Role, "").equalsIgnoreCase("admin")) {
            binding.quoteHeader.sharePdf.setVisibility(View.VISIBLE);
        } else {
            binding.quoteHeader.sharePdf.setVisibility(View.GONE);
        }

        binding.quoteHeader.ivSharePdf.setVisibility(View.VISIBLE);
        binding.quoteHeader.searchLay.setOnClickListener(view -> {

            binding.quoteHeader.searchView.setFocusable(true);
        });

        binding.quoteHeader.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                itemOnOnePage(searchTextValue);
                binding.quoteHeader.searchLay.setVisibility(View.GONE);
                return false;
            }
        });

        binding.quoteHeader.sharePdf.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        binding.quoteHeader.filterView.setOnClickListener(view -> {
            // openpopup();
            openNewPopUpCustomize();
        });
        // ledgerImage.setImageResource(R.drawable.ic_ledger);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                //AllitemsList
//                pageNo = 1;
//                searchTextValue = query;
//
//                if (!searchTextValue.isEmpty())
//                    itemOnOnePage(searchTextValue);
//
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (adapter != null) {
//                    adapter.filter(newText);
//                }
//              /* if(!newText.isEmpty()&&Globals.autoCheck())
//               {
//                   itemOnOnePage(newText);
//               }*/
//                return false;
//            }
//        });


        SearchViewUtils.setupSearchView(binding.quoteHeader.searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("NEWSHUBH", "onQueryTextSubmit: " + query);
                binding.quoteHeader.searchView.clearFocus();
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
                binding.quoteHeader.searchView.setIconifiedByDefault(true);
                binding.quoteHeader.searchView.setFocusable(true);
                binding.quoteHeader.searchView.setIconified(false);
                binding.quoteHeader.searchView.requestFocusFromTouch();
                binding.quoteHeader.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = newText;
                if (!searchTextValue.isEmpty())
                    itemOnOnePage(searchTextValue);

            }
        });


    }


    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PartyActivity.this);
        final ProgressDialog progressDialog = new ProgressDialog(PartyActivity.this);
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

        final ProgressDialog progressDialog = new ProgressDialog(PartyActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(this, webView, f, fileName, new PdfView.Callback() {

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
                PartyActivity.this, getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(PartyActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

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

        final ProgressDialog progressDialog = new ProgressDialog(PartyActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(PartyActivity.this, webView, f, fileName, new PdfView.Callback() {

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
                PartyActivity.this, PartyActivity.this.
                        getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(PartyActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(PartyActivity.this, " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }
    }


    /***shubh****/
    private void otherShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                PartyActivity.this, PartyActivity.this.
                        getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(PartyActivity.this, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, apkURI);


        if (intent.resolveActivity(PartyActivity.this.getPackageManager()) != null) {
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

        final ProgressDialog progressDialog = new ProgressDialog(PartyActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(PartyActivity.this, webView, f, fileName, new PdfView.Callback() {

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

        PowerMenu powerMenu = new PowerMenu.Builder(PartyActivity.this)
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(PartyActivity.this, R.color.black))
                .setTextGravity(Gravity.START)
                .setTextSize(12)
                .setTextTypeface(Typeface.createFromAsset(PartyActivity.this.getAssets(), "poppins_regular.ttf"))
                .setSelectedTextColor(Color.BLACK)
                .setWidth(Globals.pxFromDp(PartyActivity.this, 220f))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(PartyActivity.this, R.color.colorPrimary))
                .build();
        powerMenu.showAsDropDown(binding.quoteHeader.filterView);


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


    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(PartyActivity.this, binding.quoteHeader.filterView);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu_pending_order, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menuAmountAsc).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menuAmountDesc).setVisible(false);
        popupMenu.getMenu().findItem(R.id.menuAllFilter).setVisible(false);

        // Set a menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // Handle menu item clicks
                switch (menuItem.getItemId()) {

                    case R.id.menuAToz:
                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                        pageNo = 1;
                        AllitemsList.clear();
                        orderByName="a-z";
                        itemOnOnePage("");
                        adapter.notifyDataSetChanged();
                        return true;
                    case R.id.menuZtoA:
                        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
                        pageNo = 1;
                        AllitemsList.clear();
                        orderByName="z-a";
                        itemOnOnePage("");

                        adapter.notifyDataSetChanged();
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
        binding.quoteHeader.backPress.setOnClickListener(this);
        binding.quoteHeader.search.setOnClickListener(this);
        binding.quoteHeader.back.setVisibility(View.GONE);


        binding.quoteHeader.newQuatos.setVisibility(View.GONE);
        //  loader.setVisibility(View.VISIBLE);
        //   binding.loader.setVisibility(View.VISIBLE);
        AllitemsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(PartyActivity.this, RecyclerView.VERTICAL, false);
        binding.fragmentCustomer.customerLeadList.setLayoutManager(layoutManager);
        adapter = new LedgerCustomersAdapter(PartyActivity.this, AllitemsList);
        binding.fragmentCustomer.customerLeadList.setAdapter(adapter);
        binding.fragmentCustomer.customerLeadList.addOnScrollListener(scrollListener);


        if (Globals.checkInternet(PartyActivity.this)) {
            // loader.setVisibility(View.VISIBLE);
            binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            AllitemsList.clear();
            itemOnOnePage("");
            // callApi(binding.fragmentCustomer.loaderCustomer.loader);
            // callApi(loader);
        }

        binding.fragmentCustomer.swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(PartyActivity.this)) {
                    pageNo = 1;
                    AllitemsList.clear();
                    orderByName = "";
                    // if(searchView.getVisibility()==View.GONE)
                    if (binding.quoteHeader.searchLay.getVisibility() == View.VISIBLE)
                        searchTextValue = binding.quoteHeader.searchView.getQuery().toString();
                    else {
                        binding.quoteHeader.searchView.setQuery("", false);

                        searchTextValue = "";

                    }
                    itemOnOnePage(searchTextValue);
                    //   callApi(binding.fragmentCustomer.loaderCustomer.loader);

                } else
                    binding.fragmentCustomer.swipeRefreshLayout.setRefreshing(false);

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_press:

                //  PartyActivity.this.finish();
                break;
            case R.id.search:
                binding.quoteHeader.mainHeaderLay.setVisibility(View.GONE);
                binding.quoteHeader.searchLay.setVisibility(View.VISIBLE);

                binding.quoteHeader.searchView.setIconifiedByDefault(true);
                binding.quoteHeader.searchView.setFocusable(true);
                binding.quoteHeader.searchView.setIconified(false);
                binding.quoteHeader.searchView.requestFocusFromTouch();

                break;

        }
    }

 /*   public boolean onBackPressed() {
        if (mainHeaderLay.getVisibility() == View.GONE) {
            searchLay.setVisibility(View.GONE);
            searchView.setQuery("", false);
            mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            PartyActivity.this.finish();
         //   Toast.makeText(PartyActivity.this, "fragment else", Toast.LENGTH_SHORT).show();
        }
        // Add your logic here
        //  Toast.makeText(getActivity(), "backPressed....", Toast.LENGTH_SHORT).show();
        return true; // Return true if you've handled the event, otherwise return false
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    ArrayList<BusinessPartnerData> AllitemsList;
    LedgerCustomersAdapter adapter;

    String orderByName = "";

    private void itemOnOnePage(String searchValue) {
        binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.VISIBLE);
        pageNo = 1;

        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchValue);
        hde.put("Zones", Zones);
        hde.put("OrderByName", orderByName);


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
                        binding.fragmentCustomer.noDatafound.setVisibility(View.VISIBLE);
                    }
                    binding.fragmentCustomer.swipeRefreshLayout.setRefreshing(false);

                    if (adapter != null && adapter.getItemCount() > 0)
                        binding.fragmentCustomer.noDatafound.setVisibility(View.GONE);
                    //  adapter.notifyDataSetChanged();


//                if(response.body().getValue().size()==0){
//                    dataoverFromAPI = false;
//                    pageNo++;
//                }
                    binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                } else if (response.code() == 404) {
                    Toast.makeText(PartyActivity.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(PartyActivity.this, "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PartyActivity.this, "An unknown error occurred", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                Toast.makeText(PartyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        hde.put("OrderByName", orderByName);
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
                    Toast.makeText(PartyActivity.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(PartyActivity.this, "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PartyActivity.this, "An unknown error occurred", Toast.LENGTH_SHORT).show();
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