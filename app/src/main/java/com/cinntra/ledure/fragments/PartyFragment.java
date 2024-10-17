package com.cinntra.ledure.fragments;




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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.adapters.LedgerCustomersAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.FragmentPartyBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.globals.SessionManagement;
import com.cinntra.ledure.model.AttachmentModel;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
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


public class PartyFragment extends Fragment implements View.OnClickListener {
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

    FragmentPartyBinding binding;

    LinearLayoutManager layoutManager;
    int skipSize = 20;
    int pageSize = 100;
    int pageNo = 1;
    /***shubh****/
    WebView dialogWeb;
    String url;
    private String searchTextValue = "";
    private String Zones = "";
    private String cardType = Globals.cardTypeCCustomer;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Objects.requireNonNull(((MainActivity_B2C) requireActivity()).getSupportActionBar()).hide();
        getActivity().findViewById(R.id.app_bar).setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_party, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentPartyBinding.bind(view);
        url = Globals.allCustomerPdfUrl + Prefs.getString(Globals.SalesEmployeeCode, "") + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&SearchText=";
        Log.e("PDF URL===>:", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + url);
        title.setVisibility(View.GONE);
        Zones = requireActivity().getIntent().getStringExtra("Zones");
        Prefs.putBoolean(Globals.ISPURCHASE, false);
        setDefaults();
        sessionManagement=new SessionManagement(getActivity());
        callAttachmentAllApi();

        eventSearchManager();
    }

    String salePurchaseGroupType = "";

    private void eventSearchManager() {
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        relativeInfoView.setVisibility(View.GONE);
        relativeCalView.setVisibility(View.GONE);
        //todo sale purchase option is disable according to client

        if (Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("28") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("21") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("32") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("31") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("-1")) {
            binding.quoteHeader.salesAndPurchaseLayout.setVisibility(View.VISIBLE);
        } else {
            binding.quoteHeader.salesAndPurchaseLayout.setVisibility(View.INVISIBLE);
        }


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


        //todo set zone --
        if (Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("admin") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Director") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Accounts")) {
            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.data_type, // Replace with your item array resource
                    R.layout.spinner_white_textview);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.quoteHeader.typeDropdown.setAdapter(spinnerArrayAdapter);
        } else {

            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.data_type, // Replace with your item array resource
                    R.layout.spinner_white_textview);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.quoteHeader.typeDropdown.setAdapter(spinnerArrayAdapter);
        }

        binding.quoteHeader.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                salePurchaseGroupType = binding.quoteHeader.typeDropdown.getSelectedItem().toString();
                pageNo = 1;
                searchTextValue = "";
                binding.quoteHeader.searchView.setQuery("", false);
                if (salePurchaseGroupType.equals("Sales")) {
                    Prefs.putBoolean(Globals.ISPURCHASE, false);
                    cardType = Globals.cardTypeCCustomer;
                    itemOnOnePage("");

                } else {
                    Prefs.putBoolean(Globals.ISPURCHASE, true);
                    //todo add login for sale purchase
                    cardType = Globals.cardTypeCSupplier;
                    itemOnOnePage("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.quoteHeader.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.quoteHeader.mainHeaderLay.setVisibility(View.VISIBLE);
                binding.quoteHeader.searchLay.setVisibility(View.GONE);

                return false;
            }
        });

        share_pdf.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

 /*       searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchLay.setVisibility(View.GONE);
                relativeInfoView.setVisibility(View.VISIBLE);
                relativeCalView.setVisibility(View.VISIBLE);
                binding.quoteHeader.salesAndPurchaseLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });*/

        filterView.setOnClickListener(view -> {
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
                else if (searchTextValue.isEmpty()) {
                    itemOnOnePage("");

                }

            }
        });


    }


    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding bindingBottom;
        bindingBottom = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());

        //todo pdf
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            url = Globals.allCustomerPdfUrlPurchase + Prefs.getString(Globals.SalesEmployeeCode, "") +"&CardType="+cardType;
        } else {
            url = Globals.allCustomerPdfUrl + Prefs.getString(Globals.SalesEmployeeCode, "") +"&CardType="+cardType;

        }

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

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(requireActivity(), webView, f, fileName, new PdfView.Callback() {

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
                requireContext(),
                requireActivity().getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(requireActivity(), webView, f, fileName, new PdfView.Callback() {

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
                requireContext(), requireActivity().
                        getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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
            if (isAppInstalled("com.whatsapp"))
                share.setPackage("com.whatsapp");
            else if (isAppInstalled("com.whatsapp.w4b"))
                share.setPackage("com.whatsapp.w4b");

            startActivity(share);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }
    }


    /***shubh****/
    private void otherShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                requireContext(), requireActivity().
                        getApplicationContext()
                        .getPackageName() + ".FileProvider", file);


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

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(requireActivity(), webView, f, fileName, new PdfView.Callback() {

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

        PowerMenu powerMenu = new PowerMenu.Builder(requireContext())
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                .setTextGravity(Gravity.START)
                .setTextSize(12)
                .setTextTypeface(Typeface.createFromAsset(requireActivity().getAssets(), "poppins_regular.ttf"))
                .setSelectedTextColor(Color.BLACK)
                .setWidth(Globals.pxFromDp(requireContext(), 220f))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
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


    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), filterView);

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
                      /*  Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s1.compareToIgnoreCase(s2);

                            }

                        });
                        adapter.notifyDataSetChanged();*/

                        pageNo = 1;
                        AllitemsList.clear();
                        orderByName = "a-z";
                        itemOnOnePage("");
                        adapter.notifyDataSetChanged();
                        return true;
                    case R.id.menuZtoA:
                 /*       Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s2.compareToIgnoreCase(s1);
                            }

                        });*/
                        pageNo = 1;
                        AllitemsList.clear();
                        orderByName = "z-a";
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
        //   head_title.setText(getResources().getString(R.string.customers));
        back_press.setOnClickListener(this);
        search.setOnClickListener(this);
        binding.quoteHeader.back.setVisibility(View.GONE);


        new_quatos.setVisibility(View.GONE);
        //  loader.setVisibility(View.VISIBLE);
        //   binding.loader.setVisibility(View.VISIBLE);
        AllitemsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        customer_lead_List.setLayoutManager(layoutManager);
        adapter = new LedgerCustomersAdapter(requireContext(), AllitemsList);
        customer_lead_List.setAdapter(adapter);
        customer_lead_List.addOnScrollListener(scrollListener);


        if (Globals.checkInternet(requireContext())) {
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

                if (Globals.checkInternet(requireContext())) {
                    pageNo = 1;
                    AllitemsList.clear();
                    orderByName = "";
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

                //  requireActivity().finish();
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

 /*   public boolean onBackPressed() {
        if (mainHeaderLay.getVisibility() == View.GONE) {
            searchLay.setVisibility(View.GONE);
            searchView.setQuery("", false);
            mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            requireActivity().finish();
         //   Toast.makeText(requireContext(), "fragment else", Toast.LENGTH_SHORT).show();
        }
        // Add your logic here
        //  Toast.makeText(getActivity(), "backPressed....", Toast.LENGTH_SHORT).show();
        return true; // Return true if you've handled the event, otherwise return false
    }*/


    public boolean onBackPressed() {
        getActivity().onBackPressed();
        getParentFragmentManager().popBackStack();
        return true; // Return true if you've handled the event, otherwise return false
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
        hde.put(Globals.payLoadOrderByName, orderByName);
        hde.put("Zones", Zones);
        hde.put(Globals.payLoadCardType, cardType);


        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).getAllBusinessPartnerWithPagination(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {


                if (response.body().getStatus()==200) {
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
                    Toast.makeText(requireContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(requireContext(), "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(), "An unknown error occurred", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                binding.fragmentCustomer.loaderCustomer.loader.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("api_failure--->", t.getMessage());
            }
        });
    }

    SessionManagement sessionManagement;
    private static final String TAG = "PartyFragment";

    private void callAttachmentAllApi(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
     /*   jsonObject.addProperty("LinkID", Prefs.getString(Globals.MyID, ""));
        jsonObject.addProperty("LinkType", "ProfilePic");*/
//        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(getActivity()).getAllAttachment(jsonObject);
        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(getActivity()).getNewAllAttachmentApi(jsonObject);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: "+response.body().getMessage() );

                        if (response.body().getStatus() == 200){

                            if (response.body().getData().size() > 0) {
                                String filePath = Globals.ImageURL + response.body().getData().get(0).getProfileImage();

                                if (filePath != null) {
                             /*   Glide.with(getActivity())
                                        .load(filePath)
                                        .into(binding.proImg);*/
                                } else {
                                    // binding.proImg.setImageResource(R.drawable.ic_profileicon);
                                }

                            }

                        }
                        else if (response.body().getStatus() == 401) {
                            Toast.makeText(getActivity(), "Session Expired, Please Login Again", Toast.LENGTH_SHORT).show();

                            Prefs.clear();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            getActivity().finish();
                            sessionManagement.ClearSession();
                        }


                    } else if (response.code() == 201) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
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
        hde.put(Globals.payLoadOrderByName, orderByName);
        hde.put(Globals.payLoadCardType, cardType);
        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).getAllBusinessPartnerWithPagination(hde);
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
                    Toast.makeText(requireContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 201) {
                    Toast.makeText(requireContext(), "Something Missing", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(), "An unknown error occurred", Toast.LENGTH_SHORT).show();
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
            requireActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }
}