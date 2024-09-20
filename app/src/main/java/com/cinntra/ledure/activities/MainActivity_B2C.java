package com.cinntra.ledure.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BPDropdownAdapter;
import com.cinntra.ledure.adapters.ContactPersonAdapter;
import com.cinntra.ledure.adapters.LeadDropDownAdapter;
import com.cinntra.ledure.adapters.RecentAdapter;
import com.cinntra.ledure.adapters.RecentOrderAdapter;
import com.cinntra.ledure.adapters.YoutubeLisAdapter;
import com.cinntra.ledure.adapters.ZonesAdapter;
import com.cinntra.ledure.databinding.ActivityMainB2CBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowCustomerDataBinding;
import com.cinntra.ledure.databinding.BottomSheetZonesBinding;
import com.cinntra.ledure.databinding.CheckOutExpenseDialogBinding;
import com.cinntra.ledure.databinding.CheckinDialogBinding;
import com.cinntra.ledure.fragments.Dashboard;
import com.cinntra.ledure.fragments.DashboardFragmentFromActivity;
import com.cinntra.ledure.fragments.Graph_;
import com.cinntra.ledure.fragments.ItemsBottomFragment;
import com.cinntra.ledure.fragments.MoreViewsFragmnet;
import com.cinntra.ledure.fragments.PartyFragment;
import com.cinntra.ledure.fragments.PurchaseFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.globals.SessionManagement;
import com.cinntra.ledure.model.AttachmentModel;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ContactPerson;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.CounterResponse;
import com.cinntra.ledure.model.CountryResponse;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.DashboardCounterData;
import com.cinntra.ledure.model.DashboardCounterResponse;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.ExpenseDataModel;
import com.cinntra.ledure.model.ExpenseResponse;
import com.cinntra.ledure.model.GraphModel;
import com.cinntra.ledure.model.LeadFilter;
import com.cinntra.ledure.model.MapData;
import com.cinntra.ledure.model.MapResponse;
import com.cinntra.ledure.model.NewEvent;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.ReceiptCustomerBusinessRes;
import com.cinntra.ledure.model.ReceivableResponse;
import com.cinntra.ledure.model.ResponseTripCheckIn;
import com.cinntra.ledure.model.ResponseTripCheckOut;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.SalesGraphResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.newapimodel.ResponsePayMentDueCounter;
import com.cinntra.ledure.newapimodel.ResponseReceivableGraph;
import com.cinntra.ledure.spinner.SpinnerLocal;
import com.cinntra.ledure.viewModel.CustomerViewModel;
import com.cinntra.ledure.viewpager.GraphPagerAdapter;
import com.cinntra.ledure.viewpager.GraphPagerPurchaseAdapter;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import ng.max.slideview.SlideView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_B2C extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private static final int PICK_FILE_REQUEST_CODE = 11111;
    private static final int PICK_FILE_CHECKOUT_REQUEST_CODE = 14321;
    private static final int REQUEST_IMAGE_CAPTURE = 10111;
    String totalAmount = "0";
    String pendingAmount = "0";
    String locationtype;
    LocationManager locationManager;
    double latitude;
    double longitude;
    LeadDropDownAdapter leadDropDownAdapter;
    FusedLocationProviderClient client;

    String bpType = "";
    String bpFullName = "";
    String bpCardCode = "";
    File imageFile;


    private ActivityMainB2CBinding binding;
    RecentAdapter adapter;
    YoutubeLisAdapter youtubeadapter;
    String dateSelectorValue = "";

    BarChart volumeReportChart;
    ArrayList<String> youtubelist = new ArrayList<>();
    ArrayList<LeadValue> leadValueList = new ArrayList<>();
    String bpName = "";
    String bpId = "";
    private String picturePath = "";
    private String checkOUtPicturePath = "";
    private String cameraImagePath = "";
    private Uri cameraCaptureUri;

    CheckinDialogBinding checkinDialogBinding;


    // GraphViewPagerAdapter fakeLiveMatchAdapter;
    GraphPagerAdapter graphPagerAdapter;
    GraphPagerPurchaseAdapter graphPagerPurchaseAdapter;
    public static View dateSPinner;
    ArrayAdapter<CharSequence> dateSpinnerAdapter;

    String dataTypeValue = "";

    private List<SpinnerLocal> employeeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainB2CBinding.inflate(getLayoutInflater());
        ButterKnife.bind(this);
        setContentView(binding.getRoot());

        Prefs.putBoolean(Globals.ISPURCHASE, false);
        String reverse = Globals.convertDateFormat("2023-03-01");
        Log.e("REVERSE", "onCreate: " + reverse);
        Globals.CURRENT_CLASS = getClass().getName();
        dateSPinner = binding.contentData.dateSelector;
        leadDropDownAdapter = new LeadDropDownAdapter(MainActivity_B2C.this, leadValueList);
        client = LocationServices.getFusedLocationProviderClient(MainActivity_B2C.this);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        binding.contentData.loader.loader.setVisibility(View.GONE);
        binding.appBar.setVisibility(View.VISIBLE);
        FloatingActionButton fab = binding.fab;
        volumeReportChart = binding.contentData.anyChartView;
        binding.lineartopToolbar.setVisibility(View.GONE);
        // Create an ArrayAdapter using a custom layout for the dropdown items

        sessionManagement = new SessionManagement(MainActivity_B2C.this);

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog_alert)
                .setCancelable(false);


        alertDialog = builder.create();


        //todo setting textview for previosu and final year chart
        binding.contentData.tvCurrentYearHeading.setText(Globals.getCurrentFinancialYear());
        binding.contentData.tvPreviousYearHeading.setText(Globals.getPreviousFinancialYear());

        dateSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_selector, // Replace with your item array resource
                R.layout.spinner_textview_dashboard);


        //todo set sales and purchase drop down value---
        ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(MainActivity_B2C.this,
                R.array.data_type, // Replace with your item array resource
                R.layout.spinner_white_textview);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeDropdown.setAdapter(spinnerArrayAdapter);


        //todo set graph spinner logic---
        // Initialize your list of objects
        employeeList = new ArrayList<>();
        employeeList.add(new SpinnerLocal("John Doe", "E001"));
        employeeList.add(new SpinnerLocal("Jane Smith", "E002"));
        employeeList.add(new SpinnerLocal("Sam Wilson", "E003"));

        // Set up the ArrayAdapter with the list of objects
        ArrayAdapter<SpinnerLocal> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_white_textview,
                employeeList
        );

        // Set the dropdown layout for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        binding.contentData.spinnerGraphYearSelection.setAdapter(adapter);

        // Handle item selection event
        binding.contentData.spinnerGraphYearSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected Employee object
                SpinnerLocal selectedEmployee = (SpinnerLocal) parent.getItemAtPosition(position);
                //  Toast.makeText(MainActivity_B2C.this, "Selected: " + selectedEmployee.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action
            }
        });


        dataTypeValue = binding.typeDropdown.getSelectedItem().toString();

        Log.e(TAG, "onCreate: " + dataTypeValue);
        Log.e(TAG, "CheckPefOn--: " + Prefs.getString(Globals.IS_SALE_OR_PURCHASE, ""));


        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("")) {
            Prefs.putString(Globals.IS_SALE_OR_PURCHASE, binding.typeDropdown.getSelectedItem().toString());
        }

        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            binding.typeDropdown.setSelection(0);

            callDashboardCounter();
            callDashboardCounter_Receiable();

            callPaymentDueCounter();
            callAllDueCounter();


        } else {
            binding.typeDropdown.setSelection(1);

            callDashboardPurchaseCounter();
            callDashboardPurchaseCounter_Receiable();

            callAllPurchaseDueCounter();
            callPurchasePaymentDueCounter();
        }


        //todo sale purchase option is disable according to client
        if (Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("28") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("21") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("32") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("31") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("-1")) {
            binding.typeDropdown.setVisibility(View.VISIBLE);
        } else {
            binding.typeDropdown.setVisibility(View.INVISIBLE);
        }

        binding.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Prefs.putString(Globals.IS_SALE_OR_PURCHASE, binding.typeDropdown.getSelectedItem().toString());

                Log.e(TAG, "onItemSelected: " + Prefs.getString(Globals.IS_SALE_OR_PURCHASE, ""));
                if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                    binding.contentData.salesIncludeDashboardLayout.salesIncludeDashboard.setVisibility(View.GONE);
                    binding.contentData.purchaseIncludeDashboardLayout.purchaseIncludeDashboard.setVisibility(View.VISIBLE);
                    Prefs.putBoolean(Globals.ISPURCHASE, true);
                    saleGraphApi();
                    callDashboardPurchaseCounter();
                    callDashboardPurchaseCounter_Receiable();

                    callAllPurchaseDueCounter();
                    callPurchasePaymentDueCounter();


                } else {
                    saleGraphApi();
                    binding.contentData.salesIncludeDashboardLayout.salesIncludeDashboard.setVisibility(View.VISIBLE);
                    binding.contentData.purchaseIncludeDashboardLayout.purchaseIncludeDashboard.setVisibility(View.GONE);
                    Prefs.putBoolean(Globals.ISPURCHASE, false);
                    callDashboardCounter();
                    callDashboardCounter_Receiable();

                    callPaymentDueCounter();
                    callAllDueCounter();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Prefs.putString(Globals.IS_SALE_OR_PURCHASE, binding.typeDropdown.getSelectedItem().toString());
            }
        });


//        saleGraphApi();


        binding.tvClientName.setText("Welcome " + Prefs.getString(Globals.SalesEmployeeName, ""));

        dateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.contentData.dateSelector.setAdapter(dateSpinnerAdapter);


        binding.zoneLayout.setOnClickListener(view -> {
            showBottomSheetDialog();
        });


        if (Prefs.getString(Globals.CHECK_IN_STATUS, "").equalsIgnoreCase("Start")) {

            MainBaseActivity.boolean_permission = true;
        }

     /*   if (MainBaseActivity.boolean_permission) {
            Log.e("start", "start");
            getLatitudeLongitudeForCheckIn();
               // openremarksdialog();
        } else {
            givepermission();
        }*/


        binding.contentData.linearDate.setOnClickListener(bind -> {
            showDateInDashboardBottomSheetDialog(this);
        });


        if (Prefs.getString(Globals.CHECK_IN_STATUS, "").equalsIgnoreCase("Start")) {
            binding.toggleCheckIN.setChecked(true);
            binding.headingcheckIn.setText("Check Out");
            binding.slideView.setText(" Check Out");
            MainBaseActivity.boolean_permission = true;
        } else {
            binding.toggleCheckIN.setChecked(false);
            binding.headingcheckIn.setText("Check In");
            binding.slideView.setText(" Check In");
        }

        if (Prefs.getString(Globals.EXPENSE_STATUS, "").equalsIgnoreCase("Start")) {
            binding.slideExpense.setText(" Start");
        } else {
            binding.slideExpense.setText(" Stop");
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        dashboardcounter();

        callCountryApi();


        binding.proImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_B2C.this, ProfileActivity.class);
                startActivity(i);
            }
        });


        BottomNavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setSelectedItemId(R.id.home);
        // navigationView.setBackground(null);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.home: {
                        fragment = new DashboardFragmentFromActivity();
                        break;
                    }

                    case R.id.partyBottomMenu:
                        fragment = new PartyFragment();
                        break;


                  /*  case R.id.purchaseBottomMenu:
                        Prefs.putBoolean(Globals.ISPURCHASE, true);
                        fragment = new PurchaseFragment();

                        break;*/

                    case R.id.reportBottomMenu:
                        fragment = new Graph_();
                        break;

                    case R.id.itemListDashboardMenu:
                        fragment = new ItemsBottomFragment();
                        break;

                    case R.id.settings:
                        fragment = new MoreViewsFragmnet();

                }
                return loadFragment(fragment);
            }
        });

        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // Prevent reselection by doing nothing when an item is reselected
            }
        });


       /* youtubelist.add("HfkyXuZdD_c");
        youtubelist.add("O87EFe1-M4I");
        youtubelist.add("cUIbkjyuJzU");


        youtubeadapter = new YoutubeLisAdapter(this, youtubelist);

        //   binding.contentData.featurerecyvlerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        //  binding.contentData.featurerecyvlerview.setAdapter(youtubeadapter);

        youtubeadapter.notifyDataSetChanged();*/

        binding.quickCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpopup();
//                startActivity(new Intent(MainActivity_B2C.this,Reports.class));
            }
        });


        opengraph();


        binding.datePicker.setVisibility(View.GONE);
        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dateRangeSelector();
            }
        });


        binding.contentData.salesIncludeDashboardLayout.pendingOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_B2C.this, PendingOrders.class));

            }
        });


        binding.contentData.salesIncludeDashboardLayout.receivedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "ReceiptLedger");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.salesIncludeDashboardLayout.salesAmountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                startActivity(new Intent(MainActivity_B2C.this, Sale_Inovice_Reports.class));

            }
        });


        binding.contentData.salesIncludeDashboardLayout.paymentCollectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "Receivable");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.salesIncludeDashboardLayout.newtypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "payment");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.salesIncludeDashboardLayout.overDuecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "overDue");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.purchaseIncludeDashboardLayout.pendingOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_B2C.this, PendingOrders.class));
            }
        });


        binding.contentData.purchaseIncludeDashboardLayout.receivedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "ReceiptLedger");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.purchaseIncludeDashboardLayout.salesAmountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                startActivity(new Intent(MainActivity_B2C.this, Sale_Inovice_Reports.class));

            }
        });

        binding.contentData.purchaseIncludeDashboardLayout.paymentCollectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "Receivable");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.purchaseIncludeDashboardLayout.newtypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "payment");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });


        binding.contentData.purchaseIncludeDashboardLayout.overDuecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "overDue");
                startActivity(new Intent(MainActivity_B2C.this, Reports.class));

            }
        });

        binding.contentData.dateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dateSelectorValue = binding.contentData.dateSelector.getSelectedItem().toString().trim();
                //selectDate(dateSelectorValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Prefs.putString(Globals.IS_SALE_OR_PURCHASE, binding.typeDropdown.getSelectedItem().toString());

                Log.e(TAG, "onItemSelected: " + Prefs.getString(Globals.IS_SALE_OR_PURCHASE, ""));
                if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                    binding.contentData.salesIncludeDashboardLayout.salesIncludeDashboard.setVisibility(View.GONE);
                    binding.contentData.purchaseIncludeDashboardLayout.purchaseIncludeDashboard.setVisibility(View.VISIBLE);
                    Prefs.putBoolean(Globals.ISPURCHASE, true);
                    callDashboardPurchaseCounter();
                    callDashboardPurchaseCounter_Receiable();

                    callAllPurchaseDueCounter();
                    callPurchasePaymentDueCounter();
                    saleGraphApi();
                } else {
                    binding.contentData.salesIncludeDashboardLayout.salesIncludeDashboard.setVisibility(View.VISIBLE);
                    binding.contentData.purchaseIncludeDashboardLayout.purchaseIncludeDashboard.setVisibility(View.GONE);
                    Prefs.putBoolean(Globals.ISPURCHASE, false);
                    callDashboardCounter();
                    callDashboardCounter_Receiable();

                    callPaymentDueCounter();
                    callAllDueCounter();

                    saleGraphApi();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Prefs.putString(Globals.IS_SALE_OR_PURCHASE, binding.typeDropdown.getSelectedItem().toString());
            }
        });


    }


    private void callDashboardCounter_Receiable() {
        alertDialog.show();

        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross"); //Net
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(this).getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();

                    binding.contentData.salesIncludeDashboardLayout.totalCollection.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                    binding.contentData.salesIncludeDashboardLayout.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalPendingSales())));


                } else {
                    alertDialog.dismiss();

                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }

    private void callDashboardCounter() {
        alertDialog.show();

        HashMap<String, String> obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross"); //Net
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(this).getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    setCounter(response.body().getData().get(0));


                    //  lead_spiner.setAdapter(leadAdapter);
                    //  leadAdapter.notifyDataSetChanged();
                } else {
                    alertDialog.dismiss();

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }


    //todo calling purcahse dasboard counter api here---

    private void callDashboardPurchaseCounter_Receiable() {
        alertDialog.show();

        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross"); //Net
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        obj.put("SearchText", "");
        obj.put("OrderByName", "");
        obj.put("OrderByAmt", "");
        obj.put("PageNo", "");
        obj.put("MaxSize", "");
        obj.put("DueDaysGroup", "");
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(this).getDashBoardCounterForPurchaseLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();

                    binding.contentData.purchaseIncludeDashboardLayout.totalCollection.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                    binding.contentData.purchaseIncludeDashboardLayout.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalPendingSales())));


                } else {
                    alertDialog.dismiss();

                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }

    private void callDashboardPurchaseCounter() {
        alertDialog.show();

        HashMap<String, String> obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross"); //Net
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        obj.put("SearchText", "");
        obj.put("OrderByName", "");
        obj.put("OrderByAmt", "");
        obj.put("PageNo", "");
        obj.put("MaxSize", "");
        obj.put("DueDaysGroup", "");


        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(this).getDashBoardCounterForPurchaseLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    setCounter(response.body().getData().get(0));

                    //  lead_spiner.setAdapter(leadAdapter);
                    //  leadAdapter.notifyDataSetChanged();
                } else {
                    alertDialog.dismiss();

                    Globals.ErrorMessage(MainActivity_B2C.this, response.errorBody().toString());
               /*     Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }*/
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }


    private void setCounter(DashboardCounterData data) {
        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            binding.contentData.salesIncludeDashboardLayout.totalAmnt.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalSalesWithoutCreditNote())));
            binding.contentData.salesIncludeDashboardLayout.receivedAmountValue.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalReceivePayment())));
            binding.contentData.salesIncludeDashboardLayout.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalPendingSales())));
        } else {
            binding.contentData.purchaseIncludeDashboardLayout.totalAmnt.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalSalesWithoutCreditNote())));
            binding.contentData.purchaseIncludeDashboardLayout.receivedAmountValue.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalReceivePayment())));
            binding.contentData.purchaseIncludeDashboardLayout.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalPendingSales())));
        }

    }


    private void callleadDialogApi(Spinner lead_spiner) {
        LeadFilter lv = new LeadFilter();
        lv.setAssignedTo(Prefs.getString(Globals.MyID, ""));
        lv.setLeadType("All");
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService(this).getAllLead(lv);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if (response.code() == 200) {
                    leadValueList.clear();
                    leadValueList.addAll(response.body().getData());
                    List<String> itemNames = new ArrayList<>();
                    List<String> itemCode = new ArrayList<>();
                    for (LeadValue item : leadValueList) {
                        itemNames.add(item.getCompanyName());
                    }

                    for (LeadValue item : leadValueList) {
                        //    itemCode.add(item.getCardCode());
                    }

                    //  LeadDropDownAdapter leadAdapter = new LeadDropDownAdapter(MainActivity_B2C.this, leadValueList);
                    lead_spiner.setAdapter(new ArrayAdapter<>(MainActivity_B2C.this, android.R.layout.simple_spinner_item, itemNames));

                    lead_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            bpFullName = leadValueList.get(i).getCompanyName();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    //  lead_spiner.setAdapter(leadAdapter);
                    //  leadAdapter.notifyDataSetChanged();
                } else {

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }

    private void callleadApi(Spinner bp_spinner, Spinner cp_spinner) {
        LeadFilter lv = new LeadFilter();
        lv.setAssignedTo(Prefs.getString(Globals.MyID, ""));
        lv.setLeadType("All");
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService(this).getAllLead(lv);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if (response.code() == 200) {
                    leadValueList.clear();
                    leadValueList.addAll(response.body().getData());
                    LeadDropDownAdapter leadAdapter = new LeadDropDownAdapter(MainActivity_B2C.this, leadValueList);

                    leadDropDownAdapter.notifyDataSetChanged();
                } else {

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        callBplistApi(bp_spinner, cp_spinner);
    }

    ArrayList<BusinessPartnerData> AllitemsList = new ArrayList<>();

    private void callBplistApi(Spinner bp_spinner, Spinner cp_spinner) {
        CustomerViewModel model = ViewModelProviders.of(MainActivity_B2C.this).get(CustomerViewModel.class);
        model.getCustomersList(binding.contentData.loader.loader).observe(MainActivity_B2C.this, new Observer<List<BusinessPartnerData>>() {
            @Override
            public void onChanged(@Nullable List<BusinessPartnerData> itemsList) {

                if (itemsList.size() >= 0) {
                    AllitemsList.clear();
                    AllitemsList.addAll(itemsList);
                    List<String> itemNames = new ArrayList<>();
                    List<String> itemCode = new ArrayList<>();
                    for (BusinessPartnerData item : AllitemsList) {
                        itemNames.add(item.getCardName());
                    }

                    for (BusinessPartnerData item : AllitemsList) {
                        itemCode.add(item.getCardCode());
                    }


                    //  bp_spinner.setAdapter(new BPDropdownAdapter(MainActivity_B2C.this, AllitemsList));
                    bp_spinner.setAdapter(new ArrayAdapter<>(MainActivity_B2C.this, android.R.layout.simple_spinner_item, itemNames));
                    bp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            bpId = AllitemsList.get(i).getCardCode();
                            bpCardCode = AllitemsList.get(i).getCardCode();
                            bpFullName = AllitemsList.get(i).getCardName();

                            //  callcontactpersonApi(cp_spinner,filterwithoutprospect(AllitemsList).get(i).getCardCode());
                            callcontactpersonApi(cp_spinner, AllitemsList.get(i).getCardCode());
                            bpReSourceID = AllitemsList.get(i).getCardCode();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
            }

        });

    }


    double startlat;
    double startlong;
    double distanceMAP;


    private void callApi(double latitude, double longitude, MapData mapData, String address) {
        mapData.setLat(String.valueOf(latitude));
        mapData.setLong(String.valueOf(longitude));
        mapData.setUpdateDate(Globals.getTodaysDatervrsfrmt());
        mapData.setUpdateTime(Globals.getTCurrentTime());
        mapData.setAddress(address);
        mapData.setShape("meeting");
        mapData.setType(locationtype);
        mapData.setEmp_Id(Prefs.getString(Globals.MyID, "1"));
        mapData.setEmp_Name(Prefs.getString(Globals.Employee_Name, ""));
        Call<MapResponse> call = NewApiClient.getInstance().getApiService(this).sendMaplatlong(mapData);

        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    if (response.body().getValue().size() > 0) {


                        startlat = Double.parseDouble(response.body().getValue().get(0).getLat());
                        startlong = Double.parseDouble(response.body().getValue().get(0).getLong());

                        distanceMAP = distance(startlat, startlong, latitude, longitude);
                    }

                    if (locationtype.equalsIgnoreCase("Start")) {
                        if (Prefs.getString(Globals.CHECK_IN_STATUS, "CheckOut").equalsIgnoreCase("Stop")) {
                            Prefs.putString(Globals.CHECK_IN_STATUS, "Start");

                            binding.slideView.setText(" Check Out");
                        } else {
                            Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");

                            binding.slideView.setText(" Check In");
                        }
                        Log.e("success", "success");
                    } else {
                        Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");

                        binding.slideView.setText(" Check In");
                        //  openExpenseDialog();
                    }


                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });
    }


    private void callExpenseTravelApi(double latitude, double longitude, MapData mapData, String address) {
        mapData.setLat(String.valueOf(latitude));
        mapData.setLong(String.valueOf(longitude));
        mapData.setUpdateDate(Globals.getTodaysDatervrsfrmt());
        mapData.setUpdateTime(Globals.getTCurrentTime());
        mapData.setAddress(address);
        mapData.setShape("travel");
        mapData.setType(locationtype);
        mapData.setEmp_Id(Prefs.getString(Globals.MyID, "1"));
        mapData.setEmp_Name(Prefs.getString(Globals.Employee_Name, ""));
        Call<MapResponse> call = NewApiClient.getInstance().getApiService(this).sendMaplatlong(mapData);

        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    if (response.body().getValue().size() > 0) {


                        startlat = Double.parseDouble(response.body().getValue().get(0).getLat());
                        startlong = Double.parseDouble(response.body().getValue().get(0).getLong());

                        distanceMAP = distance(startlat, startlong, latitude, longitude);
                    }

                    if (Prefs.getString(Globals.EXPENSE_STATUS, "").equalsIgnoreCase("Stop")) {
                        Prefs.putString(Globals.EXPENSE_STATUS, "Start");

                        binding.slideExpense.setText("Trip Stop");

                    } else {
                        Prefs.putString(Globals.EXPENSE_STATUS, "Stop");

                        binding.slideExpense.setText("Trip Start");
                    }


                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });
    }


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;


    private void showDateInDashboardBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bindingDate = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingDate.getRoot());
        bindingDate.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {

            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingDate.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            alertDialog.show();
            binding.contentData.tvDateText.setText("Today");

            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();

                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();

                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            alertDialog.show();
            binding.contentData.tvDateText.setText("Yesterday");
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();

                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();

                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            alertDialog.show();
            binding.contentData.tvDateText.setText("This Week");
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();

                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();

                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            alertDialog.show();
            binding.contentData.tvDateText.setText("This Month");
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();

                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();

                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }
            bottomSheetDialog.dismiss();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();

                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();

                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();
                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();
                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();
                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();
                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();
                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();
                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });

        //todo on visibility
        bindingDate.tvLastYearTillDateBottomSheetSelectDate.setVisibility(View.VISIBLE);

        bindingDate.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.getCurrentDateInLastFinancialYear();


            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();
                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();
                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view ->
        {
            startDate = "";
            endDate = "";
            alertDialog.show();
            binding.contentData.tvDateText.setText("All");
            if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
                callDashboardCounter();
                callDashboardCounter_Receiable();
                callPaymentDueCounter();
                callAllDueCounter();
            } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                callDashboardPurchaseCounter();
                callDashboardPurchaseCounter_Receiable();
                callAllPurchaseDueCounter();
                callPurchasePaymentDueCounter();
            }

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

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

                // binding.loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                alertDialog.show();
                binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                        + Globals.convertDateFormatInReadableFormat(endDate));
                if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {

                    callDashboardCounter();
                    callDashboardCounter_Receiable();

                    callPaymentDueCounter();
                    callAllDueCounter();
                } else if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase")) {
                    callDashboardPurchaseCounter();
                    callDashboardPurchaseCounter_Receiable();

                    callAllPurchaseDueCounter();
                    callPurchasePaymentDueCounter();
                }


            }
        });


    }


    private void callAllDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "-1");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService(this).getPaymentDueCounter(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.salesIncludeDashboardLayout.tvOverDueCounter.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


//                    Gson gson = new GsonBuilder().create();
//                    LeadResponse mError = new LeadResponse();
//                    try {
//                        String s = response.errorBody().string();
//                        mError = gson.fromJson(s, LeadResponse.class);
//                    } catch (IOException e) {
//                        //handle failure to read error
//                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callPaymentDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "7");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService(this).getPaymentDueCounter(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.salesIncludeDashboardLayout.tvAnjaliDuePayment.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


//                    Gson gson = new GsonBuilder().create();
//                    LeadResponse mError = new LeadResponse();
//                    try {
//                        String s = response.errorBody().string();
//                        mError = gson.fromJson(s, LeadResponse.class);
//                    } catch (IOException e) {
//                        //handle failure to read error
//                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callAllPurchaseDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "-1");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService(this).getPurchasePaymentDueCounter(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.purchaseIncludeDashboardLayout.tvOverDueCounter.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


//                    Gson gson = new GsonBuilder().create();
//                    LeadResponse mError = new LeadResponse();
//                    try {
//                        String s = response.errorBody().string();
//                        mError = gson.fromJson(s, LeadResponse.class);
//                    } catch (IOException e) {
//                        //handle failure to read error
//                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callPurchasePaymentDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "7");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService(this).getPurchasePaymentDueCounter(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.purchaseIncludeDashboardLayout.tvAnjaliDuePayment.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


//                    Gson gson = new GsonBuilder().create();
//                    LeadResponse mError = new LeadResponse();
//                    try {
//                        String s = response.errorBody().string();
//                        mError = gson.fromJson(s, LeadResponse.class);
//                    } catch (IOException e) {
//                        //handle failure to read error
//                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static final String TAG = "MainActivity_B2C";
    private static final int REQUEST_CODE_PERMISSIONS = 2;
    private static int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private static int RESULT_LOAD_IMAGE = 101;
    private static int PICTURE_FROM_CAMERA = 1;
    String attachID = "";

    SessionManagement sessionManagement;

    private void callAttachmentAllApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
     /*   jsonObject.addProperty("LinkID", Prefs.getString(Globals.MyID, ""));
        jsonObject.addProperty("LinkType", "ProfilePic");*/
//        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(this).getAllAttachment(jsonObject);
        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(this).getNewAllAttachmentApi(jsonObject);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: " + response.body().getMessage());

                        if (response.body().getStatus() == 200) {
                            if (response.body().getData().size() > 0) {
                                attachID = response.body().getData().get(0).getId();
                                String filePath = Globals.ImageURL + response.body().getData().get(0).getProfileImage();

                                if (response.body().getData().get(0).getProfileImage().isEmpty()) {
                                    getPictureDialog();
                                   /* if (checkAndRequestPermissions()) {
                                    } else {
                                        ActivityCompat.requestPermissions(MainActivity_B2C.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
                                    }*/
                                } else {
                                    if (filePath != null || !filePath.isEmpty()) {
                                        Glide.with(MainActivity_B2C.this)
                                                .load(filePath)
                                                .into(binding.proImg);
                                    }
                                }


                            } else {
                                Toast.makeText(MainActivity_B2C.this, response.message(), Toast.LENGTH_SHORT).show();

                            }

                        } else if (response.body().getStatus() == 401) {
                            Toast.makeText(MainActivity_B2C.this, "Session Expired, Please Login Again", Toast.LENGTH_SHORT).show();

                            Prefs.clear();
                            Intent intent = new Intent(MainActivity_B2C.this, Login.class);
                            startActivity(intent);
                            finish();
                            sessionManagement.ClearSession();
                        } else {
                            Toast.makeText(MainActivity_B2C.this, response.message(), Toast.LENGTH_SHORT).show();

                        }

                    } else if (response.code() == 201) {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void getPictureDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setWindowAnimations(R.style.AnimationsForDailog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.upload_profile_picture_layout);
        dialog.getWindow().getAttributes().width = ActionBar.LayoutParams.FILL_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.canceldialog);
        ImageView gallery = dialog.findViewById(R.id.gallerySelect);
        ImageView camera = dialog.findViewById(R.id.cameraSelect);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    dialog.dismiss();
                } else {
                    ActivityCompat.requestPermissions(MainActivity_B2C.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
                }

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    captureImageFromCamera();

                    dialog.dismiss();
                } else {
                    ActivityCompat.requestPermissions(MainActivity_B2C.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picturePath.isEmpty()) {
                    Toast.makeText(MainActivity_B2C.this, "Kindly choose image! ", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }


    //todo code for camera picture click---
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PICTURE_FROM_CAMERA);
    }


    private void callUploadImage() {
        File imageFile = new File(picturePath);


        Log.e("filePath>>>>>", "onCreate: " + picturePath);
        Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part File = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);

        RequestBody SalesEmployeeCode = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.SalesEmployeeCode, ""));

       /* RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), attachID);
        RequestBody LinkType = RequestBody.create(MediaType.parse("multipart/form-data"), "ProfilePic");
        RequestBody LinkID = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.MyID, ""));
        RequestBody Caption = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody CreateDate = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTodaysDatervrsfrmt());
        RequestBody CreateTime = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTCurrentTime());
        RequestBody UpdateDate = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody UpdateTime = RequestBody.create(MediaType.parse("multipart/form-data"), "");


        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(this).uploadProfileAttachment(File, id, LinkType, LinkID, Caption, CreateDate, CreateTime, UpdateDate, UpdateTime);*/

        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(this).uploadNewProfileAttachment(File, SalesEmployeeCode);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.body().getStatus() == 200) {
                        Toast.makeText(MainActivity_B2C.this, "Successful", Toast.LENGTH_SHORT).show();

                        callAttachmentAllApi();
                    } else if (response.code() == 201) {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private Random random = new Random();

    private File saveBitmap(Bitmap bmp) {
        File extStorageDirectory = this.getCacheDir();
        OutputStream outStream = null;
        int num = random.nextInt(90) + 10;
        Log.e("extStorageDirectory---", extStorageDirectory.toString());
        File file = new File(extStorageDirectory, "temp" + num + ".png");

        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp" + num + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            if (outStream != null) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.e("file---", file.toString());
        return file;
    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        );
        int write = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int read = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty() && camera != 0) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }


    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("in fragment on request", "Permission callback called-------");

        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                    // Process the normal flow
                    // Else any one or both the permissions are not granted
                } else {
                    Log.d("in fragment on request", "Some permissions are not granted ask again");

                    // Permission is denied (this is the first time when "never ask again" is not checked),
                    // so ask again explaining the usage of permission.
                    // shouldShowRequestPermissionRationale will return true
                    // Show the dialog or Snackbar saying it's necessary and try again, otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialogOK("Camera and Storage Permission required for this app", (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    checkAndRequestPermissions();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Do nothing or handle accordingly
                                    break;
                            }
                        });
                    } else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        // Proceed with logic by disabling the related features or quit the app.
                        // You might want to show a dialog to the user here to inform them about the necessity of these permissions.
                    }
                }
            }
        }


    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity_B2C.this);
        progressDialog.setMessage("Please wait");


        BottomSheetZonesBinding bindingBottom;
        bindingBottom = BottomSheetZonesBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());
        String zones_all[] = {};
        String zones[] = Prefs.getString(Globals.ZONE, "").split(",");


        int aLen = zones_all.length;
        int bLen = zones.length;
        String[] result = new String[aLen + bLen];

        System.arraycopy(zones_all, 0, result, 0, aLen);
        System.arraycopy(zones, 0, result, aLen, bLen);


        ZonesAdapter adapter = new ZonesAdapter(this, result, "");
        bindingBottom.zonesList.setAdapter(adapter);

        bottomSheetDialog.show();
        //    bindingBottom.headingBottomSheetShareReport.setText(R.string.share_customer_list);
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


    }


    boolean bottomNav = false;

    private void setupChartViewPgaer() {
        graphPagerAdapter = new GraphPagerAdapter(this);
        binding.contentData.viewPagerChart.setAdapter(graphPagerAdapter);
        binding.contentData.tabLayout.setupWithViewPager(binding.contentData.viewPagerChart, true);

        

        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            binding.contentData.tabLayout.getTabAt(0).setText("Sales");
            binding.contentData.tabLayout.getTabAt(1).setText("Receipt");
            binding.contentData.tabLayout.getTabAt(2).setText("Receivables");
        /*binding.contentData.tabLayout.addTab(binding.contentData.tabLayout.newTab().setText("Sales"));
        binding.contentData.tabLayout.addTab(binding.contentData.tabLayout.newTab().setText("Receipt"));
        binding.contentData.tabLayout.addTab(binding.contentData.tabLayout.newTab().setText("Receivables"));*/
            binding.contentData.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        binding.contentData.topHeadingGraph.setText("Sales");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.VISIBLE);
//                    binding.contentData.tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    } else if (tab.getPosition() == 1) {
                        binding.contentData.topHeadingGraph.setText("Receipt");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.VISIBLE);
//                    binding.contentData.tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    } else if (tab.getPosition() == 2) {
                        binding.contentData.topHeadingGraph.setText("Receivables\n From Billing Date");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.GONE);
//                    binding.contentData.tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else { // if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Purchase"))
            graphPagerPurchaseAdapter = new GraphPagerPurchaseAdapter(this);
            binding.contentData.viewPagerChart.setAdapter(graphPagerPurchaseAdapter);
            binding.contentData.tabLayout.setupWithViewPager(binding.contentData.viewPagerChart, true);
            binding.contentData.tabLayout.getTabAt(0).setText("Purchase");
            binding.contentData.tabLayout.getTabAt(1).setText("Payment");
            binding.contentData.tabLayout.getTabAt(2).setText("Payable");

            binding.contentData.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        binding.contentData.topHeadingGraph.setText("Purchase");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.VISIBLE);
                    } else if (tab.getPosition() == 1) {
                        binding.contentData.topHeadingGraph.setText("Payment");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.VISIBLE);
                    } else if (tab.getPosition() == 2) {
                        binding.contentData.topHeadingGraph.setText("Payable");
                        binding.contentData.linearGraphRepresentator.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


        // binding.contentData.ta
    }

    String sourceType = "";
    String selectsourceType = "";
    String leadReSourceID = "0";
    String prospectbpReSourceID = "";
    String bpReSourceID = "0";
    String bpContactPerson = "";
    String prospectContactPerson = "";
    Dialog dialog;
    String expenseString = "";
    String stringModeTypeOfTransport = "";
    String costSt = "";
    String distanceSt = "";
    String expenseRemarkSt = "";
    String attchmentName = "";
    String attachentUri = "";
    String attachmentcheckOut = "";
    EditText attachmentName;
    EditText etNewAttachemnt;


    boolean toggleDefaultChecker = false;

    private void opencheckinDialog() {
        MapData mapData = new MapData();

        String userType = Prefs.getString(Globals.Role, "admin");
        int transportType = -1;

        checkinDialogBinding = CheckinDialogBinding.inflate(getLayoutInflater());


        dialog = new Dialog(MainActivity_B2C.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        dialog.setContentView(checkinDialogBinding.getRoot());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        Button add = dialog.findViewById(R.id.add);
        Button btnChooseFileCheckIn = dialog.findViewById(R.id.btnChooseFIleCheckIN);
        etNewAttachemnt = dialog.findViewById(R.id.etAttachmentsNameCHeckIN);
        Button createLead = dialog.findViewById(R.id.btnCreateLead);
        LinearLayout lead_view = dialog.findViewById(R.id.lead_view);
        LinearLayout lead_view_shubh = dialog.findViewById(R.id.lead_view_shubh);
        LinearLayout linearBusinessTab = dialog.findViewById(R.id.linearBusiness);
        LinearLayout linearExpenseTab = dialog.findViewById(R.id.linearExpense);
        RadioGroup radioGroup = dialog.findViewById(R.id.rgChekIn);
        RadioButton rbBusiness = dialog.findViewById(R.id.rbBusiness);
        RadioButton rbExpense = dialog.findViewById(R.id.rbExpense);
        LinearLayout newBusinessView = dialog.findViewById(R.id.newBusinessView);
        LinearLayout prospect_view = dialog.findViewById(R.id.prospect_view);
        LinearLayout exist_view = dialog.findViewById(R.id.exist_view);
        LinearLayout tendor_view = dialog.findViewById(R.id.linearTendor);
        LinearLayout lead_add = dialog.findViewById(R.id.lead_add);
        EditText comment_value = dialog.findViewById(R.id.comment_value);
        EditText cost = dialog.findViewById(R.id.etCostOfExpense);

        Spinner bp_spinner = dialog.findViewById(R.id.bp_spinner);
        Spinner spinnerModeTransport = dialog.findViewById(R.id.spinnerModeOfTransport);
        Spinner lead_spinner = dialog.findViewById(R.id.lead_spinner);
        Spinner cp_spinner = dialog.findViewById(R.id.cp_spinner);
        Spinner prospectcp_spinner = dialog.findViewById(R.id.prospectcp_spinner);
        Spinner type_spinner = dialog.findViewById(R.id.type_spinner);
        Spinner select_spinner = dialog.findViewById(R.id.select_spinner);
        Spinner prospectbp_spinner = dialog.findViewById(R.id.prospectbp_spinner);

        Spinner expenseType = dialog.findViewById(R.id.spinnerTypeOfExpense);
        EditText expenseRemark = dialog.findViewById(R.id.etExpenseRemark);
        attachmentName = dialog.findViewById(R.id.etAttachmentsName);
        EditText distance = dialog.findViewById(R.id.etDistanceOfExpense);

        TextView contactPerson = dialog.findViewById(R.id.cp_text);

        Button btnChooseFile = dialog.findViewById(R.id.btnChooseFIle);
        lead_view_shubh.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> expenseAdapter;
        ArrayAdapter<CharSequence> modeOfTransportAdapter;


        lead_view_shubh.setVisibility(View.GONE);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                toggleDefaultChecker = true;
                binding.toggleCheckIN.setChecked(false);
            }
        });

        if (userType.equalsIgnoreCase("Executive")) {
            transportType = R.array.executive_mode_of_transport;
        } else if (userType.equalsIgnoreCase("Senior Executive") || userType.equalsIgnoreCase("Assistant Manager")) {
            transportType = R.array.senior_executive_and_assistent_Manager_mode_of_transport;
        } else if (userType.equalsIgnoreCase("Manager") || userType.equalsIgnoreCase("Sr. Manager")) {
            transportType = R.array.manager_and_senior_manager_mode_of_transport;

        } else if (userType.equalsIgnoreCase("Assistant General Manager")) {
            transportType = R.array.assistent_general_manager_mode_of_transport;


        } else if (userType.equalsIgnoreCase("General Manager") || userType.equalsIgnoreCase("Vice President")) {
            transportType = R.array.general_manager_and_vice_president_mode_of_transport;

        } else {
            transportType = R.array.executive_mode_of_transport;
        }

        modeOfTransportAdapter = ArrayAdapter.createFromResource(MainActivity_B2C.this, transportType, android.R.layout.simple_spinner_item);
        modeOfTransportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModeTransport.setAdapter(modeOfTransportAdapter);


        expenseAdapter = ArrayAdapter.createFromResource(MainActivity_B2C.this, R.array.type_of_expense_list, android.R.layout.simple_spinner_item);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(expenseAdapter);

        getLatitudeLongitudeForCheckIn();


        btnChooseFileCheckIn.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });

        checkinDialogBinding.etAttachmentsNameCHeckIN.setText(cameraImagePath);

        checkinDialogBinding.btnOpenCamera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbBusiness:
                        linearBusinessTab.setVisibility(View.VISIBLE);
                        linearExpenseTab.setVisibility(View.GONE);

                        break;
                    case R.id.rbExpense:
                        linearBusinessTab.setVisibility(View.GONE);
                        linearExpenseTab.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnChooseFile.setOnClickListener(view -> {
// Create an intent to open the file picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Set the file type to be picked

// Start the activity and wait for the user to pick a file
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });

        expenseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                expenseString = expenseAdapter.getItem(0).toString();

            }
        });


        spinnerModeTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stringModeTypeOfTransport = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stringModeTypeOfTransport = modeOfTransportAdapter.getItem(0).toString();

            }
        });


        lead_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLead addLead = new AddLead();
                startActivity(new Intent(MainActivity_B2C.this, addLead.getClass()));
            }
        });


        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bpType = adapterView.getItemAtPosition(i).toString();
                switch (i) {
                    case 0:
                        exist_view.setVisibility(view.VISIBLE);
                        bp_spinner.setVisibility(View.VISIBLE);
                        select_spinner.setVisibility(View.GONE);
                        lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.GONE);
                        createLead.setVisibility(View.GONE);
                        callBplistApi(bp_spinner, cp_spinner);
                        break;

                    case 1:
                        exist_view.setVisibility(view.GONE);
                        bp_spinner.setVisibility(View.GONE);
                        select_spinner.setVisibility(View.GONE);
                        //lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.VISIBLE);
                        lead_spinner.setVisibility(View.VISIBLE);
                        createLead.setVisibility(View.GONE);
                        callleadDialogApi(lead_spinner);
//                        callleadApi(bp_spinner, cp_spinner);

                        break;

                    case 2:
                    case 3:
                        exist_view.setVisibility(view.GONE);
                        bp_spinner.setVisibility(View.GONE);
                        select_spinner.setVisibility(View.GONE);
                        lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.GONE);
                        createLead.setVisibility(View.VISIBLE);

                        break;
                    case 5:
                        exist_view.setVisibility(view.GONE);
                        bp_spinner.setVisibility(View.GONE);
                        select_spinner.setVisibility(View.GONE);
                        lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.GONE);
                        createLead.setVisibility(View.GONE);
                        break;

                    case 4:
                        exist_view.setVisibility(view.GONE);
                        bp_spinner.setVisibility(View.GONE);
                        select_spinner.setVisibility(View.GONE);
                        lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        createLead.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.VISIBLE);
                        lead_spinner.setVisibility(View.VISIBLE);
                        callleadDialogApi(lead_spinner);

                        break;


                    case 6:
                        exist_view.setVisibility(view.GONE);
                        bp_spinner.setVisibility(View.GONE);
                        select_spinner.setVisibility(View.GONE);
                        lead_spinner.setVisibility(View.GONE);
                        cp_spinner.setVisibility(View.GONE);
                        newBusinessView.setVisibility(View.GONE);
                        contactPerson.setVisibility(View.GONE);
                        tendor_view.setVisibility(View.GONE);
                        lead_view_shubh.setVisibility(View.GONE);
                        createLead.setVisibility(View.GONE);

                        break;


                }

//                if (i == 0) {
//
//                    newBusinessView.setVisibility(View.VISIBLE);
//                    select_spinner.setVisibility(View.VISIBLE);
//                    lead_spinner.setVisibility(View.VISIBLE);
//                    exist_view.setVisibility(View.GONE);
//                    cp_spinner.setVisibility(View.GONE);
//                    bp_spinner.setVisibility(View.GONE);
//
//                } else {
//                    sourceType = "Business Partner";
//
//                    newBusinessView.setVisibility(View.GONE);
//                    select_spinner.setVisibility(View.GONE);
//                    lead_spinner.setVisibility(View.GONE);
//                    exist_view.setVisibility(View.VISIBLE);
//                    cp_spinner.setVisibility(View.VISIBLE);
//                    bp_spinner.setVisibility(View.VISIBLE);
//                    callBplistApi(bp_spinner, cp_spinner);
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bpType = adapterView.getItemAtPosition(0).toString();
            }
        });


        createLead.setOnClickListener(view -> {
            AddLead addLead = new AddLead();
            startActivity(new Intent(MainActivity_B2C.this, addLead.getClass()));
        });


        //lead_spinner.setAdapter(leadDropDownAdapter);


        prospectbp_spinner.setAdapter(new BPDropdownAdapter(MainActivity_B2C.this, filterprospect(AllitemsList)));

        lead_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                leadReSourceID = leadValueList.get(i).getId().toString();
                callleadDialogApi(lead_spinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        prospectbp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                callcontactpersonApi(prospectcp_spinner, filterprospect(AllitemsList).get(i).getCardCode());
                prospectbpReSourceID = filterprospect(AllitemsList).get(i).getCardCode();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                bpContactPerson = ContactEmployeesList.get(i).getFirstName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        prospectcp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                prospectContactPerson = ContactEmployeesList.get(i).getFirstName();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    selectsourceType = "Lead";
                    //   lead_view.setVisibility(View.VISIBLE);
                    lead_spinner.setVisibility(View.VISIBLE);
                    prospect_view.setVisibility(View.GONE);
                    prospectbp_spinner.setVisibility(View.GONE);
                    prospectcp_spinner.setVisibility(View.GONE);
                    callleadApi(bp_spinner, cp_spinner);

                } else {
                    selectsourceType = "Prospect";
                    lead_view.setVisibility(View.GONE);
                    lead_spinner.setVisibility(View.GONE);
                    prospect_view.setVisibility(View.VISIBLE);
                    prospectbp_spinner.setVisibility(View.VISIBLE);
                    prospectcp_spinner.setVisibility(View.VISIBLE);
                    callBplistApi(bp_spinner, cp_spinner);
                    // callBplistApi();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rbExpense.isChecked()) {
                    //   Toast.makeText(MainActivity_B2C.this, "expense", Toast.LENGTH_SHORT).show();
                    if (validation(cost, distance, attachmentName, expenseRemark, checkOutExpenseDialogBinding.etTripName)) {
                        mapData.setExpenseType(expenseString);
                        mapData.setExpenseCost(costSt);
                        mapData.setExpenseDistance(distanceSt);
                        mapData.setExpenseRemark(expenseRemarkSt);
                        mapData.setSourceType("");
                        mapData.setResourceId("");
                        mapData.setContactPerson("");
                        mapData.setExpenseAttach(attachentUri);
                        mapData.setAttach(attachentUri);
                        mapData.setRemark("");
                        mapData.setExpenseRemark(expenseRemarkSt);

                        getmyCurrentLocation(mapData);
                        dialog.dismiss();
                    }

//                    if (cost.getText().toString().isEmpty()){
//                        cost.setError("Can't Be Empty");
//                    }else if (distance.getText().toString().isEmpty()){
//                        distance.setError("Can't Be Empty");
//                    }
//                    else if (expenseRemark.getText().toString().isEmpty()){
//                        expenseRemark.setError("Can't Be Empty");
//                    }
//                    else {
//                        Toast.makeText(MainActivity_B2C.this, "inside expense", Toast.LENGTH_SHORT).show();
//
//                        mapData.setExpenseType(expenseString);
//                        mapData.setExpenseCost(costSt);
//                        mapData.setExpenseDistance(distanceSt);
//                        mapData.setExpenseRemark(expenseRemarkSt);
//                        mapData.setSourceType("");
//                        mapData.setResourceId("");
//                        mapData.setContactPerson("");
//                        mapData.setExpenseAttach("");
//                        getmyCurrentLocation(mapData);
//                        dialog.dismiss();
//                    }


                } else {
                    if (validateCheckIn(etNewAttachemnt, comment_value)) {
                        if (select_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setSourceType(selectsourceType);
                        } else {
                            mapData.setSourceType(sourceType);
                        }

                        if (lead_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setResourceId(leadReSourceID);
                        } else if (bp_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setResourceId(bpReSourceID);
                        } else if (prospectbp_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setResourceId(prospectbpReSourceID);
                        }

                        if (cp_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setContactPerson(bpContactPerson);
                        } else if (prospectbp_spinner.getVisibility() == View.VISIBLE) {
                            mapData.setContactPerson(prospectContactPerson);


                        } else {
                            mapData.setContactPerson("");
                        }


//                        mapData.setRemark(comment_value.getText().toString());
//                        mapData.setExpenseType("");
//                        mapData.setExpenseCost("");
//                        mapData.setExpenseDistance("");
//                        mapData.setExpenseRemark("");
//                        mapData.setSourceType("");
//                        mapData.setResourceId("");
//                        mapData.setContactPerson("");
//                        mapData.setExpenseAttach("");
//                        mapData.setAttach("");


                        File imageFile = new File(picturePath);
                        String curr_date = Globals.curntDate;

                        Log.e("filePath>>>>>", "onCreate: " + picturePath);
                        Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("CheckInAttach", imageFile.getName(), requestBody);
                        RequestBody bptype = RequestBody.create(MediaType.parse("multipart/form-data"), bpType);
                        RequestBody bpName = RequestBody.create(MediaType.parse("multipart/form-data"), bpFullName);
                        RequestBody cardCode = RequestBody.create(MediaType.parse("multipart/form-data"), bpCardCode);
                        RequestBody salesPersonCode = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.SalesEmployeeCode, "1"));
                        RequestBody modeOfTransport = RequestBody.create(MediaType.parse("multipart/form-data"), stringModeTypeOfTransport);
                        RequestBody checkInDate = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTodaysDatervrsfrmt());
                        RequestBody CheckInTime = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTCurrentTime());
                        RequestBody checkInLat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latAtCheckInMultipart));
                        RequestBody checkInLong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longAtCheckInMultipart));
                        RequestBody checkinRemark = RequestBody.create(MediaType.parse("multipart/form-data"), comment_value.getText().toString());


                        Prefs.putString(Globals.BP_TYPE_CHECK_IN, bpType);
                        Prefs.putString(Globals.BP_NAME_CHECK_IN, bpFullName);
                        Prefs.putDouble(Globals.START_LAT, latAtCheckInMultipart);
                        Prefs.putDouble(Globals.START_LONG, longAtCheckInMultipart);
                        Prefs.putString(Globals.START_DATE, Globals.getTodaysDate());
                        Prefs.putString(Globals.MODE_OF_TRANSPORT, stringModeTypeOfTransport);

                        callCheckInApi(imagePart, bptype, bpName, cardCode, salesPersonCode, modeOfTransport,
                                checkInDate, CheckInTime, checkInLat, checkInLong, checkinRemark);

                    } else {
                        Toasty.warning(MainActivity_B2C.this, "Please Fill document Carefully").show();
                    }
                }


            }
        });
        lead_view_shubh.setVisibility(View.GONE);
        dialog.show();
    }

    CheckOutExpenseDialogBinding checkOutExpenseDialogBinding;

    private void openChekOutDialog() {

        checkOutExpenseDialogBinding = CheckOutExpenseDialogBinding.inflate(getLayoutInflater());


        dialog = new Dialog(MainActivity_B2C.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        dialog.setContentView(checkOutExpenseDialogBinding.getRoot());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        distanceMAP = distance(Prefs.getDouble(Globals.START_LAT, 0.0), Prefs.getDouble(Globals.START_LONG, 0.0), latAtCheckInMultipart, longAtCheckInMultipart);
        Log.e("TAG", "openChekOutDialog: " + Prefs.getDouble(Globals.START_LAT, 0.0) + Prefs.getDouble(Globals.START_LONG, 0.0) + latAtCheckInMultipart + longAtCheckInMultipart);

        checkOutExpenseDialogBinding.etCustomerType.setText(Prefs.getString(Globals.BP_TYPE_CHECK_IN, ""));
        checkOutExpenseDialogBinding.etCustomerName.setText(Prefs.getString(Globals.BP_NAME_CHECK_IN, ""));
        checkOutExpenseDialogBinding.etModeOfTransport.setText(Prefs.getString(Globals.MODE_OF_TRANSPORT, ""));


        DecimalFormat df = new DecimalFormat("#.##");

        Double inMetre = distanceMAP * 1000;
        String formattedValue = df.format(distanceMAP);
        //    editTextKm.setText();
        checkOutExpenseDialogBinding.etDistanceAuto.setText(formattedValue + " KM");

        checkOutExpenseDialogBinding.btnChooseFIle.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FILE_CHECKOUT_REQUEST_CODE);
        });

        checkOutExpenseDialogBinding.etAttachmentsNameOut.setText(attachmentcheckOut);
        checkOutExpenseDialogBinding.etTripId.setText(Prefs.getString(Globals.TRIP_ID, "1"));
        getLatitudeLongitudeForCheckIn();


        checkOutExpenseDialogBinding.add.setOnClickListener(view -> {
            if (validation(checkOutExpenseDialogBinding.etCostOfExpense, checkOutExpenseDialogBinding.etDistanceKm, checkOutExpenseDialogBinding.etAttachmentsNameOut, checkOutExpenseDialogBinding.commentValue, checkOutExpenseDialogBinding.etTripName)) {
                imageFile = new File(checkOUtPicturePath);
                String curr_date = Globals.curntDate;

                Log.e("filePath>>>>>", "onCreate: " + checkOUtPicturePath);
                Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("CheckOutAttach", imageFile.getName(), requestBody);
                RequestBody bptype = RequestBody.create(MediaType.parse("multipart/form-data"), bpType);
                RequestBody distanceAuto = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(distanceMAP));
                RequestBody bpName = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.etDistanceKm.getText().toString());
                RequestBody cardCode = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.TRIP_ID, "1"));
                RequestBody salesPersonCode = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.SalesEmployeeCode, "1"));
                RequestBody modeOfTransport = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.etCostOfExpense.getText().toString());
                RequestBody checkInDate = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTodaysDatervrsfrmt());
                RequestBody CheckInTime = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTCurrentTime());
                RequestBody checkInLat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latAtCheckInMultipart));
                RequestBody checkInLong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longAtCheckInMultipart));
                RequestBody checkinRemark = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.commentValue.getText().toString());


//            callCheckOutApi(imagePart, bptype, bpName, cardCode, salesPersonCode,
//                    checkInDate, CheckInTime, checkInLat, checkInLong, checkinRemark);

                callCheckOutApi(imagePart, distanceAuto, bpName, modeOfTransport, salesPersonCode, cardCode, checkInDate, CheckInTime, checkInLat, checkInLong, checkinRemark);
                dialog.dismiss();

            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();


    }


    private boolean validation(EditText companyname, EditText full_name, EditText attachFile, EditText remarkExpense, EditText tripName) {

        if (companyname.length() == 0) {
            companyname.requestFocus();
            companyname.setError("Enter Cost");
            Toasty.warning(MainActivity_B2C.this, "Enter Cost ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (full_name.length() == 0) {
            full_name.requestFocus();
            full_name.setError("Enter Distance");
            Toasty.warning(MainActivity_B2C.this, "Enter Distance", Toast.LENGTH_SHORT).show();

            return false;
        } else if (attachFile.length() == 0) {
            attachFile.requestFocus();
            attachFile.setError("Choose File");
            Toasty.warning(MainActivity_B2C.this, "Choose File", Toast.LENGTH_SHORT).show();

            return false;
        } else if (remarkExpense.length() == 0) {
            attachFile.requestFocus();
            attachFile.setError("Enter Remark");
            Toasty.warning(MainActivity_B2C.this, "Enter Remark", Toast.LENGTH_SHORT).show();

            return false;
        } else if (tripName.length() == 0) {
            tripName.requestFocus();
            tripName.setError("Enter Trip Name");
            Toasty.warning(MainActivity_B2C.this, "Enter Trip Name", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }

    private boolean validateCheckIn(EditText attachment, EditText remarks) {

        if (attachment.length() == 0) {
            attachment.requestFocus();
            attachment.setError("Please Attach Document");
            Toasty.warning(MainActivity_B2C.this, "Please Attach Document ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (remarks.length() == 0) {
            remarks.requestFocus();
            remarks.setError("Enter Remarks");
            Toasty.warning(MainActivity_B2C.this, "Enter Remarks", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }


    private boolean validationExpense(EditText companyname, EditText full_name) {

        if (companyname.length() == 0) {
            companyname.requestFocus();
            companyname.setError("Enter Cost");
            Toasty.warning(MainActivity_B2C.this, "Enter Cost ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (full_name.length() == 0) {
            full_name.requestFocus();
            full_name.setError("Enter Trip");
            Toasty.warning(MainActivity_B2C.this, "Enter Trip Name", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }


    @SuppressLint("MissingPermission")
    private void getmyCurrentLocation(MapData mapData) {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) MainActivity_B2C.this
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }


                                String address = addresses != null ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                           /* String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();*/


                                callApi(location
                                        .getLatitude(), location
                                        .getLongitude(), mapData, address);


                            } else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();
                                        callApi(location1
                                                .getLatitude(), location1
                                                .getLongitude(), mapData, address);
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    private void callCheckInApi(MultipartBody.Part imagePart,
                                RequestBody bptype, RequestBody bpFullName,
                                RequestBody cardCode, RequestBody salesPersonCode, RequestBody modeOfTransport,
                                RequestBody checkInDate, RequestBody CheckInTime, RequestBody checkInLat, RequestBody checkInLong, RequestBody checkinRemark) {
        Call<ResponseTripCheckIn> call = NewApiClient.getInstance().getApiService(this).tripCheckIn(
                imagePart, bptype, bpFullName, cardCode, salesPersonCode, modeOfTransport, checkInDate, CheckInTime, checkInLat, checkInLong, checkinRemark
        );

        call.enqueue(new Callback<ResponseTripCheckIn>() {
            @Override
            public void onResponse(Call<ResponseTripCheckIn> call, Response<ResponseTripCheckIn> response) {
                if (response != null) {

                    if (response.code() == 200) {

                        dialog.dismiss();
                        Prefs.putString(Globals.TRIP_ID, response.body().getData().get(0).getId());

                        Toast.makeText(MainActivity_B2C.this, "Success", Toast.LENGTH_SHORT).show();
                        if (locationtype.equalsIgnoreCase("Start")) {
                            if (Prefs.getString(Globals.CHECK_IN_STATUS, "CheckOut").equalsIgnoreCase("Stop")) {
                                Prefs.putString(Globals.CHECK_IN_STATUS, "Start");
                                toggleDefaultChecker = false;
                                binding.toggleCheckIN.setChecked(true);
                                binding.headingcheckIn.setText("Check Out");
                                binding.slideView.setText(" Check Out");
                            } else {
                                binding.toggleCheckIN.setChecked(false);
                                binding.headingcheckIn.setText("Check In");
                                binding.slideView.setText(" Check In");
                            }
                            toggleDefaultChecker = false;
                            binding.toggleCheckIN.setChecked(true);
                            binding.headingcheckIn.setText("Check Out");
                            binding.slideView.setText("Check Out");
                            Log.e("success", "success");

                            picturePath = "";
                            attchmentName = "";
                        } else {
                            binding.toggleCheckIN.setChecked(false);
                            binding.headingcheckIn.setText("Check In");
                            binding.slideView.setText(" Check In");
                            //  openExpenseDialog();
                        }


                    } else if (response.code() == 201) {

                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseTripCheckIn> call, Throwable t) {
                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FAILURE======>", "onFailure: " + t.getMessage());
            }
        });
    }


    private void callCheckOutApi(MultipartBody.Part imagePart,
                                 RequestBody totaldistanceAuto, RequestBody totalDistanceManual,
                                 RequestBody totalExpenses, RequestBody salesPersonCode, RequestBody id,
                                 RequestBody checkOutDate, RequestBody CheckOutTime, RequestBody checkOutLat, RequestBody checkOutLong, RequestBody checkOutRemark) {
        Call<ResponseTripCheckOut> call = NewApiClient.getInstance().getApiService(this).tripCheckOut(
                imagePart, totaldistanceAuto, totalDistanceManual, totalExpenses, salesPersonCode, id, checkOutDate, CheckOutTime, checkOutLat, checkOutLong, checkOutRemark
        );

        call.enqueue(new Callback<ResponseTripCheckOut>() {
            @Override
            public void onResponse(Call<ResponseTripCheckOut> call, Response<ResponseTripCheckOut> response) {
                if (response != null) {

                    if (response.code() == 200) {
                        Calendar calendar = Calendar.getInstance();

                        // Create date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                        // Format date and time
                        String date = dateFormat.format(calendar.getTime());
                        String time = timeFormat.format(calendar.getTime());

                        Log.e("filePath>>>>>", "onCreate: " + picturePath);
                        Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
                        costSt = checkOutExpenseDialogBinding.etCostOfExpense.getText().toString();
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("Attach", imageFile.getName(), requestBody);
                        RequestBody tripName = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.etTripId.getText().toString());
                        RequestBody cost = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.etCostOfExpense.getText().toString());
                        RequestBody typeOfExpense = RequestBody.create(MediaType.parse("multipart/form-data"), "Travel");
                        RequestBody travelDistance = RequestBody.create(MediaType.parse("multipart/form-data"), " ");
                        RequestBody endLat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latAtCheckInMultipart));
                        RequestBody endLong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longAtCheckInMultipart));
                        RequestBody startlat = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        RequestBody startlong = RequestBody.create(MediaType.parse("multipart/form-data"), " ");
                        RequestBody createDate = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                        RequestBody createTime = RequestBody.create(MediaType.parse("multipart/form-data"), time);
                        RequestBody updateDate = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        RequestBody updateTime = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.TRIP_ID, ""));
                        RequestBody createBy = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.SalesEmployeeCode, ""));
                        RequestBody employeeId = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.EmployeeID, ""));
                        RequestBody expenseFrom = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                        RequestBody expenseTo = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), checkOutExpenseDialogBinding.commentValue.getText().toString());

                        Call<ExpenseResponse> callExp = NewApiClient.getInstance()
                                .getApiService(MainActivity_B2C.this).expense_create_multipart(imagePart, id, tripName, typeOfExpense,
                                        expenseFrom, expenseTo, cost, createDate, createTime, createBy, updateDate, updateTime, remark
                                        , employeeId, startlat, startlong, endLat, endLong, travelDistance, id);
                        callExp.enqueue(new Callback<ExpenseResponse>() {
                            @Override
                            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                                if (response != null && response.code() == 200) {
                                    attachmentcheckOut = "";
                                    Toast.makeText(MainActivity_B2C.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

//                                if (Prefs.getString(Globals.CheckMode, "CheckOut").equalsIgnoreCase("CheckOut")) {
//                                    Prefs.putString(Globals.CheckMode, "CheckIn");
//
//                                    binding.slideView.setText("Slide to Check Out");
//                                } else {
//                                    Prefs.putString(Globals.CheckMode, "CheckOut");
//
//                                    binding.slideView.setText("Slide to Check In");
//                                }
//                                Log.e("success", "success");


                                }
                            }

                            @Override
                            public void onFailure(Call<ExpenseResponse> call, Throwable t) {

                            }
                        });


                        picturePath = "";
                        attchmentName = "";


                        Toast.makeText(MainActivity_B2C.this, "Success", Toast.LENGTH_SHORT).show();
                        if (locationtype.equalsIgnoreCase("Stop")) {
                            if (Prefs.getString(Globals.CHECK_IN_STATUS, "CheckOut").equalsIgnoreCase("Start")) {
                                Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");
                                binding.toggleCheckIN.setChecked(false);
                                binding.headingcheckIn.setText("Check In");

                                binding.slideView.setText(" Check In");
                            } else {
                                binding.toggleCheckIN.setChecked(true);
                                binding.headingcheckIn.setText("Check Out");
                                binding.slideView.setText(" Check Out");
                            }
                            binding.toggleCheckIN.setChecked(false);
                            binding.headingcheckIn.setText("Check In");
                            binding.slideView.setText("Check In");
                            Log.e("success", "success");
                        } else {
                            binding.toggleCheckIN.setChecked(true);
                            binding.headingcheckIn.setText("Check Out");
                            binding.slideView.setText(" Check Out");
                            //  openExpenseDialog();
                        }


                    } else if (response.code() == 201) {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity_B2C.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

//                    if (response.body().getValue().size() > 0) {
//
//
//                        startlat = Double.parseDouble(response.body().getValue().get(0).getLat());
//                        startlong = Double.parseDouble(response.body().getValue().get(0).getLong());
//
//                        distanceMAP = distance(startlat, startlong, latitude, longitude);
//                    }
//
//                    if (locationtype.equalsIgnoreCase("Start")) {
//                        if (Prefs.getString(Globals.CHECK_IN_STATUS, "CheckOut").equalsIgnoreCase("Stop")) {
//                            Prefs.putString(Globals.CHECK_IN_STATUS, "Start");
//
//                            binding.slideView.setText(" Check Out");
//                        } else {
//                            Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");
//
//                            binding.slideView.setText(" Check In");
//                        }
//                        Log.e("success", "success");
//                    } else {
//                        Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");
//
//                        binding.slideView.setText(" Check In");
//                        //  openExpenseDialog();
//                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseTripCheckOut> call, Throwable t) {

            }
        });
    }


    double latAtCheckInMultipart;
    double longAtCheckInMultipart;

    @SuppressLint("MissingPermission")
    private void getLatitudeLongitudeForCheckIn() {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) MainActivity_B2C.this
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }


                                String address = addresses != null ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                           /* String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();*/
//                                callCheckInApi(imagePart, bptype, bpFullName,
//                                        cardCode, salesPersonCode, modeOfTransport,
//                                        checkInDate, CheckInTime,
//                                        location.getLatitude(), location.getLongitude(), checkinRemark);


//                                callApi(location
//                                        .getLatitude(), location
//                                        .getLongitude(), mapData, address);
                                latAtCheckInMultipart = location.getLatitude();
                                longAtCheckInMultipart = location.getLongitude();
                                Log.e("LAT AND LONG>>>>>", "onComplete: " + location.getLatitude() + "" + location.getLongitude());

                                //    Toast.makeText(MainActivity_B2C.this, "" + location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();


                            } else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();
//                                        callApi(location1
//                                                .getLatitude(), location1
//                                                .getLongitude(), mapData, address);
                                        latAtCheckInMultipart = location1.getLatitude();
                                        longAtCheckInMultipart = location1.getLongitude();

                                        //   Toast.makeText(MainActivity_B2C.this, "" + location1.getLatitude() + "" + location1.getLongitude(), Toast.LENGTH_SHORT).show();

                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @SuppressLint("MissingPermission")
    private void getmyCurrentLocationForExpense(MapData mapData) {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) MainActivity_B2C.this
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }


                                String address = addresses != null ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                           /* String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();*/


                                callExpenseTravelApi(location
                                        .getLatitude(), location
                                        .getLongitude(), mapData, address);


                            } else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(MainActivity_B2C.this, Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();
                                        callExpenseTravelApi(location1
                                                .getLatitude(), location1
                                                .getLongitude(), mapData, address);
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    private List<BusinessPartnerData> filterprospect(ArrayList<BusinessPartnerData> allitemsList) {
        List<BusinessPartnerData> bde = new ArrayList<>();
        for (BusinessPartnerData bd : allitemsList) {
//if(bd.getUType().get(0).getType().equalsIgnoreCase("Prospect")){
//bde.add(bd);
//            }
        }
        return bde;
    }

    private ArrayList<ContactPersonData> ContactEmployeesList = new ArrayList<>();

    private void callcontactpersonApi(Spinner cp_spinner, String cardCode) {
        ContactPersonData contactPersonData = new ContactPersonData();
        contactPersonData.setCardCode(cardCode);
        binding.contentData.loader.loader.setVisibility(View.VISIBLE);
        Call<ContactPerson> call = NewApiClient.getInstance().getApiService(this).contactemplist(contactPersonData);
        call.enqueue(new Callback<ContactPerson>() {
            @Override
            public void onResponse(Call<ContactPerson> call, Response<ContactPerson> response) {
                binding.contentData.loader.loader.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getData().size() > 0) {
                        ContactEmployeesList.clear();
                        ContactEmployeesList.addAll(response.body().getData());
                        cp_spinner.setAdapter(new ContactPersonAdapter(MainActivity_B2C.this, ContactEmployeesList));
                    }


                } else {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(MainActivity_B2C.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContactPerson> call, Throwable t) {
                binding.contentData.loader.loader.setVisibility(View.GONE);
                Toast.makeText(MainActivity_B2C.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void openChooseExpenseDialog() {
        MapData mapData = new MapData();

        final Dialog dialog = new Dialog(MainActivity_B2C.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        dialog.setContentView(R.layout.dialog_expense_type);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnSelectType = dialog.findViewById(R.id.btnSelectedExpense);
        RadioGroup radioGroup = dialog.findViewById(R.id.rgExpenseType);

        mapData.setRemark("");
        mapData.setExpenseType("");
        mapData.setExpenseCost("");
        mapData.setExpenseDistance("");
        mapData.setExpenseRemark("");
        mapData.setSourceType("");
        mapData.setResourceId("");
        mapData.setContactPerson("");
        mapData.setExpenseAttach("");
        mapData.setAttach("");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbHotel:
                        openExpenseDialog();
                        dialog.dismiss();

                        break;

                    case R.id.rbTravel:

                        getmyCurrentLocationForExpense(mapData);
                        Toast.makeText(MainActivity_B2C.this, "Your Trip has been started", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        break;

                    case R.id.rbCab:

                        getmyCurrentLocationForExpense(mapData);
                        Toast.makeText(MainActivity_B2C.this, "Your Trip has been started", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                        break;

                    case R.id.rbTransport:

                        getmyCurrentLocationForExpense(mapData);
                        Toast.makeText(MainActivity_B2C.this, "Your Trip has been started", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                        break;

                    case R.id.rbPublic:

                        openExpenseDialog();
                        dialog.dismiss();

                        break;


                }
            }
        });
        btnSelectType.setOnClickListener(view -> {

        });
        dialog.show();

    }


    private void openExpenseDialog() {


        final Dialog dialog = new Dialog(MainActivity_B2C.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        dialog.setContentView(R.layout.expense_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button add = dialog.findViewById(R.id.add);
        EditText comment_value = dialog.findViewById(R.id.comment_value);


        Spinner expenseType = dialog.findViewById(R.id.spinnerTypeOfExpense);
        //    EditText expenseRemark = dialog.findViewById(R.id.comment_value);
        EditText cost = dialog.findViewById(R.id.etCostOfExpense);
        attachmentName = dialog.findViewById(R.id.etAttachmentsName);
        EditText distance = dialog.findViewById(R.id.etDistanceOfExpense);

        LinearLayout expenseFrom = dialog.findViewById(R.id.postingDate);
        EditText editTextExpenseFrom = dialog.findViewById(R.id.posting_value);
        EditText editTextExpenseTo = dialog.findViewById(R.id.expense_to_date);
        EditText editTextKm = dialog.findViewById(R.id.etDistanceKm);
        LinearLayout expenseToDate = dialog.findViewById(R.id.expenseToDate);
        Button btnChooseFile = dialog.findViewById(R.id.btnChooseFIle);
        ArrayAdapter<CharSequence> expenseAdapter;

        DecimalFormat df = new DecimalFormat("#.###");

        Double inMetre = distanceMAP * 1000;
        String formattedValue = df.format(inMetre);
        editTextKm.setText(formattedValue + " M");


        expenseAdapter = ArrayAdapter.createFromResource(MainActivity_B2C.this, R.array.type_of_expense_list, android.R.layout.simple_spinner_item);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(expenseAdapter);

        Calendar calendar = Calendar.getInstance();

        // Create date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // Format date and time
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());


        btnChooseFile.setOnClickListener(view -> {
// Create an intent to open the file picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Set the file type to be picked

// Start the activity and wait for the user to pick a file
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });


        expenseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                expenseString = expenseAdapter.getItem(0).toString();

            }
        });


        editTextExpenseFrom.setOnClickListener(view -> {

            Globals.selectDate(MainActivity_B2C.this, editTextExpenseFrom);
        });

        editTextExpenseTo.setOnClickListener(view -> {

            Globals.selectDate(MainActivity_B2C.this, editTextExpenseTo);
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationExpense(cost, distance)) {
                    costSt = cost.getText().toString();
                    distanceSt = distance.getText().toString();
                    ExpenseDataModel expense = new ExpenseDataModel();
                    expense.setAttach(attachentUri);
                    expense.setRemarks("");
                    expense.setTripName(distanceSt);
                    expense.setTypeOfExpense(expenseString);
                    expense.setTravelDistance(String.valueOf(distanceMAP));
                    expense.setStartLat(String.valueOf(startlat));
                    expense.setStartLong(String.valueOf(startlong));
                    expense.setEndLat("");
                    expense.setEndLong("");
                    expense.setExpenseFrom(editTextExpenseFrom.getText().toString());
                    expense.setExpenseTo(editTextExpenseTo.getText().toString());

                    expense.setTotalAmount(Integer.valueOf(costSt));
                    expense.setCreateDate(date);
                    expense.setCreateTime(time);
                    expense.setCreatedBy(Prefs.getString(Globals.SalesEmployeeCode, ""));
                    expense.setEmployeeId(Prefs.getString(Globals.EmployeeID, ""));

                    Call<ExpenseResponse> callExp = NewApiClient.getInstance().getApiService(MainActivity_B2C.this).expense_create(expense);
                    callExp.enqueue(new Callback<ExpenseResponse>() {
                        @Override
                        public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                            if (response != null && response.code() == 200) {

                                Toast.makeText(MainActivity_B2C.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();


                            }
                        }

                        @Override
                        public void onFailure(Call<ExpenseResponse> call, Throwable t) {

                        }
                    });
                }


            }
        });


        dialog.show();

    }


    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();


    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double latDistance = Math.toRadians(endLat - startLat);
        double lonDistance = Math.toRadians(endLong - startLong);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }


    private void opengraph() {

        List<String> dates = new ArrayList<>();
        dates.add("2020-01-25");
        dates.add("2020-02-25");
        dates.add("2020-03-25");
        dates.add("2020-04-25");
        dates.add("2020-05-25");
        dates.add("2020-06-25");
        dates.add("2020-07-25");
        dates.add("2020-08-25");
        dates.add("2020-09-25");
        dates.add("2020-10-25");
        dates.add("2020-11-25");
        dates.add("2020-12-25");
        List<Double> allAmounts = new ArrayList<>();
        allAmounts.add(1.2);
        allAmounts.add(2.9);
        allAmounts.add(3.4);
        allAmounts.add(2.2);
        allAmounts.add(1.5);
        allAmounts.add(3.8);
        allAmounts.add(4.2);
        allAmounts.add(5.7);
        allAmounts.add(4.5);
        allAmounts.add(2.2);
        allAmounts.add(1.1);
        allAmounts.add(0.9);

        binding.contentData.anyChartView.setTouchEnabled(true);
        binding.contentData.anyChartView.setPinchZoom(true);
        XAxis xAxis = volumeReportChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));
        renderData(dates, allAmounts);


    }

    public void renderData(List<String> dates, List<Double> allAmounts) {

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("1");
        xAxisLabel.add("7");
        xAxisLabel.add("14");
        xAxisLabel.add("21");
        xAxisLabel.add("28");
        xAxisLabel.add("30");

        XAxis xAxis = volumeReportChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        //  xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setAxisMaximum(13f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setLabelCount(12, true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setLabelRotationAngle(315f);

        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));

        xAxis.setCenterAxisLabels(true);


        xAxis.setDrawLimitLinesBehindData(false);



     /*   LimitLine ll1 = new LimitLine(4f);
        ll1.setLineColor(getResources().getColor(R.color.safron_barChart));
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);*/

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTextColor(getResources().getColor(R.color.white));
        ll2.setLineColor(getResources().getColor(R.color.safron_barChart));

        xAxis.removeAllLimitLines();
//        xAxis.addLimitLine(ll1);
//        xAxis.addLimitLine(ll2);

        volumeReportChart.getAxisRight().setEnabled(false);

        //setData()-- allAmounts is data to display;
        setDataForWeeksWise(allAmounts);


    }

    private void givepermission() {
        Dexter.withActivity(MainActivity_B2C.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            MainBaseActivity.boolean_permission = true;
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .onSameThread()
                .check();
    }

    private void setDataForWeeksWise(List<Double> amounts) {

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(1, amounts.get(0).floatValue()));
        values.add(new BarEntry(2, amounts.get(1).floatValue()));
        values.add(new BarEntry(3, amounts.get(2).floatValue()));
        values.add(new BarEntry(4, amounts.get(3).floatValue()));
        values.add(new BarEntry(5, amounts.get(4).floatValue()));
        values.add(new BarEntry(6, amounts.get(5).floatValue()));
        values.add(new BarEntry(7, amounts.get(6).floatValue()));
        values.add(new BarEntry(8, amounts.get(7).floatValue()));
        values.add(new BarEntry(9, amounts.get(8).floatValue()));
        values.add(new BarEntry(10, amounts.get(9).floatValue()));
        values.add(new BarEntry(11, amounts.get(10).floatValue()));
        values.add(new BarEntry(12, amounts.get(11).floatValue()));


        BarDataSet set1;
        if (volumeReportChart.getData() != null &&
                volumeReportChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) volumeReportChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            volumeReportChart.getData().notifyDataChanged();
            volumeReportChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Total volume");

            //  set1.setDrawCircles(true);

            set1.setColor(getResources().getColor(R.color.yellow));
            //  set1.setCircleColor(getResources().getColor(R.color.yellow));
            //   set1.setLineWidth(2f);//line size
            //  set1.setCircleRadius(5f);
            set1.setValueTextColor(getResources().getColor(R.color.white));
            //    set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            //    set1.setDrawFilled(true);

            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);
            //   set1.setFillColor(Color.WHITE);
            set1.setDrawValues(true);
            //   set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<BarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData();

            volumeReportChart.setData(data);
            volumeReportChart.getAxisLeft().setDrawLabels(false);
            volumeReportChart.getAxisRight().setDrawLabels(false);
            //  volumeReportChart.getXAxis().setDrawLabels(false);
            volumeReportChart.getDescription().setEnabled(false);
            volumeReportChart.setDrawGridBackground(false);
            volumeReportChart.getLegend().setEnabled(false);
            volumeReportChart.animate();
        }
    }

    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void dashboardcounter() {

        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setSalesEmployeeCode(Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<CounterResponse> call = NewApiClient.getInstance().getApiService(this).dashboardcounter(salesEmployeeItem);
        call.enqueue(new Callback<CounterResponse>() {
            @Override
            public void onResponse(Call<CounterResponse> call, Response<CounterResponse> response) {
                if (response != null) {


                }
            }

            @Override
            public void onFailure(Call<CounterResponse> call, Throwable t) {

            }
        });
    }


    private void callCountryApi() {
        Call<CountryResponse> call = NewApiClient.getInstance().getApiService(this).getCountryList();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getData().size() > 0) {
                        Dashboard.countrylist.clear();
                        Dashboard.countrylist.addAll(response.body().getData());
                    }

                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                Toast.makeText(MainActivity_B2C.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        callrecentactivityapi();
        callrecent_5_order();
    }

    List<EventValue> recentlist = new ArrayList<>();
    List<QuotationItem> recenOrdertlist = new ArrayList<>();

    private void callrecentactivityapi() {
        SalesEmployeeItem si = new SalesEmployeeItem();
        Log.e("TAG", "callrecentactivityapi: " + Prefs.getString(Globals.EmployeeID, ""));
        si.setEmp(Prefs.getString(Globals.EmployeeID, ""));
        // Call<EventResponse> call = NewApiClient.getInstance().getApiService(this).getcalendardata(si);
        Call<EventResponse> call = NewApiClient.getInstance().getApiService(this).getrecentactivity(si);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.code() == 200) {
                    recentlist.clear();
                    recentlist.addAll(response.body().getData());
                    if (response.body().getData().size() == 0) {
                        EventValue eventValue = new EventValue();
                        eventValue.setTitle("No Activity");
                        eventValue.setCreateDate(" None ");
                        eventValue.setComment("No recent activity created.");
                        recentlist.add(eventValue);

                    }
                    adapter = new RecentAdapter(MainActivity_B2C.this, recentlist);

                    if (dataTypeValue.equalsIgnoreCase("Sales")) {
                        binding.contentData.salesIncludeDashboardLayout.cominguprecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.salesIncludeDashboardLayout.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.salesIncludeDashboardLayout.cominguprecyvlerview.setAdapter(adapter);
                        binding.contentData.salesIncludeDashboardLayout.recentrecyvlerview.setAdapter(adapter);
                    } else {
                        binding.contentData.purchaseIncludeDashboardLayout.cominguprecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.purchaseIncludeDashboardLayout.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.purchaseIncludeDashboardLayout.cominguprecyvlerview.setAdapter(adapter);
                        binding.contentData.purchaseIncludeDashboardLayout.recentrecyvlerview.setAdapter(adapter);
                    }

                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(MainActivity_B2C.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callrecent_5_order() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<QuotationResponse> call = NewApiClient.getInstance().getApiService(this).top5order(hde);
        call.enqueue(new Callback<QuotationResponse>() {
            @Override
            public void onResponse(Call<QuotationResponse> call, Response<QuotationResponse> response) {
                if (response.code() == 200) {
                    recenOrdertlist.clear();
                    recenOrdertlist.addAll(response.body().getValue());
                    if (response.body().getValue().size() == 0) {
                        EventValue eventValue = new EventValue();
                        eventValue.setTitle("No Activity");
                        eventValue.setCreateDate(" None ");
                        eventValue.setComment("No recent activity created.");

                    }
                    RecentOrderAdapter adapter = new RecentOrderAdapter(MainActivity_B2C.this, recenOrdertlist);


                    if (dataTypeValue.equalsIgnoreCase("Sales")) {
                        binding.contentData.salesIncludeDashboardLayout.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.salesIncludeDashboardLayout.recentrecyvlerview.setAdapter(adapter);
                    } else {
                        binding.contentData.purchaseIncludeDashboardLayout.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(MainActivity_B2C.this, RecyclerView.HORIZONTAL, false));
                        binding.contentData.purchaseIncludeDashboardLayout.recentrecyvlerview.setAdapter(adapter);
                    }

                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<QuotationResponse> call, Throwable t) {
                Toast.makeText(MainActivity_B2C.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment).addToBackStack(null)
                .commit();
        return true;
    }


    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("New Lead", R.drawable.ic_newlead, false)) // aad an item list.
                // .addItem(new PowerMenuItem("New Opportunity", R.drawable.ic_newopp, false)) // aad an item list.
                .addItem(new PowerMenuItem("New Contact", R.drawable.ic_newcontact, false)) // aad an item list.
                //  .addItem(new PowerMenuItem("New Quotation", R.drawable.ic_newtask, false)) // aad an item list.
                // .addItem(new PowerMenuItem("New Order", R.drawable.ic_new_event, false)) // aad an item list.
                .addItem(new PowerMenuItem("New Campaign", R.drawable.ic_note, false)) // aad an item list.
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
        powerMenu.showAsDropDown(binding.quickCreate);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {

                    case 0:
                        Prefs.putString(Globals.BussinessPageType, "DashBoard");
                        startActivity(new Intent(MainActivity_B2C.this, AddLead.class));
                        break;
                    case 1:
                        Prefs.putString(Globals.SelectOpportnity, "Dashboard");
                        startActivity(new Intent(MainActivity_B2C.this, AddOpportunityActivity.class));
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity_B2C.this, AddContact.class);
                        Prefs.putString(Globals.AddContactPerson, "Dashboard");
                        startActivity(intent);
                        break;
                    case 3:
                        Prefs.putString(Globals.QuotationListing, "null");
                        Prefs.putBoolean(Globals.SelectQuotation, true);
                        Intent i = new Intent(MainActivity_B2C.this, AddQuotationAct.class);
                        startActivity(i);
                        break;
                    case 4:
                        Prefs.putString(Globals.FromQuotation, "Order");
                        startActivity(new Intent(MainActivity_B2C.this, AddOrderAct.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity_B2C.this, AddCampaign.class));
                        break;

                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    public class ClaimsXAxisValueFormatter extends ValueFormatter {

        List<String> datesList;

        public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
            this.datesList = arrayOfDates;
        }


        @Override
        public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
            Integer position = Math.round(value);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM");

            if (value > 1 && value < 2) {
                position = 0;
            } else if (value > 2 && value < 3) {
                position = 1;
            } else if (value > 3 && value < 4) {
                position = 2;
            } else if (value > 4 && value < 5) {
                position = 3;
            } else if (value > 5 && value < 6) {
                position = 4;
            } else if (value > 6 && value < 7) {
                position = 5;
            } else if (value > 7 && value < 8) {
                position = 6;
            } else if (value > 8 && value < 9) {
                position = 7;
            } else if (value > 9 && value <= 10) {
                position = 8;
            }
            if (position < datesList.size())
                return sdf.format(new Date((Globals.getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
            return "";
        }
    }


    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getAddressFromLocation(latitude, longitude);
            }
        } else {
            showGPSDisabledAlert();
        }
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName();
            // Do something with the address information
            //Toast.makeText(this, addressLine, Toast.LENGTH_SHORT).show();
            callMapApi(latitude, longitude, locationtype, addressLine);


        }
    }


    private void callMapApi(double latitude, double longitude, String type, String address) {
        Log.d("TAG", "callMapApi: ");
        MapData mapData = new MapData();
        mapData.setEmp_Id(Prefs.getString(Globals.EmployeeID, ""));
        mapData.setEmp_Name(Prefs.getString(Globals.Employee_Name, ""));
        mapData.setLat(String.valueOf(latitude));
        mapData.setLong(String.valueOf(longitude));
        mapData.setUpdateDate(Globals.getTodaysDatervrsfrmt());
        mapData.setUpdateTime(Globals.getTCurrentTime());
        mapData.setAddress(address);
        mapData.setShape("meeting");
        mapData.setType(type);
        mapData.setRemark("");
        mapData.setResourceId("");
        mapData.setContactPerson("");
        mapData.setSourceType("");
        Call<MapResponse> call = NewApiClient.getInstance().getApiService(this).sendMaplatlong(mapData);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e("success", "success-Bhupi");
                            Log.d("response", "success-shubh");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });
    }


    private File file;
    private Uri fileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the URI of the selected file
            Uri uri = data.getData();
            attachentUri = uri.toString();
            attchmentName = getFileName(uri);
            attachmentName.setText(attchmentName);
            etNewAttachemnt.setText(attchmentName);
            picturePath = getRealPathFromURI(uri);
            //  Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();


            // Use the URI to access the file
            // ...
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                Uri uri = data.getData();
                //  attachmentcheckOut=getFileName(uri);
                attachmentcheckOut = getRealPathFromURI(uri);
                checkinDialogBinding.etAttachmentsNameCHeckIN.setText(attachmentcheckOut);
            }

        } else if (requestCode == PICK_FILE_CHECKOUT_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            //  attachmentcheckOut=getFileName(uri);
            attachmentcheckOut = getRealPathFromURIchekOut(uri);
            checkOutExpenseDialogBinding.etAttachmentsNameOut.setText(attachmentcheckOut);


        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Bundle extras = data.getExtras();
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("picturePath", picturePath);

//                    pageBinding.nameIcon.setImageURI(Uri.parse(picturePath));


                    callUploadImage();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICTURE_FROM_CAMERA && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                file = saveBitmap(photo);
                picturePath = file.getPath();
                fileUri = Uri.fromFile(file);
                Log.e("fileUri---", fileUri.toString());
                Log.e("picturePath---", picturePath.toString());

//                pageBinding.nameIcon.setImageBitmap(photo);


                callUploadImage();


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            Globals.showMessage(MainActivity_B2C.this, "No Image Found");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            picturePath = filePath;
            result = filePath;

        }
        return result;

    }


    public String getRealPathFromURIchekOut(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            checkOUtPicturePath = filePath;
            result = filePath;
            //  Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();

            // Do something with the file path, such as displaying it in a TextView
            // TextView filePathTextView = findViewById(R.id.file_path_text_view);
            //  filePathTextView.setText(filePath);
        }
        return result;

    }


    // Helper method to get the file name from the URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    /*********************** Graphs APIs**************************/
    public static List<BarEntry> Salesentries = new ArrayList<>();
    public static List<BarEntry> SalesPreviousentries = new ArrayList<>();
    public static List<BarEntry> Receiptentries = new ArrayList<>();
    //todo receiptpreviosu value marker
    public static List<BarEntry> ReceiptPreviousentries = new ArrayList<>();


    public static List<BarEntry> Receivableentries = new ArrayList<>();
    public static List<String> ReceivableentriesXaxis = new ArrayList<>();
    public static List<String> ReceivableentriesYaxis = new ArrayList<>();

    public static List<String> SalesValueForMarker = new ArrayList<>();
    //todo new key for previosu Sales
    public static List<String> previousSalesValueForMarker = new ArrayList<>();
    public static List<String> previousReceiptValueForMarker = new ArrayList<>();
    public static List<String> ReceivableValueForMarker = new ArrayList<>();
    public static List<String> ReceiptValueForMarker = new ArrayList<>();

    /********************** Sales Graph*************************/

    private void saleGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", Globals.firstDateOfFinancialYear());
        obj.put("ToDate", Globals.lastDateOfFinancialYear());
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<SalesGraphResponse> call;
        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            call = NewApiClient.getInstance().getApiService(this).salesGraph(obj);
        } else {
            call = NewApiClient.getInstance().getApiService(this).purchaseGraph(obj);
        }
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Salesentries.clear();
                        SalesPreviousentries.clear();
                        SalesValueForMarker.clear();
                        previousSalesValueForMarker.clear();

                        //todo calling attachment api after alertDialog dissmiss
                        callAttachmentAllApi();

                        for (int i = 0; i < response.body().data.size(); i++) {
                            Salesentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                            SalesPreviousentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getLastMonthlySales())));
                            SalesValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getMonthlySales()));
                            previousSalesValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getLastMonthlySales()));
                        }
                        // opengraph();

                    }

                    ReceiptGraphApi();
                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }

    /********************** Receipt Graph*************************/

    private void ReceiptGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", Globals.firstDateOfFinancialYear());
        obj.put("ToDate", Globals.lastDateOfFinancialYear());
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call;
        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            call = NewApiClient.getInstance().getApiService(this).receiptGraph(obj);
        } else {
            call = NewApiClient.getInstance().getApiService(this).receiptGraphPurchase(obj);
        }
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Receiptentries.clear();
                        ReceiptPreviousentries.clear();
                        ReceiptValueForMarker.clear();
                        previousReceiptValueForMarker.clear();

                        for (int i = 0; i < response.body().data.size(); i++) {

                            Receiptentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                            ReceiptPreviousentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getLastMonthlySales())));
                            ReceiptValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getMonthlySales()));
                            previousReceiptValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getLastMonthlySales()));

                        }
                        // opengraph();


                    }

                    ReceivableGraphApi();
                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }

    /********************** Receivable Graph*************************/


    private void ReceivableGraphApi() {

       /* {
            "FromDate":"2023-01-01",
                "ToDate":"2023-12-31",
                "SalesPersonCode":-1
        }*/

        alertDialog.show();

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", Globals.firstDateOfFinancialYear());
        obj.put("ToDate", Globals.lastDateOfFinancialYear());
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<ResponseReceivableGraph> call;

        if (Prefs.getString(Globals.IS_SALE_OR_PURCHASE, "").equalsIgnoreCase("Sales")) {
            call = NewApiClient.getInstance().getApiService(this).receivableDueMonthGraph(obj);
        } else {
            call = NewApiClient.getInstance().getApiService(this).receivableDueMonthGraphPurchase(obj);
        }
        call.enqueue(new Callback<ResponseReceivableGraph>() {
            @Override
            public void onResponse(Call<ResponseReceivableGraph> call, Response<ResponseReceivableGraph> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        alertDialog.dismiss();
                        if (response.body() != null && response.body().data.size() > 0)
                            Receivableentries.clear();
                        ReceivableentriesXaxis.clear();
                        ReceivableentriesYaxis.clear();
                        ReceivableValueForMarker.clear();
                        for (int i = 0; i < response.body().data.size(); i++) {
                            ArrayList<String> daysGroup = new ArrayList<>();
                           /* if (response.body().data.get(i).getOverDueDaysGroup().equalsIgnoreCase("")) {

                            }*/
                            ReceivableentriesXaxis.add(response.body().getData().get(i).getOverDueDaysGroup());
                            ReceivableentriesYaxis.add("" + Globals.numberToK(String.valueOf(Double.valueOf(response.body().getData().get(i).getTotalDue()))));
                            Receivableentries.add(new BarEntry(i, Float.parseFloat(response.body().getData().get(i).getTotalDue())));
                            ReceivableValueForMarker.add("" + Globals.convertToLakhAndCroreFromString(response.body().getData().get(i).getTotalDue()));

                        }
                        // opengraph();

                    }

                    setupChartViewPgaer();
                }
            }

            @Override
            public void onFailure(Call<ResponseReceivableGraph> call, Throwable t) {

            }
        });
    }


}
